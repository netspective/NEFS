/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

package com.netspective.sparx.navigate;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.commons.xml.template.TemplateProducers;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.io.InputSourceLocator;
import com.netspective.sparx.Project;

public class NavigationTree implements TemplateProducerParent, XmlDataModelSchema.InputSourceLocatorListener, XmlDataModelSchema.ConstructionFinalizeListener
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final Log log = LogFactory.getLog(NavigationTree.class);
    public static final String TEMPLATEELEMNAME_PAGE_TYPE = "page-type";

    private Project project;
    private InputSourceLocator inputSourceLocator;
    private String name;
    private NavigationPage root;
    private NavigationPage homePage;
    private NavigationPage popupPage;
    private Map pagesByQualifiedName = new HashMap();
    private TemplateProducers templateProducers;
    private TemplateProducer pageTypes;
    private int maxLevel = -1;
    private boolean defaultTree;

    public NavigationTree(Project project)
    {
        this.project = project;
        root = constructRoot();
        root.setOwner(this);
        root.setName("");
    }

    public Project getProject()
    {
        return project;
    }

    public InputSourceLocator getInputSourceLocator()
    {
        return inputSourceLocator;
    }

    public void setInputSourceLocator(InputSourceLocator inputSourceLocator)
    {
        this.inputSourceLocator = inputSourceLocator;
    }

    public void finalizeContents()
    {
        if(root != null)
        {
            if (popupPage == null)
            {
                try
                {
                    popupPage = createPage();
                    NavigationPathFlags flags =  popupPage.createFlags();
                    flags.setFlag(NavigationPage.Flags.IS_POPUP_MODE | NavigationPage.Flags.HIDDEN);
                    popupPage.setFlags(flags);
                    popupPage.setName("popup");
                    root.addPage(popupPage);
                }
                catch (Exception e)
                {
                    log.warn("Failed to create default popup page", e);
                }
            }
            root.finalizeContents();
        }
    }

    public void finalizeConstruction(XdmParseContext pc, Object element, String elementName) throws DataModelException
    {
        finalizeContents();
    }

    public int size()
    {
        return root.size();
    }

    public boolean isDefaultTree()
    {
        return defaultTree;
    }

    public void setDefault(boolean defaultTree)
    {
        this.defaultTree = defaultTree;
    }

    public String getPageTypesTemplatesNameSpaceId()
    {
        return "/navigation-tree/" + getName() + "/page-type";
    }

    public TemplateProducers getTemplateProducers()
    {
        if(templateProducers == null)
        {
            templateProducers = new TemplateProducers();
            pageTypes = new TemplateProducer(getPageTypesTemplatesNameSpaceId(), TEMPLATEELEMNAME_PAGE_TYPE, "name", "type", false, false);
            templateProducers.add(pageTypes);
        }
        return templateProducers;
    }

    public TemplateProducer getPageTypes()
    {
        return pageTypes;
    }

    public void register(NavigationPath path)
    {
        pagesByQualifiedName.put(path.getQualifiedName(), path);
    }

    public void unregister(NavigationPath path)
    {
        pagesByQualifiedName.remove(path.getQualifiedName());
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public NavigationPage constructRoot()
    {
        return new NavigationPage();
    }

    public NavigationPage createPage() throws InstantiationException, IllegalAccessException
    {
        return root.createPage();
    }

    public void addPage(NavigationPage page)
    {
        root.addPage(page);
        if(page.isDefault())
        {
            pagesByQualifiedName.put("/", page);
            homePage = page;
        }
    }

    public NavigationPage getRoot()
    {
        return root;
    }

    public NavigationPage getHomePage()
    {
        return homePage;
    }

    public NavigationPage getPopupPage()
    {
        return popupPage;
    }

    public int getMaxLevel()
    {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel)
    {
        this.maxLevel = maxLevel;
    }

    public FindResults findPath(String path)
    {
        return new FindResults(path);
    };

    /**
     * A Class that describes the Results of matching the Http Request with the available Paths in NavigationPath.
     */
    public class FindResults
    {
        private String searchForPath;
        private NavigationPath searched;
        private NavigationPath matchedPath;
        private String[] unmatchedItems;

        /**
         * Constructs a <code>FindResult</code> object that will contain the appropiate information regarding the matching up of
         * the path string in the search NavigationPath.
         * @param path  A <code>String</code> object that represents tha path that is being requested.
         */
        public FindResults(String path)
        {
            if (path == null || path.length() == 0)
                path = "/";

            searchForPath = path;
            searched = root;

            matchedPath = (NavigationPath) pagesByQualifiedName.get(path);

            if (matchedPath != null)
                return;

            List unmatchedItemsList = new ArrayList();
            String partialPath = path;
            boolean finished = false;
            while (matchedPath == null && !finished)
            {
                int partialItemIndex = partialPath.lastIndexOf(NavigationPath.PATH_SEPARATOR);
                if (partialItemIndex == -1)
                {
                    matchedPath = (NavigationPath) pagesByQualifiedName.get(partialPath);
                    if (matchedPath == null)
                        unmatchedItemsList.add(0, partialPath);
                    finished = true;
                }
                else
                {
                    unmatchedItemsList.add(0, partialPath.substring(partialItemIndex + 1));
                    partialPath = partialPath.substring(0, partialItemIndex);
                    matchedPath = (NavigationPath) pagesByQualifiedName.get(partialPath);
                }
            }

            unmatchedItems = (String[]) unmatchedItemsList.toArray(new String[unmatchedItemsList.size()]);
        }

        /**
         * Returns the requested path.
         * @return  String  A string that represents the requested path.
         */
        public String getSearchedForPath()
        {
            return searchForPath;
        }

        /**
         * Returns <code>NavigationPath</code> object that the requested path is being matched against.
         * @return  NavigationPath  The object that the path is being searched on.
         */
        public NavigationPath getSearchedInPath()
        {
            return searched;
        }

        /**
         * Returns the <code>NavigationPath</code> object which id matched the requested path, or there was an absolute path
         * registered with the string as its id.
         * @return NavigationPath
         */
        public NavigationPath getMatchedPath()
        {
            return matchedPath;
        }

        /**
         * Returns a String array that contains the portions of the path that could not be matched.
         * @return String[]  Array of unmatched path items
         */
        public String[] getUnmatchedPathItems()
        {
            return unmatchedItems;
        }

        public boolean hasUnmatchedPathItems()
        {
            return unmatchedItems != null;
        }

        /**
         * Returns a concatenated String of all of the elements of getUnmatchedPathItems with a "/" as a path separator.
         * @param startItem the item number to start with
         * @return String  Full unmatched path
         */
        public String getUnmatchedPath(int startItem)
        {
            if (unmatchedItems == null || unmatchedItems.length == 0)
                return null;

            StringBuffer result = new StringBuffer();
            for (int i = startItem; i < unmatchedItems.length; i++)
            {
                result.append(NavigationPath.PATH_SEPARATOR);
                result.append(unmatchedItems[i]);
            }
            return result.toString();
        }
    }

    public String toString()
    {
        return getName() + "\n" + root;
    }
}
