 create table users (
  id int not null primary key auto_increment,
  name varchar(255) not null,
  role varchar(255) not null,
  created_at datetime not null,
  updated_at datetime not null
);
