# --- !Ups

CREATE TABLE dd_user (
  id serial,
  email character varying(150) NOT NULL,
  password character varying(250) NOT NULL,
  name character varying(250) NOT NULL
);

CREATE TABLE dd_role (
  id serial,
  name character varying(150) NOT NULL
);

CREATE TABLE dd_user_role (
  id serial,
  user_id integer NOT NULL,
  role_id integer NOT NULL
);

ALTER TABLE ONLY dd_user ADD CONSTRAINT dd_user_pkey PRIMARY KEY (id);
ALTER TABLE ONLY dd_role ADD CONSTRAINT dd_role_pkey PRIMARY KEY (id);
ALTER TABLE ONLY dd_user_role ADD CONSTRAINT dd_user_role_pkey PRIMARY KEY (id);

CREATE INDEX dd_user_email on dd_user USING btree (email);
CREATE INDEX dd_user_password on dd_user USING btree (password);

ALTER TABLE ONLY dd_user_role ADD CONSTRAINT user_role_user_id_fkey FOREIGN KEY (user_id) REFERENCES dd_user(id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE ONLY dd_user_role ADD CONSTRAINT user_role_role_id_fkey FOREIGN KEY (role_id) REFERENCES dd_role(id) DEFERRABLE INITIALLY DEFERRED;

INSERT INTO dd_user (email, password, name) VALUES ('jeffusan@atware.jp', 'secret', 'Jeff');
INSERT INTO dd_user (email, password, name) VALUES ('bigman@atware.jp', 'secret', 'Big Man');

INSERT INTO dd_role (name) VAlUES ('user');
INSERT INTO dd_role (name) VALUES ('administrator');

INSERT INTO dd_user_role (user_id, role_id) VALUES ((select id from dd_user where name='Jeff'), (select id from dd_role where name='user'));
INSERT INTO dd_user_role (user_id, role_id) VALUES ((select id from dd_user where name='Big Man'), (select id from dd_role where name='administrator'));

# --- !Downs

DROP TABLE dd_user_role;
DROP TABLE dd_role cascade;
DROP TABLE dd_user cascade;
