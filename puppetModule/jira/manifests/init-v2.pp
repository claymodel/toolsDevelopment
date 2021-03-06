class jira{

	Exec['INSTALL-down'] -> Exec['mysql-3']

	exec { "INSTALL-down":
		cwd => "/",
		command => "/usr/bin/wget http://hasnatlnx01/install.zip",
		creates => "/install.zip",
	}

	exec { "HOME-down":
        	cwd => "/",
	        command => "/usr/bin/wget http://hasnatlnx01/home.zip",
	        creates => "/home.zip",
	}

	exec { "INSTALL-unzip":
		cwd => "/opt",
		command => "/usr/bin/unzip  /install.zip",
    		creates => "/opt/atlassian-jira-4.3.4-standalone",
  	}

	exec { "HOME-unzip":
		cwd => "/opt",
		command => "/usr/bin/unzip  /data.zip",
		creates => "/opt/jira-data-434",
	}

	exec { "EXEC-permission":
		cwd => "/opt/atlassian-jira-4.3.4-standalone/bin",
		command => "/bin/chmod 777  /opt/atlassian-jira-4.3.4-standalone/bin/*.sh",
	}

	exec { "mysql-1":
	    command => "/usr/bin/mysql -uroot -phasnat -e \"create database jiradb_4_3_4 character set utf8;\"",
	}

	exec { "mysql-2":
		command => "/usr/bin/mysql -uroot -phasnat -e \"grant all on jiradb_4_3_4.* to 'root'@'localhost' identified by 'hasnat';\"",
	}

	exec { "mysql-3":
		command => "/usr/bin/mysql -uroot -phasnat jiradb_4_3_4 < /opt/atlassian-jira-4.3.4-standalone/jiradb_4_3_4_backup.Aug3.sql",
	}
}

