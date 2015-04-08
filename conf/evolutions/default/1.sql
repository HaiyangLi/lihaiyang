# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table users (
  userid                    bigint auto_increment not null,
  username                  varchar(255),
  description               varchar(255),
  constraint pk_users primary key (userid))
;

create table weibo (
  id                        bigint auto_increment not null,
  created_at                varchar(255),
  mid                       bigint,
  idstr                     varchar(255),
  text                      varchar(255),
  source                    varchar(255),
  username                  varchar(255),
  retweeted_text            varchar(255),
  retweeted_content         varchar(255),
  constraint pk_weibo primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table users;

drop table weibo;

SET FOREIGN_KEY_CHECKS=1;

