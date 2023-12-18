-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database

INSERT INTO flights(id, number, origin, destination, departureDate, departureTime, arrivalDate, arrivalTime, planeId) VALUES(NEXTVAL('flights_sequence'), 'C-000001', 'Londres', 'New York', DATE '2023-12-02', TIME '13:00:00', DATE '2023-12-03', TIME '02:00:00', 1);

INSERT INTO flights(id, number, origin, destination, departureDate, departureTime, arrivalDate, arrivalTime, planeId) VALUES(NEXTVAL('flights_sequence'), 'D-000002', 'Tokyo', 'San Francisco', DATE '2023-12-03', TIME '15:00:00', DATE '2023-12-04', TIME '05:00:00', 2);

INSERT INTO flights(id, number, origin, destination, departureDate, departureTime, arrivalDate, arrivalTime, planeId) VALUES(NEXTVAL('flights_sequence'), 'E-000003', 'Sydney', 'Dubai', DATE '2023-12-04', TIME '21:00:00', DATE '2023-12-05', TIME '06:00:00', 1);
