#!/usr/bin/perl
#
# Script to parse gi-demog.txt and generate XML to feed into Medigy
#

use URI::Escape;

sub storeItem ($$) {
	my ($store, $item) = @_;

	return 0 unless (defined $item && 'HASH' eq ref $item);

	my $id = $item->{ID};
	return undef unless (defined $id);

	my ($type, undef) = split /_/, $id, 2;

	$store->{$type} = {} unless (defined $store->{$type});
	
	if (defined $store->{$type} && 'HASH' eq ref $store->{$type}) {
		# Store it if it's not already there
		unless (defined $store->{$type}->{$id} && 'HASH' eq ref $store->{$type}->{$id}) {
			$store->{$type}->{$id} = $item;
		}

		# Let's see if it has any sub-records

		foreach my $field (keys %{$item}) {
			next unless (defined $item->{$field});

			if ('HASH' eq ref $item->{$field} && defined $item->{$field}->{ID}) {
				my $subRecordId = $item->{$field}->{ID};
				storeItem ($store, $item->{$field});

				# Store an IDREF attribute here somewhere...
				$store->{$type}->{$id}->{$field} = "IDREF:$subRecordId";
			} else {
				# This is not a sub-record
				# Update any data that might be here but not in $store

				unless (defined $store->{$type}->{$id}->{$field}) {
					$store->{$type}->{$id}->{$field} = $item->{$field};
				}
			}
		}
	}
}

