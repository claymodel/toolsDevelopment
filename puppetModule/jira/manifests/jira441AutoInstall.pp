class jira::jira441AutoInstall{
 
    Exec['DB-backup'] -> Exec['Jira4.4.1-Scilent-Install']
 
    exec { "DB-backup":
        cwd => "{jira::params::ins_loc}",
        command => "/usr/bin/mysqldump -uroot -p***** {jira::params::db_name} > {jira::params::db_name}.sql",
        creates => "{jira::params::ins_loc}/{jira::params::db_name}.sql",
    }
 
    exec { "INSTALL-zip":
            cwd => "/opt",
            command => "/usr/bin/zip -r install.zip {jira::params::ins_loc}/",
            creates => "{jira::params::ins_loc}/install.zip",
    }
 
    exec { "HOME-zip":
        cwd => "/opt",
        command => "/usr/bin/zip -r home.zip {jira::params::home_loc}/",
            creates => "{jira::params::home_loc}/home.zip",
    }
 
 
    exec { "INSTALL-scp":
        cwd => "/opt",
        command => "/bin/bin/scp /opt/install.zip root@hasnatlnx01:/var/www/nginx-default/",
    }
 
    exec { "HOME-scp":
        cwd => "/opt",
        command => "/bin/bin/scp /opt/home.zip root@hasnatlnx01:/var/www/nginx-default/",
    }
 
    exec { "Jira4.4.1-INSTALL-download":
        cwd => "/usr/local/src/",
        command => "/usr/bin/wget http://hasnatlnx01/atlassian-jira-4.4.1-x64.bin",
        creates => "/usr/local/src/atlassian-jira-4.4.1-x64.bin",
    }
 
    exec { "Jira4.4.1-Scilent-Config-Copy":
        cwd => "/usr/local/src/",
        command => "/bin/cp {jira::params::ins_loc}/.install4j/response.varfile /usr/local/src/",
        creates => "/usr/local/src/response.varfile",
    }
 
    exec { "Jira4.4.1-Scilent-Install":
        cwd => "/usr/local/src/",
        command => "/bin/sh /usr/local/src/atlassian-jira-4.4.1-x64.bin -q -varfile response.varfile",
        creates => "/usr/local/src/.install4j/response.varfile",
    }
 
}
