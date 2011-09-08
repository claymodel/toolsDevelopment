#!/usr/bin/perl

my $switch;
my $filesnames;
my $testoutput;
my $flags=0;
my $delimitor=',';
my $lineDelimit=\n;
my $catch;
my $result;

$switch = $ARGV[0];
if( $switch ) 
{
	if (scalar(@ARGV) != 1)
	{
	   print "usage:\n";
	   print "test_result.pl [test_Output_pv_engine.txt] \n";
	   exit 1;
	}

	$filesnames = $ARGV[0];
	
}

open( FILE, "< $filesnames" ) or die "Can't open $filenames : $!";

open(OUTFILE, ">output_pv_author.csv")or die "Can't open $filenames : $!";

$numTestsRun = 1;
@lines="Test#, Test case Name, Result , Error Details";

print OUTFILE "@lines\n\n";
 


while( <FILE> )
{
	s/#.*//;            # ignore comments by erasing them
    next if /^(\s)*$/;  # skip blank lines

    chomp;              # remove trailing newline characters

	$linedata = $_;

	if ($flags =~ /1/)
	{
		if (/Errors:/)
		{
			  $flags=0;
			  push @lines, $delimitor;
			 

		}
		else 
		{
			if (/numAllocFails/)
			{
				  $flags=0;
				  my $null = substr $linedata, 0, 200;
				  push @lines, $null; 
				 # print "@lines\n\n"; 

				
				 
			}
			else
			{
				my $null = substr $linedata, 0, 200;
				!#  print "$null,";
				  push @lines, $null;    # push the data line onto the array
				  			
			}
		}
	}
	if (/Memory Stats:/)
	{

		#	  my $null = substr $linedata, 0, 100;
		 # print "$null,";
		#  push @lines, $null;    # push the data line onto the array
		#  push @lines, $delimitor;
		# $flags=1;
	}

   if (/Starting Test/)
	{
		@lines=" ";
		  my $null = substr $linedata, 14, 70;
		!#  print "$null,";
		  push @lines, $null;    # push the data line onto the array
		  push @lines, $delimitor;
	}

	if (/Number of tests/)
	{

		  my $null = substr $linedata, 16, 50;
		!#  print "$null,";
		  push @lines, $null;    # push the data line onto the array
		  push @lines, $delimitor;
	}

	if (/  Successes /)
	{
			my $null = substr $linedata, 12, 1 ;
			if ($null =~ /-/){ 	# Sometimes success is shown as -1.
		   			my $null = substr $linedata, 12, 2 ;
					push @lines, $null;    # push the data line onto the array
					push @lines, $delimitor;
					$catch = "$null";
					#print "$linedata\n";
			} else {
					$catch = "nothing";
			}
	}

	if (/, Failures /)
	{
		  if ($catch == "-1") {
			$result = "FAIL, negative";
                  	push @lines, $result;    # push the data line onto the array
                  	push @lines, $delimitor;
                  	print OUTFILE "@lines\n\n";
		  } elsif ($catch == "nothing") {
			my $null = substr $linedata, 12, 1 ;
                        if ($null == "0") {
                                $result = "FAIL, zero";
                       		push @lines, $result;    # push the data line onto the array
                        	push @lines, $delimitor;
                        	print OUTFILE "@lines\n\n";
                        } else {
				my $null1 = substr $linedata, 24, 1;
				#print "$null1\n";
				if ($null1 =~ /-/){
					$result = "FAIL, negative-error ";
					push @lines, $result;
					push @lines, $delimitor;
                                        print OUTFILE "@lines\n\n";
				} else {
                                	$result = "PASS";
                       			push @lines, $result;    # push the data line onto the array
                        		push @lines, $delimitor;
                        		print OUTFILE "@lines\n\n";
				}
                        }
		}
	}
   	$numTestsRun++;
}
close OUTFILE;
close FILE;

