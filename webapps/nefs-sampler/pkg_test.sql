/**
 * --------------------------------------------------------------------------
 * PACKAGE PKG_TEST
 * --------------------------------------------------------------------------
 *
 * Package for testing
 *
 * --------------------------------------------------------------------------
 **/

CREATE OR REPLACE PACKAGE PKG_TEST AS

    -- ---------------------------------------------
    -- Global Package Constants
	-- ------------------------
	VERSION_INFO            CONSTANT VARCHAR2(50)   := '1.0, 12/16/2002';
	NUM_GRADE_CRITERIA      CONSTANT INTEGER        := 4;
	ASTERISK                CONSTANT VARCHAR2(1)    := '*';
	COMMA                   CONSTANT VARCHAR2(1)    := ',';
	DASH                    CONSTANT VARCHAR2(1)    := '-';

	
	dashLine                CONSTANT VARCHAR2(80)   :=
	'-----------------------------------------------------------------------------';

	-- ----------------
    -- Package Variable
	-- ----------------
	DEBUG_ON                VARCHAR2(500)  := NULL;
	thisRoutine             VARCHAR2(500)  := 'PKG_TEST';
	DATE_FORMAT             VARCHAR2(10)   := 'mm/dd/yyyy';
	DATE_TIME_FORMAT        VARCHAR2(30)   := 'mm/dd/yyyy hh:mi:ss AM';
	CRLF                    VARCHAR2(2)    := chr(13) || chr(10);
	INDENT                  VARCHAR2(4)    := '	';

     

    
    
    
    TYPE RefCursorType    is REF CURSOR;

    -- -------------------------------------------------------------------
	-- PUBLIC APIs
	-- -------------------------------------------------------------------
	FUNCTION getVersion return VARCHAR2;
    PROCEDURE appendString(string IN OUT NOCOPY VARCHAR2, appendText VARCHAR2, level INTEGER := 0);
    FUNCTION getXmlFriendlyText(text VARCHAR2) return VARCHAR2;   
    FUNCTION testRefCursor(lastName DONOR.LNAME%type) return RefCursorType;    
    -- -------------------------------------------------------------------

END PKG_TEST;
/

-- --------------------------------------------------
CREATE OR REPLACE PACKAGE BODY PKG_TEST AS
-- --------------------------------------------------
	FUNCTION testRefCursor(lastName DONOR.LNAME%type) return RefCursorType IS

	    testCursor RefCursorType;

	BEGIN
        open testCursor FOR
        	SELECT * from donor where lname like lastName;
		return testCursor;
	EXCEPTION
        when OTHERS then
			thisRoutine := 'testRefCursor(' || lastName || ')';
			raise_application_error (-20013, thisRoutine || ' / ' || SQLERRM);
	END testRefCursor;
    /**
     * ---------------------------------------------------------------------------------
     * FUNCTION: getVersion
     * ---------------------------------------------------------------------------------
     * DESCRIPTION:
     * Gets the version of the package
	 * ---------------------------------------------------------------------------------
	 * RETURNS:
	 * String containing the version number
	 * ---------------------------------------------------------------------------------
	 **/
	FUNCTION getVersion return VARCHAR2 is

	BEGIN
		return VERSION_INFO;

	EXCEPTION
		when OTHERS then
			thisRoutine := 'getVersion()';
			raise_application_error (-20001, thisRoutine || ' / ' || SQLERRM);

	END getVersion;
    /**
     * ---------------------------------------------------------------------------------
     * PROCEDURE: appendString
     * ---------------------------------------------------------------------------------
     * DESCRIPTION:
     * Appends a text to the passed in string
	 * ---------------------------------------------------------------------------------
	 * RETURNS:
	 *
	 * ---------------------------------------------------------------------------------
	 **/

	PROCEDURE appendString(string IN OUT NOCOPY VARCHAR2, appendText VARCHAR2, level INTEGER := 0) IS

		indentString  VARCHAR2(32000) := NULL;

	BEGIN
		for i in 1..level loop
			indentString := indentString || INDENT;
		end loop;
		string := string || indentString || appendText || CRLF;

	EXCEPTION
		when OTHERS then
			thisRoutine := 'appendString(' || string || ',' || appendText || ')';
			raise_application_error (-20013, thisRoutine || ' / ' || SQLERRM);
	END appendString;

	/**
     * ---------------------------------------------------------------------------------
     * FUNCTION: getXmlFriendlyText
     * ---------------------------------------------------------------------------------
     * DESCRIPTION:
     * Replaces reserved XML characters with their unicode counterparts
	 * ---------------------------------------------------------------------------------
	 * RETURNS:
	 *
	 * ---------------------------------------------------------------------------------
	 **/
	FUNCTION getXmlFriendlyText(text VARCHAR2) return VARCHAR2 IS
	    newText VARCHAR2(32000) := NULL;
	BEGIN
        newText := replace(text, '&', '&' || 'amp;');
	    newText := replace(newText, '<', '&' || 'lt;');
        newText := replace(newText, '>', '&' || 'gt;');
        return newText;
	EXCEPTION
	    when OTHERS then
			thisRoutine := 'getXmlFriendlyText(' || text || ')';
			raise_application_error (-20013, thisRoutine || ' / ' || SQLERRM);
	END getXmlFriendlyText;
    
	
End PKG_TEST;
/
