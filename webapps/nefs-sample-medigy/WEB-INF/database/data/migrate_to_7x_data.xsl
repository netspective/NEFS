<?xml version='1.0' encoding='iso-8859-1'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>

<xsl:output method='xml' version='1.0' encoding='iso-8859-1' indent='yes'/>

<xsl:template match="org">
	<org>
	<xsl:attribute name="ID"><xsl:value-of select="@ID"/></xsl:attribute>
	<xsl:attribute name="rec-stat-id"><xsl:value-of select="rec-stat-id/text()"/></xsl:attribute>
	<xsl:attribute name="org-name"><xsl:value-of select="org-name/text()"/></xsl:attribute>
	<xsl:attribute name="org-code"><xsl:value-of select="org-code/text()"/></xsl:attribute>
	<xsl:attribute name="org-abbrev"><xsl:value-of select="org-abbrev/text()"/></xsl:attribute>
	<xsl:attribute name="ownership-id"><xsl:value-of select="ownership-id/text()"/></xsl:attribute>
	<xsl:attribute name="ticker-symbol"><xsl:value-of select="ticker-symbol/text()"/></xsl:attribute>
	<xsl:attribute name="sic-code"><xsl:value-of select="sic-code/text()"/></xsl:attribute>
	<xsl:attribute name="employees"><xsl:value-of select="employees/text()"/></xsl:attribute>
	<xsl:attribute name="time-zone"><xsl:value-of select="time-zone/text()"/></xsl:attribute>
	<xsl:attribute name="hcfa-servplace-id"><xsl:value-of select="hcfa-servplace-id/text()"/></xsl:attribute>
	<xsl:if test="org-address">
		<org-address >
		<xsl:attribute name="address-name"><xsl:value-of select="org-address/address-name/text()"/></xsl:attribute>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="org-address/rec-stat-id/text()"/></xsl:attribute>
		<xsl:attribute name="address-type-id"><xsl:value-of select="org-address/address-type-id/text()"/></xsl:attribute>
		<xsl:attribute name="line1"><xsl:value-of select="org-address/line1/text()"/></xsl:attribute>
		<xsl:attribute name="city"><xsl:value-of select="org-address/city/text()"/></xsl:attribute>
		<xsl:attribute name="state-id"><xsl:value-of select="org-address/state-id/text()"/></xsl:attribute>
		<xsl:attribute name="county"><xsl:value-of select="org-address/county/text()"/></xsl:attribute>
		<xsl:attribute name="zip"><xsl:value-of select="org-address/zip/text()"/></xsl:attribute>
		</org-address>
	</xsl:if>
	<xsl:if test="org-classification">
	<org-classification>
		<xsl:attribute name="org-type-id"><xsl:value-of select="org-classification/org-type-id/text()"/></xsl:attribute>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="org-classification/rec-stat-id/text()"/></xsl:attribute>
	</org-classification>
	</xsl:if>
	<xsl:for-each select="org-contact">
	<org-contact>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="rec-stat-id/text()"/></xsl:attribute>
		<xsl:attribute name="method-type"><xsl:value-of select="method-type/text()"/></xsl:attribute>
		<xsl:attribute name="method-value"><xsl:value-of select="method-value/text()"/></xsl:attribute>
		<xsl:attribute name="phone-ac"><xsl:value-of select="phone-ac/text()"/></xsl:attribute>
	</org-contact>
	</xsl:for-each>
	<xsl:if test="org-relationship">
	<org-relationship>
		<xsl:attribute name="rel-type-id"><xsl:value-of select="org-relationship/rel-type-id/text()"/></xsl:attribute>
		<xsl:attribute name="rel-entity-id">IDREF:<xsl:value-of select="org-relationship/rel-entity-id/@IDREF"/></xsl:attribute>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="org-relationship/rec-stat-id/text()"/></xsl:attribute>
	</org-relationship>
	</xsl:if>
	<xsl:if test="org-industry">
	<org-industry>
		<xsl:attribute name="industry-type-id"><xsl:value-of select="org-industry/industry-type-id/text()"/></xsl:attribute>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="org-industry/rec-stat-id/text()"/></xsl:attribute>
	</org-industry>
	</xsl:if>
	<xsl:for-each select="org-identifier">
	<org-identifier>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="rec-stat-id/text()"/></xsl:attribute>
		<xsl:attribute name="identifier-type-id"><xsl:value-of select="identifier-type-id/text()"/></xsl:attribute>
		<xsl:attribute name="identifier"><xsl:value-of select="identifier/text()"/></xsl:attribute>
	</org-identifier>
	</xsl:for-each>
	</org>
</xsl:template>

