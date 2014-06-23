

insert into FieldRouter (id , longitude , latitude  , routername , lastsynctime , displayname , targetsensorname) values
(1,136.8725,34.907167,"vbox0094","2014-01-01","半田","HandaMet");

insert into WaterLevelField ( id , sensorcolumnname , timestampcolumnname , fieldrouter , valuefactor ,valueoffset , sensorname ) values
(1,"Port2","Timestamp",1,-0.0318 ,49.682, "HandaW2"),
(2,"Port5","Timestamp",1,-0.0317 ,49.705, "HandaMet"),
(3,"Port2","Timestamp",1,-0.0343 ,51.787, "HandaW1");


insert into FieldRouter (id , longitude , latitude  , routername , lastsynctime , displayname , targetsensorname) values
(2,140.218666666667, 35.7318333333333,"vbox0115","2014-01-01","印旛沼1","inba1");

insert into WaterLevelField ( id , sensorcolumnname , timestampcolumnname , fieldrouter , valuefactor ,valueoffset , sensorname ) values
(21,"Port5","Timestamp",2,-0.0332 ,52.696, "inba1"),
(22,"Port3","Timestamp",2,-0.0243 ,42.287, "inbaW1");


insert into FieldRouter (id , longitude , latitude  , routername , lastsynctime , displayname , targetsensorname) values
(3,140.205166666667, 35.7423333333333,"vbox0116","2014-01-01","印旛沼2","inba2");


insert into WaterLevelField ( id , sensorcolumnname , timestampcolumnname , fieldrouter , valuefactor ,valueoffset , sensorname ) values
(31,"Port5","Timestamp",3,-0.0297 ,48.743, "inba2"),
(32,"Port3","Timestamp",3,-0.0433 ,69.255, "inbaW2");