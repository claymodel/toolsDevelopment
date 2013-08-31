class mariadb (
  $version = hiera( 'mysql_version', '' )
) {

  if ( ( $mariadb::version == '' ) or ( $mariadb::version == undef ) ) {
    fail( 'Invalid or no version supplied' )
  }

  $distro_lc = inline_template("<%= operatingsystem.downcase %>")
  $repo_version = inline_template("<%=@version.to_s.match(/\d+.\d+/)[0] %>")
  $distro_url = "http://mirrors.supportex.net/mariadb/repo/${repo_version}/${distro_lc}"

  if ( ( $::operatingsystem != 'debian' ) and ( $::operatingsystem != 'ubuntu' ) ) {
    fail( "Distro ${::operatingsystem} not supported." )
  }

  apt::repository { "mariadb":
    url => $distro_url,
    distro => $::lsbdistcodename,
    repository=> 'main',
    key => 'dbart@askmonty.org',
    key_url => '"http://keyserver.ubuntu.com:11371/pks/lookup?op=get&search=0xCBCB082A1BB943DB"',
 }

  exec {'mariadb_aptgetupdate':
    command => 'apt-get update',
    onlyif => "/bin/bash -c 'x=\$(apt-cache madison ${mysql::package} | \
grep \"${distro_url}\" | wc -l); test \"\$x\" = \"0\" -a \"\$x\" != \"\" '"
  }

  Apt::Repository['mariadb'] -> Exec['mariadb_aptgetupdate'] -> Package['mysql']


}