<xsl:template match="person">
	<person>
	<xsl:attribute name="ID"><xsl:value-of select="@ID"/></xsl:attribute>
	<xsl:attribute name="name-first"><xsl:value-of select="name-first/text()"/></xsl:attribute>
	<xsl:attribute name="name-last"><xsl:value-of select="name-last/text()"/></xsl:attribute>
	<xsl:attribute name="ssn"><xsl:value-of select="ssn/text()"/></xsl:attribute>
	<xsl:attribute name="rec-stat-id"><xsl:value-of select="rec-stat-id/text()"/></xsl:attribute>
	<xsl:attribute name="gender-id"><xsl:value-of select="gender-id/text()"/></xsl:attribute>
	<xsl:attribute name="language-id"><xsl:value-of select="language-id/text()"/></xsl:attribute>
	<xsl:attribute name="marital-status-id"><xsl:value-of select="marital-status-id/text()"/></xsl:attribute>
	<xsl:attribute name="blood-type-id"><xsl:value-of select="blood-type-id/text()"/></xsl:attribute>
	<xsl:attribute name="blood-type-id"><xsl:value-of select="blood-type-id/text()"/></xsl:attribute>
	<xsl:if test="person-address">
		<person-address>
		<xsl:attribute name="name"><xsl:value-of select="person-address/@address-name"/></xsl:attribute>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="person-address/rec-stat-id/text()"/></xsl:attribute>
		<xsl:attribute name="address-type-id"><xsl:value-of select="person-address/address-type-id/text()"/></xsl:attribute>
		<xsl:attribute name="line1"><xsl:value-of select="person-address/line1/text()"/></xsl:attribute>
		<xsl:attribute name="city"><xsl:value-of select="person-address/city/text()"/></xsl:attribute>
		<xsl:attribute name="state-id"><xsl:value-of select="person-address/state-id/text()"/></xsl:attribute>
		<xsl:attribute name="county"><xsl:value-of select="person-address/county/text()"/></xsl:attribute>
		<xsl:attribute name="zip"><xsl:value-of select="person-address/zip/text()"/></xsl:attribute>
		</person-address>
	</xsl:if>
	<xsl:if test="person-classification">
	<person-classification>
		<xsl:attribute name="org-id">IDREF:<xsl:value-of select="person-classification/org-id/@IDREF"/></xsl:attribute>
		<xsl:attribute name="person-type-id"><xsl:value-of select="person-classification/person-type-id/text()"/></xsl:attribute>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="person-classification/rec-stat-id/text()"/></xsl:attribute>
	</person-classification>
	</xsl:if>
	<xsl:if test="person-contact">
	<person-contact>
		<xsl:attribute name="method-type"><xsl:value-of select="person-contact/method-type/text()"/></xsl:attribute>
		<xsl:attribute name="method-value"><xsl:value-of select="person-contact/method-value/text()"/></xsl:attribute>
		<xsl:attribute name="phone-ac"><xsl:value-of select="person-contact/phone-ac/text()"/></xsl:attribute>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="person-contact/rec-stat-id/text()"/></xsl:attribute>
	</person-contact>
	</xsl:if>
	<xsl:if test="person-login">
	<person-login>
		<xsl:attribute name="user-id"><xsl:value-of select="person-login/user-id/text()"/></xsl:attribute>
		<xsl:attribute name="password"><xsl:value-of select="person-login/password/text()"/></xsl:attribute>
		<xsl:attribute name="login-status"><xsl:value-of select="person-login/login-status/text()"/></xsl:attribute>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="person-login/rec-stat-id/text()"/></xsl:attribute>
	</person-login>
	</xsl:if>
	<xsl:if test="personorg-relationship">
	<personorg-relationship>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="personorg-relationship/rec-stat-id/text()"/></xsl:attribute>
		<xsl:attribute name="rel-type-id"><xsl:value-of select="personorg-relationship/rel-type-id/text()"/></xsl:attribute>
		<xsl:attribute name="rel-entity-id">IDREF:<xsl:value-of select="personorg-relationship/rel-entity-id/@IDREF"/></xsl:attribute>
	</personorg-relationship>
	</xsl:if>
	<xsl:if test="patient-indication">
	<patient-indication>
		<xsl:attribute name="rec-stat-id"><xsl:value-of select="patient-indication/rec-stat-id/text()"/></xsl:attribute>
		<xsl:attribute name="indication-type-id"><xsl:value-of select="patient-indication/indication-type-id/text()"/></xsl:attribute>
		<xsl:attribute name="indication"><xsl:value-of select="patient-indication/indication/text()"/></xsl:attribute>
	</patient-indication>
	</xsl:if>
	</person>
</xsl:template>
</xsl:stylesheet>