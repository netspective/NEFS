header{
package com.netspective.junxion.edi.format.sef;

/**
 * Parser for the EDISIM SEF (Standard Export Format) v1.0 - v1.5
 */
 
import com.netspective.junxion.edi.element.Segment;
import com.netspective.junxion.edi.element.Occurrence;
import com.netspective.junxion.edi.element.DataElement;
import com.netspective.junxion.edi.element.CompositeDataElement;
import com.netspective.junxion.edi.element.data.BasicDataElement;
import com.netspective.junxion.edi.element.basic.BasicSegment;
import com.netspective.junxion.edi.element.collection.SegmentsCollection;
import com.netspective.junxion.edi.element.collection.DataElementsCollection;
import com.netspective.junxion.edi.element.collection.CompositeDataElementsCollection;
import com.netspective.junxion.edi.format.InterchangeFormat;
 
}

class StandardExchangeFormatParser extends Parser;

options 
{
	k = 15;
}

{
	protected StandardExchangeFormat format = new StandardExchangeFormat();
	protected Segment activeParentSegment = format;
	protected DataElementsCollection activeParentDataElems = format;
	protected SegmentsCollection segmentsDict = format.getSegmentsDictionary();
	protected DataElementsCollection dataElemsDict = format.getDataElementsDictionary();
	protected CompositeDataElementsCollection dataElemGroupsDict = format.getCompositeDataElementsDictionary();

	/**Actual segment group counter*/
	protected int sgCounter = 0;

	/**Multiplicator for elements at the first level below
	* the segments, this is set by the repeat_structure
	*/
	protected int structureRepeatMultiplicator = 1;


	/**Returns the next possible segment group name
	* for a found segment group*/
	protected String getNextSGName()
	{
		return( "SG" + this.sgCounter++ );
	}	
	
	protected void finalizeFormat()
	{
	}
	
	public InterchangeFormat getFormat()
	{
		return format;
	}
}

format: (version)?
		(init)?
		(std)? sets segs (coms)? elms 
		(codes)? (valref)? (text)? 
		{
			//create the structure of the created hashmaps
			finalizeFormat();
		}
		EOF;

version: DOT "VER" eatup;

eatupall: (~SINGLE_NEWLINE)* SINGLE_NEWLINE;

//eat up the line until the CR..this
//will eat up all but the equal sign
eatup: (eatupspecial (LBRACE|RBRACE)* )*
		SINGLE_NEWLINE;

eatupspecial: PLUS|COMMA|TOKEN|DOT|HASH|PIPE|PERCENT|TILDE|BS
			|LPAREN|RPAREN|QUOTE|APOS|AT|COLON|TIMES|MINUS
			|"VER"|"INI"
			|"PRIVATE"|"PUBLIC"|"STD"|"SETS"|"SEGS"|"COMS"|"ELMS"
			|"CODES"|"TEXT"|"VALREFS"; 


init: DOT "INI" SINGLE_NEWLINE eatup 
	  (priv (ignoresection)* pub)*;

ignoresection: DOT TOKEN eatup;

priv: DOT "PRIVATE" eatup;

pub: DOT "PUBLIC" SINGLE_NEWLINE;

std: DOT "STD" (COMMA TOKEN)*;

//structure of the message, only one set per input file
//is computed
sets: DOT "SETS" SINGLE_NEWLINE 
	(messagedefinition)+;

//only one message per file is computed!!
messagedefinition:
	 messageName:TOKEN EQUAL 
	( (HUETCHEN)? (changeinc)? (structure_sg | structure_seg ) )+ 
	SINGLE_NEWLINE;

//main structure of SEF elements: could be used
//for segments
structure_seg: 
	LBRACE (prefix)? segName:TOKEN 
	{ 
		int maxRepeat = -1;

		Segment segment = new BasicSegment();
		segment.setIdentifier(segName.getText());
		activeParentSegment.addSegment(segment);
	} 
	(TIMES maskNumber:TOKEN)? (AT TOKEN)? 
	( COMMA (requirement:TOKEN
		{
			//Mandatory?
			if( requirement.getText().equals( "M" ))
				segment.setRequired(true);
		}
	)? (COMMA 
	(maxRepeat=repeat)? )? )?
	RBRACE
	{
		segment.setMaxRepeat(maxRepeat);
	};


