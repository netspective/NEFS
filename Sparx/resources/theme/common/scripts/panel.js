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
 * @author Shahid N. Shah
 */

/**
 * $Id: panel.js,v 1.1 2003-08-22 14:29:40 shahid.shah Exp $
 */

// **************************************************************************
// Cookie functions
// **************************************************************************

// name - name of the cookie
// value - value of the cookie
// [expires] - expiration date of the cookie (defaults to end of current session)
// [path] - path for which the cookie is valid (defaults to path of calling document)
// [domain] - domain for which the cookie is valid (defaults to domain of calling document)
// [secure] - Boolean value indicating if the cookie transmission requires a secure transmission
// * an argument defaults when it is assigned null as a placeholder
// * a null placeholder is not required for trailing omitted arguments

function setCookie(name, value, expires, path, domain, secure)
{
    var curCookie = name + "=" + escape(value) +
      ((expires) ? "; expires=" + expires.toGMTString() : "") +
      ((path) ? "; path=" + path : "") +
      ((domain) ? "; domain=" + domain : "") +
      ((secure) ? "; secure" : "");
    document.cookie = curCookie;
}

// name - name of the desired cookie
// * return string containing value of specified cookie or null if cookie does not exist
function getCookie(name)
{
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1)
    {
        begin = dc.indexOf(prefix);
        if (begin != 0) return null;
    }
    else
        begin += 2;

    var end = document.cookie.indexOf(";", begin);
    if (end == -1)
        end = dc.length;

    return unescape(dc.substring(begin + prefix.length, end));
}

// name - name of the cookie
// [path] - path of the cookie (must be same as path used to create cookie)
// [domain] - domain of the cookie (must be same as domain used to create cookie)
// * path and domain default if assigned null or omitted if no explicit argument proceeds
function deleteCookie(name, path, domain)
{
    if (getCookie(name))
    {
        document.cookie = name + "=" +
        ((path) ? "; path=" + path : "") +
        ((domain) ? "; domain=" + domain : "") +
        "; expires=Thu, 01-Jan-70 00:00:01 GMT";
    }
}

// **************************************************************************
// Browser functions
// **************************************************************************
function Browser()
{
    //Browsercheck (needed)
    this.ver = navigator.appVersion;
    this.agent = navigator.userAgent;
    this.dom = document.getElementById? true : false;
    this.ie5 = (this.ver.indexOf("MSIE 5")>-1 && this.dom)? true : false;
    this.ie6 = (this.ver.indexOf("MSIE 6")>-1 && this.dom)? true : false;
    this.ie4 = (document.all && !this.dom)? true : false;
    this.ie = this.ie4 || this.ie5 || this.ie6;
    this.mac = this.agent.indexOf("Mac") > -1;
    this.opera5 = this.agent.indexOf("Opera 5") > -1;
    this.ns6 = (this.dom && parseInt(this.ver) >= 5) ? true : false;
    this.ns4 = (document.layers && !this.dom)? true : false;
    this.browser = (this.ie6 || this.ie5 || this.ie4 || this.ns4 || this.ns6 || this.opera5 || this.dom);

    if (this.ie5 || this.ie6 || this.ns6)
    {
        this.getElement = Browser_getControl_Dom;
    }
    else if (this.ie4)
    {
        this.getElement = Browser_getControl_IE4;
    }
    else
    {
        this.getElement = Browser_getControl_NotSupported;
    }

    return this
}

var browser = new Browser();

// Get the dialog field control for IE4
function Browser_getControl_IE4(id)
{
    return document.all.item(id);
}

// Get the dialog field control for DOM browsers such as IE5, IE6 and NS6
function Browser_getControl_Dom(id)
{
    return document.getElementById(id);
}

// Get the dialog field control for all other browsers
function Browser_getControl_NotSupported(id)
{
    alert("browser.getControl() not supported");
}

// -------------------------------------------------------------------------------------------------------------------
// -- DHTML display/hide functions
// -------------------------------------------------------------------------------------------------------------------

var DISPLAYTYPE_TOGGLEVISIBLILITY = -1; // use this for "visible" param when visibility should be toggled from DOM node

function setDisplay(elementId, visible, alertIfNotFound)
{
    var element = browser.getElement(elementId);
    if(element == null)
    {
        if(alertIfNotFound)
            alert("setDisplay() element '"+ elementId +"' is NULL");
        return null;
    }

    if(element != null)
    {
        if(visible == DISPLAYTYPE_TOGGLEVISIBLILITY)
        {
            if(element.style.display == 'none')
                visible = true;
            else
                visible = false;
        }

        if(visible)
            element.style.display = '';
        else
            element.style.display = 'none';

        return visible;
    }
}

function toggleDisplay(controllerId, partnerId, visibleHtml, invisibleHtml)
{
    var visible = setDisplay(partnerId, DISPLAYTYPE_TOGGLEVISIBLILITY, true);
    if(visible == null)
        return;

    var controllerElement = browser.getElement(controllerId);
    if(controllerElement != null)
        controllerElement.innerHtml = visible ? visibleHtml : invisibleHtml;
}

