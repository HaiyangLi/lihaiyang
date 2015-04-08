# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table weibo (
  id                        bigint not null,
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

create sequence weibo_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists weibo;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists weibo_seq;

