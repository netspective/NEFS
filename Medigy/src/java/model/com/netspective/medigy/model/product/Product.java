/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.netspective.medigy.model.product;

import com.netspective.medigy.model.common.AbstractTopLevelEntity;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import java.util.Date;

@Entity
public class Product extends AbstractTopLevelEntity
{
    private Long productId;
    private String name;
    private Date introductionDate;
    private Date saleDiscontinuationDate;
    private Date supportDiscontinuationDate;
    private String comment;

    @Id(generate = GeneratorType.AUTO)
    public Long getProductId()
    {
        return productId;
    }

    protected void setProductId(final Long productId)
    {
        this.productId = productId;
    }

    public String getName()
    {
        return name;
    }

    @Column(length = 100)
    public void setName(final String name)
    {
        this.name = name;
    }

    public Date getIntroductionDate()
    {
        return introductionDate;
    }

    public void setIntroductionDate(final Date introductionDate)
    {
        this.introductionDate = introductionDate;
    }

    public Date getSaleDiscontinuationDate()
    {
        return saleDiscontinuationDate;
    }

    public void setSaleDiscontinuationDate(final Date saleDiscontinuationDate)
    {
        this.saleDiscontinuationDate = saleDiscontinuationDate;
    }

    public Date getSupportDiscontinuationDate()
    {
        return supportDiscontinuationDate;
    }

    public void setSupportDiscontinuationDate(final Date supportDiscontinuationDate)
    {
        this.supportDiscontinuationDate = supportDiscontinuationDate;
    }

    @Column(length = 100)
    public String getComment()
    {
        return comment;
    }

    public void setComment(final String comment)
    {
        this.comment = comment;
    }
}
