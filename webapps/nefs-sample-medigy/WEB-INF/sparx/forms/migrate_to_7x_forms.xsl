<?xml version='1.0' encoding='iso-8859-1'?>
<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:xdm="http://www.netspective.org/Framework/Commons/XMLDataModel">

<xsl:output method='xml' version='1.0' encoding='iso-8859-1' indent='yes'/>
	<xsl:template match="include">
		<xdm:include>
			<xsl:attribute name="file"><xsl:value-of select="@file"/></xsl:attribute>
		</xdm:include>
	</xsl:template>
	<xsl:template match="xaf">
	<xdm:container xmlns:xdm="http://www.netspective.org/Framework/Commons/XMLDataModel">
	<xsl:apply-templates/>
	</xdm:container>
	</xsl:template>
	<xsl:template match="templates">
		<xsl:apply-templates />
	</xsl:template>
	<xsl:template match="template">
		<xdm:template>
		<xsl:attribute name="name"><xsl:value-of select="parent::templates/@package"/>.<xsl:value-of select="@name"/></xsl:attribute>
		<xsl:apply-templates />
		</xdm:template>
	</xsl:template>
	<xsl:template match="dialogs">
		<dialogs>
			<xsl:attribute name="package"><xsl:value-of select="@package"/> </xsl:attribute>
			<xsl:apply-templates />
		</dialogs>
	</xsl:template>
	<xsl:template match="dialog">
		<dialog>
			<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
			<xsl:if test="@class">
				<xsl:attribute name="class"><xsl:value-of select="@class"/> </xsl:attribute>
			</xsl:if>
			<xsl:if test="@retain-params">
				<xsl:attribute name="retain-params"><xsl:value-of select="@retain-params"/> </xsl:attribute>
			</xsl:if>
			<xsl:if test="@heading">
			<xsl:choose>
			<xsl:when test="contains(@heading, 'create-data-cmd-heading')" >
				<frame><xsl:attribute name="heading">
     			<xsl:text>create-dialog-perspective-heading:</xsl:text><xsl:value-of select="substring-after(@heading, ':')" />
				</xsl:attribute></frame>
   			</xsl:when>
  			<xsl:otherwise>
				<frame><xsl:attribute name="heading"><xsl:value-of select="@heading"/></xsl:attribute></frame>
			</xsl:otherwise>	
			</xsl:choose>
			</xsl:if>
			<xsl:apply-templates />
		</dialog>
	</xsl:template>
	<xsl:template match="field.separator">		
		<field type="separator">
		<xsl:attribute name="heading"><xsl:value-of select="@heading"/> </xsl:attribute>
		</field>
	</xsl:template>
	<xsl:template match="field.text">
		<field type="text">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@size">
		<xsl:attribute name="size"><xsl:value-of select="@size"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>		
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@mask-entry">
		<xsl:attribute name="mask-entry"><xsl:value-of select="@mask-entry"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@submit-pattern">
		<xsl:attribute name="submit-pattern"><xsl:value-of select="@ submit-pattern"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@col-break = 'after'">
		<xsl:attribute name="column-break-after">yes</xsl:attribute>
		</xsl:if>
		<xsl:if test="@col-break = 'before'">
		<xsl:attribute name="column-break-before">yes</xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.integer">
		<field type="integer">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@size">
		<xsl:attribute name="size"><xsl:value-of select="@size"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.float">
		<field type="float">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@size">
		<xsl:attribute name="size"><xsl:value-of select="@size"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.duration">
		<field type="duration">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.select">
		<field type="select">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:attribute name="choices"><xsl:value-of select="@choices"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@style">
		<xsl:attribute name="style"><xsl:value-of select="@style"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@prepend-blank">
		<xsl:attribute name="prepend-blank"><xsl:value-of select="@prepend-blank"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.boolean">
		<field type="boolean">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@style">
		<xsl:attribute name="style"><xsl:value-of select="@style"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	
	<xsl:template match="field.composite">
		<composite>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:if test="@caption">
			<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
			</xsl:if>
			<xsl:apply-templates />
		</composite>
	</xsl:template>
	<xsl:template match="field.time">
		<field type="time">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@size">
		<xsl:attribute name="size"><xsl:value-of select="@size"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.date">
		<field type="date">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@size">
		<xsl:attribute name="size"><xsl:value-of select="@size"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@popup-calendar">
		<xsl:attribute name="popup-calendar"><xsl:value-of select="@popup-calendar"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.zip">
		<field type="zip-code">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@size">
		<xsl:attribute name="size"><xsl:value-of select="@size"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.ssn">
		<field type="ssn">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@size">
		<xsl:attribute name="size"><xsl:value-of select="@size"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.phone">
		<field type="phone">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@size">
		<xsl:attribute name="size"><xsl:value-of select="@size"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.email">
		<field type="e-mail">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@size">
		<xsl:attribute name="size"><xsl:value-of select="@size"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="field.currency">
		<field type="currency">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@size">
		<xsl:attribute name="size"><xsl:value-of select="@size"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>
	</xsl:template>
	<xsl:template match="conditional">
		<conditional>		
		<xsl:if test="@flag">
		<xsl:attribute name="flags"><xsl:value-of select="@flag"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@has-value">
		<xsl:attribute name="has-value"><xsl:value-of select="@has-value"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@data-cmd">
		<xsl:attribute name="perspective"><xsl:value-of select="@data-cmd"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@is-true">
		<xsl:attribute name="is-true"><xsl:value-of select="@is-true"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@action">
		<xsl:attribute name="action">
		<xsl:choose>
			<xsl:when test="contains(@action, 'apply-flag')">
     			<xsl:text>apply-flags</xsl:text>
   			</xsl:when>
  			<xsl:otherwise>
				<xsl:value-of select="@action"/>
			</xsl:otherwise>	
		</xsl:choose>
		</xsl:attribute>
		</xsl:if>
		<xsl:if test="@partner">
		<xsl:attribute name="partner-field-name"><xsl:value-of select="@partner"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@js-expr">
		<xsl:attribute name="expression"><xsl:value-of select="@js-expr"/> </xsl:attribute>
		</xsl:if>
		</conditional>
	</xsl:template>
	<xsl:template match="field.grid">
		<grid>
			<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
			<xsl:if test="@hint">
			<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
			</xsl:if>
			<xsl:if test="@caption">
			<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
			</xsl:if>
			<xsl:for-each select="field.composite">
			<row>
				<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
				<xsl:apply-templates/>
			</row>
			</xsl:for-each>
		</grid>
	</xsl:template>
	<xsl:template match="field.memo">
		<field type="memo">
		<xsl:attribute name="name"><xsl:value-of select="@name"/> </xsl:attribute>
		<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
		<xsl:if test="@required">
		<xsl:attribute name="required"><xsl:value-of select="@required"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hidden">
		<xsl:attribute name="hidden"><xsl:value-of select="@hidden"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@read-only">
		<xsl:attribute name="read-only"><xsl:value-of select="@read-only"/> </xsl:attribute>
		</xsl:if>
  		<xsl:if test="@default">
		<xsl:attribute name="default"><xsl:value-of select="@default"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@max-length">
		<xsl:attribute name="max-length"><xsl:value-of select="@max-length"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@initial-focus">
		<xsl:attribute name="initial-focus"><xsl:value-of select="@initial-focus"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@rows">
		<xsl:attribute name="rows"><xsl:value-of select="@ rows"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@cols">
		<xsl:attribute name="cols"><xsl:value-of select="@ cols"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@hint">
		<xsl:attribute name="hint"><xsl:value-of select="@hint"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</field>	
	</xsl:template>
	
	<xsl:template match="client-js">
		<client-js>
			<xsl:attribute name="event"><xsl:value-of select="@event"/></xsl:attribute>
			<xsl:attribute name="type"><xsl:value-of select="@type"/></xsl:attribute>
			<xsl:attribute name="js-expr"><xsl:value-of select="@js-expr"/></xsl:attribute>
		</client-js>
	</xsl:template>
	<xsl:template match="include-template">
		<xdm:include>
			<xsl:attribute name="template"><xsl:value-of select="@id"/></xsl:attribute>
		</xdm:include>
	</xsl:template>
	<xsl:template match="director">
		<director>
		<xsl:if test="@style">
		<xsl:attribute name="style"><xsl:value-of select="@ style"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@submit-caption">
		<xsl:attribute name="submit-caption"><xsl:value-of select="@submit-caption"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@pending-caption">
		<xsl:attribute name="pending-caption"><xsl:value-of select="@pending-caption"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@cancel-caption">
		<xsl:attribute name="cancel-caption"><xsl:value-of select="@cancel-caption"/> </xsl:attribute>
		</xsl:if>
		<xsl:if test="@submit-url">
		<xsl:attribute name="submit-url"><xsl:value-of select="@submit-url"/> </xsl:attribute>
		</xsl:if>
		<xsl:apply-templates />
		</director>
	</xsl:template>
	<xsl:template match="next-actions">
		<next-actions>
			<xsl:if test="@caption">
			<xsl:attribute name="caption"><xsl:value-of select="@caption"/> </xsl:attribute>
			</xsl:if>
			<xsl:apply-templates />
		</next-actions>
	</xsl:template>
	<xsl:template match="choices">
		<items>
			<xsl:for-each select="choice">
				<item>
				<xsl:attribute name="value">
				<xsl:choose>
				<xsl:when test="contains(@value, 'nav-url')">
     				<xsl:text>page-id:</xsl:text><xsl:value-of select="substring-after(@value, ':')" />
   				</xsl:when>
				<xsl:when test="contains(@value, 'nav-id-url')">
     				<xsl:text>page-id:</xsl:text><xsl:value-of select="substring-after(@value, ':')" />
   				</xsl:when>
  				<xsl:otherwise>
					<xsl:value-of select="@value"/>
				</xsl:otherwise>	
				</xsl:choose>
				</xsl:attribute>
				<xsl:attribute name="caption"><xsl:value-of select="."/></xsl:attribute>
				</item>
			</xsl:for-each>
		</items>
	</xsl:template>
</xsl:stylesheet>