/*
 * Created by IntelliJ IDEA.
 * User: aye
 * Date: Mar 2, 2004
 * Time: 3:50:34 PM
 */
package com.netspective.sparx.form;

public interface DialogsNameSpace
{
    public Dialogs getContainer();
    public void setContainer(Dialogs container);

    String getNameSpaceId();
    void setNameSpaceId(String identifier);
}