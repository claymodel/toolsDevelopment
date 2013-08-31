#!/usr/bin/env python2.7
# -*- coding: utf-8 -*-

#
# @Author: "Elias Hasnat"
# @Date:   May 2013
#
# Script to analyze reserved instance utilization.
#
# Identifies:
#   - reservations that aren't being used
#   - running instances that aren't reserved
#   - cost savings if you were to reserve all running on-demand instances
##
# Requires: ~/.boto, boto lib, texttable lib
#
#
import sys
import os
import re
import math
import logging
import boto.ec2
import locale
import texttable
from optparse import OptionParser

locale.setlocale(locale.LC_ALL, '')

parser = OptionParser("usage: %prog [options]")
parser.add_option("-d", "--debug", default=None, action="store_true",
                  help="enable debug output")
parser.add_option("-l", "--list", default=None, action="store_true",
                  help="list all reservations and exit")
parser.add_option("-p", "--print-monthly", default=None, action="store_true",
                  help="list all reservations instances' monthly cost (1-yr ri)")
parser.add_option("-e", "--exclude", metavar="regex", default='__None__',
                  help="exclude instances by security group name. takes regex")
parser.add_option("-r", "--region", default='us-east-1',
                  help="ec2 region to connect to")
parser.add_option("--vpc", default=False, action="store_true",
                  help="operate on VPC instances/reservations only")
(options, args) = parser.parse_args()

# set up logging
if options.debug:
    log_level = logging.DEBUG
else:
    log_level = logging.INFO

logging.basicConfig(stream=sys.stdout, level=log_level)
logging.basicConfig(stream=sys.stderr, level=(logging.ERROR, logging.CRITICAL))

