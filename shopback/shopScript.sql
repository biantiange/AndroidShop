create table cart_specs
(
    specs_id int auto_increment
        primary key,
    detail   varchar(500) null,
    cart_id  int          not null
);

create table collection
(
    user_id      int null,
    commodity_id int null,
    delete_flag  int null,
    constraint collection_pk
        unique (user_id, commodity_id)
);

create table post
(
    post_id     int auto_increment,
    user_id     int           not null,
    img_one     varchar(200)  null,
    img_two     varchar(200)  null,
    img_three   varchar(200)  null,
    content     varchar(1000) null,
    create_time varchar(100)  null,
    delete_flag int default 0 null,
    constraint post_post_id_uindex
        unique (post_id)
);

alter table post
    add primary key (post_id);

create table praise
(
    user_id     int           not null,
    post_id     int           not null,
    delete_flag int default 0 null,
    constraint praise_pk
        unique (user_id, post_id)
);

create table specs
(
    specs_id     int auto_increment
        primary key,
    detail       varchar(500) null,
    commodity_id int          not null
)
    comment '商品规格';

create table t_business_commodity
(
    id                       int auto_increment comment '商品id'
        primary key,
    commodity_name           varchar(50)   default ''   null comment '商品名称',
    commodity_type           int                        null comment '商品类别id',
    commodity_info           text                       null comment '商品描述',
    commodity_img            longtext                   null comment '商品图片(Base64编码)',
    commodity_price          double(10, 2) default 0.00 null comment '商品价格',
    commodity_total          bigint        default 0    null comment '商品总量',
    commodity_surplus        bigint                     null comment '商品剩余',
    commodity_other_img_urls text                       null comment '商品其他图片Url',
    is_deleted               varchar(2)    default 'N'  null comment '数据是否被删除'
);

create table t_business_commodity_type
(
    id         int auto_increment comment '商品类别自增id'
        primary key,
    name       varchar(50)  default ''  null comment '商品类别解释',
    icon_path  varchar(800) default ''  null comment '图标地址',
    is_deleted varchar(2)   default 'N' null comment '是否删除数据'
);

create table t_business_user_cart
(
    id           int auto_increment comment '购物车-自增id'
        primary key,
    uid          int                       null comment '用户id',
    cid          int                       null comment '商品id',
    number       int                       null comment '购买数量',
    total_price  double(10, 2)             null comment '总花费',
    is_purchased int(1)                    null comment '是否已经购买(0：否；1：是)[用这个字段也可以区分商品究竟是加入购物车还是已购买]',
    is_deleted   varchar(2)  default 'N'   null comment '数据是否被删除',
    status       varchar(10) default '待发货' null
);

create table t_comment
(
    comment_id  int auto_increment,
    uid         int           null,
    post_id     int           not null,
    content     varchar(1000) null,
    create_time varchar(200)  null,
    delete_flag int           null,
    constraint t_comment_comment_id_uindex
        unique (comment_id)
);

alter table t_comment
    add primary key (comment_id);

create table t_sys_user
(
    id          int(10) auto_increment comment '用户id'
        primary key,
    account     varchar(20)                  default ''                null comment '用户账号',
    password    varchar(256)                 default ''                null comment '密码',
    uname       varchar(30) collate utf8_bin default '新注册用户'           null comment '用户昵称',
    create_time timestamp                    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    is_deleted  varchar(2)                   default 'N'               null comment '数据是否被删除',
    constraint unique_account
        unique (account)
);