sub parseRecord (@) {
	my (
		$patientName, $patientAddress, $patientCity, $patientZip, $patientAccountNum,
		$patientGender, $patientSSN, $patientBirthdate, $patientAge,
		$guarantorName, $guarantorAddress, $guarantorCity, $guarantorZip, $guarantorPhone,
		$patientMaritalStatus, $patientId,
		$doctorName,
		$currentDate,
		$patientWorkPhone,
		$insuredName, $insuredAddress, $insuredCity,
		$employerName,
		$insuranceCompanyAddress, $insuranceCompanyName, $insurancePolicyNumber,
		$insuranceGroupNumber, $relationshipToInsured, undef) = trim (@_);

	my ($physician, $insured, $employer, $insurance, $policy, $guarantor, $patient) = undef;
	my ($physicianID, $insuredID, $employerID, $insuranceID, $policyID, $guarantorID, $patientID) = undef;

	if ($doctorName =~ /,\s+MD\s*$/i) {
		$doctorName =~ s/,\s+MD\s*$//;
		my @doctorName = split /\s+/, $doctorName;

		my $lastName = $doctorName[-1];
		$#doctorName --;

		$doctorName = "$lastName, ".(join (' ', @doctorName));
	}

	unless ($doctorName =~ /^\s*$/) {
		$physician = {
			'rec-stat-id' => 'Active',
			'name-first' => xmlEscape (parseName ($doctorName, 'first')),
			'name-middle' => xmlEscape (parseName ($doctorName, 'middle')),
			'name-last' => xmlEscape (parseName ($doctorName, 'last')),
			'name-suffix' => xmlEscape (parseName ($doctorName, 'suffix')),
		};

		$physicianID = xmlEscape (generateID ('PERSON', $physician, qw/name-first name-middle name-last name-suffix/));
		$physician->{ID} = $physicianID if (defined $physicianID);
	}
	
	unless ($insuredName =~ /^\s*$/) {
		$insured = {
			'rec-stat-id' => 'Active',
			'name-first' => xmlEscape (parseName ($insuredName, 'first')),
			'name-middle' => xmlEscape (parseName ($insuredName, 'middle')),
			'name-last' => xmlEscape (parseName ($insuredName, 'last')),
			'name-suffix' => xmlEscape (parseName ($insuredName, 'suffix')),
		};

		$insuredID = xmlEscape (generateID ('PERSON', $insured, qw/name-first name-middle name-last name-suffix/));
		$insured->{ID} = $insuredID if (defined $insuredID);

		unless ($insuredAddress =~ /^\s*$/ || $insuredCity =~ /^\s*$/) {
			my $address = {
				'rec-stat-id' => 'Active',
				'address-name' => 'Main Address',
				'address-type-id' => 'Primary',
				'line1' => xmlEscape (parseAddress ($insuredAddress)),
				'city' => xmlEscape (parseCity ($insuredCity, 'city')),
				'state-id' => xmlEscape (parseCity ($insuredCity, 'state')),
				'zip' => xmlEscape (parseCity ($insuredCity, 'zip')),
			};

			$addressID = xmlEscape (generateID ('PERSONADDRESS', $address, qw/line1 city state-id/));
			$address->{ID} = $addressID if (defined $addressID);

			$insured->{'person-address'} = $address;
		}
	}

	unless ($employerName =~ /^\s*$/) {
		$employer = {
			'rec-stat-id' => 'Active',
			'org-code' => xmlEscape ("ORG_" . singleWord ($employerName)),
			'org-name' => xmlEscape (headingCase ($employerName)),

			'org-classification' => {
				'rec-stat-id' => 'Active',
				'org-type-id' => 'Employer',
			},
		};

		$employerID = xmlEscape (generateID ('ORG', $employer, qw/org-name/));
		$employer->{ID} = $employerID if (defined $employerID);
	};
	
	unless ($insuranceCompanyName =~ /^\s*$/) {
		$insurance = {
			'rec-stat-id' => 'Active',
			'org-code' => xmlEscape ("ORG_" . singleWord ($insuranceCompanyName)),
			'org-name' => xmlEscape (headingCase ($insuranceCompanyName)),
		
			'org-classification' => {
				'rec-stat-id' => 'Active',
				'org-type-id' => 'Insurance',
			},
		};

		$insuranceID = xmlEscape (generateID ('ORG', $insurance, qw/org-name/));
		$insurance->{ID} = $insuranceID if (defined $insuranceID);

		unless ($insuranceCompanyAddress =~ /^\s*$/) {
			my $address = {
				'rec-stat-id' => 'Active',
				'address-name' => 'Main Address',
				'address-type-id' => 'Primary',
				'line1' => xmlEscape (parseAddress ($insuranceCompanyAddress)),
			};

			$addressID = xmlEscape (generateID ('ORGADDRESS', $address, qw/line1 city state-id/));
			$address->{ID} = $addressID if (defined $addressID);

			$insurance->{'org-address'} = $address;
		}
	};
	
	$policy = {};

	unless ($guarantorName =~ /^\s*$/) {
		$guarantor = {
			'rec-stat-id' => 'Active',
			'name-first' => xmlEscape (parseName ($guarantorName, 'first')),
			'name-middle' => xmlEscape (parseName ($guarantorName, 'middle')),
			'name-last' => xmlEscape (parseName ($guarantorName, 'last')),
			'name-suffix' => xmlEscape (parseName ($guarantorName, 'suffix')),
		};

		$guarantorID = xmlEscape (generateID ('PERSON', $guarantor, qw/name-first name-middle name-last name-suffix/));
		$guarantor->{ID} = $guarantorID if (defined $guarantorID);

		unless ($guarantorAddress =~ /^\s*$/ || $guarantorCity =~ /^\s*$/) {
			my $address = {
				'rec-stat-id' => 'Active',
				'address-name' => 'Main Address',
				'address-type-id' => 'Primary',
				'line1' => xmlEscape (parseAddress ($guarantorAddress)),
				'city' => xmlEscape (parseCity ($guarantorCity, 'city')),
				'state-id' => xmlEscape (parseCity ($guarantorCity, 'state')),
				'zip' => xmlEscape ($guarantorZip),
			};

			$addressID = xmlEscape (generateID ('PERSONADDRESS', $address, qw/line1 city state-id/));
			$address->{ID} = $addressID if (defined $addressID);

			$guarantor->{'person-address'} = $address;
		}

		if ($guarantorPhone =~ /\d{3}[^0-9]*\d{3}[^0-9]*\d{4}/) {
			my $contact = {
				'rec-stat-id' => 'Active',
				'method-type' => xmlEscape ('Telephone: Home'),
				'method-value' => xmlEscape ($guarantorPhone),
			};

			my $contactID = xmlEscape (generateID ('PERSONCONTACT', $contact, qw/method-type method-value/));
			$contact->{ID} = $contactID if (defined $contactID);

			$guarantor->{'person-contact'} = $contact;
		};
	}

	unless ($patientName =~ /^\s*$/) {
		$patient = {
			'rec-stat-id' => 'Active',
			'name-first' => xmlEscape (parseName ($patientName, 'first')),
			'name-middle' => xmlEscape (parseName ($patientName, 'middle')),
			'name-last' => xmlEscape (parseName ($patientName, 'last')),
			'name-suffix' => xmlEscape (parseName ($patientName, 'suffix')),
			'ssn' => xmlEscape ($patientSSN),
			'gender-id' => xmlEscape (parseGender ($patientGender)),
			'marital-status-id' => xmlEscape (parseMaritalStatus ($patientMaritalStatus)),
			'birth-date' => xmlEscape ($patientBirthdate),
			'age' => xmlEscape ($patientAge),
		};

		$patientID = xmlEscape (generateID ('PERSON', $patient, qw/name-first name-middle name-last name-suffix/));
		$patient->{ID} = $patientID if (defined $patientID);

		unless ($patientAddress =~ /^\s*$/ || $patientCity =~ /^\s*$/) {
			my $address = {
				'address-name' => 'Main Address',
				'address-type-id' => 'Primary',
				'line1' => xmlEscape (parseAddress ($patientAddress)),
				'city' => xmlEscape (parseCity ($patientCity, 'city')),
				'state-id' => xmlEscape (parseCity ($patientCity, 'state')),
				'zip' => xmlEscape ($patientZip),
				'country' => 'USA',
			};

			$addressID = xmlEscape (generateID ('PERSONADDRESS', $address, qw/line1 city state-id/));
			$address->{ID} = $addressID if (defined $addressID);

			$patient->{'person-address'} = $address;
		}

		if ($patientWorkPhone =~ /\d{3}[^0-9]*\d{3}[^0-9]*\d{4}/) {
			my $contact = {
				'method-type' => xmlEscape ('Telephone: Work'),
				'method-value' => xmlEscape ($patientWorkPhone),
			};
		
			my $contactID = xmlEscape (generateID ('PERSONCONTACT', $contact, qw/method-type method-value/));
			$contact->{ID} = $contactID if (defined $contactID);

			$patient->{'person-contact'} = $contact;
		}
	};

	return ($patient, $guarantor, $physician, $insured, $employer, $insurance, $policy);
}

