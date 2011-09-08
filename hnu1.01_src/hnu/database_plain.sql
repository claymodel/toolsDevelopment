# MySQL-Front Dump 2.5
#
# Host: localhost   Database: java
# --------------------------------------------------------
# Server version 4.0.7-gamma-nt


#
# Table structure for table 'TAdmin'
#

CREATE TABLE TAdmin (
  aId int(11) NOT NULL auto_increment,
  aLogin varchar(15) NOT NULL default '',
  aPass varchar(46) NOT NULL default '',
  PRIMARY KEY  (aId),
  UNIQUE KEY UC_aId (aId),
  UNIQUE KEY UC_aLogin (aLogin)
) TYPE=MyISAM;

# SHA256 Password admin
INSERT INTO TAdmin VALUES("1", "admin", "jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=");


#
# Table structure for table 'TGroup'
#

CREATE TABLE TGroup (
  gId int(11) NOT NULL auto_increment,
  gText varchar(50) NOT NULL default '',
  PRIMARY KEY  (gId),
  UNIQUE KEY UC_gId (gId),
  UNIQUE KEY UC_gText (gText)
) TYPE=MyISAM;

#
# Table structure for table 'TGroupmembers'
#

CREATE TABLE TGroupMembers (
  gmStaff int(11) NOT NULL default '0',
  gmGroup int(11) NOT NULL default '0',
  PRIMARY KEY  (gmGroup,gmStaff)
) TYPE=MyISAM;


#
# Table structure for table 'TMessage'
#

CREATE TABLE TMessage (
  mId int(11) NOT NULL auto_increment,
  mDate datetime NOT NULL default '0000-00-00 00:00:00',
  mText text NOT NULL,
  mAuthor int(11) NOT NULL default '0',
  mIsUser tinyint(4) NOT NULL default '0',
  mTicket int(11) NOT NULL default '0',
  PRIMARY KEY  (mId),
  UNIQUE KEY UC_mId (mId)
) TYPE=MyISAM;


#
# Table structure for table 'TStaff'
#

CREATE TABLE TStaff (
  sId int(11) NOT NULL auto_increment,
  sName varchar(30) NOT NULL default '',
  sFirst varchar(30) NOT NULL default '',
  sLogin varchar(15) NOT NULL default '',
  sPass varchar(46) NOT NULL default '',
  sPic mediumtext,
  PRIMARY KEY  (sId),
  UNIQUE KEY UC_sId (sId),
  UNIQUE KEY UC_sLogin (sLogin)
) TYPE=MyISAM;


#
# Table structure for table 'TStatus'
#

CREATE TABLE TStatus (
  stId int(11) NOT NULL auto_increment,
  stText varchar(20) NOT NULL default '',
  PRIMARY KEY  (stId),
  UNIQUE KEY UC_stId (stId),
  UNIQUE KEY UC_stText (stText)
) TYPE=MyISAM;

INSERT INTO TStatus VALUES("1", "new");
INSERT INTO TStatus VALUES("2", "on hold");
INSERT INTO TStatus VALUES("3", "processing");
INSERT INTO TStatus VALUES("4", "solved");


#
# Table structure for table 'TTicket'
#

CREATE TABLE TTicket (
  tId int(11) NOT NULL auto_increment,
  TUser int(11) NOT NULL default '0',
  TStaff int(11) NOT NULL default '0',
  TGroup int(11) NOT NULL default '0',
  tDate datetime default NULL,
  tPriority tinyint(4) NOT NULL default '5',
  tSubject varchar(70) NOT NULL default '',
  TStatus int(11) NOT NULL default '0',
  PRIMARY KEY  (tId),
  UNIQUE KEY UC_tId (tId)
) TYPE=MyISAM;


#
# Table structure for table 'TUser'
#

CREATE TABLE TUser (
  uId int(11) NOT NULL auto_increment,
  uName varchar(30) NOT NULL default '',
  uFirst varchar(30) NOT NULL default '',
  uMail varchar(50) NOT NULL default '',
  uLogin varchar(15) default NULL,
  uStreet varchar(40) default NULL,
  uZip varchar(7) default NULL,
  uCity varchar(40) default NULL,
  uCountry varchar(30) default NULL,
  uDob date default NULL,
  uTelephone varchar(20) default NULL,
  uPass varchar(46) NOT NULL default '',
  uActive tinyint(4) default '0',
  uDate date NOT NULL default '0000-00-00',
  PRIMARY KEY  (uId),
  UNIQUE KEY UC_uId (uId),
  KEY UC_uLogin (uLogin)
) TYPE=MyISAM;