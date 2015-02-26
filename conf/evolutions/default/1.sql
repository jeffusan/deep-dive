# --- !Ups

CREATE TABLE reef_type (
    id serial primary key,
    name character varying(200) NOT NULL,
    depth character varying (20) NOT NULL
    );

CREATE TABLE region (
    id serial primary key,
    name character varying(200) NOT NULL
    );

CREATE TABLE subregion (
    id serial primary key,
    name character varying(200) NOT NULL,
    region_id integer references region(id) ON DELETE CASCADE,
    code character varying(10) NOT NULL
    );

CREATE TABLE site (
    id serial primary key,
    subregion_id integer references subregion(id) ON DELETE CASCADE,
    reef_type_id integer references reef_type(id),
    name character varying(200),
    latitude numeric(21,10) NOT NULL,
    longitude numeric(21,10) NOT NULL,
    map_datum character varying(200) NOT NULL
    );

CREATE TABLE survey_event (
    id serial primary key,
    site_id integer references site(id) on delete cascade,
    event_date date NOT NULL,
    transect_length smallint NOT NULL,
    photographer character varying(25) NOT NULL,
    analyzer character varying(25) NOT NULL,
    monitoring_teams character varying(250) NOT NULL,
    transect_depth smallint NOT NULL,
    data jsonb
    );

CREATE TABLE dd_user (
  id serial primary key,
  email character varying(150) NOT NULL,
  password character varying(250) NOT NULL,
  name character varying(250) NOT NULL
);

CREATE TABLE dd_role (
  id serial primary key,
  name character varying(150) NOT NULL
);

CREATE TABLE dd_user_role (
  id serial primary key,
  user_id integer references dd_user(id),
  role_id integer references dd_role(id)
);

CREATE INDEX dd_user_email on dd_user USING btree (email);
CREATE INDEX dd_user_password on dd_user USING btree (password);

CREATE INDEX site_reef_type_id ON site USING btree (reef_type_id);

CREATE INDEX site_subregion_id ON site USING btree (subregion_id);

CREATE INDEX subregion_region_id ON subregion USING btree (region_id);

CREATE INDEX survey_event_site_id ON survey_event USING btree (site_id);

CREATE INDEX survey_event_data ON survey_event USING GIN(data);

insert into region (name) values ('Tokyo Prefecture');
insert into region (name) values ('Kagoshima Prefecture');
insert into region (name) values ('Hokkaido Prefecture');

insert into reef_type (name, depth) values ('Inner', '3-5m');
insert into reef_type (name, depth) values ('Channel', '7-9m');
insert into reef_type (name, depth) values ('Outer', '7-9m');
insert into reef_type (name, depth) values ('Patch/back', '7-9m');

insert into subregion (name, region_id, code) values ('Izu-Oshima', 1, 'OSH');
insert into subregion (name, region_id, code) values ('Nii-Jima',1,'NII');
insert into subregion (name, region_id, code) values ('Miyake-Jima',1,'MIY');
insert into subregion (name, region_id, code) values ('Yakushima',2,'YAK');
insert into subregion (name, region_id, code) values ('Nakanoshima',2,'NAK');
insert into subregion (name, region_id, code) values ('Tagenashima',2,'TAG');
insert into subregion (name, region_id, code) values ('Rishiri',3,'RISH');
insert into subregion (name, region_id, code) values ('Rebun',3,'REB');

insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('OSH-1', 1, 1, '34.793752', '139.357570', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('OSH-2', 1, 2, '34.781910', '139.421428', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('OSH-3', 1, 3, '34.751733', '139.441684', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('OSH-4', 1, 4, '34.743693', '139.353278', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('OSH-5', 1, 1, '34.698825', '139.370787', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('OSH-6', 1, 2, '34.686404', '139.393447', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('OSH-7', 1, 3, '34.693462', '139.448378', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('OSH-8', 1, 4, '34.725353', '139.446318', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NII-1', 1, 1, '34.429390', '139.286330', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NII-2', 1, 2, '34.416929', '139.291137', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NII-3', 1, 3, '34.391435', '139.280150', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NII-4', 1, 4, '34.358565', '139.276030', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NII-5', 1, 1, '34.331352', '139.272940', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NII-6', 1, 2, '34.336738', '139.256461', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NII-7', 1, 3, '34.354880', '139.245131', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NII-8', 1, 4, '34.371459', '139.248908', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NII-9', 1, 1, '34.398730', '139.255259', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NII-10', 1, 2, '34.409211', '139.266246', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('MIY-1', 1, 3, '34.119969', '139.493894', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('MIY-2', 1, 4, '34.070784', '139.563932', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('MIY-3', 1, 1, '34.077040', '139.472951', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('YAK-1', 1, 2, '30.409890', '130.603640', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('YAK-2', 1, 3, '30.265291', '130.418245', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('YAK-3', 1, 4, '30.406337', '130.425798', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NAK-1', 1, 1, '29.875202', '129.878775', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NAK-2', 1, 2, '29.865080', '129.891478', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('NAK-3', 1, 3, '29.847512', '129.837920', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('TAG-1', 1, 4, '30.665850', '131.058682', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('TAG-2', 1, 1, '30.475483', '130.871914', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('TAG-3', 1, 2, '30.354684', '130.893887', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('RISH-1', 1, 3, '45.183561', '141.325960', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('RISH-2', 1, 4, '45.165409', '141.329050', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('RISH-3', 1, 1, '45.201344', '141.312914', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('REB-1', 1, 2, '45.426201', '141.070065', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('REB-2', 1, 3, '45.421864', '140.989041', 'WGS 1984');
insert into site (name, subregion_id, reef_type_id, latitude, longitude, map_datum) values ('REB-3', 1, 4, '45.406921', '140.988354', 'WGS 1984');

insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, monitoring_teams, transect_depth, data) values (1,'2012-05-30',50,'photag','analyzer', 'monitoring team 1', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, monitoring_teams, transect_depth, data) values (2,'2012-05-30',50,'photag','analyzer', 'monitoring team 2', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, monitoring_teams, transect_depth, data) values (3,'2012-05-29',50,'photag','analyzer', 'monitoring team 3', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, monitoring_teams, transect_depth, data) values (4,'2012-05-29',50,'photag','analyzer', 'monitoring team 4', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, monitoring_teams, transect_depth, data) values (5,'2013-06-03',50,'photag','analyzer', 'monitoring team 5', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, monitoring_teams, transect_depth, data) values (6,'2013-06-03',50,'photag','analyzer', 'monitoring team 6', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, monitoring_teams, transect_depth, data) values (7,'2011-07-04',25,'photag','analyzer', 'monitoring team 7', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, monitoring_teams, transect_depth, data) values (8,'2011-07-04',50,'photag','analyzer', 'monitoring team 8', 50, '{"id": "id1", "name": "name1"}');

INSERT INTO dd_user (email, password, name) VALUES ('jeffusan@atware.jp', 'secret', 'Jeff');
INSERT INTO dd_user (email, password, name) VALUES ('bigman@atware.jp', 'secret', 'Big Man');

INSERT INTO dd_role (name) VAlUES ('user');
INSERT INTO dd_role (name) VALUES ('administrator');

INSERT INTO dd_user_role (user_id, role_id) VALUES ((select id from dd_user where name='Jeff'), (select id from dd_role where name='user'));
INSERT INTO dd_user_role (user_id, role_id) VALUES ((select id from dd_user where name='Big Man'), (select id from dd_role where name='administrator'));

# --- !Downs

DROP TABLE region cascade;

DROP TABLE reef_type;

DROP TABLE survey_event;

DROP TABLE dd_user_role cascade;