sub generateID ($$@) {
	my ($type, $hash, @keyList) = @_;

	my $id = undef;
	my @idCmp = map { (defined $hash->{$_} && $hash->{$_} !~ /^\s*$/) ? uc singleWord ($hash->{$_}) : () } @keyList;

	unshift @idCmp, $type;

	$id = join ('_', @idCmp);
	$id =~ s/[^A-Z0-9]/_/g;

	return $id;
}

sub xmlEscape ($) {
	my ($text) = @_;

	my $finalText = $text;
	$finalText =~ s/&/&amp;/;
	$finalText =~ s/</&lt;/;
	$finalText =~ s/>/&gt;/;

	return $finalText;
}

sub singleWord ($) {
	my ($text) = @_;

	my $finalText = uc $text;
	$finalText =~ s/ +/ /g;
	$finalText =~ s/ /_/g;
	$finalText =~ s/\s//g;

	return $finalText;
}

sub trim ($) {
	my (@text) = @_;

	my @finalText = ();

	foreach my $input (@text) {
		my $output = $input;

		$output =~ s/\s+/ /g;
		$output =~ s/^ //;
		$output =~ s/ $//;

		push @finalText, $output;
	}

	return (wantarray ? @finalText : \@finalText);
}

sub parseName ($$) {
	my ($name, $type) = @_;

	my ($prefix, $firstName, $middleName, $lastName, $suffix) = ();

	($lastName, $firstName) = split /, */, $name;
	($lastName, $suffix) = split / +/, $lastName, 2 if ($lastName =~ / /);
	($firstName, $middleName) = split / +/, $firstName, 2 if ($firstName =~ / /);

	return (ucfirst (lc $lastName)) if ('last' eq $type);
	return (ucfirst (lc $firstName)) if ('first' eq $type);
	return (ucfirst (lc $middleName)) if ('middle' eq $type);
	return (ucfirst (lc $suffix)) if ('suffix' eq $type);

	return (ucfirst (lc $firstName));
}

sub parseCity ($$) {
	my ($city, $type) = @_;

	$city =~ s/\s+/ /g;
	$city =~ s/^ //;
	$city =~ s/ $//;

	my ($cityName, $stateZip, $stateName, $zipCode) = ();
	($cityName, $stateZip) = split /,\s+/, $city;
	($stateName, $zipCode) = split /\s+(?=\d{5})/, $stateZip;

	return (headingCase ($cityName)) if ('city' eq $type);
	return (uc $stateName) if ('state' eq $type);
	return $zipCode if ('zip' eq $type);

	return (ucfirst (lc $cityName));
}

sub headingCase ($) {
	my ($text) = @_;

	return join (' ', map { ucfirst lc } (split / +/, $text));
}

sub parseAddress ($) {
	my ($address) = @_;

	return headingCase ($address);
}

sub parseGender ($) {
	my ($gender) = @_;

	my %genderHash = ( 'm' => 'Male', 'f' => 'Female' );

	return $genderHash{$gender} if (exists $genderHash{$gender});
	return 'Not applicable';
}

sub parseMaritalStatus ($) {
	my ($maritalStatus) = @_;

	my %maritalStatusHash = ( 's' => 'Single', 'm' => 'Married' );

	return $maritalStatusHash{$maritalStatus} if (exists $maritalStatusHash{$maritalStatus});
	return 'Unknown';
}

