create table issue_queries (
  id int not null primary key auto_increment,
  name varchar(255) not null,
  owner_name varchar(255) not null,
  repository_name varchar(255) not null,
  parameters LONGTEXT,
  created_at datetime not null,
  updated_at datetime not null
);
