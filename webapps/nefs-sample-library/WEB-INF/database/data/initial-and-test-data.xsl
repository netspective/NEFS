<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:dal="http://www.netspective.org/Framework/Axiom/DataAccessLayer">

    <xsl:output method="xml" indent="yes"/>

    <!-- default template -->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="attribute::*[. != '']"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="generate-asset-records">
        <xsl:call-template name="iterate-one">
            <xsl:with-param name="x">0</xsl:with-param>
            <xsl:with-param name="count"><xsl:value-of select="@count"/></xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="iterate-one">
        <xsl:param name="x"/>
        <xsl:param name="count"/>

        <asset>
            <xsl:attribute name="name">Item <xsl:value-of select="$x"/></xsl:attribute>
            <xsl:attribute name="type">Book</xsl:attribute>
            <xsl:attribute name="quantity">5</xsl:attribute>
        </asset>

        <xsl:if test="$x &lt; $count">
            <xsl:call-template name="iterate-one">
                <xsl:with-param name="x" select="$x + 1"/>
                <xsl:with-param name="count"><xsl:value-of select="$count"/></xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>