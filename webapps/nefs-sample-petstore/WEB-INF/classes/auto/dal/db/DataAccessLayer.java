package auto.dal.db;

import com.netspective.axiom.schema.Schema;
import auto.dal.db.dao.AccountTable;
import auto.dal.db.dao.BannerDataTable;
import auto.dal.db.dao.CategoryTable;
import auto.dal.db.dao.SupplierTable;

public final class DataAccessLayer
{
    private static final DataAccessLayer INSTANCE = new DataAccessLayer();
    
    public final AccountTable getAccountTable()
    {
        return accountTable;
    }
    
    public final BannerDataTable getBannerDataTable()
    {
        return bannerDataTable;
    }
    
    public final CategoryTable getCategoryTable()
    {
        return categoryTable;
    }
    
    public final Schema getSchema()
    {
        return this.schema;
    }
    
    public final SupplierTable getSupplierTable()
    {
        return supplierTable;
    }
    
    public final void setSchema(Schema schema)
    {
        this.schema = schema;
        accountTable = new AccountTable(schema.getTables().getByName("Account"));
        bannerDataTable = new BannerDataTable(schema.getTables().getByName("Banner_Data"));
        categoryTable = new CategoryTable(schema.getTables().getByName("Category"));
        supplierTable = new SupplierTable(schema.getTables().getByName("Supplier"));
    }
    
    public static final DataAccessLayer getInstance()
    {
        return INSTANCE;
    }
    private AccountTable accountTable;
    private BannerDataTable bannerDataTable;
    private CategoryTable categoryTable;
    private Schema schema;
    private SupplierTable supplierTable;
}