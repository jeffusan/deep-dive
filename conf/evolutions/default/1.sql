# --- !Ups

CREATE TABLE monitoring_team (
    id serial,
    name character varying(200) NOT NULL
    );

CREATE TABLE reef_type (
    id serial,
    name character varying(200) NOT NULL,
    depth character varying (20) NOT NULL
    );

CREATE TABLE region (
    id serial,
    name character varying(200) NOT NULL
    );

CREATE TABLE site (
    id serial,
    subregion_id integer NOT NULL,
    reef_type_id integer NOT NULL,
    name character varying(200),
    latitude numeric(21,10) NOT NULL,
    longitude numeric(21,10) NOT NULL,
    map_datum character varying(200) NOT NULL
    );

CREATE TABLE subregion (
    id serial,
    name character varying(200) NOT NULL,
    region_id integer NOT NULL,
    code character varying(10) NOT NULL
    );

CREATE TABLE survey_event (
    id serial,
    site_id integer NOT NULL,
    event_date date NOT NULL,
    transect_length smallint NOT NULL,
    photographer character varying(25) NOT NULL,
    analyzer character varying(25) NOT NULL,
    transect_depth smallint NOT NULL,
    data jsonb
    );

CREATE TABLE surveyevent_monitoring_team (
    id serial,
    surveyevent_id integer NOT NULL,
    monitoringteam_id integer NOT NULL
    );

ALTER TABLE ONLY monitoring_team
    ADD CONSTRAINT monitoring_team_pkey PRIMARY KEY (id);

ALTER TABLE ONLY reef_type
    ADD CONSTRAINT reef_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY region
    ADD CONSTRAINT region_pkey PRIMARY KEY (id);

ALTER TABLE ONLY site
    ADD CONSTRAINT site_pkey PRIMARY KEY (id);

ALTER TABLE ONLY subregion
    ADD CONSTRAINT subregion_pkey PRIMARY KEY (id);

ALTER TABLE ONLY surveyevent_monitoring_team
    ADD CONSTRAINT surveyevent_monitoring_surveyevent_id_monitoringteam_key UNIQUE (surveyevent_id, monitoringteam_id);

ALTER TABLE ONLY surveyevent_monitoring_team
    ADD CONSTRAINT surveyevent_monitoring_team_pkey PRIMARY KEY (id);

ALTER TABLE ONLY survey_event
    ADD CONSTRAINT survey_event_pkey PRIMARY KEY (id);


CREATE INDEX site_reef_type_id ON site USING btree (reef_type_id);

CREATE INDEX site_subregion_id ON site USING btree (subregion_id);

CREATE INDEX subregion_region_id ON subregion USING btree (region_id);

CREATE INDEX surveyevent_monitoring_team_monitoringteam_id ON surveyevent_monitoring_team USING btree (monitoringteam_id);

CREATE INDEX surveyevent_monitoring_team_surveyevent_id ON surveyevent_monitoring_team USING btree (surveyevent_id);

CREATE INDEX survey_event_site_id ON survey_event USING btree (site_id);

CREATE INDEX survey_event_data ON survey_event USING GIN(data);

ALTER TABLE ONLY site
    ADD CONSTRAINT site_reef_type_id_fkey FOREIGN KEY (reef_type_id) REFERENCES reef_type(id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY site
    ADD CONSTRAINT site_subregion_id_fkey FOREIGN KEY (subregion_id) REFERENCES subregion(id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY subregion
    ADD CONSTRAINT subregion_region_id_fkey FOREIGN KEY (region_id) REFERENCES region(id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY surveyevent_monitoring_team
    ADD CONSTRAINT surveyevent_monitoring_team_monitoringteam_id_fkey FOREIGN KEY (monitoringteam_id) REFERENCES monitoring_team(id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY survey_event
    ADD CONSTRAINT survey_event_site_id_fkey FOREIGN KEY (site_id) REFERENCES site(id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY surveyevent_monitoring_team
    ADD CONSTRAINT surveyevent_id_refs_id_c36946ba FOREIGN KEY (surveyevent_id) REFERENCES survey_event(id) DEFERRABLE INITIALLY DEFERRED;

insert into region (name) values ('Tokyo Prefecture');
insert into region (name) values ('Kagoshima Prefecture');
insert into region (name) values ('Hokkaido Prefecture');

insert into monitoring_team (name) values ('Monitoring 1');
insert into monitoring_team (name) values ('Monitoring 2');
insert into monitoring_team (name) values ('Monitoring 3');
insert into monitoring_team (name) values ('Monitoring 4');
insert into monitoring_team (name) values ('Monitoring 5');
insert into monitoring_team (name) values ('Monitoring 6');
insert into monitoring_team (name) values ('Monitoring 7');
insert into monitoring_team (name) values ('Monitoring 8');
insert into monitoring_team (name) values ('Monitoring 9');
insert into monitoring_team (name) values ('Monitoring 10');

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

insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, transect_depth, data) values (1,'2012-05-30',50,'photag','analyzer', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, transect_depth, data) values (2,'2012-05-30',50,'photag','analyzer', 50), '{"id": "id1", "name": "name1"}';
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, transect_depth, data) values (3,'2012-05-29',50,'photag','analyzer', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, transect_depth, data) values (4,'2012-05-29',50,'photag','analyzer', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, transect_depth, data) values (5,'2013-06-03',50,'photag','analyzer', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, transect_depth, data) values (6,'2013-06-03',50,'photag','analyzer', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, transect_depth, data) values (7,'2011-07-04',25,'photag','analyzer', 50, '{"id": "id1", "name": "name1"}');
insert into survey_event (site_id, event_date, transect_length, photographer, analyzer, transect_depth, data) values (8,'2011-07-04',50,'photag','analyzer', 50, '{"id": "id1", "name": "name1"}');


# --- !Downs

DROP TABLE monitoring_team;

DROP TABLE reef_type;

DROP TABLE region;

DROP TABLE site;

DROP TABLE subregion;

DROP TABLE survey_event;

DROP TABLE surveyevent_monitoring_team;
