<?xml version="1.0"?>

<!--
  This XSLT style-sheet converts SchemaDoc (XML) files from Sparx 2.0 format to Axiom 3.0 format.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:xdm="http://www.netspective.org/Framework/Commons/XMLDataModel">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="attribute::*[. != '']"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- <query-defn> has attributes organized differently now -->
    <xsl:template match="query-defn">
        <xsl:copy>
            <xsl:for-each select="attribute::*[. != '' and name() != 'sortable']">
                <xsl:choose>
                    <xsl:when test="name() = 'id'">
                        <xsl:attribute name="name"><xsl:value-of select="."/></xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            <xsl:apply-templates/>

            <xsl:if test="@sortable">
                <xsl:comment>
                    CONVERSION NOTICE:
                    The 'query-defn' tag in the old version had a 'sortable' attribute but it has now been
                    moved into the 'presentation' tag. Please make manual changes.
                </xsl:comment>
            </xsl:if>
        </xsl:copy>
    </xsl:template>

    <!-- <join>, and <field> tags are still called the same but some of the attributes are different -->
    <xsl:template match="join | field">
        <xsl:copy>
            <xsl:for-each select="attribute::*[. != '']">
                <xsl:choose>
                    <xsl:when test="name() = 'id'">
                        <xsl:attribute name="name"><xsl:value-of select="."/></xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- <select-dialog> and <report> tags are now under <presentation> tag -->
    <xsl:template match="report | select-dialog">
        <presentation>
            <xsl:copy>
                <xsl:copy-of select="attribute::*[. != '']"/>
                <xsl:apply-templates/>
            </xsl:copy>
        </presentation>
    </xsl:template>

    <!-- <statemen> tags are now called <query> -->
    <xsl:template match="statement">
        <query>
            <xsl:copy-of select="attribute::*[. != '']"/>
            <xsl:apply-templates/>
        </query>
    </xsl:template>

    <!-- <sql-statement> packages are now called <queries> -->
    <xsl:template match="sql-statements">
        <queries>
            <xsl:copy-of select="attribute::*[. != '']"/>
            <xsl:apply-templates/>
        </queries>
    </xsl:template>

    <!-- <include> tags are now called <xdm:include> -->
    <xsl:template match="include">
        <xdm:include>
            <xsl:copy-of select="attribute::*[. != '']"/>
        </xdm:include>
    </xsl:template>

    <!-- the new root tag is <component>, not <xaf> -->
    <xsl:template match="xaf">
        <component>
            <xsl:copy-of select="attribute::*[. != '']"/>
            <xsl:apply-templates/>
        </component>
    </xsl:template>

</xsl:stylesheet>
