<div align="left" style="padding:10">
    <font size="3">
        <b>Have you completed the survey? If so, please click <a href="../submit.jsp">here</a> to submit the results to Marsh.</b>
    </font>
    <p>
    Please realize that once you submit the results, your responses will be e-mailed to Marsh and you will no longer be
    able to modify the responses. If you would like to change any of your responses before submitting, please make the
    appropriate changes by clicking the menus on the left and then return to this Summary page to submit the results.
    <p>
    <table cellpadding=0 border=0 cellpadding=0>
    <tr valign="top"><td>
    <sparx:query name="table.Respondent.summary" skin="detail-compressed"/>
    </td><td>&nbsp;&nbsp;&nbsp;</td><td>
    <%
        ServletValueContext vc = new ServletValueContext(application, (Servlet) page, request, response);
    %>
    <sparx:panel heading="Current Environment">
    <%
        try
        {
            ConnectionContext cc = ConnectionContext.getConnectionContext(DatabaseContextFactory.getContext(request, application), application.getInitParameter("default-data-source"), ConnectionContext.CONNCTXTYPE_AUTO);
            CurrentEnvironmentTable ceTable = DataAccessLayer.instance.getCurrentEnvironmentTable();
            CurrentEnvironmentRow ceRow = ceTable.getCurrentEnvironmentByPin(cc, AppLoginDialog.getActivePin(vc));
            if(ceRow != null)
            {
                %>
                <table>
                    <tr valign="top"><td>Is there a formalized approach to identify risks to the business objectives:</td><td><%= ceRow.getApproach("<not answsered>") + (ceRow.getApproachExpl() != null ? (" (" + ceRow.getApproachExpl() + ")") : "") %></td></tr>
                    <tr valign="top"><td>Are risks adequately identified for your business unit:</td><td><%= ceRow.getRisksDept("<not answsered>") + (ceRow.getRisksDeptExpl() != null ? (" (" + ceRow.getRisksDeptExpl() + ")") : "") %></td></tr>
                    <tr valign="top"><td>Are risks adequately identified for your division:</td><td><%= ceRow.getRisksDiv("<not answsered>") + (ceRow.getRisksDivExpl() != null ? (" (" + ceRow.getRisksDivExpl() + ")") : "") %></td></tr>
                    <tr valign="top"><td>Are risks adequately identified for your project:</td><td><%= ceRow.getRisksPrj("<not answsered>") + (ceRow.getRisksPrjExpl() != null ? (" (" + ceRow.getRisksPrjExpl() + ")") : "") %></td></tr>
                    <tr valign="top"><td>Are risks increasing, decreasing, or remaining the same over the past year:</td><td><%= ceRow.getRisksDelta("<not answsered>") + (ceRow.getRisksDeltaExpl() != null ? (" (" + ceRow.getRisksDeltaExpl() + ")") : "") %></td></tr>
                </table>
                <%
            }
            else
            {
                out.write("No Current Environment questions answered yet.");
            }
        }
        catch(Exception e)
        {
            out.write(e.toString());
        }
    %>
    </sparx:panel>
    </td></tr>
    </table>
    <p>
    <sparx:panel heading="Risk Responses">
    <%
        // read all the responses into a variable
        Object[][] riskResponses = null;
        StatementManager stmtMgr = StatementManagerFactory.getManager(application);
        StatementInfo si = stmtMgr.getStatement(application, null, id.statement.Responses.BY_PIN);
        try
        {
            ResultInfo ri = si.execute(DatabaseContextFactory.getContext(request, application), vc, null, null);
            riskResponses = StatementManager.getResultSetRowsAsMatrix(ri.getResultSet());
            ri.close();
        }
        catch (Exception e)
        {
            throw new JspException(e);
        }

        if(riskResponses != null)
        {
            // replace all the enumeration values with their captions (probably faster than doing joins)
            String lastRiskGroup = "";
            for(int i = 0; i < riskResponses.length; i++)
            {
                Object[] riskResponse = riskResponses[i];
                String thisRiskGroup = (String) riskResponse[0];

                if(i > 0 && riskResponse[0].equals(lastRiskGroup))
                    riskResponse[0] = "";

                for(int j = 2; j < 8; j++)
                {
                    int enumValue = riskResponse[j] != null ? Integer.parseInt(riskResponse[j].toString()) : 0;
                    if(enumValue == 0)
                        riskResponse[j] = "";
                    else
                    {
                        RiskRatingTable.EnumeratedItem item = RiskRatingTable.EnumeratedItem.getItemById(new Integer(enumValue));
                        riskResponse[j] = item.getCaption();
                    }
                }

                lastRiskGroup = thisRiskGroup;
            }

            final ReportColumn[] columns =
                    new ReportColumn[]
                    {
                        new GeneralColumn(0, "Group"),
                        new GeneralColumn(1, "Risk"),
                        new GeneralColumn(2, "Business Unit Significance"),
                        new GeneralColumn(3, "Business Unit Effectiveness"),
                        new GeneralColumn(4, "Larger Group Significance"),
                        new GeneralColumn(5, "Larger Group Effectiveness"),
                        new GeneralColumn(6, "Batelle Overall Significance"),
                        new GeneralColumn(7, "Batelle Overall Effectiveness"),
                    };

            ReportSkin skin = SkinFactory.getDefaultReportSkin();
            Report report = new StandardReport();
            report.initialize(columns, null);

            ReportContext rc = new com.netspective.sparx.xaf.report.ReportContext(vc, report, skin);
            rc.produceReport(out, riskResponses);
        }
        else
        {
            out.write("No risk responses entered at this time.");
        }
    %>
    </sparx:panel>
</div>
