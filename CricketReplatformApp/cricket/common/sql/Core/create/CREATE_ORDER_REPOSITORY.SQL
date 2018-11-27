CREATE TABLE ckt_order(
        order_id                VARCHAR(40)    NOT NULL REFERENCES dcspp_order(order_id),
        work_order_type         VARCHAR(40) NULL,
        package_type            VARCHAR(40) NULL,
        refund_amount           NUMERIC(6, 6) NULL,
        market_code             VARCHAR(40) NULL,
        account_balance         NUMERIC(6, 6) NULL,
        e91desc                 VARCHAR(220) NULL,
        e911_amount             NUMERIC(6, 6) NULL,
        bo_amount               NUMERIC(6, 6) NULL,
        tax_summary             NUMERIC(6, 6) NULL,
        billSys_order_id 	 	VARCHAR(40) NULL,
        vestaSys_Order_Id    	VARCHAR(49) NULL,
        billSys_payment_refId 	VARCHAR(40) NULL,
        billSys_appr_transId 	VARCHAR(40) NULL,
        tele_sale_code 			VARCHAR(20) NULL,
        estimated_delivery_date VARCHAR(40) NULL,
        pos_sale_id  			VARCHAR(40) NULL,
        rem_phone_id 	 		VARCHAR(40) NULL,
     	rem_phone_sku_id 	 	VARCHAR(40) NULL,
     	rem_plan_id 	 		VARCHAR(40) NULL,
     	rem_plan_sku_id 	 	VARCHAR(40) NULL,
     	upg_model_no			VARCHAR(40) NULL,
     	upg_mdn					VARCHAR(40) NULL,
        PRIMARY KEY(order_id)
);
   
CREATE TABLE ckt_removed_addons (
        order_id                VARCHAR(20)    NOT NULL REFERENCES dcspp_order(order_id),
        rem_addon_id            VARCHAR(40) NOT NULL,
        rem_addon_sku_id        VARCHAR(40)    NULL,
        PRIMARY KEY(order_id, rem_addon_id)
);
CREATE INDEX ckt_order_order_idx ON ckt_order(order_id);

CREATE TABLE ckt_telecom_Tax_Imposition (
        order_id                VARCHAR(20)    NOT NULL REFERENCES dcspp_order(order_id),
        name                    VARCHAR(254)    NULL,
        value                   NUMERIC(6, 6) NULL,
        PRIMARY KEY(order_id)
);

CREATE INDEX ckt_telecom_Tax_order_idx ON ckt_telecom_Tax_Imposition(order_id);

CREATE TABLE ckt_bo_tax_Imposition (
        order_id                VARCHAR(20)    NOT NULL REFERENCES dcspp_order(order_id),
        name                    VARCHAR(254)    NULL,
        value                   NUMERIC(6, 6) NULL,
        PRIMARY KEY(order_id)
);

CREATE INDEX ckt_bo_tax_order_idx ON ckt_bo_tax_Imposition(order_id); 
CREATE TABLE ckt_package (
        package_id              VARCHAR(20)    NOT NULL,
        package_number          INTEGER NULL,
        activation_fee          NUMERIC(6, 6) NULL,
        package_type            VARCHAR(20)    NULL,
        new_mdn                 VARCHAR(40)    NULL,
        mdn                     VARCHAR(40)    NULL,
        PRIMARY KEY(package_id)
);

CREATE TABLE ckt_packages (
        order_id                VARCHAR(20)    NOT NULL REFERENCES dcspp_order(order_id),
        seq_num                 INTEGER NOT NULL,
        package_id              VARCHAR(254)    NULL,
        PRIMARY KEY(order_id, seq_num)
);
CREATE INDEX ckt_packages_order_idx ON ckt_packages(order_id, seq_num);
CREATE TABLE ckt_credit_card (
        payment_group_id        VARCHAR(40)    NOT NULL REFERENCES dcspp_pay_group(payment_group_id),
        vesta_token             VARCHAR(40)    NULL,
        transaction_Id          VARCHAR(40)    NULL,
        cc_first_name          VARCHAR(40)    NULL,
        cc_last_name          VARCHAR(40)    NULL,
        auto_bill_Payment       NUMERIC(1)      NULL,
        is_Different_Card       NUMERIC(1)      NULL,
        CHECK (auto_bill_Payment IN (0, 1)),
        PRIMARY KEY(payment_group_id)
);

CREATE INDEX ckt_payment_group_idx ON ckt_credit_card(payment_group_id);

