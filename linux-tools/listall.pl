#!/usr/bin/perl
use strict;

sub readdirR{
    my ($dir) = @_;
    opendir DIR, $dir || warn "Cannot open directory $dir: $!";
    my @files = readdir DIR ;
    closedir DIR;
    @files = grep {$_ !~ /^(\.|\.\.)$/} @files;

    my @simpleFiles = grep -f, (map {"$dir/$_"} @files);
    my @directories = grep -d, (map {"$dir/$_"} @files);
    my @recursiveFileList = map {readdirR ($_)} @directories;

    return (@simpleFiles, @directories,  @recursiveFileList);
}

##main program###########
$" = "\n";
my @FDToRead = @ARGV;

if (!@FDToRead)
   {@FDToRead = ".";}

my @simpleFiles = grep -f, @FDToRead;
my @directories = grep -d, @FDToRead;
my @recursiveFiles = map {readdirR ($_);} @directories;

my @allFiles = sort (@FDToRead, @recursiveFiles);
print "@allFiles\n";
