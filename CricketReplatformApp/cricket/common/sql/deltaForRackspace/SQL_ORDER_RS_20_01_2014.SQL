alter table ckt_removed_addons DROP PRIMARY KEY;
alter table ckt_removed_addons Add PRIMARY KEY(order_id, rem_addon_id);

