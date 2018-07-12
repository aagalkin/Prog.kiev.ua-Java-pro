create table categories (id bigint not null auto_increment, link_name varchar(255), name varchar(255), primary key (id)) engine=MyISAM
create table client (id bigint not null auto_increment, city varchar(255), email varchar(255), name varchar(255), password varchar(255), phone integer, role varchar(255), surname varchar(255), container_id bigint, primary key (id)) engine=MyISAM
create table container (id bigint not null auto_increment, count integer, client_id bigint, primary key (id)) engine=MyISAM
create table container_values (container_id bigint not null, count integer, products_key bigint not null, primary key (container_id, products_key)) engine=MyISAM
create table my_order (id bigint not null auto_increment, date date, status varchar(255), total_price double precision, client_id bigint, primary key (id)) engine=MyISAM
create table order_product (order_id bigint not null, product_id bigint not null) engine=MyISAM
create table product (id bigint not null auto_increment, descr varchar(255), img longblob, img_type varchar(255), name varchar(255), price double precision, category_id bigint, primary key (id)) engine=MyISAM
create table product_count (id bigint not null auto_increment, count integer, product_id bigint, primary key (id)) engine=MyISAM
create table warehouse (id bigint not null auto_increment, primary key (id)) engine=MyISAM
create table wh (wh_id bigint not null, count integer, products_key bigint not null, primary key (wh_id, products_key)) engine=MyISAM