rates = {'us-east-1': {'m1.small': {'hourly': .06, 'hu-1y': (169, .014)},
                       'm1.medium': {'hourly': .12, 'hu-1y': (338, .028)},
                       'm1.large': {'hourly': .24, 'hu-1y': (676, .056)},
                       'm1.xlarge': {'hourly': .48, 'hu-1y': (1352, .112)},
                       'm3.xlarge': {'hourly': .50, 'hu-1y': (1489, .123)},
                       'm3.2xlarge': {'hourly': 1.00, 'hu-1y': (2978, .246)},
                       't1.micro': {'hourly': .02, 'hu-1y': (62, .005)},
                       'm2.xlarge': {'hourly': .41, 'hu-1y': (789, .068)},
                       'm2.2xlarge': {'hourly': .82, 'hu-1y': (1578, .136)},
                       'm2.4xlarge': {'hourly': 1.64, 'hu-1y': (3156, .272)},
                       'c1.medium': {'hourly': .145, 'hu-1y': (450, .036)},
                       'c1.xlarge': {'hourly': .58, 'hu-1y': (1800, .144)},
                       'cc1.4xlarge': {'hourly': 1.30, 'hu-1y': (4060, .297)},
                       'cc2.8xlarge': {'hourly': 2.40, 'hu-1y': (5000, .361)},
                       'cr1.8xlarge': {'hourly': 3.50, 'hu-1y': (7220, .62)},
                       'hi1.4xlarge': {'hourly': 3.10, 'hu-1y': (7280, .621)},
                       'hs1.8xlarge': {'hourly': 4.60, 'hu-1y': (11213, .92)},
                       },
         'us-west-2': {'m1.small': {'hourly': .06, 'hu-1y': (169, .014)},
                       'm1.medium': {'hourly': .12, 'hu-1y': (338, .028)},
                       'm1.large': {'hourly': .24, 'hu-1y': (676, .056)},
                       'm1.xlarge': {'hourly': .48, 'hu-1y': (1352, .112)},
                       'm3.xlarge': {'hourly': .50, 'hu-1y': (1489, .123)},
                       'm3.2xlarge': {'hourly': 1.00, 'hu-1y': (2978, .256)},
                       't1.micro': {'hourly': .02, 'hu-1y': (62, .005)},
                       'm2.xlarge': {'hourly': .41, 'hu-1y': (789, .068)},
                       'm2.2xlarge': {'hourly': .82, 'hu-1y': (1578, .136)},
                       'm2.4xlarge': {'hourly': 1.64, 'hu-1y': (3156, .272)},
                       'c1.medium': {'hourly': .145, 'hu-1y': (450, .036)},
                       'c1.xlarge': {'hourly': .58, 'hu-1y': (1800, .144)},
                       'cr1.8xlarge': {'hourly': 3.50, 'hu-1y': (7220, .62)},
                       'hi1.4xlarge': {'hourly': 3.10, 'hu-1y': (7280, .621)},
                       'hs1.8xlarge': {'hourly': 4.60, 'hu-1y': (11213, .92)},
                       },
         'us-west-1': {'m1.small': {'hourly': .065, 'hu-1y': (169, .022)},
                       'm1.medium': {'hourly': .13, 'hu-1y': (338, .044)},
                       'm1.large': {'hourly': .26, 'hu-1y': (676, .87)},
                       'm1.xlarge': {'hourly': .52, 'hu-1y': (1352, .174)},
                       'm3.xlarge': {'hourly': .55, 'hu-1y': (1489, .191)},
                       'm3.2xlarge': {'hourly': 1.10, 'hu-1y': (2978, .382)},
                       't1.micro': {'hourly': .025, 'hu-1y': (62, .008)},
                       'm2.xlarge': {'hourly': .46, 'hu-1y': (789, .102)},
                       'm2.2xlarge': {'hourly': .92, 'hu-1y': (1578, .204)},
                       'm2.4xlarge': {'hourly': 1.84, 'hu-1y': (3156, .408)},
                       'c1.medium': {'hourly': .165, 'hu-1y': (450, .057)},
                       'c1.xlarge': {'hourly': .66, 'hu-1y': (1800, .228)},
                       },
         'eu-west-1': {'m1.small': {'hourly': .065, 'hu-1y': (169, .022)},
                       'm1.medium': {'hourly': .13, 'hu-1y': (338, .044)},
                       'm1.large': {'hourly': .26, 'hu-1y': (676, .87)},
                       'm1.xlarge': {'hourly': .52, 'hu-1y': (1352, .174)},
                       'm3.xlarge': {'hourly': .55, 'hu-1y': (1489, .189)},
                       'm3.2xlarge': {'hourly': 1.10, 'hu-1y': (2978, .378)},
                       't1.micro': {'hourly': .02, 'hu-1y': (62, .008)},
                       'm2.xlarge': {'hourly': .46, 'hu-1y': (789, .102)},
                       'm2.2xlarge': {'hourly': .92, 'hu-1y': (1578, .204)},
                       'm2.4xlarge': {'hourly': 1.84, 'hu-1y': (3156, .408)},
                       'c1.medium': {'hourly': .165, 'hu-1y': (450, .057)},
                       'c1.xlarge': {'hourly': .66, 'hu-1y': (1800, .228)},
                       'cc2.8xlarge': {'hourly': 2.70, 'hu-1y': (5000, .61)},
                       'hi1.4xlarge': {'hourly': 3.41, 'hu-1y': (7280, .931)},
                       },
         }


def costs(item):
    """ takes a tuple of properties, and returns:
        ((monthly, yearly), (monthly, yearly), upfront) cost
        of (ondemand, 1-yr-heavy-utilization-ri).. for one instance.
        imput: (instance_type, region)
    """
    instance, zone = item
    monthly_ondemand = float(
        730 * float(rates[options.region][instance]['hourly']))
    yearly_ondemand = 12 * monthly_ondemand

    monthly_ri = float(730
                       * float(rates[options.region][instance]['hu-1y'][1])
                       + float(rates[options.region][instance]['hu-1y'][0])
                       / 12)
    yearly_ri = 12 * monthly_ri

    upfront = float(rates[options.region][instance]['hu-1y'][0])

    return (('%.2f' % monthly_ondemand, '%.2f' % yearly_ondemand),
            ('%.2f' % monthly_ri, '%.2f' % yearly_ri), upfront)


