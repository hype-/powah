# --- !Ups

create table "entry" ("id" SERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL);

# --- !Downs

drop table "entry";

