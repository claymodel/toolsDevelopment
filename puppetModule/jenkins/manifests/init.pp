class jenkins{
    Exec['repo-Download'] -> Exec['repo-Import']
 
    exec { "repo-Download":
            cwd => "/usr/local/src",
            command => "/usr/bin/wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins-ci.org/redhat/jenkins.repo",
            creates => "/etc/yum.repos.d/jenkins.repo",
    }
 
    exec { "repo-Import":
            command => "/bin/rpm --import http://pkg.jenkins-ci.org/redhat/jenkins-ci.org.key",
    }
 
    package { "jenkins":
           ensure => installed,
    }
 
    service { "jenkins":
           ensure => running,
           hasstatus => true,
           require => package["jenkins"],
    }
 
}