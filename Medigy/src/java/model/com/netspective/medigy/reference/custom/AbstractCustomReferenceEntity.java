/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Aye Thu
 */
package com.netspective.medigy.reference.custom;

import javax.ejb.CascadeType;
import javax.ejb.Column;
import javax.ejb.JoinColumn;
import javax.ejb.ManyToOne;
import javax.ejb.Transient;

import com.netspective.medigy.model.common.AbstractTopLevelEntity;
import com.netspective.medigy.model.common.DataEncryptionType;
import com.netspective.medigy.model.common.EntitySeedData;
import com.netspective.medigy.model.party.Party;


public class AbstractCustomReferenceEntity extends AbstractTopLevelEntity implements CustomReferenceEntity, Comparable
{
    private Long systemId;
    private String code;
    private String name;
    private String description;
    private DataEncryptionType encryptionType;
    private int maxAllowed = 1;

    private Party party;

    @Transient
            public Long getSystemId()
    {
        return systemId;
    }

    protected void setSystemId(Long systemId)
    {
        this.systemId = systemId;
    }

    @Column(name = "code")
            public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @Column(nullable = false)
            public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(nullable = false)
            public DataEncryptionType getEncryptionType()
    {
        return encryptionType;
    }

    public void setEncryptionType(DataEncryptionType encryptionType)
    {
        this.encryptionType = encryptionType;
    }

    @Column(nullable = false)
            public int getMaxAllowed()
    {
        return maxAllowed;
    }

    public void setMaxAllowed(int maxAllowed)
    {
        this.maxAllowed = maxAllowed;
    }

    @Column(length = 256)
            public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    @ManyToOne(cascade = CascadeType.ALL)
            @JoinColumn(name = "party_id")
            public Party getParty()
    {
        return party;
    }

    public void setParty(final Party party)
    {
        this.party = party;
    }

    public int compareTo(Object o)
    {
        if (o == this)
            return 0;

        final CustomReferenceEntity otherType = (CustomReferenceEntity) o;
        if (otherType.getSystemId().longValue() == this.getSystemId().longValue())
            return 0;
        else
            return -1;
    }

    protected class CustomSeedData
    {
        private String code;
        private String label;
        private String description;

        public CustomSeedData(String code)
        {
            this.code = code;
        }

        public CustomSeedData(String code, String label)
        {
            this.code = code;
            this.label = label;
        }

        public CustomSeedData(String code, String label, String description)
        {
            this.code = code;
            this.label = label;
            this.description = description;
        }
    }

    protected static final EntitySeedData createSeedData(final CustomSeedData[] customSeedData)
    {
        final String[] columnNames = new String[]{"code", "label", "description"};
        final Object[][] data = new Object[customSeedData.length][];
        for (int i = 0; i < customSeedData.length; i++)
        {
            final CustomSeedData srcRow = customSeedData[i];
            final Object[] destRow = new Object[3];
            destRow[0] = srcRow.code;
            destRow[1] = srcRow.label;
            destRow[2] = srcRow.description;
            data[i] = destRow;
        }

        return new EntitySeedData()
        {
            public String[] getColumnNames()
            {
                return columnNames;
            }

            public Object[][] getSeedData()
            {
                return data;
            }
        };

    }
}
