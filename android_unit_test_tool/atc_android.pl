#!/usr/bin/perl
use Cwd;

my $cwd;
my $switch;
my $testexe;
my $filesnames;
my $testoutput = 'test_output_android.txt';
my $flags;

sub open_shell {
        #open up the shell
        open(ADB, "| ./adb shell\n");
        print ADB "cd /sdcard\n";
	print ADB "rm $testoutput\n";
	print ADB "rm *.dat\n";
}

sub save_log {
	$cwd = getcwd(); 
	chdir("$cwd");
	open(PULL, "./adb pull /sdcard/$testoutput temp.log |");
	close PULL;
}

sub close_shell {
	print ADB "exit\n";
	close ADB;
}


#generate the command file
#call the configfile creator


$switch = $ARGV[0];
if( $switch ) 
{
	if (scalar(@ARGV) != 2)
	{
	   print "usage:\n";
	   print "atc_android.pl [test_exe] [text_file_containing_commands] \n";
	   exit 1;
	}
	$testexe = $ARGV[0];
	$filesnames = $ARGV[1];
}

open( FILE, "$filesnames" ) or die "Can't open $filenames : $!";

if ( -e $testoutput )  
{
print "$testoutput already exists\n";
unlink "$testoutput";
print "$testoutput deleted\n";
}

$numTestsRun = 0;
while( <FILE> )
{
    	s/#.*//;            # ignore comments by erasing them
	next if /^(\s)*$/;  # skip blank lines
    	chomp;              # remove trailing newline characters
    	push @lines, $_;    # push the data line onto the array
    	my @splitCommand = split(/:;/, $_);
	$testNumber = $splitCommand[0];
	$flags=$splitCommand[2];
	$numTestsRun++;
	open_shell();  
	print "\n\nStart test case $numTestsRun ....\n";
        #print "Running $splitCommand[0] $splitCommand[1] $splitCommand[2] ...\n";
	#print ADB  "$testexe -output $testoutput -test $testNumber $testNumber -source $splitCommand[1] $flags\n";
	print ADB  "$testexe -output $testoutput -test $testNumber $testNumber $splitCommand[1] $flags\n";
        close_shell();	
	save_log();
	open(OUTFILE, ">> $testoutput")or die "Can't open $filenames : $!";
	print OUTFILE "\nStart test case $numTestsRun ....\n";
 	system("cat temp.log >> $testoutput");	
	print OUTFILE "%%%%%END%%%%%\n";
	close OUTFILE;	
	print "Done....\n\n";
}

close FILE;
print "Number of Test Cases Run: $numTestsRun\n";
print "\nAll Done\n";
system( "perl test_result.pl $testoutput" );
