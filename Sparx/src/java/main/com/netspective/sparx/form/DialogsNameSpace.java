/*
 * Created by IntelliJ IDEA.
 * User: aye
 * Date: Mar 2, 2004
 * Time: 3:50:34 PM
 */
package com.netspective.sparx.form;

import java.lang.reflect.InvocationTargetException;

public interface DialogsNameSpace
{
    public Dialogs getContainer();
    public void setContainer(Dialogs container);

    String getNameSpaceId();
    void setNameSpaceId(String identifier);

    public Dialog createDialog();
    public Dialog createDialog(Class cls) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
    public void addDialog(Dialog dialog);
}