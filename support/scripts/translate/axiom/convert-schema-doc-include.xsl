<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:xdm="http://www.netspective.org/Framework/Commons/XMLDataModel">

    <xsl:import href="convert-schema-doc.xsl"/>

    <xsl:template match="schema">
        <xdm:container>
            <xsl:apply-templates/>
        </xdm:container>
    </xsl:template>

</xsl:stylesheet>