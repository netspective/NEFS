#!/usr/bin/perl

# Read in the Procedure Code Master Listing...
my @demoContents;
open (DEMO, 'im-demog.txt') or die "Cannot find the Demographics Data in im-demog.txt";
@demoContents = <DEMO>;
chomp @demoContents;
close DEMO;

my $numRecords = ($#demoContents + 1) / 66;
print STDERR "Total number of data lines read: ", $#demoContents + 1, "\n";
print STDERR "Total number of demographic records: ", $numRecords, "\n";

my %demoRecords;

# Split up each record line into corresponding fields ...
for my $recIdx (0..$numRecords) {
	my $lineIdx = $recIdx * 66;

	my $recordInfo = dismemberLine (\%demoRecords, $lineIdx);

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
# Generate SQL from each record ...

foreach my $record (sort keys %demoRecords) {
	my $recordInfo = $demoRecords {$record};

	#############################################################################
	# Table: Person
	# 
	# person_id, person_ref, ssn, name_prefix formatted correctly
	# Handle: name_first, name_middle, name_last, name_suffix
	formatPatientName ($recordInfo);
	
	# Handle: gender
	formatGender ($recordInfo);

	# Handle: maritalStatus
	formatMaritalStatus ($recordInfo);

	# Handle: date_of_birth
	formatDOB ($recordInfo);

	# age formatted correctly
	#
	# ###########################################################################
	
	#############################################################################
	# Table: Person_Address
	#
	# parent_id, address_name formatted correctly
	# Handle: addressLine1
	formatAddressLine1 ($recordInfo);

	# line2 formatted correctly (non-existant)
	# Handle: city, state
	formatCityState ($recordInfo);

	# zip formatted correctly
	#
	# ###########################################################################
	my $sqlStatement = generateSQL ($recordInfo);

	# Figure out if guarantor == person himself or someone else in the db...
#	my $tempGuarantorName = $recordInfo -> {guarantorName};
#	foreach my $tempRec (sort keys %demoRecords) {
#		my $tempRecordInfo = $demoRecords {$tempRec};
#
#		if ($tempRecordInfo -> {patientName} eq $tempGuarantorName && $tempRec != $record) {
#			$recordInfo -> {guarantorPatientID} = $tempRecordInfo -> {accountNum} . " $tempRec";
#			last;
#		}
#	}
	
	my @outputAttributeList = qw(patientName guarantorName guarantorAddress guarantorCityState guarantorZip guarantorTelephone patientNumber doctorName currentDate workTelephone insuredName insuredAddress insuredCityState employerName comp1Address1 comp1Name comp1Policy comp1Group comp1Relat);
#	print "Record Key: $record = {\n";
#	foreach my $infoKey (sort keys %{$recordInfo}) {
#	foreach my $infoKey (@outputAttributeList) {
#		print "\t$infoKey => $recordInfo->{$infoKey}\n";
#	}

#	foreach my $sqlKey (sort keys %{$sqlStatement}) {
#		print "\t$sqlKey => $sqlStatement->{$sqlKey}\n";
#	}
#	print "}\n\n";

	foreach my $sqlKey (sort keys %{$sqlStatement}) {
		print $sqlStatement -> {$sqlKey}, ";\n";
	}
}


sub dismemberLine {
	my ($demoContents, $lineIdx) = @_;
	my $recordInfo;

	my @attributeList = qw(patientName patientAddress patientCityState patientZip accountNum sex ssn birthdate age guarantorName guarantorAddress guarantorCityState guarantorZip guarantorTelephone maritalStatus patientNumber doctorName currentDate workTelephone insuredName insuredAddress insuredCityState employerName comp1Address1 comp1Name comp1Policy comp1Group comp1Relat);

	for ($i = 0; $i <= $#attributeList; $i ++) {
		$recordInfo -> {$attributeList [$i]} = $demoContents [$lineIdx + $i]
	}
	
	return $recordInfo;
}



sub formatPatientName {
	my ($recordInfo) = @_;

	my ($tempLastName, $tempFirstName) = split ', *', $recordInfo -> {patientName};
	
	if ($tempLastName =~ / /) {
		# Last name contains a suffix?
		# Split them up
		my ($tempLastName, $tempSuffix) = split ' ', $tempLastName;

		$tempLastName = ucfirst (lc ($tempLastName));
		$tempSuffix = ucfirst (lc ($tempSuffix));

		$recordInfo -> {patientLastName} = $tempLastName;
		$recordInfo -> {patientSuffix} = $tempSuffix;
	} else {
		$recordInfo -> {patientLastName} = ucfirst (lc ($tempLastName));
	}

	if ($tempFirstName =~ / /) {
		# First name contains a middle initial or name?
		# Split them up
		my ($tempFirstName, $tempRestNames) = split ' ', $tempFirstName, 2;
		
		$tempFirstName = ucfirst (lc ($tempFirstName));
		$tempRestNames = ucfirst (lc ($tempRestNames));

		$recordInfo -> {patientFirstName} = $tempFirstName;
		$recordInfo -> {patientMiddleName} = $tempRestNames;
	} else {
		$recordInfo -> {patientFirstName} = ucfirst (lc ($tempFirstName));
	}
}


sub formatGender {
	my ($recordInfo) = @_;

	my %tempGenderHash = ( 'm' => 0, 'f' => 1 );
	my $tempGender = $recordInfo -> {sex};
	if ($tempGender =~ /m/ || $tempGender =~ /f/) {
		$recordInfo -> {gender} = $tempGenderHash {$tempGender};
	} else {
		$recordInfo -> {gender} = 3;
	}
}


sub formatMaritalStatus {
	my ($recordInfo) = @_;

	my %tempMaritalStatusHash = ( 'o' => 0, 's' => 1, 'm' => 2 );
	my $tempMaritalStatus = $recordInfo -> {maritalStatus};
	if ($tempMaritalStatus =~ /m/ || $tempMaritalStatus =~ /s/) {
		$recordInfo -> {maritalStatus} = $tempMaritalStatusHash {$tempMaritalStatus};
	} else {
		$recordInfo -> {maritalStatus} = 0;
	}
}


sub formatDOB {
	my ($recordInfo) = @_;
	my %monthAbbrev = ( '00' => 'Jan', '01' => 'Jan', '02' => 'Feb', '03' => 'Mar', '04' => 'Apr', '05' => 'May', '06' => 'Jun', '07' => 'Jul', '08' => 'Aug', '09' => 'Sep', '10' => 'Oct', '11' => 'Nov', '12' => 'Dec' );

	my ($tempDOBMonth, $tempDOBDate, $tempDOBYear) = split '/', $recordInfo -> {birthdate};

	$tempDOBMonth = '00' unless ($tempDOBMonth =~ /^\d+$/);
	$tempDOBMonth = '0'.$tempDOBMonth unless (length ($tempDOBMonth) == 2);
	$tempDOBDate = '00' unless ($tempDOBDate =~ /^\d+$/);
	$tempDOBDate = '0'.$tempDOBDate unless (length ($tempDOBDate) == 2);
	$tempDOBYear = '00' unless ($tempDOBYear =~ /^\d+$/);
	$tempDOBYear = '00' if ($tempDOBYear > 99);
	$tempDOBYear = '19' . $tempDOBYear;

	$recordInfo -> {dateOfBirth}  = $tempDOBDate . '-' . $monthAbbrev {$tempDOBMonth} . '-' . $tempDOBYear;
}


sub formatAddressLine1 {
	my ($recordInfo) = @_;

	my $tempAddress = $recordInfo -> {patientAddress};
	my @addressWord = split / +/, $tempAddress;
	@address = map { ucfirst lc } @addressWord;

	$recordInfo -> {patientAddressLine1} = join ' ', @address;
}


sub formatCityState {
	my ($recordInfo) = @_;

	my $tempCityState = $recordInfo -> {patientCityState};
	my ($tempCity, $tempState) = split /, +/, $tempCityState;

	$recordInfo -> {patientCity} = ucfirst lc $tempCity;
	$recordInfo -> {patientState} = uc $tempState;
}


sub generateSQL {
	my ($recordInfo) = @_;
	my $sqlStatement;
	
	my $personSQLTemplate = 'insert into Person (person_id, person_ref, ssn, name_prefix, name_first, name_middle, name_last, name_suffix, gender, marital_status, date_of_birth, age) values (\'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', %d, %d, \'%s\', %d)';
	
	$sqlStatement -> {sqlPerson} = sprintf $personSQLTemplate, $recordInfo -> {accountNum}, $recordInfo -> {accountNum}, $recordInfo -> {ssn}, $recordInfo -> {patientNamePrefix}, $recordInfo -> {patientFirstName}, $recordInfo -> {patientMiddleName}, $recordInfo -> {patientLastName}, $recordInfo -> {patientSuffix}, $recordInfo -> {gender}, $recordInfo -> {maritalStatus}, $recordInfo -> {dateOfBirth}, $recordInfo -> {age};

	my $personAddressSQLTemplate = 'insert into Person_Address (parent_id, address_name, line1, line2, city, state, zip) values (\'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\')';

	$sqlStatement -> {sqlPersonAddress} = sprintf $personAddressSQLTemplate, $recordInfo -> {accountNum}, 'Home', $recordInfo -> {patientAddressLine1}, '', $recordInfo -> {patientCity}, $recordInfo -> {patientState}, $recordInfo -> {patientZip};

	return $sqlStatement;
}
