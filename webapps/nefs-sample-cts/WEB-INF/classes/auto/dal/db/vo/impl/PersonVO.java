package auto.dal.db.vo.impl;


public class PersonVO
implements auto.dal.db.vo.Person
{
    
    public java.lang.Integer getAge()
    {
        return age;
    }
    
    public int getAgeInt()
    {
        return getAgeInt(-1);
    }
    
    public int getAgeInt(int defaultValue)
    {
        return age != null ? age.intValue() : defaultValue;
    }
    
    public java.util.Date getBirthDate()
    {
        return birthDate;
    }
    
    public java.lang.Integer getBloodTypeId()
    {
        return bloodTypeId;
    }
    
    public int getBloodTypeIdInt()
    {
        return getBloodTypeIdInt(-1);
    }
    
    public int getBloodTypeIdInt(int defaultValue)
    {
        return bloodTypeId != null ? bloodTypeId.intValue() : defaultValue;
    }
    
    public java.lang.String getCompleteName()
    {
        return completeName;
    }
    
    public java.lang.String getCompleteSortableName()
    {
        return completeSortableName;
    }
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.String getEthnicityId()
    {
        return ethnicityId;
    }
    
    public java.lang.Integer getGenderId()
    {
        return genderId;
    }
    
    public int getGenderIdInt()
    {
        return getGenderIdInt(-1);
    }
    
    public int getGenderIdInt(int defaultValue)
    {
        return genderId != null ? genderId.intValue() : defaultValue;
    }
    
    public java.lang.String getLanguageId()
    {
        return languageId;
    }
    
    public java.lang.Integer getMaritalStatusId()
    {
        return maritalStatusId;
    }
    
    public int getMaritalStatusIdInt()
    {
        return getMaritalStatusIdInt(-1);
    }
    
    public int getMaritalStatusIdInt(int defaultValue)
    {
        return maritalStatusId != null ? maritalStatusId.intValue() : defaultValue;
    }
    
    public java.lang.String getNameFirst()
    {
        return nameFirst;
    }
    
    public java.lang.String getNameLast()
    {
        return nameLast;
    }
    
    public java.lang.String getNameMiddle()
    {
        return nameMiddle;
    }
    
    public java.lang.String getNamePrefix()
    {
        return namePrefix;
    }
    
    public java.lang.Integer getNamePrefixId()
    {
        return namePrefixId;
    }
    
    public int getNamePrefixIdInt()
    {
        return getNamePrefixIdInt(-1);
    }
    
    public int getNamePrefixIdInt(int defaultValue)
    {
        return namePrefixId != null ? namePrefixId.intValue() : defaultValue;
    }
    
    public java.lang.String getNameSuffix()
    {
        return nameSuffix;
    }
    
    public java.lang.Long getPersonId()
    {
        return personId;
    }
    
    public long getPersonIdLong()
    {
        return getPersonIdLong(-1);
    }
    
    public long getPersonIdLong(long defaultValue)
    {
        return personId != null ? personId.longValue() : defaultValue;
    }
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
    }
    
    public int getRecStatIdInt()
    {
        return getRecStatIdInt(-1);
    }
    
    public int getRecStatIdInt(int defaultValue)
    {
        return recStatId != null ? recStatId.intValue() : defaultValue;
    }
    
    public java.lang.String getShortName()
    {
        return shortName;
    }
    
    public java.lang.String getShortSortableName()
    {
        return shortSortableName;
    }
    
    public java.lang.String getSimpleName()
    {
        return simpleName;
    }
    
    public java.lang.String getSsn()
    {
        return ssn;
    }
    
    public void setAge(java.lang.Integer age)
    {
        this.age = age;
    }
    
    public void setAgeInt(int age)
    {
        this.age = new java.lang.Integer(age);
    }
    
    public void setBirthDate(java.util.Date birthDate)
    {
        this.birthDate = birthDate;
    }
    
    public void setBloodTypeId(java.lang.Integer bloodTypeId)
    {
        this.bloodTypeId = bloodTypeId;
    }
    
    public void setBloodTypeIdInt(int bloodTypeId)
    {
        this.bloodTypeId = new java.lang.Integer(bloodTypeId);
    }
    
    public void setCompleteName(java.lang.String completeName)
    {
        this.completeName = completeName;
    }
    
    public void setCompleteSortableName(java.lang.String completeSortableName)
    {
        this.completeSortableName = completeSortableName;
    }
    
    public void setCrSessId(java.lang.String crSessId)
    {
        this.crSessId = crSessId;
    }
    
    public void setCrStamp(java.util.Date crStamp)
    {
        this.crStamp = crStamp;
    }
    
    public void setEthnicityId(java.lang.String ethnicityId)
    {
        this.ethnicityId = ethnicityId;
    }
    
    public void setGenderId(java.lang.Integer genderId)
    {
        this.genderId = genderId;
    }
    
    public void setGenderIdInt(int genderId)
    {
        this.genderId = new java.lang.Integer(genderId);
    }
    
    public void setLanguageId(java.lang.String languageId)
    {
        this.languageId = languageId;
    }
    
    public void setMaritalStatusId(java.lang.Integer maritalStatusId)
    {
        this.maritalStatusId = maritalStatusId;
    }
    
    public void setMaritalStatusIdInt(int maritalStatusId)
    {
        this.maritalStatusId = new java.lang.Integer(maritalStatusId);
    }
    
    public void setNameFirst(java.lang.String nameFirst)
    {
        this.nameFirst = nameFirst;
    }
    
    public void setNameLast(java.lang.String nameLast)
    {
        this.nameLast = nameLast;
    }
    
    public void setNameMiddle(java.lang.String nameMiddle)
    {
        this.nameMiddle = nameMiddle;
    }
    
    public void setNamePrefix(java.lang.String namePrefix)
    {
        this.namePrefix = namePrefix;
    }
    
    public void setNamePrefixId(java.lang.Integer namePrefixId)
    {
        this.namePrefixId = namePrefixId;
    }
    
    public void setNamePrefixIdInt(int namePrefixId)
    {
        this.namePrefixId = new java.lang.Integer(namePrefixId);
    }
    
    public void setNameSuffix(java.lang.String nameSuffix)
    {
        this.nameSuffix = nameSuffix;
    }
    
    public void setPersonId(java.lang.Long personId)
    {
        this.personId = personId;
    }
    
    public void setPersonIdLong(long personId)
    {
        this.personId = new java.lang.Long(personId);
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
    }
    
    public void setShortName(java.lang.String shortName)
    {
        this.shortName = shortName;
    }
    
    public void setShortSortableName(java.lang.String shortSortableName)
    {
        this.shortSortableName = shortSortableName;
    }
    
    public void setSimpleName(java.lang.String simpleName)
    {
        this.simpleName = simpleName;
    }
    
    public void setSsn(java.lang.String ssn)
    {
        this.ssn = ssn;
    }
    private java.lang.Integer age;
    private java.util.Date birthDate;
    private java.lang.Integer bloodTypeId;
    private java.lang.String completeName;
    private java.lang.String completeSortableName;
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.String ethnicityId;
    private java.lang.Integer genderId;
    private java.lang.String languageId;
    private java.lang.Integer maritalStatusId;
    private java.lang.String nameFirst;
    private java.lang.String nameLast;
    private java.lang.String nameMiddle;
    private java.lang.String namePrefix;
    private java.lang.Integer namePrefixId;
    private java.lang.String nameSuffix;
    private java.lang.Long personId;
    private java.lang.Integer recStatId;
    private java.lang.String shortName;
    private java.lang.String shortSortableName;
    private java.lang.String simpleName;
    private java.lang.String ssn;
}