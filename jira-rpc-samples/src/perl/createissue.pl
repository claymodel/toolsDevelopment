#!/usr/bin/perl

# toy jira perl client using XMLRPC
# logs in, creates an issue
# handles failure or prints issue fields
# logs out.

# See the thread:
# http://forums.atlassian.com/thread.jspa?forumID=46&threadID=10484

use strict;
use warnings;

use XMLRPC::Lite;
use Data::Dumper;

my $jira = XMLRPC::Lite->proxy('http://jira.atlassian.com/rpc/xmlrpc');
my $auth = $jira->call("jira1.login", "xmlrpctester", "xmlrpctester")->result();
my $call = $jira->call("jira1.createIssue", $auth, {
	'project' => 'TST',
	'type' => 1,
	'summary' => 'Issue created via XMLRPC',
	'description' => 'Created with a Perl client',
	'customFieldValues' => [{
	   	'customfieldId' => 'customfield_10010', 'values' => ['Textfield custom field value']
	}]
});
my $fault = $call->fault();
if (defined $fault) {
	die $call->faultstring();
} else {
	print "issue created:\n";
	print Dumper($call->result());
}
$jira->call("jira1.logout", $auth); 
