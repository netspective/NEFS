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

    <!-- transform contents of all <column> and <datatype> contents -->
    <xsl:template name="datatype">
        <xsl:variable name="java-dal-prefix">java-</xsl:variable>
        <xsl:variable name="dialog-field-prefix">field.</xsl:variable>

        <!-- emit everything that we're not transforming -->
        <xsl:apply-templates
                   select="*[ name() != 'sqldefn' and name() != 'validate' and name() != 'trigger' and
                              not(starts-with(name(), $java-dal-prefix)) and
                              not(starts-with(name(), $dialog-field-prefix))
                            ]"/>

        <xsl:if test="sqldefn | default">
            <sql-ddl>
                <xsl:for-each select="sqldefn">
                    <define>
                        <xsl:copy-of select="attribute::*[. != '']"/>
                        <xsl:attribute name="xdm:replace-template-expressions">no</xsl:attribute>
                        <xsl:value-of select="."/>
                   </define>
                </xsl:for-each>
                <xsl:for-each select="default">
                    <default>
                        <xsl:copy-of select="attribute::*[. != '']"/>
                        <xsl:attribute name="xdm:replace-template-expressions">no</xsl:attribute>
                        <xsl:value-of select="."/>
                   </default>
                </xsl:for-each>
            </sql-ddl>
        </xsl:if>

        <xsl:for-each select="*[starts-with(name(), $java-dal-prefix) or name() = 'validate' or name() = 'trigger']">
            <xsl:comment>
                AXIOM CONVERSION NOTICE: XIF SchemaDoc tag <xsl:text>&lt;</xsl:text><xsl:value-of select="name()"/>> is no longer used in Axiom.
                Column triggers, validation, and DAL information must now be placed in Java classes.
                <xsl:copy>
                    <xsl:copy-of select="attribute::*[. != '']"/>
                </xsl:copy>
            </xsl:comment>
        </xsl:for-each>

        <xsl:if test="*[starts-with(name(), $dialog-field-prefix)]">
            <presentation>
                <xsl:for-each select="*[starts-with(name(), $dialog-field-prefix)]">
                    <xsl:copy-of select="."/>
                </xsl:for-each>
            </presentation>
        </xsl:if>
    </xsl:template>

    <!-- transform contents of all <tabletype> and <table> contents -->
    <xsl:template name="tabletype">
        <xsl:param name="type"/>
        <xsl:variable name="java-dal-prefix">java-</xsl:variable>
        <xsl:variable name="table-name"><xsl:value-of select="@name"/></xsl:variable>
        <xsl:variable name="table-abbrev"><xsl:value-of select="@abbrev"/></xsl:variable>

        <xsl:if test="param">
            <xsl:choose>
                <xsl:when test="$type = 'table'">
                    <!-- template params are now placed as attributes with the "xdm:param-" prefix -->
                    <xsl:for-each select="param">
                        <xsl:attribute name="{concat('xdm:param-', @name)}"><xsl:value-of select="."/></xsl:attribute>
                    </xsl:for-each>
                </xsl:when>

                <xsl:otherwise>
                    <!-- it's a table-type, put in the <xdm:template-param> tag in place of <param> tags -->
                    <xsl:for-each select="param">
                        <xdm:template-param>
                            <xsl:copy-of select="attribute::*[. != '']"/>
                            <xsl:choose>
                                <xsl:when test="."><xsl:attribute name="default"><xsl:value-of select="."/></xsl:attribute></xsl:when>
                                <xsl:otherwise><xsl:attribute name="required">yes</xsl:attribute></xsl:otherwise>
                            </xsl:choose>
                        </xdm:template-param>
                    </xsl:for-each>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>

        <!-- emit everything that we're not transforming -->
        <xsl:apply-templates select="*[name() != 'param' and
                                       name() != 'enum' and
                                       name() != 'dialog' and
                                       name() != 'trigger' and
                                       name() != 'index' and
                                       not(starts-with(name(), $java-dal-prefix))]"/>

        <xsl:for-each select="trigger">
            <xsl:comment>
                AXIOM CONVERSION NOTICE: XIF SchemaDoc tag <xsl:text>&lt;</xsl:text><xsl:value-of select="name()"/>> is no longer used in Axiom.
                Table triggers must now be placed in Java classes.
                <xsl:copy>
                    <xsl:copy-of select="attribute::*[. != '']"/>
                </xsl:copy>
            </xsl:comment>
        </xsl:for-each>

        <xsl:for-each select="*[starts-with(name(), $java-dal-prefix)]">
            <xsl:comment>
                AXIOM CONVERSION NOTICE: XIF SchemaDoc tag <xsl:text>&lt;</xsl:text><xsl:value-of select="name()"/>> is no longer used in Axiom.
                <xsl:copy>
                    <xsl:copy-of select="attribute::*[. != '']"/>
                </xsl:copy>
            </xsl:comment>
        </xsl:for-each>

        <xsl:if test="index">
            <xsl:choose>
                <xsl:when test="$type = 'table'">
                    <!-- indexes need their names fixed up (removed 'type' attribute as well) -->
                    <xsl:for-each select="index">
                        <xsl:copy>
                            <xsl:attribute name="name"><xsl:value-of select="concat($table-name, '_unq')"/></xsl:attribute>
                            <xsl:copy-of select="attribute::columns"/>
                            <xsl:if test="@type='unique'">
                                <xsl:attribute name="unique">yes</xsl:attribute>
                            </xsl:if>
                        </xsl:copy>
                    </xsl:for-each>
                </xsl:when>

                <xsl:otherwise>
                    <!-- indexes need their names to be variable (removed 'type' attribute as well) -->
                    <xsl:for-each select="index">
                        <xsl:copy>
                            <xsl:attribute name="name">${owner.abbrev}_unq</xsl:attribute>
                            <xsl:copy-of select="attribute::columns"/>
                            <xsl:if test="@type='unique'">
                                <xsl:attribute name="unique">yes</xsl:attribute>
                            </xsl:if>
                        </xsl:copy>
                    </xsl:for-each>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>

        <xsl:if test="enum">
            <enumerations>
                <xsl:for-each select="enum">
                    <xsl:copy-of select="."/>
                </xsl:for-each>
            </enumerations>
        </xsl:if>

        <xsl:if test="dialog">
            <presentation>
                <xsl:for-each select="dialog">
                    <xsl:copy-of select="."/>
                </xsl:for-each>
            </presentation>
        </xsl:if>
    </xsl:template>

    <!-- <datatype> tags are now called <data-type> and the children are organized differently -->
    <xsl:template match="datatype">
        <data-type>
            <xsl:copy-of select="attribute::*[. != '']"/>
            <xsl:call-template name="datatype"/>
        </data-type>
    </xsl:template>

    <!-- <column> tags are still called <column> but the children are organized differently -->
    <xsl:template match="column">
        <xsl:copy>
            <xsl:for-each select="attribute::*[. != '']">
                <xsl:choose>
                    <xsl:when test="name() = 'default'">
                        <!-- remove the attribute since we need to add DBMS-specific <sql-ddl> -->
                    </xsl:when>
                    <xsl:when test="name() = 'enumeration-table'">
                        <xsl:attribute name="xdm:param-enumerationTable"><xsl:value-of select="."/></xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            <xsl:if test="@default = 'sysdate'">
                <sql-ddl>
                    <default dbms="oracle">sysdate</default>
                </sql-ddl>
                <value-defn>
                    <default dbms="hsqldb">curdate()</default>
                </value-defn>
            </xsl:if>
            <xsl:call-template name="datatype"/>
        </xsl:copy>
    </xsl:template>

    <!-- <tabletype> tags are now called <table-type> and the children are organized differently -->
    <xsl:template match="tabletype">
        <table-type>
            <xsl:copy-of select="attribute::*[. != '' and name() != 'parent']"/>
            <xsl:if test="@parent">
                <hierarchy><xsl:attribute name="parent"><xsl:value-of select="@parent"/></xsl:attribute></hierarchy>
            </xsl:if>
            <xsl:call-template name="tabletype"/>
        </table-type>
    </xsl:template>

    <!-- moved these to the "standard" table-types.xml file so remove them from the applications -->
    <xsl:template match="indextype"/>
    <xsl:template match="table[@name = 'Reference_Item']"/>
    <xsl:template match="tabletype[@name = 'Enumeration']"/>
    <xsl:template match="table[@name = 'Lookup_Result_Type']"/>
    <xsl:template match="tabletype[@name = 'Lookup']"/>
    <xsl:template match="tabletype[@name = 'Status']"/>

    <!-- <table> tags are still called <table> but the children are organized differently -->
    <xsl:template match="table">
        <xsl:copy>
            <xsl:copy-of select="attribute::*[. != '' and name() != 'parent']"/>
            <xsl:if test="@parent">
                <hierarchy><xsl:attribute name="parent"><xsl:value-of select="@parent"/></xsl:attribute></hierarchy>
            </xsl:if>
            <xsl:call-template name="tabletype">
                <xsl:with-param name="type">table</xsl:with-param>
            </xsl:call-template>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="include[@file='../../sparx/resources/schema/data-types.xml']">
        <xdm:include resource="com/netspective/axiom/conf/schema.xml"/>
    </xsl:template>

    <xsl:template match="include">
        <xdm:include>
            <xsl:copy-of select="attribute::*[. != '']"/>
        </xdm:include>
    </xsl:template>

    <xsl:template match="schema">
        <component>
            <xdm:include resource="com/netspective/axiom/conf/axiom.xml"/>

            <xsl:copy>
                <xsl:copy-of select="attribute::*[. != '']"/>
                <xsl:apply-templates/>
            </xsl:copy>
        </component>
    </xsl:template>

</xsl:stylesheet>
