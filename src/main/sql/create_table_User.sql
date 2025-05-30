create table User(
	id int primary key auto_increment,
    first_name nvarchar(255) not null,
    last_name nvarchar(255) not null,
    age int not null,
    email nvarchar(255) not null,
    user_password nvarchar(255) not null
);