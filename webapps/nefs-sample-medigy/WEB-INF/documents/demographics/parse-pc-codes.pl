#!/usr/bin/perl

# Read in the Procedure Code Master Listing...
open (PCML, 'pc-codes.txt') or die "Cannot find the Procedure Code Master Listing in pc-codes.txt";
my @pcmlContents = <PCML>;
close PCML;

my $numRecords = ($#pcmlContents + 1) / 3;
print "Total number of data lines read: ", $#pcmlContents + 1, "\n";
print "Total number of procedure code records: ", $numRecords, "\n";

my %pcmlRecords;

# Split up each record line into corresponding fields ...
for my $recIdx (0..$numRecords) {
	my $lineIdx = $recIdx * 3;

	my $recLine1 = $pcmlContents [$lineIdx];
	my $recLine2 = $pcmlContents [$lineIdx + 1];

	my $recordInfo;
	$recordInfo -> {procCode} = substr $recLine1, 1, 10;
	$recordInfo -> {c} = substr $recLine1, 12, 1;
	$recordInfo -> {description} = substr $recLine1, 14, 40;

	$recordInfo -> {t0} = substr $recLine1, 55, 1;
	$recordInfo -> {amount0} = substr $recLine1, 57, 7;
	$recordInfo -> {accepted0} = substr $recLine1, 65, 8;
	
	$recordInfo -> {code1} = substr $recLine1, 74, 10;
	$recordInfo -> {t1} = substr $recLine1, 85, 1;
	$recordInfo -> {amount1} = substr $recLine1, 87, 7;
	$recordInfo -> {accepted1} = substr $recLine1, 96, 8;

	$recordInfo -> {code2} = substr $recLine1, 105, 10;
	$recordInfo -> {t2} = substr $recLine1, 116, 1;
	$recordInfo -> {amount2} = substr $recLine1, 118, 7;
	$recordInfo -> {accepted2} = substr $recLine1, 127, 8;
	
	$recordInfo -> {code3} = substr $recLine2, 74, 10;
	$recordInfo -> {t3} = substr $recLine2, 85, 1;
	$recordInfo -> {amount3} = substr $recLine2, 87, 7;
	$recordInfo -> {accepted3} = substr $recLine2, 96, 8;

	$recordInfo -> {code4} = substr $recLine2, 105, 10;
	$recordInfo -> {t4} = substr $recLine2, 116, 1;
	$recordInfo -> {amount4} = substr $recLine2, 118, 7;
	$recordInfo -> {accepted4} = substr $recLine2, 127, 8;

	# Trim each field to remove spaces
	foreach my $field (sort keys %{$recordInfo}) {
		my $fieldContents = $recordInfo -> {$field};
		$fieldContents =~ s/^ +//;
		$fieldContents =~ s/ +$//;

		$recordInfo -> {$field} = $fieldContents;
	}
	
	$pcmlRecords {$recIdx} = $recordInfo;
}

# Process records ...
# Sample processing: Pretty Print records to STDOUT
foreach my $record (sort keys %pcmlRecords) {
	my $recordInfo = $pcmlRecords {$record};

	print "Record Key: $record = {\n";
	foreach my $infoKey (sort keys %{$recordInfo}) {
		print "\t$infoKey => $recordInfo->{$infoKey}\n";
	}
	print "}\n\n";
}