def summarize_tuples(items):
    ''' takes a tuple of properties, and summarizes into a dict.
        input: (instance_type, availability_zone, instance_count) '''
    result = {}
    for res in items:
        key = (res[0], res[1])
        if key not in result:
            result.update({key: res[2]})
        else:
            result[key] += res[2]
    return result

if __name__ == '__main__':
    # TODO: security group based filtering doesn't work on VPC instances.
    if "None" not in options.exclude and options.vpc:
        logging.error("Sorry, you can't currently exclude by security group "
                      "regex with VPC enabled.")
        sys.exit(1)

    if options.region and options.region not in rates:
        logging.error("Sorry, region %s is not currently supported"
                      "." % options.region)
        sys.exit(1)

    ''' just print monthly prices, if -p was used '''
    if options.print_monthly:
        print "Current monthly pricing (1-year reserved) per instance type:"
        for _zone, _rates in rates.iteritems():
            if options.region not in _zone:
                continue
            _query = _zone, _rates.keys()
            _output = [("%s: " % instance, costs((instance, _zone))[1][0]) for instance in _query[1]]
            for inst, cost in sorted(_output, key=lambda x: float(x[1])):
                print "%s\t%s" % (inst, cost)

        sys.exit(0)

    conn = boto.ec2.connect_to_region(options.region)

    # not filtering by security group? it'll break with vpc instances that
    # don't have a 'name' attribute, so don't even try:
    if "None" not in options.exclude:
        instances = [i for r in conn.get_all_instances()
                     for i in r.instances
                     if len(r.groups) > 0 and not re.match(options.exclude, r.groups[0].name)]
    else:
        instances = [i for r in conn.get_all_instances() for i in r.instances]

    active_reservations = [i for i in conn.get_all_reserved_instances()
                           if 'active' in i.state
                           or 'payment-pending' in i.state]

    # re-set list of instances and reservations to only VPC ones, if --vpc
    # otherwise, exclude VPC instances/reservations. *hacky*
    if options.vpc:
        active_reservations = [res for res in active_reservations
                               if "VPC" in res.description]
        instances = [inst for inst in instances if inst.vpc_id]
    else:
        active_reservations = [res for res in active_reservations
                               if "VPC" not in res.description]
        instances = [inst for inst in instances if inst.vpc_id is None]

    # no instances were found, just bail:
    if len(instances) == 0:
        logging.error("Sorry, you don't seem to have any instances "
                      "here. Nothing to do. (try --vpc?)")
        sys.exit(1)

    all_res = [(res.instance_type, res.availability_zone,
                res.instance_count) for res in active_reservations]
    res_dict = summarize_tuples(all_res)

    ''' just print reservations, if -l is used '''
    if options.list:
        print "Current active reservations:\n"
        for i in sorted(res_dict.iteritems()):
            print i[0][0], i[0][1], i[1]
        sys.exit(0)

    ''' find cases where we're running fewer instances than we've reserved '''
    total_waste = 0
    for res in active_reservations:
        matches = [
            i for i in instances if res.availability_zone in i.placement]
        running = len(
            [i.instance_type for i in matches
                if i.instance_type in res.instance_type
                and "running" in i.state])

        if running < res.instance_count:
            waste = float(costs((res.instance_type, res.instance_count))[
                          1][0]) * (res.instance_count - running)
            total_waste += waste

            print "ERR: only %i running %s instances in %s, but %s are " \
                  "reserved! Monthly waste: " \
                  "%s" % (running, res.instance_type,
                          res.availability_zone, res.instance_count,
                          locale.currency(waste, grouping=True).decode(locale.getpreferredencoding())
                          )

    if total_waste > 0:
        print "\nTotal monthly waste: %s\n" % locale.currency(total_waste,
                                                              grouping=True).decode(locale.getpreferredencoding())

    ''' identify non-reserved running instances '''

    all_instances = [(ins.instance_type, ins.placement, 1)
                     for ins in instances if "running" in ins.state]
    ins_dict = summarize_tuples(all_instances).iteritems()

    print "\n== Summary of running instances and their reserved instances ==\n"

    yearly_savings = float(0)
    monthly_savings = float(0)
    upfront_cost = float(0)
    total_instances = 0
    res_instances = 0
    monthly_od_sum = 0
    monthly_ri_sum = 0

    table = texttable.Texttable(max_width=0)
    table.set_deco(texttable.Texttable.HEADER)
    table.set_cols_dtype(['t', 't', 't', 't', 't', 't', 't', 't'])
    table.set_cols_align(["l", "c", "c", "c", "r", "r", "r", "r"])
    table.add_row(
        ["instance type", "zone", "# running", "# reserved", "monthly savings",
         "yearly savings", "current monthly", "only_RIs monthly"])

    for i in sorted(ins_dict):
        # dict i is: {(inst_type, az): count}

        # find # of reserved instances, and # on-demand:
        if i[0] in res_dict:
            res_count = int(res_dict[i[0]])
        else:
            res_count = 0

        run_count = int(i[1])

        inst_type, az = i[0]

        od, ri, upfront = costs(tuple(i[0]))
        od_monthly, od_yearly = [float(x) for x in od]
        ri_monthly, ri_yearly = [float(x) for x in ri]

        # determine monthly savings, if we're running more than are reserved:
        od_count = int(run_count) - int(res_count)

        if od_count > 0:
            monthly = od_count * (od_monthly - ri_monthly)
            yearly = od_count * (od_yearly - ri_yearly)
            upfront_cur = float(upfront * od_count)
            cur_monthly = (od_count * od_monthly) + (res_count * ri_monthly)
            all_ri_monthly = (od_count + res_count) * ri_monthly
            cur_yearly = (od_count * od_yearly) + (res_count * ri_yearly)
            all_ri_yearly = (od_count + res_count) * ri_yearly
        else:
            monthly = 0
            yearly = 0
            upfront_cur = 0
            cur_monthly = (res_count * ri_monthly)
            all_ri_monthly = (res_count) * ri_monthly
            cur_yearly = (res_count * ri_yearly)
            all_ri_yearly = (res_count) * ri_yearly

        # totals
        yearly_savings += yearly
        monthly_savings += monthly
        upfront_cost += float(upfront_cur)
        monthly_od_sum += cur_monthly
        monthly_ri_sum += all_ri_monthly

        total_instances += int(run_count)
        res_instances += int(res_count)

        table.add_row(
            [inst_type, az, run_count, res_count,
             locale.currency(monthly, grouping=True),
             locale.currency(yearly, grouping=True),
             locale.currency(cur_monthly, grouping=True),
             locale.currency(all_ri_monthly, grouping=True),
             ])

    table.add_row(['Totals:', '', '', '', '', '', '', '', ])
    table.add_row(
        [' ', ' ', total_instances, res_instances,
            locale.currency(monthly_savings, grouping=True),
            locale.currency(yearly_savings, grouping=True),
            locale.currency(monthly_od_sum, grouping=True),
            locale.currency(monthly_ri_sum, grouping=True),
         ])
    print table.draw()

    ''' more summaries '''

    print "\n== Savings Potential (reserve all on-demand instances) =="
    print "monthly: %s, yearly: %s\nupfront cost (already amortized in " \
          "'savings' calculations): %s" \
          "" % (locale.currency(monthly_savings, grouping=True),
                locale.currency(yearly_savings, grouping=True),
                locale.currency(upfront_cost, grouping=True),
                )

    print "\n== Current Costs (including waste; i.e. unused RIs) =="
    real_monthly = monthly_od_sum + total_waste
    real_yearly = real_monthly * 12

    print "Current total monthly expense: %s" % (
        locale.currency(real_monthly, grouping=True))
    print "Current total yearly expense: %s" % (
        locale.currency(real_yearly, grouping=True))

    try:
        rf, rm = math.modf(upfront_cost
                           / (monthly_savings + (upfront_cost / 12)))
        rd = rf * 30
        print "Time to recover up-front cost: %s months, %s days.\n" % (
              int(rm), int(rd))
    except ZeroDivisionError:
        pass
