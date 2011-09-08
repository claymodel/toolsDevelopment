#!/usr/bin/python

# Sample Python client accessing JIRA via XML-RPC. Methods requiring
# more than basic user-level access are commented out.
#
# Refer to the XML-RPC Javadoc to see what calls are available:
# http://www.atlassian.com/software/jira/docs/api/rpc-jira-plugin/latest/com/atlassian/jira/rpc/xmlrpc/XmlRpcService.html

import xmlrpclib

s = xmlrpclib.ServerProxy('http://jira.atlassian.com/rpc/xmlrpc')
#s = xmlrpclib.ServerProxy('http://192.168.0.87:8080/rpc/xmlrpc')
auth = s.jira1.login('xmlrpctester', 'xmlrpctester')

newissue = s.jira1.createIssue(auth, { 'project': 'TST', 'type': 2,
	'summary': 'Issue created via XML-RPC', 'description': 'Created with a Python client'})

print "Created %s/browse/%s" % (s.jira1.getServerInfo(auth)['baseUrl'], newissue['key'])

print "Commenting on issue.."
s.jira1.addComment(auth, newissue['key'], 'Comment added with XML-RPC')

print "Modifying issue..."
s.jira1.updateIssue(auth, newissue['key'], {
		"summary": ["[Updated] issue created via XML-RPC"],

		# Setting a custom field. The id (10010) is discoverable from
		# the database or URLs in the admin section

		"customfield_10010": ["Random text set in updateIssue method"],

		# Demonstrate setting a cascading selectlist:
		"customfield_10061": ["10098"],
		"customfield_10061_1": ["10105"],
		"components": ["10370"]

		})


print "Done!"
