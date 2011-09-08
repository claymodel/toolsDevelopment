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



#
# Dumping data for table 'TAdmin'
#

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
# Dumping data for table 'TGroup'
#

INSERT INTO TGroup VALUES("6", "office");
INSERT INTO TGroup VALUES("7", "sofware general");
INSERT INTO TGroup VALUES("8", "hardware general");
INSERT INTO TGroup VALUES("9", "other");


#
# Table structure for table 'TGroupmembers'
#

CREATE TABLE TGroupMembers (
  gmStaff int(11) NOT NULL default '0',
  gmGroup int(11) NOT NULL default '0',
  PRIMARY KEY  (gmGroup,gmStaff)
) TYPE=MyISAM;



#
# Dumping data for table 'TGroupmembers'
#

INSERT INTO TGroupMembers VALUES("10", "6");
INSERT INTO TGroupMembers VALUES("11", "6");
INSERT INTO TGroupMembers VALUES("10", "7");
INSERT INTO TGroupMembers VALUES("11", "7");
INSERT INTO TGroupMembers VALUES("10", "8");
INSERT INTO TGroupMembers VALUES("11", "8");
INSERT INTO TGroupMembers VALUES("10", "9");
INSERT INTO TGroupMembers VALUES("11", "9");


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
# Dumping data for table 'TMessage'
#

INSERT INTO TMessage VALUES("7", "2003-01-21 23:34:26", "Dear Team!<br />How can I format words as bold??<br />Yours\r\n Martin", "10", "1", "33");
INSERT INTO TMessage VALUES("8", "2003-01-21 23:35:49", "Dear Team!<br />I want to buy a new pc.\r\nDo you have some tips?\r\nIt should not be, i repeat, NOT be, a MAC!<br />Yours\r\nthomas", "11", "1", "34");
INSERT INTO TMessage VALUES("9", "2003-01-22 00:00:51", "Dear martin!<br />You should press the button with the bold F.<br />Yours\r\nIngo", "10", "0", "33");
INSERT INTO TMessage VALUES("10", "2003-01-22 00:04:52", "Dear thomas!<br />You should buy an intel 486/DX.<br />Yours \r\nfritzw", "11", "0", "34");
INSERT INTO TMessage VALUES("11", "2003-01-22 00:36:33", "That sounds great!!!", "11", "1", "34");


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
# Dumping data for table 'TStaff'
#

# SHA256 Passwd blabla
INSERT INTO TStaff VALUES("11", "Fritz", "Werner", "fritzw", "zK3ZmxbNPSAMItbbRdi2Yw7z2TZ2cSc0fsinarmSwuo=", "fritzw.jpg");
INSERT INTO TStaff VALUES("10", "Farcher", "Ingo", "ingo", "zK3ZmxbNPSAMItbbRdi2Yw7z2TZ2cSc0fsinarmSwuo=", "farche.jpg");


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



#
# Dumping data for table 'TStatus'
#

INSERT INTO TStatus VALUES("1", "new");
INSERT INTO TStatus VALUES("2", "on hold");
INSERT INTO TStatus VALUES("3", "processing");
INSERT INTO TStatus VALUES("4", "solved");


#
# Table structure for table 'tticket'
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
# Dumping data for table 'tticket'
#

INSERT INTO TTicket VALUES("33", "10", "10", "6", "2003-01-21 23:34:26", "2", "word 2k", "3");
INSERT INTO TTicket VALUES("34", "11", "11", "8", "2003-01-21 23:35:49", "4", "new pc", "3");


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



#
# Dumping data for table 'TUser'
#

# SHA256 passwd blabla
INSERT INTO TUser VALUES("10", "maier", "martin", "martin.maier@fh-joanneum.at", "martin", "", "", "", "", "0000-00-00", "", "zK3ZmxbNPSAMItbbRdi2Yw7z2TZ2cSc0fsinarmSwuo=", "1", "2003-01-21");
INSERT INTO TUser VALUES("11", "maschutznig", "thomas", "thomas.maschutznig@fh-joanneum.at", "thomas", "", "", "", "", "0000-00-00", "", "zK3ZmxbNPSAMItbbRdi2Yw7z2TZ2cSc0fsinarmSwuo=", "1", "2003-01-21");