CREATE TABLE CKT_COMMERCE_ITEM (
COMMERCE_ITEM_ID VARCHAR2 (40) NOT NULL REFERENCES DCSPP_ITEM (COMMERCE_ITEM_ID),
PACKAGE_ID VARCHAR2 (40) NULL,
IS_LOS NUMBER (1,0) NULL,
CITEM_TYPE varchar2(256) NULL,
MARKET_CODE VARCHAR2 (40) NULL,
EID VARCHAR2 (40),
Constraint CKT_COMMERCE_ITEM_P PRIMARY KEY (COMMERCE_ITEM_ID)
);

--    Added on  DATE : 30-09-2013
CREATE TABLE CKT_ITEM_PRICE(
		AMOUNT_INFO_ID  VARCHAR2(40 BYTE) NOT NULL ENABLE,
		ADMINISTRATION_FEE NUMBER(*,0),
		PRIMARY KEY(AMOUNT_INFO_ID)
);

-- Added columns billingQuoteId and VestaTransactionId on DATE:- 14-10-2013
ALTER TABLE CKT_ORDER ADD billing_quote_id VARCHAR (20);
ALTER TABLE CKT_ORDER ADD vesta_trans_id VARCHAR (20);

-- ADDED COLUMNS cric_customer_id AND cric_customer_billing_no ON DATE-19-10-2013
ALTER TABLE CKT_ORDER ADD cric_customer_id VARCHAR(40) NULL;
ALTER TABLE CKT_ORDER ADD cric_customer_billing_no VARCHAR(40) NULL;

--    Added on column for activation fee DATE : 21-10-2013
ALTER TABLE CKT_ITEM_PRICE ADD ACTIVATION_FEE NUMBER(*,0);

--  Added on column for VestaTransactionId on DATE:- 31-10-2013
ALTER TABLE CKT_ORDER DROP COLUMN vesta_trans_id;
ALTER TABLE CKT_ORDER ADD vesta_trans_id VARCHAR (60);

--    Added on column for prorated price DATE : 05-11-2013
ALTER TABLE CKT_ITEM_PRICE ADD PRORATED_PRICE NUMBER(*,0);
ALTER TABLE CKT_COMMERCE_ITEM ADD IS_CHANGE_FLOW NUMBER (1,0) NULL;

--  rename the  column for tax_summary to  total_tax [review comment ] on DATE:- 11/04/2013
ALTER TABLE CKT_ORDER DROP COLUMN tax_summary;
ALTER TABLE CKT_ORDER ADD total_tax NUMERIC(*, 6) NULL;

-- rename column datatype
ALTER TABLE CKT_ORDER DROP COLUMN estimated_delivery_date;
ALTER TABLE CKT_ORDER ADD estimated_delivery_date TIMESTAMP NULL;

--    Added on column for storing upgrade MDN details : 06-11-2013
ALTER TABLE CKT_ORDER ADD upg_model_no VARCHAR(40) NULL;
ALTER TABLE CKT_ORDER ADD upg_mdn VARCHAR(40) NULL;

ALTER TABLE CKT_ORDER ADD shipping_method VARCHAR(60) NULL;

ALTER TABLE ckt_package ADD package_total VARCHAR(60) NULL;

--    Added on column for downgrade flow DATE : 24-12-2013
ALTER TABLE CKT_COMMERCE_ITEM ADD IS_DOWN_GRADE NUMBER (1,0) NULL;

--    Added on column for downgrade flow DATE : 24-12-2013
ALTER TABLE CKT_ORDER ADD IS_DOWN_GRADE NUMBER (1,0) NULL;
--    Added on column for address types flow DATE : 30-12-2013
ALTER TABLE CKT_ORDER  ADD ("SHIPPING_ADDR_TYPE" NUMBER(1,0), "BILLING_ADDR_TYPE" NUMBER(1,0));

--    Added on column for removed addon commerce item compatibility flow DATE : 24-12-2013
ALTER TABLE CKT_COMMERCE_ITEM ADD IS_COMPATIBLE_ADDON NUMBER (1,0) NULL;

-- added on column for phone number and email id from shipping and billing page DATG : 04-FEB-2013
ALTER TABLE CKT_ORDER ADD phone_number VARCHAR(15) NULL;
ALTER TABLE CKT_ORDER ADD email_id VARCHAR(60) NULL;
ALTER TABLE CKT_ORDER ADD language_identifier VARCHAR(1) NULL;
ALTER TABLE CKT_ORDER ADD  user_ip_address VARCHAR(30) NULL;

ALTER TABLE CKT_ORDER ADD IS_ANANYMOUS_USER NUMBER (1,0) NULL;
ALTER TABLE CKT_ORDER ADD ACCOUNT_ADDRESSID VARCHAR(40) NULL;