//main structure of SEF elements: could be used
//for elements in the first level below the segment
structure_firstlevel: 
	LBRACE (prefix)? elementName:TOKEN 
	{
		int maxRepeat = 1;

		//all elements that match from the .COMS section have to overwrite the
		//type later

		DataElement dataElement = new BasicDataElement();
		dataElement.setIdentifier(elementName.getText());
		dataElement.setMaxRepeat(maxRepeat);
		activeParentSegment.addDataElement(dataElement);	
	}
	(TIMES maskNumber:TOKEN)? (AT TOKEN)? 
	( COMMA (requirement:TOKEN
		{
			//Mandatory?
			if( requirement.getText().equals( "M" ))
				dataElement.setRequired(true);
		}
	)? 
		(COMMA (maxRepeat=repeat)? )? 
	)?
	RBRACE{
		dataElement.setMaxRepeat(maxRepeat);
	};


//main structure of SEF elements: could be used
//for dataelementgroups
structure_deg: 
	LBRACE (prefix)? degName:TOKEN 
	{
		int maxRepeat = 1;
		
		CompositeDataElement dataElemGroup = new com.netspective.junxion.edi.element.data.CompositeDataElement();
		dataElemGroup.setIdentifier(degName.getText());
		activeParentSegment.addCompositeDataElement(dataElemGroup);
	} 
	(TIMES maskNumber:TOKEN)? (AT TOKEN)? 
	( SEMICOLON (minLength:TOKEN
		{
			dataElemGroup.setMinLength(new Integer( minLength.getText() ).intValue());
		}

	  )? COLON maxLength:TOKEN 
		{
			dataElemGroup.setMaxLength(new Integer( maxLength.getText() ).intValue());
		})?
	( COMMA (requirement:TOKEN
		{
			//Mandatory?
			if( requirement.getText().equals( "M" ))
				dataElemGroup.setRequired(true);
		}
		)? 
		(COMMA (maxRepeat=repeat
		{
			dataElemGroup.setMaxRepeat(maxRepeat);
		})? )? 
	)?
	RBRACE;

//structure of a segment group
structure_sg:
	LBRACKET
	{
		int maxRepeatValue = 1;
		String groupName = null;
	}
	( (loopId:TOKEN
	{
		groupName = loopId.getText();
	}	
	)?
	  COLON maxRepeatValue=repeat )?
	{
		if( groupName == null )
			groupName = this.getNextSGName();
		
		Segment segmentGroup = new BasicSegment();
		segmentGroup.setIdentifier(groupName);
		segmentGroup.setMaxRepeat(maxRepeatValue);
		activeParentSegment.addSegment(segmentGroup);
		activeParentSegment = segmentGroup;			
	}
	( (HUETCHEN)? (changeinc)? (structure_sg | structure_seg ) )+
	RBRACKET
	{
		//end of segment group: go up one level to the parent
		activeParentSegment = (Segment) segmentGroup.getParent();
	};


repeat
returns [int returnValue = 1]:
	{ returnValue = 1; }
	(repeatNo:TOKEN 
		{
			returnValue = new Integer( repeatNo.getText()).intValue();
		}
		| 
		GT TOKEN
		{
			returnValue = Occurrence.INFINITE_REPEAT;
		});

//change the position increment, could be for
//example +10 or -20
changeinc: (PLUS | MINUS ) TOKEN;


//prefix before a structure name
prefix: ( AT | EXCLA | AND | MINUS | DOT );

//description which elements are in a segment
segs: DOT "SEGS" SINGLE_NEWLINE (segdefinition)+;

//define the contents of a single segment
segdefinition: (segName:TOKEN|segNameINI:"INI"|segNameVER:"VER"|segNameSTD:"STD")
			   {
					String segmentName = null;
					if( segName != null )
						segmentName = segName.getText();
					if( segNameINI != null )
						segmentName = segNameINI.getText();
					if( segNameVER != null )
						segmentName = segNameVER.getText();
					if( segNameSTD != null )
						segmentName = segNameSTD.getText();
					
					Segment segmentDefn = new BasicSegment();
					segmentDefn.setMaxRepeat(structureRepeatMultiplicator);
					activeParentSegment = segmentDefn;
					
					System.out.println("defining segment: " + segmentName);
			   }	
			   EQUAL
			   (structure_firstlevel | repeat_structure)+ 
			   eatup
			   {
			   		segmentsDict.addSegment(segmentDefn);
			   }
			   ;

