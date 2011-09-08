class jirabackup{

	Exec['DB-backup'] -> Exec['HOME-scp']

	exec { "DB-backup":
		cwd => "/opt/atlassian-jira-4.3.4-standalone",
		command => "/usr/bin/mysqldump -u root -p jiradb_4_3_4 > jiradb_4_3_4.sql",
		creates => "/opt/atlassian-jira-4.3.4-standalone/jiradb_4_3_4.sql",
	}

	exec { "INSTALL-zip":
        	cwd => "/opt",
	        command => "/usr/bin/zip -r install.zip /opt/atlassian-jira-4.3.4-standalone/",
	        creates => "/opt/atlassian-jira-4.3.4-standalone/install.zip",
	}

	exec { "HOME-zip":
		cwd => "/opt",
		command => "/usr/bin/zip -r home.zip /opt/jira-data-434/",
    		creates => "/opt/jira-data-434/home.zip",
  	}


	exec { "INSTALL-scp":
		cwd => "/opt",
		command => "/bin/bin/scp /opt/install.zip root@hasnatlnx01:/var/www/nginx-default/",
	}

	exec { "HOME-scp":
		cwd => "/opt",
		command => "/bin/bin/scp /opt/home.zip root@hasnatlnx01:/var/www/nginx-default/",
	}

}

