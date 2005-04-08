/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.netspective.medigy.model.invoice;

import com.netspective.medigy.model.common.AbstractTopLevelEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

@Entity
public class Invoice  extends AbstractTopLevelEntity
{
    private Long invoiceId;
    private Date invoiceDate;
    private String description;
    private String message;

    private Set<InvoiceItem> items = new HashSet<InvoiceItem>();

    @Id(generate = GeneratorType.AUTO)
    public Long getInvoiceId()
    {
        return invoiceId;
    }

    protected void setInvoiceId(final Long invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    public Date getInvoiceDate()
    {
        return invoiceDate;
    }

    public void setInvoiceDate(final Date invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescriptionfinal(final String description)
    {
        this.description = description;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(final String message)
    {
        this.message = message;
    }

    @OneToMany
    @JoinColumn(name = "invoice_id")
    public Set<InvoiceItem> getItems()
    {
        return items;
    }

    public void setItems(final Set<InvoiceItem> items)
    {
        this.items = items;
    }
}
