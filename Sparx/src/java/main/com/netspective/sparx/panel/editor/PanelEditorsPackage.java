/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.netspective.sparx.panel.editor;

import com.netspective.axiom.sql.QueriesNameSpace;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.form.DialogsNameSpace;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.sql.QueriesPackage;

public class PanelEditorsPackage
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    /**
     * Prefix string for dialog and query packages defined in panel editors
     */
    public static final String NAMESPACE_PREFIX = "panel-editor.";

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[]{"container"});
        XML_DATA_MODEL_SCHEMA_OPTIONS.addAliases("name-space-id", new String[]{"package"});
    }

    private QueriesPackage queriesPkg;
    private DialogsPackage dialogsPkg;
    private PanelEditors container;
    private String packageName;

    public PanelEditorsPackage(PanelEditors panels)
    {
        setContainer(panels);
        queriesPkg = new QueriesPackage(panels.getProject(), panels.getProject().getQueries());
        dialogsPkg = new DialogsPackage(panels.getProject().getDialogs());
    }

    public PanelEditors getContainer()
    {
        return container;
    }

    public void setContainer(PanelEditors container)
    {
        this.container = container;
    }

    public String getNameSpaceId()
    {
        return packageName;
    }

    /**
     * Sets the name space (package) id. This name will also be used to assign name space IDs to the
     * associated dialogs and queries namespaces.
     */
    public void setNameSpaceId(String identifier)
    {
        this.packageName = identifier;
        queriesPkg.setNameSpaceId(NAMESPACE_PREFIX + packageName);
        dialogsPkg.setNameSpaceId(NAMESPACE_PREFIX + packageName);
    }

    /**
     * Gets the query name space used for this panel editor package
     *
     * @return associated queries name space
     */
    public QueriesNameSpace getQueriesNameSpace()
    {
        return queriesPkg;
    }

    /**
     * Gets the dialog name space used for this panel editor package
     *
     * @return associated dialogs name space
     */
    public DialogsNameSpace getDialogsNameSpace()
    {
        return dialogsPkg;
    }

    public PanelEditor createPanelEditor()
    {
        return new PanelEditor(container.getProject(), this);
    }


    public void addPanelEditor(PanelEditor panel)
    {
        panel.setNameSpace(this);
        container.add(panel);
    }

}