CREATE CACHED TABLE Lookup_Result_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Lookup_Result_Type on Lookup_Result_Type (id);

CREATE CACHED TABLE Record_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE unique INDEX UNQ_RecStatus_abbrev on Record_Status (abbrev);
CREATE  INDEX PK_Record_Status on Record_Status (id);

CREATE CACHED TABLE firms
(
    firm_seq_no INTEGER PRIMARY KEY, /* type.IntegerColumn */
    lblcode VARCHAR(6), /* type.TextColumn */
    firm_name VARCHAR(65), /* type.TextColumn */
    addr_header VARCHAR(65), /* type.TextColumn */
    street VARCHAR(40), /* type.TextColumn */
    po_box VARCHAR(9), /* type.TextColumn */
    foreign_addr VARCHAR(40), /* type.TextColumn */
    city VARCHAR(30), /* type.TextColumn */
    state VARCHAR(2), /* type.TextColumn */
    province VARCHAR(30), /* type.TextColumn */
    zip VARCHAR(9), /* type.TextColumn */
    country_name VARCHAR(40) /* type.TextColumn */
);
CREATE  INDEX PK_firms on firms (firm_seq_no);

CREATE CACHED TABLE listings
(
    listing_seq_no VARCHAR(10) PRIMARY KEY, /* type.TextColumn */
    lblcode VARCHAR(6), /* type.TextColumn */
    prodcode VARCHAR(4), /* type.TextColumn */
    strength VARCHAR(10), /* type.TextColumn */
    unit VARCHAR(10), /* type.TextColumn */
    rx_otc VARCHAR(1), /* type.TextColumn */
    dosage_form VARCHAR(25), /* type.TextColumn */
    firm_seq_no INTEGER, /* type.IntegerColumn */
    tradename VARCHAR(100), /* type.TextColumn */

    CONSTRAINT FK_LST_FIRM_SEQ_NO FOREIGN KEY (firm_seq_no) REFERENCES firms (firm_seq_no) ON DELETE CASCADE
);
CREATE  INDEX lst_tradename on listings (tradename);
CREATE  INDEX PK_listings on listings (listing_seq_no);

CREATE CACHED TABLE drugclas
(
    ID INTEGER PRIMARY KEY, /* type.IntegerColumn */
    listing_seq_no VARCHAR(10), /* type.TextColumn */
    product_class_no VARCHAR(4), /* type.TextColumn */
    drug_classification VARCHAR(52), /* type.TextColumn */

    CONSTRAINT FK_DGC_LISTING_SEQ_NO FOREIGN KEY (listing_seq_no) REFERENCES listings (listing_seq_no) ON DELETE CASCADE
);
CREATE  INDEX dgc_drug_classification on drugclas (drug_classification);
CREATE  INDEX PK_drugclas on drugclas (ID);

CREATE CACHED TABLE formulat
(
    ID INTEGER PRIMARY KEY, /* type.IntegerColumn */
    listing_seq_no VARCHAR(10), /* type.TextColumn */
    strength VARCHAR(10), /* type.TextColumn */
    unit VARCHAR(5), /* type.TextColumn */
    ingredient_name VARCHAR(105), /* type.TextColumn */

    CONSTRAINT FK_FRM_LISTING_SEQ_NO FOREIGN KEY (listing_seq_no) REFERENCES listings (listing_seq_no) ON DELETE CASCADE
);
CREATE  INDEX frm_ingredient_name on formulat (ingredient_name);
CREATE  INDEX PK_formulat on formulat (ID);

CREATE CACHED TABLE packages
(
    ID INTEGER PRIMARY KEY, /* type.IntegerColumn */
    listing_seq_no VARCHAR(10), /* type.TextColumn */
    pkgcode VARCHAR(2), /* type.TextColumn */
    packsize VARCHAR(25), /* type.TextColumn */
    packtype VARCHAR(28), /* type.TextColumn */

    CONSTRAINT FK_PKG_LISTING_SEQ_NO FOREIGN KEY (listing_seq_no) REFERENCES listings (listing_seq_no) ON DELETE CASCADE
);
CREATE  INDEX PK_packages on packages (ID);

CREATE CACHED TABLE routes
(
    ID INTEGER PRIMARY KEY, /* type.IntegerColumn */
    listing_seq_no VARCHAR(10), /* type.TextColumn */
    route_code VARCHAR(3), /* type.TextColumn */
    route_name VARCHAR(28), /* type.TextColumn */

    CONSTRAINT FK_RTS_LISTING_SEQ_NO FOREIGN KEY (listing_seq_no) REFERENCES listings (listing_seq_no) ON DELETE CASCADE
);
CREATE  INDEX PK_routes on routes (ID);

insert into Lookup_Result_Type (id, caption, abbrev) values (0, 'ID', NULL);
insert into Lookup_Result_Type (id, caption, abbrev) values (1, 'Caption', NULL);
insert into Lookup_Result_Type (id, caption, abbrev) values (2, 'Abbreviation', NULL);

insert into Record_Status (id, caption, abbrev) values (0, 'Inactive', 'I');
insert into Record_Status (id, caption, abbrev) values (1, 'Active', 'A');
insert into Record_Status (id, caption, abbrev) values (99, 'Unknown', 'U');
