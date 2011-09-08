package com.atlassian.jira_soapclient;


import _soapclient.JiraSoapService;
import _soapclient.JiraSoapServiceService;
import _soapclient.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.beans.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.rmi.RemoteException;

/**
  * Sample JIRA SOAP client. Note that the constants sit in the {@link ClientConstants} interface
 */
public class SOAPClient implements ClientConstants
{
    // To edit the constants, see the ClientConstants interface
    public static void main(String[] args) throws Exception
    {
        System.out.println("Running test SOAP client...");
        JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();

        JiraSoapService jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();

        String token = jiraSoapService.login(LOGIN_NAME, LOGIN_PASSWORD);

        final RemoteIssue issue = testCreateIssue(jiraSoapService, token);
        testAddAttachment(jiraSoapService, token, issue);

        String issueKey = issue.getKey();

        testGetIssueById(jiraSoapService, token, issue.getId());
        testAddComment(jiraSoapService, token, issueKey);
        testGetFilters(jiraSoapService, token);
        testGetIssues(jiraSoapService, token);
        testFindIssuesWithTerm(jiraSoapService, token, SEARCH_TERM);
        testGetIssueCountForFilter(jiraSoapService,  token, FILTER_ID);


        // All the tests below need extra permissions. Enable in your own environment
//        testGetCustomFieldValues(jiraSoapService, token, issueKey);
//        testGetRemoteConfiguration(jiraSoapService, token);
//        testProgressWorkflow(jiraSoapService, token, issueKey);
//
//        testUpdateIssue(jiraSoapService, token, issueKey);
//
//        testGetCustomFields(jiraSoapService, token);
//
//        testCreateVersion(jiraSoapService, token);
//        testGetAllPermissions(jiraSoapService, token);
//
//        final String projectKey = testCreateProject(jiraSoapService, token);
//
//        testUpdateGroup(jiraSoapService, token);

    }

    private static void testGetCustomFieldValues(JiraSoapService jiraSoapService, String token, String issueKey)  throws RemoteException
    {
        System.out.println("Testing getting Custom Field values for Issue "+issueKey);
        RemoteField[] remoteFields = jiraSoapService.getCustomFields(token);

        // Get an issue with CustomField Values set.
        RemoteIssue issue = jiraSoapService.getIssue(token, issueKey);
        RemoteCustomFieldValue[] customFieldValues = issue.getCustomFieldValues();
        for(int i = 0; i < customFieldValues.length ; i++)
        {
            System.out.println("Custom Field Id: "+customFieldValues[i].getCustomfieldId());
            String[] values = customFieldValues[i].getValues();
            for(int j = 0; j < values.length ; j++)
            {
                System.out.println("Custom Field Value: "+values[j]);
            }
        }
        System.out.println("Ending getting Custom Field values for Issue "+issueKey);
    }

    private static void testGetIssueById(JiraSoapService jiraSoapService, String token, String issueId) throws RemoteException
    {
        System.out.println("Testing getIssueById ...");
        RemoteIssue issue = jiraSoapService.getIssueById(token, issueId);
        System.out.println("Returned an issue id: "+issue.getId()+" key: "+issue.getKey());
    }

    private static void testGetRemoteConfiguration(JiraSoapService jiraSoapService, String token) throws RemoteException
    {
        System.out.println("Testing getRemoteConfiguration ...");
        RemoteConfiguration config = jiraSoapService.getConfiguration(token);
        System.out.println("Returned Configuration: \nAllow Attachments: "+config.isAllowAttachments()+"\nAllow Issue Linking: "+config.isAllowIssueLinking());
    }

    private static void testGetIssueCountForFilter(JiraSoapService jiraSoapService, String token, String filterId)
            throws RemoteException
    {
        System.out.println("Testing getIssueCountForFilter ...");
        long issueCount = jiraSoapService.getIssueCountForFilter(token, filterId);
        System.out.println("Returned an issue count of "+issueCount+" for filter "+filterId);
    }

