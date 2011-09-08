class jira::restoredb{
	Exec['mysql-1'] -> Exec['mysql-2']
	Exec['mysql-2'] -> Exec['mysql-3']
	
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
