#!/usr/bin/perl

# Read in the Procedure Code Master Listing...
open (DEMO, 'pc-demog.txt') or die "Cannot find the Demographics Data in pc-demog.txt";
my @demoContents = <DEMO>;
chomp @demoContents;
close DEMO;

my $numRecords = ($#demoContents + 1) / 60;
print "Total number of data lines read: ", $#demoContents + 1, "\n";
print "Total number of demographic records: ", $numRecords, "\n";

my %demoRecords;

# Split up each record line into corresponding fields ...
for my $recIdx (0..$numRecords) {
	my $lineIdx = $recIdx * 60;

	my $recordInfo;
	$recordInfo -> {patientName}        = $demoContents [$lineIdx + 0];
	$recordInfo -> {patientAddress}     = $demoContents [$lineIdx + 1];
	$recordInfo -> {patientCityState}   = $demoContents [$lineIdx + 2];
	$recordInfo -> {patientZip}         = $demoContents [$lineIdx + 3];
	$recordInfo -> {accountNum}         = $demoContents [$lineIdx + 4];
	$recordInfo -> {sex}                = $demoContents [$lineIdx + 5];
	$recordInfo -> {ssn}                = $demoContents [$lineIdx + 6];
	$recordInfo -> {birthdate}          = $demoContents [$lineIdx + 7];
	$recordInfo -> {age}                = $demoContents [$lineIdx + 8];
	$recordInfo -> {guarantorName}      = $demoContents [$lineIdx + 9];
	$recordInfo -> {guarantorAddress}   = $demoContents [$lineIdx + 10];
	$recordInfo -> {guarantorCityState} = $demoContents [$lineIdx + 11];
	$recordInfo -> {guarantorZip}       = $demoContents [$lineIdx + 12];
	$recordInfo -> {guarantorTelephone} = $demoContents [$lineIdx + 13];
	$recordInfo -> {maritalStatus}      = $demoContents [$lineIdx + 14];
	$recordInfo -> {patientNumber}      = $demoContents [$lineIdx + 15];
	$recordInfo -> {doctorName}         = $demoContents [$lineIdx + 16];
	$recordInfo -> {currentDate}        = $demoContents [$lineIdx + 17];
	$recordInfo -> {workTelephone}      = $demoContents [$lineIdx + 18];
	$recordInfo -> {insuredName}        = $demoContents [$lineIdx + 19];
	$recordInfo -> {insuredAddress}     = $demoContents [$lineIdx + 20];
	$recordInfo -> {insuredCityState}   = $demoContents [$lineIdx + 21];
	$recordInfo -> {employerName}       = $demoContents [$lineIdx + 22];
	$recordInfo -> {comp1Address1}      = $demoContents [$lineIdx + 23];
	$recordInfo -> {comp1Name}          = $demoContents [$lineIdx + 24];
	$recordInfo -> {comp1Policy}        = $demoContents [$lineIdx + 25];
	$recordInfo -> {comp1Group}         = $demoContents [$lineIdx + 26];
	$recordInfo -> {comp1Relat}         = $demoContents [$lineIdx + 27];

	# Trim each field to remove spaces
	foreach my $field (sort keys %{$recordInfo}) {
		my $fieldContents = $recordInfo -> {$field};
		$fieldContents =~ s/^ +//;
		$fieldContents =~ s/ +$//;

		$recordInfo -> {$field} = $fieldContents;
	}
	
	$demoRecords {$recIdx} = $recordInfo;
}

# Process records ...
# Sample processing: Pretty Print records to STDOUT
foreach my $record (sort keys %demoRecords) {
	my $recordInfo = $demoRecords {$record};

	print "Record Key: $record = {\n";
	foreach my $infoKey (sort keys %{$recordInfo}) {
		print "\t$infoKey => $recordInfo->{$infoKey}\n";
	}
	print "}\n\n";
}