    private static void testProgressWorkflow(JiraSoapService jiraSoapService, String token, String issueKey)
            throws java.rmi.RemoteException
    {
        System.out.println("Progressing workflow of "+issueKey+"...");
        RemoteNamedObject[] availableActions = jiraSoapService.getAvailableActions(token, issueKey);
        String actionId = null;
        for (int i = 0; i < availableActions.length; i++)
        {
            RemoteNamedObject availableAction = availableActions[i];
            System.out.println("availableAction: " + availableAction.getId() + " - " + availableAction.getName());
            if (actionId == null) actionId = availableAction.getId();
        }

        if (actionId != null)
        {
            RemoteField[] fieldsForAction = jiraSoapService.getFieldsForAction(token, issueKey, actionId);
            for (int i = 0; i < fieldsForAction.length; i++)
            {
                RemoteField remoteField = fieldsForAction[i];
                System.out.println("remoteField: " + remoteField.getId() + " - " + remoteField.getName());
            }

            RemoteFieldValue[] actionParams = new RemoteFieldValue[]{
                    new RemoteFieldValue("assignee", new String[]{LOGIN_NAME})
            };

            RemoteIssue remoteIssue = jiraSoapService.progressWorkflowAction(token, issueKey, actionId, actionParams);
            System.out.println("Progressed workflow of "+remoteIssue.getKey()+" to: " + remoteIssue.getStatus());
        }
    }

    private static void testUpdateGroup(JiraSoapService jiraSoapService, String token)
            throws java.rmi.RemoteException
    {
        System.out.println("Testing group update..");
        RemoteGroup group = jiraSoapService.getGroup(token, "jira-developers");
        System.out.println("Updating group: " + group.getName());
        System.out.println("group.getUsers(): " + (group.getUsers().length));


        RemoteUser[] remoteUsers = new RemoteUser[group.getUsers().length + 1];
        final RemoteUser[] oldUsers = group.getUsers();
        for (int i = 0; i < oldUsers.length; i++)
        {
            remoteUsers[i] = oldUsers[i];
        }
        remoteUsers[remoteUsers.length-1] = new RemoteUser(null, null, "fred");
        group.setUsers(remoteUsers);
        jiraSoapService.updateGroup(token, group);

        group = jiraSoapService.getGroup(token, "jira-developers");
        System.out.println("group: " + group);
        System.out.println("group.getUsers(): " + (group.getUsers().length));
    }

    private static void testAddAttachment(JiraSoapService jiraSoapService, String token, RemoteIssue issue) throws IOException
    {
        File tmpFile = File.createTempFile("attachment", ".txt");
        FileWriter fw = new FileWriter(tmpFile);
        fw.write("A sample file attached via SOAP to JIRA issue "+issue.getKey());
        fw.close();

        boolean added = jiraSoapService.addAttachmentsToIssue(token,
                                                              issue.getKey(),
                                                              new String[]{tmpFile.getName()},
                                                              new byte[][]{getBytesFromFile(tmpFile)});
        System.out.println((added ? "Added" : "Failed to add") + " attachment "+tmpFile.getName()+" to issue "+issue.getKey());
        tmpFile.delete();
    }

    private static void testGetAllPermissions(JiraSoapService jiraSoapService, String token)
            throws java.rmi.RemoteException
    {
        RemotePermission[] allPermissions = jiraSoapService.getAllPermissions(token);
        for (int i = 0; i < allPermissions.length; i++)
        {
            RemotePermission allPermission = allPermissions[i];
            System.out.println("allPermission.getName(): " + allPermission.getName());
        }
    }

    private static void testCreateVersion(JiraSoapService jiraSoapService, String token)
            throws java.rmi.RemoteException
    {
        final RemoteVersion version = new RemoteVersion();
        version.setName("3 SOAP created version " + new Date());
        version.setSequence(new Long(6));
        final RemoteVersion createdVersion = jiraSoapService.addVersion(token, PROJECT_KEY, version);
        System.out.println("createdVersion.getId(): " + createdVersion.getId());
    }

