<%@ page import="com.netspective.sparx.value.HttpServletValueContext,
                 com.netspective.sparx.value.BasicDbHttpServletValueContext,
                 com.netspective.axiom.sql.StoredProcedure,
                 com.netspective.sparx.navigate.NavigationControllerServlet,
                 com.netspective.sparx.navigate.NavigationContext,
                 com.netspective.axiom.sql.QueryResultSet"%>
<%

    BasicDbHttpServletValueContext vc = new  BasicDbHttpServletValueContext(this,
            request, response);
    NavigationContext nc = NavigationControllerServlet.getThreadNavigationContext();
    StoredProcedure sp = nc.getProject().getStoredProcedure("test.sp");
    int[] overrideIndexes = new int[] {2};
    Object[] overrideValues = new Object[] { new String("LABONTE")};
    QueryResultSet rs = sp.execute(nc, overrideIndexes, overrideValues, false);


%>

