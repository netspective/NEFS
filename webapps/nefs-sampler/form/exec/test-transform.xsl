<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:xdm="http://www.netspective.org/Framework/Commons/XMLDataModel">

    <xsl:output method="html" indent="yes"/>

    <xsl:template match="*">
        Contents of XML passed in (syntax highlighted):<p/>
        <DIV STYLE="margin-left:2em;text-indent:-2em;font-family:lucida console,courier;font-size:8pt;">
        <xsl:call-template name="xml-element">
            <xsl:with-param name="element" select="."/>
        </xsl:call-template>
        </DIV>
    </xsl:template>

    <xsl:template name="xml-element">
        <xsl:param name="element"/>
        <xsl:variable name="elem-text"><xsl:value-of select="normalize-space(string($element/text()))"/></xsl:variable>

        <xsl:if test="string-length($elem-text) > 0">
        <font color="green"><DIV STYLE="margin-left:2em;text-indent:-2em"><xsl:value-of select="$elem-text"/></DIV></font>
        </xsl:if>

        <xsl:for-each select="$element/* | $element/comment()">
            <xsl:choose>
                <xsl:when test=". = $element/comment()">
                    <DIV>
                        <br/>
                        <font face="arial,helvetica" size="2" color="#777777">&lt;!-- <xsl:value-of select="."/> --&gt;</font>
                        <br/>
                    </DIV>
                </xsl:when>
                <xsl:when test="self::*">
                    <DIV STYLE="margin-left:2em;text-indent:-2em;font-family:lucida console,courier;font-size:8pt;">
                        &lt;<font color="navy"><xsl:value-of select="name()"/></font>
                        <xsl:for-each select="@*"><xsl:text> </xsl:text><font color="darkred"><xsl:value-of select="name()"/></font>=&quot;<font color="green"><xsl:value-of select="."/></font>&quot;</xsl:for-each><xsl:if test="(count(*) = 0) and not(text())">/</xsl:if>&gt;
                        <xsl:call-template name="xml-element">
                            <xsl:with-param name="element" select="."/>
                        </xsl:call-template>
                    </DIV>
                    <xsl:if test="(count(*) > 0) or text()">&lt;/<font color="navy"><xsl:value-of select="name()"/></font>&gt;</xsl:if>
                </xsl:when>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>