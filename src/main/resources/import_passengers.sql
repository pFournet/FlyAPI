-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database

INSERT INTO passengers(id, surname, firstname, emailAddress) VALUES (NEXTVAL('passengers_sequence'), 'Polo', 'Fournet', 'john.smith@example.com');

INSERT INTO passengers(id, surname, firstname, emailAddress) VALUES (NEXTVAL('passengers_sequence'), 'Aly', 'Hutin', 'maria.garcia@example.net');

INSERT INTO passengers(id, surname, firstname, emailAddress) VALUES (NEXTVAL('passengers_sequence'), 'Gio', 'Lerigolo', 'emily.brown@example.org');

INSERT INTO passengers(id, surname, firstname, emailAddress) VALUES (NEXTVAL('passengers_sequence'), 'LEE', 'David', 'david.lee@example.co.uk');
