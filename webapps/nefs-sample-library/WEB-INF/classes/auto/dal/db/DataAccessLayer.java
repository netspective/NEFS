package auto.dal.db;

import com.netspective.axiom.schema.Schema;
import auto.dal.db.dao.AssetTable;
import auto.dal.db.dao.BorrowerTable;

public final class DataAccessLayer
{
    private static final DataAccessLayer INSTANCE = new DataAccessLayer();
    
    public final AssetTable getAssetTable()
    {
        return assetTable;
    }
    
    public final BorrowerTable getBorrowerTable()
    {
        return borrowerTable;
    }
    
    public final Schema getSchema()
    {
        return this.schema;
    }
    
    public final void setSchema(Schema schema)
    {
        this.schema = schema;
        assetTable = new AssetTable(schema.getTables().getByName("Asset"));
        borrowerTable = new BorrowerTable(schema.getTables().getByName("Borrower"));
    }
    
    public static final DataAccessLayer getInstance()
    {
        return INSTANCE;
    }
    private AssetTable assetTable;
    private BorrowerTable borrowerTable;
    private Schema schema;
}