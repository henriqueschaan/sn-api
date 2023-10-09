create table user_friends(

    user_id bigint not null,
    friend_id bigint not null,

    primary key(user_id, friend_id),
    foreign key(user_id) references users(id),
    foreign key(friend_id) references users(id)

);
