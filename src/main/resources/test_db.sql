drop table if exists orders_detail;

drop table if exists orders;

drop table if exists cart_item;

drop table if exists product;

drop table if exists customer;

create table customer
(
    id       bigint      not null auto_increment,
    username varchar(20) not null unique,
    password varchar(64) not null,
    nickname varchar(10) not null,
    age      int         not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table product
(
    id        bigint       not null auto_increment,
    name      varchar(255) not null,
    price     integer      not null,
    thumbnail varchar(255),
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table cart_item
(
    id          bigint not null auto_increment,
    customer_id bigint not null,
    product_id  bigint not null,
    quantity    bigint not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

create table orders
(
    id          bigint not null auto_increment,
    customer_id bigint not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

alter table orders
    add constraint fk_orders_to_customer
        foreign key (customer_id) references customer (id);

create table orders_detail
(
    id         bigint  not null auto_increment,
    orders_id  bigint  not null,
    product_id bigint  not null,
    quantity   integer not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

alter table orders_detail
    add constraint fk_orders_detail_to_orders
        foreign key (orders_id) references orders (id);

alter table orders_detail
    add constraint fk_orders_detail_to_product
        foreign key (product_id) references product (id);