#!/usr/bin/perl
###############################################################################
# IMPORTANT
#  Define CATALOG_ID1 and CATALOG_ID2 to the IDs of the two fee schedules you
#  want to add entries for.
#  These are referenced in the generateSQL method all the way at the bottom.

$CATALOG_ID1 = 1;
$CATALOG_ID2 = 2;

# Read in the Procedure Code Master Listing...
open (PCML, 'im-codes.txt') or die "Cannot find the Procedure Code Master Listing in im-codes.txt";
my @pcmlContents = <PCML>;
close PCML;

my $numRecords = ($#pcmlContents + 1) / 3;
print STDERR "Total number of data lines read: ", $#pcmlContents + 1, "\n";
print STDERR "Total number of procedure code records: ", $numRecords, "\n";

my %pcmlRecords = ();

# Split up each record line into corresponding fields ...
for my $recIdx (0..$numRecords) {
	my $lineIdx = $recIdx * 3;

	my $recordInfo = dismemberLine (\@pcmlContents, $lineIdx);
	$pcmlRecords {$recIdx} = $recordInfo;
}

# Process records ...
# Sample processing: Pretty Print records to STDOUT
foreach my $record (sort keys %pcmlRecords) {
	my $recordInfo = $pcmlRecords {$record};

	##############################################################################
	# Table: Offering_Catalog_Entry
	#
	# catalog_id (user-defined), entry_type (100 - CPT), status (1 - active)
	# Handle: code
	formatCode ($recordInfo);

	# cost_type (1 - specific dollar)
	# Handle: unit_cost
	formatCost ($recordInfo) if (exists $recordInfo -> {procCode});

	# Handle: description
	formatDescription ($recordInfo) if (exists $recordInfo -> {procCode});

	my $sqlStatement = generateSQL ($recordInfo) if (exists $recordInfo -> {procCode});
	
	if (defined $recordInfo -> {procCode}) {
		foreach my $sqlKey (sort keys %{$sqlStatement}) {
			print $sqlStatement -> {$sqlKey}, ";\n";
		}
	}

	
#	print "Record Key: $record = {\n";
#	foreach my $infoKey (sort keys %{$recordInfo}) {
#		print "\t$infoKey => $recordInfo->{$infoKey}\n";
#	}
#	print "}\n\n";
}



sub dismemberLine {
	my ($pcmlContents, $lineIdx) = @_;

	my $recLine1 = $pcmlContents -> [$lineIdx];
	my $recLine2 = $pcmlContents -> [$lineIdx + 1];
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

	return $recordInfo;
}


sub formatCode {
	my ($recordInfo) = @_;

	my $procCode = $recordInfo -> {procCode};
	
	# delete the procCode element unless it has only digits in it and has 5 or more digits
	delete $recordInfo -> {procCode} unless ($procCode =~ /\d{5,}/);
}


sub formatCost {
	my ($recordInfo) = @_;
}


sub formatDescription {
	my ($recordInfo) = @_;

	my $desc = $recordInfo -> {description};
	my @descriptionWord = split / +/, $desc;
	my @description = map { ucfirst lc } @descriptionWord;

	$recordInfo -> {description} = join ' ', @description;
}


sub generateSQL {
	my ($recordInfo) = @_;
	my $sqlStatement;

	my $offeringCatalogEntrySQLTemplate = 'insert into Offering_Catalog_Entry (catalog_id, entry_type, status, code, cost_type, unit_cost, description) values (%d, 100, 1, \'%s\', 1, %f, \'%s\')';

	$sqlStatement -> {offeringCatalogEntry1} = sprintf $offeringCatalogEntrySQLTemplate, $CATALOG_ID1, $recordInfo -> {procCode}, $recordInfo -> {amount1}, $recordInfo -> {description};
	$sqlStatement -> {offeringCatalogEntry2} = sprintf $offeringCatalogEntrySQLTemplate, $CATALOG_ID2, $recordInfo -> {procCode}, $recordInfo -> {accepted1}, $recordInfo -> {description};

	return $sqlStatement;
}
