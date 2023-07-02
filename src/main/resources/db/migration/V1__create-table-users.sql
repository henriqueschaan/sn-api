create table users(

    id bigint not null auto_increment,
    deleted tinyint,
    created_at datetime,
    updated_at datetime,
    name varchar(100) not null,
    username varchar(100) not null unique,
    phone varchar(100) not null,
    email varchar(100) not null unique,
    password varchar(200) not null,
    pic_link varchar(200),

    primary key(id)

);