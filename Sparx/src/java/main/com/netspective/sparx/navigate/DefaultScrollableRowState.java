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
package com.netspective.sparx.navigate;

public class DefaultScrollableRowState implements ScrollableRowsState
{
    private final int scrollTotalRows;
    private final int scrollRowsPerPage;
    private final int scrollTotalPages;
    private final int scrollPagesRangeSize;  // number of scrollable pages to show in the page navigation screen
    private int scrollActivePage;

    public DefaultScrollableRowState(int scrollTotalRows, int scrollRowsPerPage, int scrollPagesRangeSize)
    {
        this.scrollTotalRows = scrollTotalRows;
        this.scrollRowsPerPage = scrollRowsPerPage;
        this.scrollPagesRangeSize = scrollPagesRangeSize;

        this.scrollActivePage = 1;
        this.scrollTotalPages = (scrollTotalRows % scrollRowsPerPage == 0) ? (scrollTotalRows / scrollRowsPerPage) :
                                ((scrollTotalRows / scrollRowsPerPage) + 1);
    }

    public int getScrollActivePageStartRow()
    {
        return ((scrollActivePage - 1) * scrollRowsPerPage);
    }

    public int getScrollActivePageEndRow()
    {
        int result = getScrollActivePageStartRow() + scrollRowsPerPage;
        if(result > scrollTotalRows)
            result = scrollTotalRows;
        return result;
    }

    public int getScrollTotalRows()
    {
        return scrollTotalRows;
    }

    public int getScrollRowsPerPage()
    {
        return scrollRowsPerPage;
    }

    public int getScrollTotalPages()
    {
        return scrollTotalPages;
    }

    public int getScrollActivePage()
    {
        return scrollActivePage;
    }

    public boolean scrollToPage(int page)
    {
        if(page < 0 || page > scrollTotalPages)
            return false;

        this.scrollActivePage = page;
        return true;
    }

    public boolean isScrollable()
    {
        return scrollTotalRows > scrollRowsPerPage;
    }

    public int getScrollPagesRangeSize()
    {
        return scrollPagesRangeSize;
    }

    public int getScrollPagesRangeStartPage()
    {
        if(scrollTotalPages < scrollPagesRangeSize)
            return 1;

        int result = scrollActivePage - scrollPagesRangeSize;
        return result > 0 ? result : 1;
    }

    public int getScrollPagesRangeEndPage()
    {
        if(scrollTotalPages < scrollPagesRangeSize)
            return scrollTotalPages;

        int result = scrollActivePage + scrollPagesRangeSize;
        return result <= scrollTotalPages ? result : scrollTotalPages;
    }
}
