#!/usr/bin/perl

# Read in the Procedure Code Master Listing...
open (PML, 'im-md.txt') or die "Cannot find the Physician Master Listing in im-md.txt";
my @pmlContents = <PML>;
close PML;

my $numRecords = $#pmlContents + 1;
print "Total number of data lines read: ", $#pmlContents + 1, "\n";
print "Total number of procedure code records: ", $numRecords, "\n";

my %pmlRecords;

# Split up each record line into corresponding fields ...
for my $recIdx (0..$numRecords) {
	my $lineIdx = $recIdx;

	my $recLine = $pmlContents [$lineIdx];

	my $recordInfo;
	$recordInfo -> {number} = substr $recLine, 1, 8;
	$recordInfo -> {name} = substr $recLine, 10, 21;
	$recordInfo -> {idNumber} = substr $recLine, 32, 27;
	$recordInfo -> {ssn} = substr $recLine, 60, 15;
	$recordInfo -> {address} = substr $recLine, 76, 60;
	$recordInfo -> {telephone} = substr $recLine, 137, 11;
	$recordInfo -> {refPhysicianFax} = substr $recLine, 149, 13;
	$recordInfo -> {specialty} = substr $recLine, 163, 13;

	# Trim each field to remove spaces
	foreach my $field (sort keys %{$recordInfo}) {
		my $fieldContents = $recordInfo -> {$field};
		$fieldContents =~ s/^ +//;
		$fieldContents =~ s/ +$//;

		$recordInfo -> {$field} = $fieldContents;
	}
	
	$pmlRecords {$recIdx} = $recordInfo;
}

# Process records ...
# Sample processing: Pretty Print records to STDOUT
foreach my $record (sort keys %pmlRecords) {
	my $recordInfo = $pmlRecords {$record};

	print "Record Key: $record = {\n";
	foreach my $infoKey (sort keys %{$recordInfo}) {
		print "\t$infoKey => $recordInfo->{$infoKey}\n";
	}
	print "}\n\n";
}
