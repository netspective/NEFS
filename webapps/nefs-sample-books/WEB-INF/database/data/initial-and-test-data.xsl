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

    <xsl:template match="generate-book-info-records">
        <xsl:call-template name="iterate-one">
            <xsl:with-param name="x"><xsl:value-of select="@count"/></xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="iterate-one">
        <xsl:param name="x"/>

        <book-info>
            <xsl:attribute name="id">BOOK_<xsl:value-of select="$x"/></xsl:attribute>
            <xsl:attribute name="name">Book <xsl:value-of select="$x"/></xsl:attribute>
            <xsl:attribute name="author">Author <xsl:value-of select="$x"/></xsl:attribute>
            <xsl:attribute name="genre">Science Fiction</xsl:attribute>
            <xsl:attribute name="isbn">ISBN <xsl:value-of select="$x"/></xsl:attribute>
        </book-info>

        <xsl:if test="$x > 1">
            <xsl:call-template name="iterate-one">
                <xsl:with-param name="x" select="$x - 1"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>