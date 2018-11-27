CREATE TABLE ckt_user_cookie_info (
        id                      VARCHAR(254)    NOT NULL REFERENCES dps_user(id),
        MDN                     VARCHAR(254)    NULL,
        Phone_Code              VARCHAR(254)    NULL,
        Plane_Name              VARCHAR(254)    NULL,
        Rate_Plan_Code          VARCHAR(254)    NULL,
        Product_Type            VARCHAR(254)    NULL,
        Customer_Type           VARCHAR(254)    NULL,
		abp_flag           NUMBER    NULL,
        PRIMARY KEY(id)
);



CREATE TABLE ckt_user_cookie_info_los (
        id                      VARCHAR(254)    NOT NULL REFERENCES dps_user(id),
        idx                     INTEGER NOT NULL,
        Number_of_Lines         VARCHAR(254)    NULL,
        PRIMARY KEY(id, idx)
);



CREATE TABLE ckt_contact_info (
        id                      VARCHAR(254)    NOT NULL REFERENCES dps_contact_info(id),
        atg_order_Id            VARCHAR(254)    NULL,
        PRIMARY KEY(id)
);

commit;