// -------------------------------------------------------------------------------------------------------------------
// -- Panels collection object
// -------------------------------------------------------------------------------------------------------------------

function Panels()
{
    this.byId = new Array();
    this.byIndex = new Array();
    this.initialize = Panels_initialize;
    this.registerPanel = Panels_registerPanel;
    this.getPanel = Panels_getPanel;
    this.setPanelExpandCollapse = Panels_setPanelExpandCollapse;
    this.togglePanelExpandCollapse = Panels_togglePanelExpandCollapse;
}

function Panels_initialize()
{
    for(var i = 0; i < this.byIndex.length; i++)
        this.byIndex[i].initialize();
}

function Panels_registerPanel(panel)
{
    this.byId[panel.identifier] = panel;
    this.byIndex[this.byIndex.length] = panel;
    panel.parent = this;
}

function Panels_getPanel(panelId)
{
    var panel = this.byId[panelId];
    if(panel == null)
    {
        alert("Panel '"+ panelId +"' was not registered.");
        return null;
    }

    return panel;
}

function Panels_setPanelExpandCollapse(panelId, minimized)
{
    var panel = this.getPanel(panelId);
    if(panel != null)
        panel.setExpandCollapse(minimized);
}

function Panels_togglePanelExpandCollapse(panelId)
{
    var panel = this.getPanel(panelId);
    if(panel != null)
        panel.toggleExpandCollapse();
}

var ALL_PANELS = new Panels();
var ACTIVE_PANEL_PARENT = ALL_PANELS;   // new panels created will become children of this panel

function initializeBody()
{
    ALL_PANELS.initialize();
}

// the following are used to "start" and "end" panels so that any panels created in the middle of a start and end
// become children of the given panel -- this works with only one level (not really like a stack)

function startParentPanel(panel)
{
    ACTIVE_PANEL_PARENT = panel.children;
}

function endParentPanel()
{
    ACTIVE_PANEL_PARENT.initialize();
    ACTIVE_PANEL_PARENT = ALL_PANELS;
}

// -------------------------------------------------------------------------------------------------------------------
// -- Panel object
// -------------------------------------------------------------------------------------------------------------------

var PANELSTYLE_TOPLEVEL = 0;
var PANELSTYLE_TABBED   = 1;

function Panel(panelId, minimized, style, classNamePrefix)
{
    this.parent = null;
    this.identifier = panelId;
    this.minimized = minimized == null ? (style == PANELSTYLE_TABBED ? true : false) : minimized;
    this.classNamePrefix = classNamePrefix;
    this.initialize = Panel_initialize;
    this.setStyle = Panel_setStyle;
    this.getMinimizedCookieName = Panel_getMinimizedCookieName;
    this.toggleExpandCollapse = Panel_toggleExpandCollapse;
    this.children = new Panels();

    this.setStyle(style == null ? PANELSTYLE_TOPLEVEL : style, true);

    return this;
}

function Panel_setStyle(style, inConstructor)
{
    this.style = style;

    if(this.style == PANELSTYLE_TOPLEVEL)
        this.setExpandCollapse = Panel_setExpandCollapseTopLevel;
    else if(this.style == PANELSTYLE_TABBED)
    {
        this.setExpandCollapse = Panel_setExpandCollapseTabbed;
        this.minimized = true;
    }
    else
        alert("Unknown panel style " + this.style + " in panel " + this.identifier);
}

function Panel_initialize()
{
    var isMinimized = getCookie(this.getMinimizedCookieName());
    if(isMinimized != null)
        this.minimized = isMinimized == 1 ? true : false;

    this.setExpandCollapse(this.minimized);
}

function Panel_getMinimizedCookieName()
{
    return "panel_" + this.identifier + "_minimized";
}

function Panel_setExpandCollapseTopLevel(minimized)
{
    this.minimized = minimized;

    setDisplay(this.identifier + "_content", ! minimized, true);
    setDisplay(this.identifier + "_tabs", ! minimized, false);
    setDisplay(this.identifier + "_banner", ! minimized, false);
    setDisplay(this.identifier + "_banner_footer", ! minimized, false);

    var actionCell = browser.getElement(this.identifier + "_action");
    if(actionCell != null)
        actionCell.className = this.classNamePrefix + "-" + (this.minimized ? "frame-heading-action-expand" : "frame-heading-action-collapse");

    setCookie(this.getMinimizedCookieName(), this.minimized ? 1 : 0, new Date("December 31, 2049"));
}

function Panel_setExpandCollapseTabbed(minimized, recursive)
{
    this.minimized = minimized;

    // if we're being selected, turn all the other siblings "off"
    if(!recursive && !minimized)
    {
        for(var i = 0; i < this.parent.byIndex.length; i++)
        {
            var sibling = this.parent.byIndex[i];
            if(sibling != this)
                sibling.setExpandCollapse(true, true);
        }
    }

    setDisplay(this.identifier + "_container", ! minimized, false);
    setDisplay(this.identifier + "_content", ! minimized, false);
    setDisplay(this.identifier + "_tabs", ! minimized, false);
    setDisplay(this.identifier + "_banner", ! minimized, false);
    setDisplay(this.identifier + "_banner_footer", ! minimized, false);

    var tab = browser.getElement(this.identifier + "_tab");
    if(tab != null)
        tab.className = this.classNamePrefix + "-" + (this.minimized ? "tab" : "tab-active");

    setCookie(this.getMinimizedCookieName(), this.minimized ? 1 : 0, new Date("December 31, 2049"));
}

