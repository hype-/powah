# --- !Ups

create table "exercise"
  ("id" SERIAL NOT NULL PRIMARY KEY,
  "name" VARCHAR(254) NOT NULL);

create unique index "uniq_name" on "exercise" ("name");

alter table "entry" rename to "rep_set";
alter table "rep_set"
  drop column "name",
  add column "reps" INT NOT NULL,
  add column "weight" INT NOT NULL,
  add column "exercise_id" INT NOT NULL,
  add constraint "exercise_fk" foreign key("exercise_id") references "exercise"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "rep_set" drop constraint "exercise_fk";
alter table "exercise" rename to "entry";
alter table "entry"
  add column "name" VARCHAR(254) NOT NULL,
  drop column "reps",
  drop column "weight",
  drop column "exercise_id";
