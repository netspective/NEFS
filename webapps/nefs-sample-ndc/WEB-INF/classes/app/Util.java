
package app;

import com.netspective.sparx.form.handler.DialogNextActionProvider;
import com.netspective.sparx.form.DialogContext;

public class Util implements DialogNextActionProvider
{
    /**
     * We always want to send our dialogs (forms) back to the home page.
     **/
    public String getDialogNextActionUrl(DialogContext dc, String defaultUrl)
    {
        return dc.getServletRootUrl();
    }
}