sub readFile ($$) {
	my ($contents, $filename) = @_;

	my @fileContents = ();

	open (INPUTFILE, $filename) or die "Couldn't open $filename for reading";
	@fileContents = <INPUTFILE>;
	close INPUTFILE;

	chomp @fileContents;

	push @{$contents}, @fileContents;

	return \@fileContents;
}

sub parseFileContents ($) {
	my ($contents) = @_;

	my $data = {};
	my $recLen = 66;
	my $numRecords = int ((scalar @$contents) / $recLen);

	foreach my $rec (0..$numRecords) {
		my $contentIdx = $recLen * $rec;

		my @rec = map { $contents->[$_] } ($contentIdx..($contentIdx + $recLen - 1));
		my @record = parseRecord (@rec);

		foreach my $item (@record) {
			storeItem ($data, $item);
		}
	}

	return $data;
}

sub displayData ($) {
	my ($data) = @_;

	my %tagMap = ( ORG => 'org', PERSON => 'person', );
	my @displayOrder = qw/ORG PERSON/;
	my $displayCount = {};

	# Let's create a structure to store information about which data item has been displayed
	# already since those items only need to maintain the IDREF tags in their XML.  The rest
	# should be printed out in full as their are encountered.
	
	print qq[<?xml version="1.0" encoding="UTF-8"?>\n];
	print qq[<!DOCTYPE dal SYSTEM "db-import.dtd">\n];
	print qq[<dal>\n];
	foreach my $type (@displayOrder) {
		next unless (defined $data->{$type});

		my $tag = $tagMap {$type};

		foreach my $item (keys %{$data->{$type}}) {
			my $xml = generateXML ($displayCount, $data, 1, $tag, $data->{$type}->{$item}, 1);
			print $xml;
		}
	}
	print qq[</dal>\n];
}

sub generateXML ($$$$$$) {
	my ($displayCount, $data, $indentLevel, $tag, $hash, $style) = @_;
	$indentLevel ||= 0;
	$style ||= 0;

	my $elementID = $hash->{ID};
	my ($type, undef) = split /_/, $elementID, 2;

	if (defined $displayCount->{$type}->{$elementID}) {
		$displayCount->{$type}->{$elementID} ++;
	} else {
		$displayCount->{$type}->{$elementID} = 1;
	}

	my $indent = ('	' x $indentLevel);
	my $xml = '';
	my @simpleAttributeList = ();
	my @subElementList = ();

	foreach my $field (keys %{$hash}) {
		if (ref ($hash->{$field})) {
			# Definitely sub-element 
			push @subElementList, $field;
		} elsif ($hash->{$field} =~ /^IDREF:(.+)$/) {
			# Probably sub-element 
			# Has the element pointed to by this IDREF been rendered already?

			my $id = $1;
			my ($type, undef) = split /_/, $id, 2;

			if (defined $displayCount->{$type}->{$id} && $displayCount->{$type}->{$id} > 0) {
				push @simpleAttributeList, $field;
			} else {
				push @subElementList, $field;
			}
		} else {
			# Definitely simple attribute 
			push @simpleAttributeList, $field;
		}
	}

	$xml .= "$indent<$tag";

	if (0 == $style) {
		if (0 < scalar @simpleAttributeList) {
			$xml .= ' '.join (' ', map { $_.'="'.$hash->{$_}.'"' } sort @simpleAttributeList);
		}

		$xml .= ">";
	} else {
		$xml .= ">\n";

		if (0 < scalar @simpleAttributeList) {
			$xml .= join ("\n", map { "$indent	<$_>".$hash->{$_}."</$_>" } sort @simpleAttributeList);
		}
	}

	$xml .= "\n";

	if (0 < scalar @subElementList) {
		my @subElementXML = ();

		foreach my $subElement (sort @subElementList) {
			my $elementXML = '';
			
			if ($hash->{$subElement} =~ /^IDREF:(.+)$/) {
				my $id = $1;
				my ($type, undef) = split /_/, $id, 2;

				$elementXML = generateXML ($displayCount, $data, $indentLevel + 1, $subElement, $data->{$type}->{$id}, $style);
			} else {
				$elementXML = generateXML ($displayCount, $data, $indentLevel + 1, $subElement, $hash->{$subElement}, $style);
			}

			push @subElementXML, $elementXML;
		}
		
		$xml .= join ("\n", @subElementXML);
		$xml .= "\n";
	}

	$xml .= "$indent</$tag>\n";

	return $xml;
}

################################################################################
# Main Procedure
################################################################################

my $fileContents = [];

foreach my $file (@ARGV) {
	readFile ($fileContents, $file);
}

my $data = parseFileContents ($fileContents);
displayData ($data);

1;
