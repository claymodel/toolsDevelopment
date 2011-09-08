exec { "HOME-down":
	cwd => "/opt",
	command => "/usr/bin/wget http://hasnatlnx01/home.zip",
	creates => "/opt/home.zip",
}

exec { "DATA-down":
        cwd => "/opt",
        command => "/usr/bin/wget http://hasnatlnx01/data.zip",
        creates => "/opt/data.zip",
}

exec { "HOME-install":
    cwd => "/opt",
    command => "/usr/bin/unzip  /opt/home.zip",
    creates => "/opt/atlassian-jira-4.3.4-standalone",
  }

exec { "DATA-install":
    cwd => "/opt",
    command => "/usr/bin/unzip  /opt/data.zip",
    creates => "/opt/jira-data-434",
  }

exec { "sh-permission":
    cwd => "/opt/atlassian-jira-4.3.4-standalone/bin",
    command => "/bin/chmod 777 /opt/atlassian-jira-4.3.4-standalone/bin/*.sh",
  }
