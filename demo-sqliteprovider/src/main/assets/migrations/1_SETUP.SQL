CREATE TABLE 'shop'
    (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, postcode TEXT);
INSERT INTO 'shop'
    (name, postcode) VALUES ("TheBigBang Shop", "L300RA");
INSERT INTO 'shop'
    (name, postcode) VALUES ("Dragons Cave", "SK85NX");
INSERT INTO 'shop'
    (name, postcode) VALUES ("Firework Corner", "BN14FT");
INSERT INTO 'shop'
    (name, postcode) VALUES ("Asda", "L319BH");
create table zweiter
    (_id INTEGER PRIMARY KEY, name TEXT, cost float, amount INTEGER, rich BOOLEAN, nice BOOLEAN NOT NULL) ;
