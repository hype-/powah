# --- !Ups

create table "user"
  ("id" SERIAL NOT NULL PRIMARY KEY,
  "username" VARCHAR(254) NOT NULL,
  "password" VARCHAR(254) NOT NULL);

create unique index "uniq_username" on "user" ("username");

alter table "entry"
  add column "user_id" INT NOT NULL,
  add constraint "user_fk" foreign key("user_id") references "user"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "entry" drop constraint "user_fk";
drop table "user";
