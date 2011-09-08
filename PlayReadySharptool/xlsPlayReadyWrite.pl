#!/usr/bin/perl -w

use DBI;
use strict;
use Spreadsheet::ParseExcel;
use Spreadsheet::ParseExcel::FmtJapan;
use Spreadsheet::WriteExcel;
use Data::Dumper;
use Encode;

use warnings;

my $dbh = DBI->connect("DBI:mysql:database=playready;host=localhost", "username", "password", {'RaiseError' => 1});

my $sourcename = shift @ARGV;
my $destname = shift @ARGV or die "invocation: $0 <source file> <destination file>";

my $fmt = Spreadsheet::ParseExcel::FmtJapan->new(Code=>'sjis');
my $source_book = Spreadsheet::ParseExcel::Workbook->Parse($sourcename, $fmt) || die "can not open $sourcename";

my $storage_book;

foreach my $source_sheet_number (0 .. $source_book->{SheetCount}-1)
{
 my $source_sheet = $source_book->{Worksheet}[$source_sheet_number];

 print "--------- SHEET:", $source_sheet->{Name}, "\n";

 # sanity checking on the source file: rows and columns should be sensible
 next unless defined $source_sheet->{MaxRow};
 next unless $source_sheet->{MinRow} <= $source_sheet->{MaxRow};
 next unless defined $source_sheet->{MaxCol};
 next unless $source_sheet->{MinCol} <= $source_sheet->{MaxCol};

 my $queryStr2 = "";
 my($a, $b, $c, $d, $immediate);

 foreach my $row_index ($source_sheet->{MinRow} .. $source_sheet->{MaxRow})
 {
  foreach my $col_index ($source_sheet->{MinCol} .. $source_sheet->{MaxCol})
  {
   my $source_cell = $source_sheet->{Cells}[$row_index][$col_index];
   	
	if ($col_index == 0) {
	    $a = $source_cell->Value if($source_cell);	
	    if (length($a)==0) {
			$a = $immediate;
	    }
	    else {
			$immediate = $a;	
	    }

	    $storage_book->{$source_sheet->{Name}}->{$row_index}->{$col_index} = $a;	
	    $queryStr2 = "SELECT id,testid,result,comments FROM pvPlayReady WHERE testid = \"" . $a . "\"";
	    my $sth = $dbh->prepare($queryStr2);
	    $sth->execute();
	    while(my $ref = $sth->fetchrow_hashref()) {
		    $b = $ref->{'result'};
		    $c = $ref->{'comments'};
	    }
   	}
	elsif ($col_index == 1) {
		$storage_book->{$source_sheet->{Name}}->{$row_index}->{$col_index} = $b;
	}
	elsif ($col_index == 2) {
		$storage_book->{$source_sheet->{Name}}->{$row_index}->{$col_index} = $c;
	}
	else{
	  $storage_book->{$source_sheet->{Name}}->{$row_index}->{$col_index} = $source_cell->Value if($source_cell);
	}# end of if/else
  } # foreach col_index
 } # foreach row_index
} # foreach source_sheet_number

print "Perl recognized the following data (sheet/row/column order):\n";
print Dumper $storage_book;


my $fmt2 = Spreadsheet::ParseExcel::FmtJapan->new(Code=>'sjis');
my $dest_book = Spreadsheet::WriteExcel->new($destname)  || die "Can not open $destname to write.\n";

print "\n\nSaving recognized data in $destname...";

foreach my $sheet (keys %$storage_book)
{
  my $dest_sheet = $dest_book->addworksheet(decode("shiftjis", $sheet));
 foreach my $row (keys %{$storage_book->{$sheet}})
 {
  foreach my $col (keys %{$storage_book->{$sheet}->{$row}})
  {
   $dest_sheet->write($row, $col, decode("shiftjis",$storage_book->{$sheet}->{$row}->{$col}));
	#print $storage_book->{$sheet}->{$row}->{$col};
  } # foreach column
 } # foreach row
} # foreach sheet

$dest_book->close();

$dbh->disconnect();

print "Writing Done!\n";
