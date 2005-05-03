/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.netspective.medigy.model.product;

import com.netspective.medigy.model.org.Organization;
import com.netspective.medigy.model.common.AbstractTopLevelEntity;

import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE, discriminatorValue = "General")
public class Product extends AbstractTopLevelEntity
{
    private Long productId;
    private String name;
    private Date introductionDate;
    private Date saleDiscontinuationDate;
    private Date supportDiscontinuationDate;
    private String comment;
    private Organization organization;

    private ProductCategory productCategory;

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

    @ManyToOne
    @JoinColumn(name = "party_id")
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization party)
    {
        this.organization = party;
    }

    @ManyToOne
    @JoinColumn(name = "product_category_id")        
    public ProductCategory getProductCategory()
    {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory)
    {
        this.productCategory = productCategory;
    }

}
