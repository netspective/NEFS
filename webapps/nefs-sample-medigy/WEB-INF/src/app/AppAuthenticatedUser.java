package app;

import com.netspective.axiom.ConnectionContext;
import com.netspective.commons.security.AuthenticatedUserInitializationException;
import com.netspective.commons.security.BasicAuthenticatedUser;
import com.netspective.commons.value.ValueContext;
import com.netspective.sparx.security.LoginDialogContext;

import java.sql.SQLException;


/**
 * @author thua
 * @version Apr 20, 2004 12:41:16 PM
 */
public class AppAuthenticatedUser extends BasicAuthenticatedUser
{
    

    public void init(ValueContext vc) throws AuthenticatedUserInitializationException
    {
        super.init(vc);
        LoginDialogContext ldc = (LoginDialogContext) vc;

        ConnectionContext cc = null;
        try
        {
            cc = ldc.getConnection(null, false);
            /*
            Query query = ldc.getProject().getQuery(auto.id.sql.query.Person.AUTHENTICATED_USER_INFO);
            QueryResultSet qrs = query.execute(cc, new Object[] {getUserId()}, false);
            ResultSet rs = qrs.getResultSet();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next())
            {
                for (int i=1; i <= rsmd.getColumnCount(); i++)
                {
                    setAttribute(rsmd.getColumnName(i).toLowerCase(), rs.getString(i));
                    System.out.println(rsmd.getColumnName(i).toLowerCase() + " " + rs.getString(i));
                }
            }
            rs.close();
            */

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(cc != null)
                    cc.close();
            }
            catch (SQLException e)
            {
                ldc.getDialog().getLog().error("Unable to close connection", e);
                throw new AuthenticatedUserInitializationException(e, this);
            }
        }


    }

}
