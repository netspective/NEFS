CREATE TABLE Lookup_Result_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Lookup_Result_Type on Lookup_Result_Type (id);

CREATE TABLE Record_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE unique INDEX UNQ_RecStatus_abbrev on Record_Status (abbrev);
CREATE  INDEX PK_Record_Status on Record_Status (id);

CREATE TABLE Login
(
    login_id VARCHAR(32) PRIMARY KEY, /* type.TextColumn */
    password VARCHAR(20) /* type.TextColumn */
);
CREATE  INDEX PK_Login on Login (login_id);

CREATE SEQUENCE ACCOUNTS_ACCOUNT_ID_SEQ increment 1 start 1;
CREATE TABLE Account
(
    account_id INTEGER PRIMARY KEY, /* type.AutoIncColumn */
    login_id VARCHAR(32) NOT NULL, /* type.TextColumn */
    email VARCHAR(30) NOT NULL, /* type.TextColumn */
    first_name VARCHAR(20) NOT NULL, /* type.TextColumn */
    last_name VARCHAR(25) NOT NULL, /* type.TextColumn */
    status VARCHAR(32), /* type.TextColumn */
    addr1 VARCHAR(64) NOT NULL, /* type.TextColumn */
    addr2 VARCHAR(64) NOT NULL, /* type.TextColumn */
    city VARCHAR(64) NOT NULL, /* type.TextColumn */
    state VARCHAR(64) NOT NULL, /* type.TextColumn */
    country VARCHAR(64) NOT NULL, /* type.TextColumn */
    zip_code VARCHAR(5) NOT NULL, /* type.TextColumn */
    phone VARCHAR(30) NOT NULL, /* type.TextColumn */
    langpref INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    favcategory INTEGER, /* type.EnumerationIdRefColumn */
    mylstopt VARCHAR(32), /* type.TextColumn */
    banneropt VARCHAR(32), /* type.TextColumn */

    CONSTRAINT FK_ACCOUNTS_LOGIN_ID FOREIGN KEY (login_id) REFERENCES Login (login_id) ON DELETE CASCADE

    /* DELAYED: CONSTRAINT FK_ACCOUNTS_FAVCATEGORY FOREIGN KEY (favcategory) REFERENCES Favorite_Type (id) ON DELETE CASCADE (Favorite_Type table not created yet) */
    /* DELAYED: CONSTRAINT FK_ACCOUNTS_LANGPREF FOREIGN KEY (langpref) REFERENCES Language (id) ON DELETE CASCADE (Language table not created yet) */
);
CREATE  INDEX PK_Account on Account (account_id);

CREATE TABLE Banner_Data
(
    banner_data_id VARCHAR(32) PRIMARY KEY, /* type.TextColumn */
    banner_name VARCHAR(64) NOT NULL /* type.TextColumn */
);
CREATE  INDEX PK_Banner_Data on Banner_Data (banner_data_id);

CREATE TABLE Category
(
    category_id VARCHAR(32) PRIMARY KEY, /* type.TextColumn */
    name VARCHAR(30) NOT NULL, /* type.TextColumn */
    descr VARCHAR(30) NOT NULL /* type.TextColumn */
);
CREATE  INDEX PK_Category on Category (category_id);

CREATE TABLE Product
(
    product_id VARCHAR(32) PRIMARY KEY, /* type.TextColumn */
    category_id VARCHAR(32), /* type.TextColumn */
    name VARCHAR(30) NOT NULL, /* type.TextColumn */
    descr VARCHAR(30) NOT NULL, /* type.TextColumn */

    CONSTRAINT FK_PRODUCTS_CATEGORY_ID FOREIGN KEY (category_id) REFERENCES Category (category_id) ON DELETE CASCADE
);
CREATE  INDEX PK_Product on Product (product_id);
CREATE  INDEX PR_products_category_id on Product (category_id);

CREATE TABLE Item
(
    item_id VARCHAR(32) PRIMARY KEY, /* type.TextColumn */
    product_id VARCHAR(32), /* type.TextColumn */
    list_price NUMERIC(12,2) NOT NULL, /* type.CurrencyColumn */
    unit_cost NUMERIC(12,2), /* type.CurrencyColumn */
    supplier_id INTEGER, /* type.LongIntegerColumn */
    status VARCHAR(20) NOT NULL, /* type.TextColumn */
    name VARCHAR(30) NOT NULL, /* type.TextColumn */
    descr VARCHAR(30) NOT NULL, /* type.TextColumn */
    image VARCHAR(20) NOT NULL, /* type.TextColumn */
    attr1 VARCHAR(30), /* type.TextColumn */
    attr2 VARCHAR(30), /* type.TextColumn */
    attr3 VARCHAR(30), /* type.TextColumn */

    CONSTRAINT FK_ITEMS_PRODUCT_ID FOREIGN KEY (product_id) REFERENCES Product (product_id) ON DELETE CASCADE

    /* DELAYED: CONSTRAINT FK_ITEMS_SUPPLIER_ID FOREIGN KEY (supplier_id) REFERENCES Supplier (supplier_id) ON DELETE CASCADE (Supplier table not created yet) */
);
CREATE  INDEX PK_Item on Item (item_id);
CREATE  INDEX PR_items_product_id on Item (product_id);

