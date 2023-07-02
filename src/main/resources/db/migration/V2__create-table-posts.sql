create table posts(

    id bigint not null auto_increment,
    deleted tinyint not null,
    created_at datetime,
    updated_at datetime,
    title varchar(50) not null,
    description varchar(200),
    pic_link varchar(255),
    vid_link varchar(255),
    is_private tinyint not null,
    user_id bigint,

    primary key(id),
    foreign key(user_id) references users(id)

);