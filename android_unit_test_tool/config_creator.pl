#!/usr/bin/perl -w 

my $numArgs = $#ARGV + 1;

if ( $numArgs == 0) {
	print "usage: perl config_greator.pl 123 2-30\n";
}
foreach my $argnum (0 .. $#ARGV) {
	my @pieces = split("-",$ARGV[$argnum]);
	my $from = sprintf("%d",$pieces[0]);
	my $to = sprintf("%d",$pieces[1]);
	if ($from > $to) {
		my $tmp = $from;
		$from = $to;
		$to = $tmp; 
	}
	if ($from == 0) {
		$from = $to;
	}
	for(my $counter = $from; $counter <= $to; $counter++){
		print "$counter:;\n";
	}
}

