package auto.dal.db;

import com.netspective.axiom.schema.Schema;
import auto.dal.db.dao.BookInfoTable;

public final class DataAccessLayer
{
    private static final DataAccessLayer INSTANCE = new DataAccessLayer();
    
    public final BookInfoTable getBookInfoTable()
    {
        return bookInfoTable;
    }
    
    public final Schema getSchema()
    {
        return this.schema;
    }
    
    public final void setSchema(Schema schema)
    {
        this.schema = schema;
        bookInfoTable = new BookInfoTable(schema.getTables().getByName("Book_Info"));
    }
    
    public static final DataAccessLayer getInstance()
    {
        return INSTANCE;
    }
    private BookInfoTable bookInfoTable;
    private Schema schema;
}