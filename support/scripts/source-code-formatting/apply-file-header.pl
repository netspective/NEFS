#!/usr/bin/perl -w

use File::Find;
use vars qw($ENV $ROOT_PATH @JAVA_LICENSE);

sub main
{
	$ROOT_PATH = $ENV{'NETSPECTIVE_FRAMEWORKS_HOME'} || 'c:\Projects\Frameworks';
		
	die "NETSPECTIVE_FRAMEWORKS_HOME environment variable not provided." unless $ROOT_PATH;
	die "NETSPECTIVE_FRAMEWORKS_HOME environment variable value is invalid." unless -d $ROOT_PATH;
	
	open(JAVA_LICENSE, "java-license.txt") || die;
	@JAVA_LICENSE = <JAVA_LICENSE>;
	close(JAVA_LICENSE);
	
	find(\&processJava, $ROOT_PATH);	
}

sub processJava
{
	return unless m/\.java$/;
	
	my @fileContents = ();
	my @fileContentsAfterPackageDecl = ();
	
	open(SOURCE, $File::Find::name) || warn "Can't open $File::Find::name: $!\n";
	@fileContents = <SOURCE>;
	close(SOURCE);

	my $foundPkgDecl = 0;
	foreach(@fileContents)
	{
		$foundPkgDecl = 1 if m/^package /;
		push(@fileContentsAfterPackageDecl, $_) if $foundPkgDecl;
	}

	replaceSourceFile($File::Find::name, \@fileContentsAfterPackageDecl);
}

sub replaceSourceFile
{
    my ($fileName, $sourceCode) = @_;

	open(DEST, ">$fileName");
	print DEST @JAVA_LICENSE;
	print DEST @$sourceCode;
	close(DEST);
}

main();