CREATE SEQUENCE SUPPLS_SUPPLIER_ID_SEQ increment 1 start 1;
CREATE TABLE Supplier
(
    supplier_id INTEGER PRIMARY KEY, /* type.AutoIncColumn */
    name VARCHAR(64) NOT NULL, /* type.TextColumn */
    status VARCHAR(20) NOT NULL, /* type.TextColumn */
    addr1 VARCHAR(64) NOT NULL, /* type.TextColumn */
    addr2 VARCHAR(64) NOT NULL, /* type.TextColumn */
    city VARCHAR(64) NOT NULL, /* type.TextColumn */
    state VARCHAR(64) NOT NULL, /* type.TextColumn */
    country VARCHAR(64) NOT NULL, /* type.TextColumn */
    phone VARCHAR(30) NOT NULL /* type.TextColumn */
);
CREATE  INDEX PK_Supplier on Supplier (supplier_id);

CREATE SEQUENCE ORDS_ORDERS_ID_SEQ increment 1 start 1;
CREATE TABLE Orders
(
    orders_id INTEGER PRIMARY KEY, /* type.AutoIncColumn */
    account_id INTEGER, /* type.LongIntegerColumn */
    order_date DATE NOT NULL, /* type.DateColumn */
    ship_addr1 VARCHAR(64) NOT NULL, /* type.TextColumn */
    ship_addr2 VARCHAR(64) NOT NULL, /* type.TextColumn */
    ship_city VARCHAR(64) NOT NULL, /* type.TextColumn */
    ship_state VARCHAR(64) NOT NULL, /* type.TextColumn */
    ship_country VARCHAR(64) NOT NULL, /* type.TextColumn */
    ship_phone VARCHAR(30) NOT NULL, /* type.TextColumn */
    bill_addr1 VARCHAR(64) NOT NULL, /* type.TextColumn */
    bill_addr2 VARCHAR(64) NOT NULL, /* type.TextColumn */
    bill_city VARCHAR(64) NOT NULL, /* type.TextColumn */
    bill_state VARCHAR(64) NOT NULL, /* type.TextColumn */
    bill_country VARCHAR(64) NOT NULL, /* type.TextColumn */
    bill_phone VARCHAR(30) NOT NULL, /* type.TextColumn */

    CONSTRAINT FK_ORDS_ACCOUNT_ID FOREIGN KEY (account_id) REFERENCES Account (account_id) ON DELETE CASCADE
);
CREATE  INDEX PK_Orders on Orders (orders_id);
CREATE  INDEX PR_ords_account_id on Orders (account_id);

CREATE SEQUENCE ORDERSTAT_ORDERSTATUS_ID_SEQ increment 1 start 1;
CREATE TABLE OrderStatus
(
    orderstatus_id INTEGER PRIMARY KEY, /* type.AutoIncColumn */
    orders_id INTEGER, /* type.LongIntegerColumn */
    itemnum INTEGER NOT NULL, /* type.IntegerColumn */
    ts DATE NOT NULL, /* type.DateColumn */
    status VARCHAR(20) NOT NULL, /* type.TextColumn */

    CONSTRAINT FK_ORDERSTAT_ORDERS_ID FOREIGN KEY (orders_id) REFERENCES Orders (orders_id) ON DELETE CASCADE
);
CREATE  INDEX PK_OrderStatus on OrderStatus (orderstatus_id);
CREATE  INDEX PR_orderstat_orders_id on OrderStatus (orders_id);

CREATE SEQUENCE LINEITEMS_LINEITEM_ID_SEQ increment 1 start 1;
CREATE TABLE LineItem
(
    lineitem_id INTEGER PRIMARY KEY, /* type.AutoIncColumn */
    orders_id INTEGER, /* type.LongIntegerColumn */
    itemnum INTEGER, /* type.IntegerColumn */
    item_id VARCHAR(32), /* type.TextColumn */
    quantity INTEGER NOT NULL, /* type.IntegerColumn */
    unit_price NUMERIC(12,2) NOT NULL, /* type.CurrencyColumn */

    CONSTRAINT FK_LINEITEMS_ORDERS_ID FOREIGN KEY (orders_id) REFERENCES Orders (orders_id) ON DELETE CASCADE,
    CONSTRAINT FK_LINEITEMS_ITEM_ID FOREIGN KEY (item_id) REFERENCES Item (item_id) ON DELETE CASCADE
);
CREATE  INDEX PK_LineItem on LineItem (lineitem_id);
CREATE  INDEX PR_lineitems_orders_id on LineItem (orders_id);

