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
package com.netspective.sparx.form.action;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.sparx.Project;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogFlags;
import com.netspective.sparx.form.DialogValidationContext;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFields;

public class ActionDialog extends Dialog
{
    private ActionWrapper action;

    /**
     * Default constructor solely used by XDM
     */
    public ActionDialog()
    {
        super();
        setDialogContextClass(ActionDialogContext.class);
    }

    public ActionDialog(Project project)
    {
        super(project);
        setDialogContextClass(ActionDialogContext.class);
    }

    public ActionDialog(Project project, DialogsPackage pkg)
    {
        super(project, pkg);
        setDialogContextClass(ActionDialogContext.class);
    }

    public DialogFlags createDialogFlags()
    {
        return new ActionDialogFlags();
    }

    public ActionWrapper createAction()
    {
        // this method will be called by XDM first to create a temporary action wrapper but it will never actually be
        // used -- the method below is the one that will actually be called
        return null;
    }

    /**
     * This is the method XDM will call when <action class="x.y.z"/> is encountered.
     *
     * @param cls The class that should be used as the action class
     *
     * @return An action
     */
    public ActionWrapper createAction(Class cls)
    {
        return new ActionWrapper(this, cls);
    }

    public void addAction(ActionWrapper action)
    {
        this.action = action;
    }

    public ActionWrapper getAction()
    {
        return action;
    }

    public boolean isValid(DialogContext dc)
    {
        ActionDialogContext adc = ((ActionDialogContext) dc);

        // if any of the default validation failed, leave now
        if(!super.isValid(dc))
        {
            // since we're leaving (execute won't be called), be sure to close any open connections
            try
            {
                adc.closeActionConnection(); // in case any database connection was opened
            }
            catch(Exception e)
            {
                getLog().error(e);
                throw new NestableRuntimeException(e);
            }
            return false;
        }

        ActionWrapper.ActionValidator actionValidator = action.getActionValidator();
        if(actionValidator == null)
            return true;

        boolean valid = true;
        List messages = new ArrayList();
        try
        {
            valid = actionValidator.isValid(adc.getActionInstance(), messages);
            if(!valid)
            {
                DialogValidationContext vc = dc.getValidationContext();
                DialogFields dialogFields = getFields();

                for(int i = 0; i < messages.size(); i++)
                {
                    Object message = messages.get(i);
                    if(message instanceof String)
                        vc.addError((String) message);
                    else if(message instanceof String[])
                    {
                        String[] components = (String[]) message;
                        String fieldName = components[0];
                        String messageText = components[1];

                        DialogField field = dialogFields.getByQualifiedName(fieldName);
                        if(field != null)
                            field.invalidate(dc, messageText);
                        else
                            vc.addError(messageText);
                    }
                }
            }
        }
        catch(Exception e)
        {
            getLog().error(e);
            throw new NestableRuntimeException(e);
        }
        finally
        {
            if(!valid)
            {
                // since we're false (execute won't be called), be sure to close any open connections
                try
                {
                    adc.closeActionConnection(); // in case any database connection was opened
                }
                catch(Exception e)
                {
                    getLog().error(e);
                    throw new NestableRuntimeException(e);
                }
            }
        }

        return valid;
    }

    public void execute(Writer writer, DialogContext dc) throws DialogExecuteException, IOException
    {
        if(dc.executeStageHandled())
            return;

        ActionDialogContext adc = ((ActionDialogContext) dc);
        Object instance = adc.getActionInstance();

        ActionWrapper.ActionExecutor actionExecutor = action.getActionExecutor();
        try
        {
            String redirect = actionExecutor.execute(writer, instance);
            if(redirect != null)
            {
                ValueSource vs = ValueSources.getInstance().getValueSourceOrStatic(redirect);
                handlePostExecute(writer, dc, vs.getTextValue(adc));
            }
            else
                handlePostExecute(writer, dc);
        }
        catch(Exception e)
        {
            handlePostExecuteException(writer, dc, "Action execution error", e);
        }
        finally
        {
            try
            {
                adc.closeActionConnection(); // in case any database connection was opened
            }
            catch(Exception e1)
            {
                getLog().error(e1);
                throw new DialogExecuteException(e1);
            }
        }
    }
}