    private static void testGetCustomFields(JiraSoapService jiraSoapService, String token)
            throws java.rmi.RemoteException
    {
        final RemoteField[] customFields = jiraSoapService.getCustomFields(token);
        for (int i = 0; i < customFields.length; i++)
        {
            RemoteField customField = customFields[i];
            System.out.println("customField.getName(): " + customField.getName());
        }
    }

    private static void testGetFilters(JiraSoapService jiraSoapService, String token)
            throws java.rmi.RemoteException
    {
        System.out.println("All saved filters:");
        RemoteFilter[] savedFilters = jiraSoapService.getSavedFilters(token);
        for (int i = 0; i < savedFilters.length; i++)
        {
            RemoteFilter filter = savedFilters[i];
            String description = filter.getDescription() != null ?  (": " + filter.getDescription()) :  "";
            System.out.println("\t" + filter.getName() + description);
        }
    }

    private static void testGetIssues(JiraSoapService jiraSoapService, String token)
            throws java.rmi.RemoteException
    {
        RemoteIssue[] issues = jiraSoapService.getIssuesFromFilter(token, FILTER_ID);
        for (int i = 0; i < issues.length; i++)
        {
            RemoteIssue issue = issues[i];
            System.out.println("issue.getSummary(): " + issue.getSummary());
        }
    }

    private static void testFindIssuesWithTerm(JiraSoapService jiraSoapService, String token, String term) throws java.rmi.RemoteException
    {
        long startTime = System.currentTimeMillis();
        RemoteIssue[] issuesFromTextSearch = jiraSoapService.getIssuesFromTextSearch(token, term);
        System.out.println(issuesFromTextSearch.length + " issues with term \"" + term + "\"");
        for (int i = 0; i < issuesFromTextSearch.length; i++)
        {
            RemoteIssue remoteIssue = issuesFromTextSearch[i];
            System.out.println("\t"+remoteIssue.getKey() + "\t" + remoteIssue.getSummary());
        }
        System.out.println("Time taken for search: " + (System.currentTimeMillis() -startTime) + "ms");
    }

    private static void testUpdateIssue(JiraSoapService jiraSoapService, String token, final String issueKey)
            throws java.rmi.RemoteException
    {
        testGetFieldsForEdit(jiraSoapService, token, issueKey);

        // Update the issue
        RemoteFieldValue[] actionParams = new RemoteFieldValue[]{
            new RemoteFieldValue("summary", new String[] {NEW_SUMMARY}),
            new RemoteFieldValue(CUSTOM_FIELD_KEY_1, new String[] {CUSTOM_FIELD_VALUE_1}),
            new RemoteFieldValue(CUSTOM_FIELD_KEY_2, new String[] {CUSTOM_FIELD_VALUE_2})};

        jiraSoapService.updateIssue(token, issueKey, actionParams);
    }

    private static void testGetFieldsForEdit(JiraSoapService jiraSoapService, String token, final String issueKey)
            throws java.rmi.RemoteException
    {
        // Editing the issue & getting the fields available on edit
        System.out.println("The issue " + issueKey + " has the following editable fields:");
        final RemoteField[] fieldsForEdit = jiraSoapService.getFieldsForEdit(token, issueKey);
        for (int i = 0; i < fieldsForEdit.length; i++)
        {
            RemoteField remoteField = fieldsForEdit[i];
            System.out.println("\tremoteField: " + remoteField.getId());
        }
    }

    private static void testAddComment(JiraSoapService jiraSoapService, String token, final String issueKey)
            throws java.rmi.RemoteException
    {
        // Adding a comment
        final RemoteComment comment = new RemoteComment();
        comment.setBody(NEW_COMMENT_BODY);
        jiraSoapService.addComment(token, issueKey, comment);
    }

