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

</xsl:stylesheet>