CREATE TABLE Status_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Status_Type on Status_Type (id);

CREATE TABLE Country_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Country_Type on Country_Type (id);

CREATE TABLE Language
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Language on Language (id);

CREATE TABLE Favorite_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Favorite_Type on Favorite_Type (id);

CREATE TABLE Language_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Language_Type on Language_Type (id);

CREATE TABLE US_State_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_US_State_Type on US_State_Type (id);

ALTER TABLE Account ADD CONSTRAINT FK_ACCOUNTS_FAVCATEGORY FOREIGN KEY (favcategory) REFERENCES Favorite_Type (id) ON DELETE CASCADE;
ALTER TABLE Item ADD CONSTRAINT FK_ITEMS_SUPPLIER_ID FOREIGN KEY (supplier_id) REFERENCES Supplier (supplier_id) ON DELETE CASCADE;
ALTER TABLE Account ADD CONSTRAINT FK_ACCOUNTS_LANGPREF FOREIGN KEY (langpref) REFERENCES Language (id) ON DELETE CASCADE;

insert into Lookup_Result_Type (id, caption, abbrev) values (0, 'ID', NULL);
insert into Lookup_Result_Type (id, caption, abbrev) values (1, 'Caption', NULL);
insert into Lookup_Result_Type (id, caption, abbrev) values (2, 'Abbreviation', NULL);

insert into Record_Status (id, caption, abbrev) values (0, 'Inactive', 'I');
insert into Record_Status (id, caption, abbrev) values (1, 'Active', 'A');
insert into Record_Status (id, caption, abbrev) values (99, 'Unknown', 'U');

insert into Status_Type (id, caption, abbrev) values (0, 'Active', NULL);

insert into Country_Type (id, caption, abbrev) values (0, 'USA', NULL);
insert into Country_Type (id, caption, abbrev) values (1, 'Canada', NULL);

insert into Language (id, caption, abbrev) values (0, 'English', NULL);
insert into Language (id, caption, abbrev) values (1, 'French', NULL);
insert into Language (id, caption, abbrev) values (2, 'Japanese', NULL);
insert into Language (id, caption, abbrev) values (3, 'Urdu', NULL);

insert into Favorite_Type (id, caption, abbrev) values (0, 'Birds', NULL);
insert into Favorite_Type (id, caption, abbrev) values (1, 'Cats', NULL);
insert into Favorite_Type (id, caption, abbrev) values (2, 'Dogs', NULL);
insert into Favorite_Type (id, caption, abbrev) values (3, 'Reptiles', NULL);
insert into Favorite_Type (id, caption, abbrev) values (4, 'Fish', NULL);

insert into Language_Type (id, caption, abbrev) values (0, 'English', NULL);
insert into Language_Type (id, caption, abbrev) values (1, 'Spanish', NULL);
insert into Language_Type (id, caption, abbrev) values (2, 'French', NULL);
insert into Language_Type (id, caption, abbrev) values (3, 'German', NULL);
insert into Language_Type (id, caption, abbrev) values (4, 'Italian', NULL);
insert into Language_Type (id, caption, abbrev) values (5, 'Chinese', NULL);
insert into Language_Type (id, caption, abbrev) values (6, 'Japanese', NULL);
insert into Language_Type (id, caption, abbrev) values (7, 'Korean', NULL);
insert into Language_Type (id, caption, abbrev) values (8, 'Vietnamese', NULL);
insert into Language_Type (id, caption, abbrev) values (9, 'Other', NULL);