function Panel_toggleExpandCollapse()
{
    this.setExpandCollapse(this.minimized ? false : true);
}

// -------------------------------------------------------------------------------------------------------------------
// -- Tree management
// -------------------------------------------------------------------------------------------------------------------

function Trees()
{
    this.byIndex = new Array();
    this.byId = new Array();
    this.registerTree = Trees_registerTree;
    this.toggleNodeExpansion = Trees_toggleNodeExpansion;
}

function Trees_registerTree(tree)
{
    this.byIndex[this.byIndex.length] = tree;
    this.byId[tree.identifier] = tree;
}

function Trees_toggleNodeExpansion(treeId, nodeId)
{
    var tree = this.byId[treeId];
    if(tree == null)
    {
        alert("Tree '"+ treeId +"' not found.");
        return null;
    }

    tree.toggleNodeExpansion(nodeId);
}

var TREES = new Trees();

// -------------------------------------------------------------------------------------------------------------------

function Tree(treeId)
{
    this.identifier = treeId;
    this.allNodesById = new Array();
    this.children = new Array();
    this.newNode = Tree_newNode;
    this.initialize = Tree_initialize;
    this.toggleNodeExpansion = Tree_toggleNodeExpansion;
}

function Tree_initialize()
{
    for(var i = 0; i < this.children.length; i++)
        this.children[i].initialize();
}

function Tree_newNode(parentNodeId, nodeId)
{
    var parentNode = parentNodeId != null ? this.allNodesById[parentNodeId] : null;
    if(parentNode != null)
        return parentNode.newNode(nodeId);
    else
    {
        var node = new TreeNode(this, null, nodeId);
        this.allNodesById[nodeId] = node;
        this.children[this.children.length] = node;
        return node;
    }
}

function Tree_toggleNodeExpansion(nodeId)
{
    var node = this.allNodesById[nodeId];
    if(node == null)
    {
        alert("Node '"+ nodeId +"' not found in tree '"+ this.identifier +"'. " + this.children.length);
        return null;
    }

    node.toggleExpansion();
}

// -------------------------------------------------------------------------------------------------------------------

function TreeNode(tree, parent, nodeId)
{
    this.tree = tree;
    this.parent = parent;
    this.identifier = nodeId;
    this.visible = true;
    this.expanded = true;
    this.children = new Array();
    this.initialize = TreeNode_initialize;
    this.newNode = TreeNode_newNode;
    this.getControllerElement = TreeNode_getControllerElement;
    this.getRow = TreeNode_getRow;
    this.toggleExpansion = TreeNode_toggleExpansion;
    this.setExpanded = TreeNode_setExpanded;
    this.setDisplay = TreeNode_setDisplay;
}

function TreeNode_initialize()
{
    this.setDisplay(this.parent != null ? this.parent.visible : true);
    this.setExpanded(true);

    for(var i = 0; i < this.children.length; i++)
        this.children[i].initialize();

}

function TreeNode_newNode(nodeId)
{
    var node = new TreeNode(this.tree, this, nodeId);
    this.children[this.children.length] = node;
    this.tree.allNodesById[nodeId] = node;
    return node;
}

function TreeNode_getControllerElement(ignoreError)
{
    var elemId = this.tree.identifier + "_" + this.identifier +"_controller";
    var elem = browser.getElement(elemId);
    if(elem == null && ! ignoreError)
        alert("Node controller element '"+ elemId +"' not found.");
    return elem;
}

function TreeNode_getRow(ignoreError)
{
    var elemId = this.tree.identifier + "_" + this.identifier;
    var elem = browser.getElement(elemId);
    if(elem == null && ! ignoreError)
        alert("Node row '"+ elemId +"' not found.");
    return elem;
}

function TreeNode_toggleExpansion()
{
    this.setExpanded(! this.expanded);
}

function TreeNode_setExpanded(expanded)
{
    var elem = this.getControllerElement(true);
    if(elem == null)
        return;

    if(this.children.length == 0)
    {
        elem.className = "panel-output-tree-leaf";
        return;
    }

    this.expanded = expanded;

    if(elem != null)
    {
        if(expanded)
            elem.className = "panel-output-tree-collapse";
        else
            elem.className = "panel-output-tree-expand";
    }
    else
        return;

    for(var i = 0; i < this.children.length; i++)
        this.children[i].setDisplay(expanded);
}

function TreeNode_setDisplay(visible)
{
    this.visible = visible;

    var elem = this.getRow(true);
    if(elem != null)
        elem.style.display = visible ? '' : 'none';
    else
        return;

    for(var i = 0; i < this.children.length; i++)
        this.children[i].setDisplay(visible);
}
