<%@ page import="com.netspective.sparx.xaf.sql.StatementManager,
                 com.netspective.sparx.xaf.sql.StatementManagerFactory,
                 com.netspective.sparx.xaf.sql.StatementInfo,
                 com.netspective.sparx.xaf.sql.ResultInfo,
                 com.netspective.sparx.xif.db.DatabaseContextFactory,
                 com.netspective.sparx.util.value.ServletValueContext,
                 javax.mail.Transport,
                 javax.mail.internet.InternetAddress,
                 javax.mail.Message,
                 javax.mail.internet.MimeMessage,
                 javax.mail.Session,
                 java.util.Properties,
                 app.security.AppLoginDialog,
                 app.skin.AppNavigationSkin"%>
<%
    ServletValueContext vc = new ServletValueContext(application, (Servlet) page, request, response);

    String origUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

    AppNavigationSkin.sendEmail(
            "PIN "+ AppLoginDialog.getActivePin(vc) +" has completed the Marsh Risk Management Survey",
            "You can visit " + origUrl + " to see the results.");

    AppLoginDialog.lockPin(vc);

    response.sendRedirect("index.jsp?_logout=1");
%>