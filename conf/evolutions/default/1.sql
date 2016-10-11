# --- First database schema
# --- !Ups
create table todo (
  id                        bigint not null auto_increment,
  name                      varchar(255) not null,
  constraint pk_todo primary key (id)
);
create sequence todo_seq start with 1000;
insert into todo (id,name) values (1,'書類の整理');
insert into todo (id,name) values (2,'本の返却');
# --- !Downs
drop table if exists todo;
drop sequence if exists todo_seq;