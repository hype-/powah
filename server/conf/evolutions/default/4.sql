# --- !Ups

alter table "rep_set" add column "time" timestamp without time zone not null;

# --- !Downs

alter table "rep_set" drop column "time";