insert into US_State_Type (id, caption, abbrev) values (0, 'Alabama', 'AL');
insert into US_State_Type (id, caption, abbrev) values (1, 'Alaska', 'AK');
insert into US_State_Type (id, caption, abbrev) values (2, 'Arizona', 'AZ');
insert into US_State_Type (id, caption, abbrev) values (3, 'Arkansas', 'AR');
insert into US_State_Type (id, caption, abbrev) values (4, 'California', 'CA');
insert into US_State_Type (id, caption, abbrev) values (5, 'Colorado', 'CO');
insert into US_State_Type (id, caption, abbrev) values (6, 'Connecticut', 'CT');
insert into US_State_Type (id, caption, abbrev) values (7, 'Delaware', 'DE');
insert into US_State_Type (id, caption, abbrev) values (8, 'District of Columbia', 'DC');
insert into US_State_Type (id, caption, abbrev) values (9, 'Florida', 'FL');
insert into US_State_Type (id, caption, abbrev) values (10, 'Georgia', 'GA');
insert into US_State_Type (id, caption, abbrev) values (11, 'Hawaii', 'HI');
insert into US_State_Type (id, caption, abbrev) values (12, 'Idaho', 'ID');
insert into US_State_Type (id, caption, abbrev) values (13, 'Illinois', 'IL');
insert into US_State_Type (id, caption, abbrev) values (14, 'Indiana', 'IN');
insert into US_State_Type (id, caption, abbrev) values (15, 'Iowa', 'IA');
insert into US_State_Type (id, caption, abbrev) values (16, 'Kansas', 'KS');
insert into US_State_Type (id, caption, abbrev) values (17, 'Kentucky', 'KY');
insert into US_State_Type (id, caption, abbrev) values (18, 'Louisiana', 'LA');
insert into US_State_Type (id, caption, abbrev) values (19, 'Maine', 'ME');
insert into US_State_Type (id, caption, abbrev) values (20, 'Maryland', 'MD');
insert into US_State_Type (id, caption, abbrev) values (21, 'Massacusetts', 'MA');
insert into US_State_Type (id, caption, abbrev) values (22, 'Michigan', 'MI');
insert into US_State_Type (id, caption, abbrev) values (23, 'Minnesota', 'MN');
insert into US_State_Type (id, caption, abbrev) values (24, 'Mississippi', 'MS');
insert into US_State_Type (id, caption, abbrev) values (25, 'Missouri', 'MO');
insert into US_State_Type (id, caption, abbrev) values (26, 'Montana', 'MT');
insert into US_State_Type (id, caption, abbrev) values (27, 'Nebraska', 'NE');
insert into US_State_Type (id, caption, abbrev) values (28, 'Nevada', 'NV');
insert into US_State_Type (id, caption, abbrev) values (29, 'New Hampshire', 'NH');
insert into US_State_Type (id, caption, abbrev) values (30, 'New Jersey', 'NJ');
insert into US_State_Type (id, caption, abbrev) values (31, 'New Mexico', 'NM');
insert into US_State_Type (id, caption, abbrev) values (32, 'New York', 'NY');
insert into US_State_Type (id, caption, abbrev) values (33, 'North Carolina', 'NC');
insert into US_State_Type (id, caption, abbrev) values (34, 'North Dakota', 'ND');
insert into US_State_Type (id, caption, abbrev) values (35, 'Ohio', 'OH');
insert into US_State_Type (id, caption, abbrev) values (36, 'Oklahoma', 'OK');
insert into US_State_Type (id, caption, abbrev) values (37, 'Oregon', 'OR');
insert into US_State_Type (id, caption, abbrev) values (38, 'Pennsylvania', 'PA');
insert into US_State_Type (id, caption, abbrev) values (39, 'Rhode Island', 'RI');
insert into US_State_Type (id, caption, abbrev) values (40, 'South Carolina', 'SC');
insert into US_State_Type (id, caption, abbrev) values (41, 'South Dakota', 'SD');
insert into US_State_Type (id, caption, abbrev) values (42, 'Tennessee', 'TN');
insert into US_State_Type (id, caption, abbrev) values (43, 'Texas', 'TX');
insert into US_State_Type (id, caption, abbrev) values (44, 'Utah', 'UT');
insert into US_State_Type (id, caption, abbrev) values (45, 'Vermont', 'VT');
insert into US_State_Type (id, caption, abbrev) values (46, 'Virginia', 'VA');
insert into US_State_Type (id, caption, abbrev) values (47, 'Washington', 'WA');
insert into US_State_Type (id, caption, abbrev) values (48, 'West Virginia', 'WV');
insert into US_State_Type (id, caption, abbrev) values (49, 'Wisconsin', 'WI');
insert into US_State_Type (id, caption, abbrev) values (50, 'Wyoming', 'WY');
insert into US_State_Type (id, caption, abbrev) values (51, 'American Samoa', 'A_S');
insert into US_State_Type (id, caption, abbrev) values (52, 'Federated States of Micronesia', 'F_S_M');
insert into US_State_Type (id, caption, abbrev) values (53, 'Guam', 'GUAM');
insert into US_State_Type (id, caption, abbrev) values (54, 'Marshall Islands', 'M_I');
insert into US_State_Type (id, caption, abbrev) values (55, 'Northern Mariana Islands', 'N_M_I');
insert into US_State_Type (id, caption, abbrev) values (56, 'Palau', 'PALAU');
insert into US_State_Type (id, caption, abbrev) values (57, 'Puerto Rico', 'P_R');
insert into US_State_Type (id, caption, abbrev) values (58, 'Virgin Islands', 'V_I');
insert into US_State_Type (id, caption, abbrev) values (59, 'Other', 'XX');
