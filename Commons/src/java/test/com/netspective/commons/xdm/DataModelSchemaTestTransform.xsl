<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="attribute::*[. != '']"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="root">
        <xsl:copy>
            <xsl:copy-of select="attribute::*[. != '']"/>
            <xsl:apply-templates/>

            <!-- add this node on the root passed in -->
            <nested1 text="TestNested1XSLTNode" integer="100">
                PCDATA in TestNested1XSLTNode.
                <nested11 text="TestNested1XSLTNodeText" integer="71"/>
                <nested11 type="type-B" text="TestText12" integer="72"/>
            </nested1>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>