# --- !Ups

ALTER TABLE site ADD site_id character varying(20) NOT NULL DEFAULT 'I';

update site set site_id='OSH-1' where name='OSH-1';
update site set name='big tuna' where site_id='OSH-1';

update site set site_id='OSH-2' where name='OSH-2';
update site set name=null where site_id='OSH-2';

update site set site_id='OSH-3' where name='OSH-3';
update site set name=null where site_id='OSH-3';

update site set site_id='OSH-4' where name='OSH-4';
update site set name=null where site_id='OSH-4';

update site set site_id='OSH-5' where name='OSH-5';
update site set name=null where site_id='OSH-5';

update site set site_id='OSH-6' where name='OSH-6';
update site set name=null where site_id='OSH-6';

update site set site_id='OSH-7' where name='OSH-7';
update site set name=null where site_id='OSH-7';

update site set site_id='OSH-8' where name='OSH-8';
update site set name=null where site_id='OSH-8';

update site set site_id='NII-1' where name='NII-1';
update site set name=null where site_id='NII-1';

update site set site_id='NII-2' where name='NII-2';
update site set name=null where site_id='NII-2';

update site set site_id='NII-3' where name='NII-3';
update site set name=null where site_id='NII-3';

update site set site_id='NII-10' where name='NII-10';
update site set name=null where site_id='NII-10';

update site set site_id='NII-4' where name='NII-4';
update site set name=null where site_id='NII-4';

update site set site_id='NII-5' where name='NII-5';
update site set name=null where site_id='NII-5';

update site set site_id='NII-6' where name='NII-6';
update site set name=null where site_id='NII-6';

update site set site_id='NII-7' where name='NII-7';
update site set name=null where site_id='NII-7';

update site set site_id='NII-8' where name='NII-8';
update site set name=null where site_id='NII-8';

update site set site_id='NII-9' where name='NII-9';
update site set name='astroturf' where site_id='NII-9';

update site set site_id='MIY-1' where name='MIY-1';
update site set name=null where site_id='MIY-1';

update site set site_id='MIY-2' where name='MIY-2';
update site set name=null where site_id='MIY-2';

update site set site_id='MIY-3' where name='MIY-3';
update site set name=null where site_id='MIY-3';

update site set site_id='YAK-1' where name='YAK-1';
update site set name=null where site_id='YAK-1';

update site set site_id='YAK-2' where name='YAK-2';
update site set name=null where site_id='YAK-2';

update site set site_id='YAK-3' where name='YAK-3';
update site set name=null where site_id='YAK-3';

update site set site_id='NAK-1' where name='NAK-1';
update site set name=null where site_id='NAK-1';

update site set site_id='NAK-2' where name='NAK-2';
update site set name=null where site_id='NAK-2';

update site set site_id='NAK-3' where name='NAK-3';
update site set name=null where site_id='NAK-3';

update site set site_id='TAG-1' where name='TAG-1';
update site set name=null where site_id='TAG-1';

update site set site_id='TAG-2' where name='TAG-2';
update site set name=null where site_id='TAG-2';

update site set site_id='TAG-3' where name='TAG-3';
update site set name='special place' where site_id='TAG-3';

update site set site_id='RISH-1' where name='RISH-1';
update site set name=null where site_id='RISH-1';

update site set site_id='RISH-2' where name='RISH-2';
update site set name=null where site_id='RISH-2';

update site set site_id='RISH-3' where name='RISH-3';
update site set name=null where site_id='RISH-3';

update site set site_id='REB-1' where name='REB-1';
update site set name=null where site_id='REB-1';

update site set site_id='REB-2' where name='REB-2';
update site set name='sea palace' where site_id='REB-2';

update site set site_id='REB-3' where name='REB-3';
update site set name=null where site_id='REB-3';

# --- !Downs
