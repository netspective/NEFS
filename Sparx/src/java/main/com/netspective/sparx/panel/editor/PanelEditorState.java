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

import java.io.Serializable;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.text.GloballyUniqueIdentifier;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.navigate.NavigationContext;

/**
 * State class for containing context specific information for rendering a panel editor
 */
public class PanelEditorState implements Serializable
{
    private static final Log log = LogFactory.getLog(PanelEditorState.class);

    /* the name of the associated panel editor */
    private String panelEditorName;
    /* current mode */
    private int currentMode;
    /* previous mode */
    private int previousMode;
    /* record key */
    private String recordKey;
    /* state unique identifier */
    private String identifier;
    /* whether or not the panel editor is within a special custom panel editor group */
    private boolean grouped;
    /* list of panel editors that belong to the same group */
    private String[] groupSiblings;
    /* current active element */
    private String activeElement;
    /* information that is specfiic to the active element's content type */
    private String activeElementInfo;
    /* list to keep track of state of child elements of the panel editor */
    private Map childStates = new HashMap();


    /**
     * @param panelEditorName name of the panel editor associated with the state
     */
    public PanelEditorState(String panelEditorName)
    {
        this.panelEditorName = panelEditorName;
        try
        {
            identifier = GloballyUniqueIdentifier.getRandomGUID(true);
        }
        catch (NoSuchAlgorithmException e)
        {
            identifier = Integer.toString(hashCode());
        }
        catch (UnknownHostException e)
        {
            identifier = Integer.toString(hashCode());
        }
    }

    public void addElementState(PanelEditorContentElement.PanelEditorContentState childState)
    {
        childStates.put(childState.getElementName(), childState);
    }

    public PanelEditorContentElement.PanelEditorContentState getElementState(String elementName)
    {
        return (PanelEditorContentElement.PanelEditorContentState) childStates.get(elementName);
    }

    public String getActiveElement()
    {
        return activeElement;
    }

    /**
     * Sets the name of the content element that is currently active
     *
     * @param activeElement
     */
    public void setActiveElement(String activeElement)
    {
        this.activeElement = activeElement;
    }

    public void initialize(NavigationContext nc)
    {

    }

    public boolean isGrouped()
    {
        return grouped;
    }

    public void setGrouped(boolean grouped)
    {
        this.grouped = grouped;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public String getPanelEditorName()
    {
        return panelEditorName;
    }

    public int getCurrentMode()
    {
        return currentMode;
    }

    public void setCurrentMode(int currentMode)
    {
        this.currentMode = currentMode;
    }

    public int getPreviousMode()
    {
        return previousMode;
    }

    public void setPreviousMode(int previousMode)
    {
        this.previousMode = previousMode;
    }

    public String getRecordKey()
    {
        return recordKey;
    }

    public void setRecordKey(String recordKey)
    {
        this.recordKey = recordKey;
    }

    public String calculateNextModeUrl(DialogContext dc)
    {
        return PanelEditor.calculateNextModeUrl(dc, panelEditorName, currentMode, previousMode, recordKey);
    }

    public String getActiveElementInfo()
    {
        return activeElementInfo;
    }

    public void setActiveElementInfo(String activeElementInfo)
    {
        this.activeElementInfo = activeElementInfo;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("PanelEdiorState id = " + getIdentifier() + ", ");
        sb.append("panel editor = " + getPanelEditorName() + ", ");
        sb.append("active element = " + getActiveElement() + ", ");
        sb.append("mode = " + getCurrentMode() + ", ");
        sb.append("previous mode = " + getPreviousMode() + ", ");
        sb.append("key = " + getRecordKey());
        return sb.toString();
    }

}