//This is used in the segs part to indicate
//repeating elements, this is NO dataelement group
//structure as we know it of EDIFACT
repeat_structure: LBRACKET 
	{
		int newRepeatMultiplicator = 1;
	}
	(newRepeatMultiplicator=repeat)
	{
		this.structureRepeatMultiplicator 
			= (int)(newRepeatMultiplicator*this.structureRepeatMultiplicator);
	} 
	(structure_firstlevel | repeat_structure)+ RBRACKET
	{
		//restore the old repeat multiplicator
		this.structureRepeatMultiplicator 
			= (int)(this.structureRepeatMultiplicator/newRepeatMultiplicator);
	};


//This section is NOT available for ANSI X.12
//it defines the dataelement groups, e.g. for
//EDIFACT
coms: DOT "COMS" SINGLE_NEWLINE (degdefinition)+;

//Define a single element on the hierarchie
//level of a deg
degdefinition: degName:TOKEN 
			   {
			   		CompositeDataElement dataElemGroup = new com.netspective.junxion.edi.element.data.CompositeDataElement();			   		
			   		dataElemGroup.setIdentifier(degName.getText());
					dataElemGroup.setMaxRepeat(structureRepeatMultiplicator);
			   		activeParentDataElems = dataElemGroup;
			   }
			   EQUAL
			   (structure_deg | repeat_structure)+ eatup
			   {
			   		dataElemGroupsDict.addCompositeDataElement(dataElemGroup);
			   }
			   ;

//section with all fields used in the file
elms: DOT "ELMS" SINGLE_NEWLINE (fielddefinition)+;

//possible values of the fields
codes: DOT "CODES" SINGLE_NEWLINE (codedefinition)+;

//defines a single code for a data element
codedefinition: TOKEN EQUAL eatupall;

//definition of a single field
fielddefinition: fieldName:TOKEN 
				 EQUAL 
				 fieldType:TOKEN COMMA 
				 minLength:TOKEN COMMA
				 maxLength:TOKEN SINGLE_NEWLINE
				 { 
				 	
				 	DataElement dataElement = new BasicDataElement();
				 	dataElement.setIdentifier(fieldName.getText());
				 	dataElement.setType(fieldType.getText());
					dataElement.setMinLength(new Integer(minLength.getText()).intValue());
					dataElement.setMaxLength(new Integer(maxLength.getText()).intValue());
					dataElemsDict.addDataElement(dataElement);
				 };

//this is the last section: eat up the stuff
text: DOT "TEXT" COMMA "SETS" SINGLE_NEWLINE (eatupall)+;

valref: DOT "VALREFS" (valrefdefinition)+;

valrefdefinition: TOKEN EQUAL TOKEN eatup;


class StandardExchangeFormatLexer extends Lexer;
options
{
	 k = 5;
}

SINGLE_NEWLINE: 
	( "\r\n"  // Evil DOS
	|	'\r'    // Macintosh
	|	'\n'    // Unix (the right way)
	)
	{ newline(); }
		;

// Whitespace -- ignored
WS	:	(	' '
		|	'\t'
		|	'\f'
		)
		{ _ttype = Token.SKIP; }
	;

AT: '@';
LPAREN:	'(';
RPAREN:	')';
LBRACKET:'{';
RBRACKET:'}';
TIMES:	'*';
PLUS:	'+';
SEMICOLON:	';';
LBRACE: '[';
RBRACE: ']';
EQUAL: '=';
COLON: ':';
DOT: '.';
COMMA: ',';
PIPE: '|';
MINUS: '-';
EXCLA: '!';
QUESTION: '?';
APOS: '\'';
BS: '\\';
PERCENT: '%';
DOLLAR: '$';
TILDE: '~';
HUETCHEN: '^';
GT: '>';
LT: '<';
HASH: '#';
QUOTE: '"';
AND: '&';

//german special characters added
TOKEN	:	('a'..'z'|'A'..'Z'|'_'|'/'|'0'..'9'
				|'\334'|'\304'|'\326'|'\374'|'\344'|'\366'|'\337')
			('a'..'z'|'A'..'Z'|'_'|'0'..'9'
				|'\334'|'\304'|'\326'|'\374'|'\344'|'\366'|'\337'
				|DOT|'/'|MINUS)*;
