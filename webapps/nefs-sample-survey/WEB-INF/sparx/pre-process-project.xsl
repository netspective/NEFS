<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>

    <xsl:template match="*">
        <xsl:copy>
        <xsl:copy-of select="attribute::*[. != '']"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="risk-group">
        <!-- we won't emit anything because this is a special tag that we use for risk-fields -->
    </xsl:template>

    <xsl:template match="risk-fields">
        <field type="separator">
            <xsl:attribute name="heading"><xsl:value-of select="@heading"/></xsl:attribute>
            <xsl:if test="@col-break"><xsl:attribute name="col-break"><xsl:value-of select="@col-break"/></xsl:attribute></xsl:if>
            <xsl:value-of select="@text"/>
        </field>

        <field type="text" caption="Risk Row System Id" hidden="yes">
            <xsl:attribute name="name">risk_row_system_id_<xsl:value-of select="@name"/></xsl:attribute>
        </field>

        <field type="text" caption="Risk Group" size="50" hidden="yes">
            <xsl:attribute name="name">risk_group_<xsl:value-of select="@name"/></xsl:attribute>
            <xsl:attribute name="default"><xsl:value-of select="../risk-group"/></xsl:attribute>
        </field>

        <xsl:if test="@custom = 'yes'">
            <field type="text" caption="Risk" size="50">
                <xsl:attribute name="name">risk_name_<xsl:value-of select="@name"/></xsl:attribute>
            </field>
        </xsl:if>

        <xsl:if test="not(@custom)">
            <field type="text" caption="Risk" size="50" hidden="yes">
                <xsl:attribute name="name">risk_name_<xsl:value-of select="@name"/></xsl:attribute>
                <xsl:attribute name="default"><xsl:value-of select="@heading"/></xsl:attribute>
            </field>
        </xsl:if>

        <grid>
            <xsl:attribute name="name">risk_response_<xsl:value-of select="@name"/></xsl:attribute>

            <row name="immediate_bus_unit" caption="of your immediate business unit">
                <field type="select" name="significance" caption="Significance of Risk&lt;br&gt;(assuming NO Controls)" style="combo" choices="text-list:Don't Know=0;1 (low)=1;2;3;4;5 (medium)=5;6;7;8;9 (high)=9"/>
                <field type="select" name="effectiveness" caption="Effectiveness of&lt;br&gt;Current Controls" style="combo" choices="text-list:Don't Know=0;1 (low)=1;2;3;4;5 (medium)=5;6;7;8;9 (high)=9"/>
            </row>

            <row name="larger_bus_group" caption="of the larger business group">
                <field type="select" name="significance" style="combo" choices="text-list:Don't Know=0;1 (low)=1;2;3;4;5 (medium)=5;6;7;8;9 (high)=9"/>
                <field type="select" name="effectiveness" style="combo" choices="text-list:Don't Know=0;1 (low)=1;2;3;4;5 (medium)=5;6;7;8;9 (high)=9"/>
            </row>

            <row name="enterprise" caption="of Battelle overall">
                <field type="select" name="significance" style="combo" choices="text-list:Don't Know=0;1 (low)=1;2;3;4;5 (medium)=5;6;7;8;9 (high)=9"/>
                <field type="select" name="effectiveness" style="combo" choices="text-list:Don't Know=0;1 (low)=1;2;3;4;5 (medium)=5;6;7;8;9 (high)=9"/>
            </row>
        </grid>
    </xsl:template>

</xsl:stylesheet>