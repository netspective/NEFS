
/* this file is generated by com.netspective.sparx.form.Dialog.getSubclassedDialogContextCode(), do not modify (you can extend it, though) */

package auto.dcb.subject;

import com.netspective.sparx.form.*;
import com.netspective.sparx.form.field.*;
import com.netspective.sparx.form.field.type.*;

public class LoginInfoContext
{
    public static final String DIALOG_ID = "subject.login_info";
    private DialogContext dialogContext;
    private DialogContext.DialogFieldStates fieldStates;

    public LoginInfoContext(DialogContext dc)
    {
        this.dialogContext = dc;
        this.fieldStates = dc.getFieldStates();
    }

    public DialogContext getDialogContext() { return dialogContext; }

	public com.netspective.sparx.form.field.type.TextField.TextFieldState getLoginIdState() { return (com.netspective.sparx.form.field.type.TextField.TextFieldState) fieldStates.getState("login_id"); }
	public com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue getLoginId() { return (com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue) getLoginIdState().getValue(); }
	public DialogFieldFlags getLoginIdStateFlags() { return getLoginIdState().getStateFlags(); }
	public String getLoginIdPrivateRequestParam() { return dialogContext.getRequest().getParameter("_dc.login_id"); }
	public String getLoginIdPublicRequestParam() { return dialogContext.getRequest().getParameter("login_id"); }
	public com.netspective.sparx.form.field.type.TextField getLoginIdField() { return (com.netspective.sparx.form.field.type.TextField) getLoginIdState().getField(); }

	public com.netspective.sparx.form.field.type.TextField.TextFieldState getOldPasswordState() { return (com.netspective.sparx.form.field.type.TextField.TextFieldState) fieldStates.getState("old_password"); }
	public com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue getOldPassword() { return (com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue) getOldPasswordState().getValue(); }
	public DialogFieldFlags getOldPasswordStateFlags() { return getOldPasswordState().getStateFlags(); }
	public String getOldPasswordPrivateRequestParam() { return dialogContext.getRequest().getParameter("_dc.old_password"); }
	public String getOldPasswordPublicRequestParam() { return dialogContext.getRequest().getParameter("old_password"); }
	public com.netspective.sparx.form.field.type.TextField getOldPasswordField() { return (com.netspective.sparx.form.field.type.TextField) getOldPasswordState().getField(); }

	public com.netspective.sparx.form.field.type.TextField.TextFieldState getNewPassword1State() { return (com.netspective.sparx.form.field.type.TextField.TextFieldState) fieldStates.getState("new_password1"); }
	public com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue getNewPassword1() { return (com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue) getNewPassword1State().getValue(); }
	public DialogFieldFlags getNewPassword1StateFlags() { return getNewPassword1State().getStateFlags(); }
	public String getNewPassword1PrivateRequestParam() { return dialogContext.getRequest().getParameter("_dc.new_password1"); }
	public String getNewPassword1PublicRequestParam() { return dialogContext.getRequest().getParameter("new_password1"); }
	public com.netspective.sparx.form.field.type.TextField getNewPassword1Field() { return (com.netspective.sparx.form.field.type.TextField) getNewPassword1State().getField(); }

	public com.netspective.sparx.form.field.type.TextField.TextFieldState getNewPassword2State() { return (com.netspective.sparx.form.field.type.TextField.TextFieldState) fieldStates.getState("new_password2"); }
	public com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue getNewPassword2() { return (com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue) getNewPassword2State().getValue(); }
	public DialogFieldFlags getNewPassword2StateFlags() { return getNewPassword2State().getStateFlags(); }
	public String getNewPassword2PrivateRequestParam() { return dialogContext.getRequest().getParameter("_dc.new_password2"); }
	public String getNewPassword2PublicRequestParam() { return dialogContext.getRequest().getParameter("new_password2"); }
	public com.netspective.sparx.form.field.type.TextField getNewPassword2Field() { return (com.netspective.sparx.form.field.type.TextField) getNewPassword2State().getField(); }

}