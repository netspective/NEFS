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
package com.netspective.sparx.security.authenticator;

import javax.servlet.ServletConfig;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.text.TextUtils;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.security.HttpLoginManager;
import com.netspective.sparx.security.LoginDialog;
import com.netspective.sparx.security.LoginDialogContext;

/**
 * Implements a basic login authenticator that assumes a single user has access to the entire servlet and the user
 * information is stored in servlet init parameter called <pre>com.netspective.sparx.security.authenticator.SingleUserServletLoginAuthenticator.OPTIONS</pre>.
 * The OPTIONS parameter is parsed as a command line using Jakarta CLI and has the following usage:
 * <pre>
 * -u,--user-id <id>                          The user id that should be used to log the user in.
 * -P,--encrypted-password <encrypted-text>   The encrypted password for the user.
 * -p,--plain-text-password <plain-text>      The plain-text password for the user.
 * -s,--show-encrypted-password               Prints the encrypted password to stdout (useful if you want to know what the plain-text password is when encrypted).
 * -?,--help                                  Print options to stdout.
 * </pre>
 * <p/>
 * Another alternative is to provide the user-id, plain-text-password, and encrypted-password as part of the XDM
 * definition in the Project declaration.
 */
public class SingleUserServletLoginAuthenticator extends AbstractLoginAuthenticator
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final Log log = LogFactory.getLog(SingleUserServletLoginAuthenticator.class);

    private String userId;
    private String plainTextPassword;
    private String encryptedPassword;
    private String optionsInitParamsName = SingleUserServletLoginAuthenticator.class.getName() + ".OPTIONS";

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getPlainTextPassword()
    {
        return plainTextPassword;
    }

    public void setPlainTextPassword(String plainTextPassword)
    {
        this.plainTextPassword = plainTextPassword;
    }

    public String getEncryptedPassword()
    {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword)
    {
        this.encryptedPassword = encryptedPassword;
    }

    public String getOptionsInitParamsName()
    {
        return optionsInitParamsName;
    }

    public void setOptionsInitParamsName(String optionsInitParamsName)
    {
        this.optionsInitParamsName = optionsInitParamsName;
    }

    private Options createAuthenticatorOptions()
    {
        Options authenticatorOptions = new Options();
        authenticatorOptions.addOption(OptionBuilder.withLongOpt("help")
                .withDescription("Print options to stdout")
                .create('?'));
        authenticatorOptions.addOption(OptionBuilder.withLongOpt("user-id")
                .hasArg().withArgName("id")
                .withDescription("The user id that should be used to log the user in")
                .isRequired()
                .create('u'));
        authenticatorOptions.addOption(OptionBuilder.withLongOpt("show-encrypted-password")
                .withDescription("Prints the encrypted version of plain-text password to stdout")
                .create('s'));

        OptionGroup passwordOptionGroup = new OptionGroup();
        passwordOptionGroup.setRequired(true);
        passwordOptionGroup.addOption(OptionBuilder.withLongOpt("plain-text-password")
                .hasArg().withArgName("plain-text")
                .withDescription("The plain-text password for the user")
                .create('p'));
        passwordOptionGroup.addOption(OptionBuilder.withLongOpt("encrypted-password")
                .hasArg().withArgName("encrypted-text")
                .withDescription("The encrypted password for the user")
                .create('P'));

        authenticatorOptions.addOptionGroup(passwordOptionGroup);
        return authenticatorOptions;
    }

    private CommandLine getOptionsCommandLine(LoginDialogContext loginDialogContext, String optionsText)
    {
        Options authenticatorOptions = createAuthenticatorOptions();
        CommandLineParser parser = new PosixParser();
        CommandLine result = null;
        try
        {
            result = parser.parse(authenticatorOptions, optionsText != null
                    ? TextUtils.getInstance().split(optionsText) : new String[0]);
            if (result.hasOption('?'))
                printHelp(authenticatorOptions);
        }
        catch (ParseException e)
        {
            log.error("Error parsing command line " + optionsText, e);
            printHelp(authenticatorOptions);
            loginDialogContext.getLoginDialog().getUserIdField().invalidate(loginDialogContext, "Error parsing command line: " + e);
            return null;
        }
        return result;
    }

    public boolean isUserValid(HttpLoginManager loginManager, LoginDialogContext loginDialogContext)
    {
        if (userId != null && (plainTextPassword != null || encryptedPassword != null))
        {
            String loginPasswordEncrypted = encryptedPassword;
            if (loginPasswordEncrypted == null)
                loginPasswordEncrypted = loginDialogContext.encryptPlainTextPassword(plainTextPassword);

            if (!userId.equals(loginDialogContext.getUserIdInput()))
                return false;

            if (!loginPasswordEncrypted.equals(loginDialogContext.getPasswordInput(!loginDialogContext.hasEncryptedPassword())))
                return false;
        }
        else
        {
            ServletConfig servletConfig = loginDialogContext.getServlet().getServletConfig();
            LoginDialog loginDialog = loginDialogContext.getLoginDialog();
            String optionsText = servletConfig.getInitParameter(optionsInitParamsName);
            if (optionsText == null)
            {
                log.error("Servlet param " + optionsInitParamsName + " not specified.");
                loginDialog.getUserIdField().invalidate(loginDialogContext, "Servlet param " + optionsInitParamsName + " not specified.");
                return false;
            }

            CommandLine commandLine = getOptionsCommandLine(loginDialogContext, optionsText);
            if (commandLine == null)
                return false;

            String loginUserId = commandLine.getOptionValue('u');
            String loginPasswordEncrypted = commandLine.getOptionValue('P');
            if (loginPasswordEncrypted == null)
            {
                String loginPasswordUnencrypted = commandLine.getOptionValue('p');
                if (loginPasswordUnencrypted != null)
                    loginPasswordEncrypted = loginDialogContext.encryptPlainTextPassword(loginPasswordUnencrypted);
            }

            if (commandLine.hasOption('s'))
                System.out.println(TextUtils.getInstance().getClassNameWithoutPackage(getClass().getName()) + " encrypted password is " + loginPasswordEncrypted);

            if (!loginUserId.equals(loginDialogContext.getUserIdInput()))
                return false;

            if (!loginPasswordEncrypted.equals(loginDialogContext.getPasswordInput(!loginDialogContext.hasEncryptedPassword())))
                return false;
        }
        return true;
    }

    private void printHelp(Options authenticatorOptions)
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.defaultWidth = 120;
        formatter.printHelp(getClass().getName(), authenticatorOptions);
    }
}
