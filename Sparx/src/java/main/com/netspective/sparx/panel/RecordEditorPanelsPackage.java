/*
 * Created by IntelliJ IDEA.
 * User: aye
 * Date: Feb 28, 2004
 * Time: 11:15:31 PM
 */
package com.netspective.sparx.panel;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.axiom.sql.QueriesNameSpace;

public class RecordEditorPanelsPackage 
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[] { "container" });
        XML_DATA_MODEL_SCHEMA_OPTIONS.addAliases("name-space-id", new String[] { "package" });
    }

    private RecordEditorPanels container;
    private String packageName;

    public RecordEditorPanelsPackage(RecordEditorPanels panels)
    {
        setContainer(panels);
    }

    public RecordEditorPanels getContainer()
    {
        return container;
    }

    public void setContainer(RecordEditorPanels container)
    {
        this.container = container;
    }

    public String getNameSpaceId()
    {
        return packageName;
    }

    public void setNameSpaceId(String identifier)
    {
        this.packageName = identifier;
    }

    public RecordEditorPanel createRecordEditorPanel()
    {
        return new RecordEditorPanel(container.getProject(), this);
    }

    public void addRecordEditorPanel(RecordEditorPanel panel)
    {
        panel.setNameSpace(this);
        container.add(panel);
    }

}