    private static String testCreateProject(JiraSoapService jiraSoapService, String token)
            throws java.rmi.RemoteException
    {
        RemoteProject project = new RemoteProject();
        project.setKey(CREATE_PROJECT_KEY);
        project.setLead(LOGIN_NAME);
        project.setName(PROJECT_NAME);
        project.setDescription(PROJECT_DESCRIPTION);

        RemotePermissionScheme defaultPermScheme = new RemotePermissionScheme();
        defaultPermScheme.setId(new Long(0));
        project.setPermissionScheme(defaultPermScheme);

        RemoteProject returnedProject = jiraSoapService.createProjectFromObject(token, project);

        final String projectKey = returnedProject.getKey();
        System.out.println("Created project " + projectKey);
        jiraSoapService.deleteProject(token, returnedProject.getKey());
        System.out.println("Deleted project "+projectKey);

        return projectKey;
    }

    private static RemoteIssue testCreateIssue(JiraSoapService jiraSoapService, String token)
            throws java.rmi.RemoteException
    {
        // Create the issue
        RemoteIssue issue = new RemoteIssue();
        issue.setProject(PROJECT_KEY);
        issue.setType(ISSUE_TYPE_ID);

        issue.setSummary(SUMMARY_NAME);
        issue.setPriority(PRIORITY_ID);
        issue.setDuedate(Calendar.getInstance());
        issue.setAssignee("");

        // Add remote compoments
        RemoteComponent component = new RemoteComponent();
        component.setId(COMPONENT_ID);
        issue.setComponents(new RemoteComponent[]{component});

        // Add remote versions
        RemoteVersion version = new RemoteVersion();
        version.setId(VERSION_ID);
        RemoteVersion[] remoteVersions = new RemoteVersion[]{version};
        issue.setFixVersions(remoteVersions);

        // Add custom fields
        RemoteCustomFieldValue customFieldValue = new RemoteCustomFieldValue(CUSTOM_FIELD_KEY_1, "", new String[]{CUSTOM_FIELD_VALUE_1});
        RemoteCustomFieldValue customFieldValue2 = new RemoteCustomFieldValue(CUSTOM_FIELD_KEY_2, "", new String[]{CUSTOM_FIELD_VALUE_2});
        RemoteCustomFieldValue[] customFieldValues = new RemoteCustomFieldValue[] {customFieldValue, customFieldValue2};
        issue.setCustomFieldValues(customFieldValues);

        // Run the create issue code
        RemoteIssue returnedIssue = jiraSoapService.createIssue(token, issue);
        final String issueKey = returnedIssue.getKey();

        System.out.println("Successfully created issue " + issueKey);
        printIssueDetails(returnedIssue);

        return returnedIssue;
    }

    private static void printIssueDetails(RemoteIssue issue)
    {
        System.out.println("Issue Details");
        Method[] declaredMethods = issue.getClass().getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++)
        {
            Method declaredMethod = declaredMethods[i];
            if (declaredMethod.getName().startsWith("get") && declaredMethod.getParameterTypes().length == 0)
            {
                System.out.print("Issue." + declaredMethod.getName() + "() -> ");
                try
                {
                    Object o = declaredMethod.invoke(issue, new Object[]{});
                    if (o instanceof Object[])
                        System.out.println(printArray((Object[]) o));
                    else
                        System.out.println(o);
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String printArray(Object[] o)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < o.length; i++)
        {
            sb.append(o[i] + " ");
        }
        return sb.toString();
    }

    // Returns the contents of the file in a byte array.
    // From http://javaalmanac.com/egs/java.io/File2ByteArray.html
    private static byte[] getBytesFromFile(File file) throws IOException
    {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length < Integer.MAX_VALUE)
        {
            // Create the byte array to hold the data
            byte[] bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                   && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
            {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length)
            {
                throw new IOException("Could not completely read file " + file.getName());
            }

            // Close the input stream and return bytes
            is.close();
            return bytes;
        }
        else
        {
            System.out.println("File is too large");
            return null;
        }

    }

}
