#!/usr/bin/perl -w

use DBI;
use strict;
use Spreadsheet::ParseExcel;

my $dbh = DBI->connect("DBI:mysql:database=playready;host=localhost", "username", "password", {'RaiseError' => 1});

my $oExcel = new Spreadsheet::ParseExcel;
my $sExcel = new Spreadsheet::ParseExcel;


die "You must provide a filename to $0 to be parsed as an Excel file" unless @ARGV;

my $oBook = $oExcel->Parse($ARGV[0]);
my($iR, $iC, $oWkS, $oWkC, $a, $b, $c, $queryStr);


for(my $iSheet=0; $iSheet < $oBook->{SheetCount} ; $iSheet++)
{
 $oWkS = $oBook->{Worksheet}[$iSheet];

 for(my $iR = $oWkS->{MinRow} ;
     defined $oWkS->{MaxRow} && $iR <= $oWkS->{MaxRow} ;
     $iR++)
 {
  my $queryStr = "";	
  for(my $iC = $oWkS->{MinCol} ;
      defined $oWkS->{MaxCol} && $iC <= $oWkS->{MaxCol} ;
      $iC++)
  {
   $oWkC = $oWkS->{Cells}[$iR][$iC];
   if($iC == 0) 	{	$a = $oWkC->Value if($oWkC);	}
   elsif ($iC == 9) 	{	$b = $oWkC->Value if($oWkC);	}
   elsif ($iC == 10)	{	$c = $oWkC->Value if($oWkC);	}
  }
	$queryStr = "INSERT INTO pvPlayReady (testid,result,comments) VALUES (\"" . $a . "\",\"" . $b . "\",\"" . $c . "\")";
	#print $queryStr, "\n";
	my $rows = $dbh->do($queryStr);
	
	#print "$rows row(s) affected\n";
 }
}

my $deleteEmpty = $dbh->do("DELETE FROM pvPlayReady WHERE testid = \"\"");	

$dbh->disconnect();
print "Reading Done!\n";

