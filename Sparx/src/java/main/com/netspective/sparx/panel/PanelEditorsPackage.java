/*
 * Created by IntelliJ IDEA.
 * User: aye
 * Date: Feb 28, 2004
 * Time: 11:15:31 PM
 */
package com.netspective.sparx.panel;

import com.netspective.axiom.sql.QueriesNameSpace;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.form.DialogsNameSpace;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.sql.QueriesPackage;
import com.netspective.sparx.Project;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;

public class PanelEditorsPackage
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[] { "container" });
        XML_DATA_MODEL_SCHEMA_OPTIONS.addAliases("name-space-id", new String[] { "package" });
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
     *
     * @param identifier
     */
    public void setNameSpaceId(String identifier)
    {
        this.packageName = identifier;
        queriesPkg.setNameSpaceId(packageName);
        dialogsPkg.setNameSpaceId(packageName);
    }

    /**
     * Gets the query name space used for this panel editor package
     *
     * @return      associated queries name space
     */
    public QueriesNameSpace getQueriesNameSpace()
    {
        return queriesPkg;
    }

    /**
     * Gets the dialog name space used for this panel editor package
     *
     * @return      associated dialogs name space
     */
    public DialogsNameSpace getDialogsNameSpace()
    {
        return dialogsPkg;
    }

    public PanelEditor createPanelEditor()
    {
        return new PanelEditor(container.getProject(), this);
    }

    public PanelEditor createPanelEditor(Class cls) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        if(PanelEditor.class.isAssignableFrom(cls))
        {
            Constructor c = cls.getConstructor(new Class[] { Project.class, PanelEditorsPackage.class });
            return (PanelEditor) c.newInstance(new Object[] { container.getProject(), this });
        }
        else
            throw new RuntimeException("Don't know what to do with with class: " + cls);
    }

    public void addPanelEditor(PanelEditor panel)
    {
        panel.setNameSpace(this);
        container.add(panel);
    }

}