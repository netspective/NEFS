<p>
<style>
    table.filesys-browser-struct {}
    table.filesys-browser-struct td { font-family: verdana; font-size: 9pt; }
    table.filesys-browser-struct tr.even {}
    table.filesys-browser-struct tr.odd {}
    table.filesys-browser-struct td.even {}
    table.filesys-browser-struct td.odd {}
    table.filesys-browser-struct span.active-entry-name { font-weight: bold; }
    table.filesys-browser-files {}
    table.filesys-browser-files td { font-family: verdana; font-size: 9pt; }
</style>
<#assign browserPage = vc.navigationContext.activePage/>
<table>
    <tr valign=top>
        <td><@panel heading='Location'>${browserPage.getDirectoryStructureHtml(vc, false, false)}</@panel></td>
        <td>&nbsp;&nbsp;</td>
        <td><@panel heading='Entries'>${browserPage.getChildEntriesHtml(vc, false, false)}</@panel></td>
    </tr>
</table>
