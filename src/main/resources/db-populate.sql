-- Quotes
INSERT INTO quotes VALUES (0, 'You must accept that you might fail.');
INSERT INTO quotes VALUES (1, 'Everyone enjoys doing the kind of work for which he is best suited.');


-- Users (identifier, firstname, lastname)
INSERT INTO users VALUES ('MARS-ID-001', 'Ana', 'Silveneyer',1,2,'critical');
INSERT INTO users VALUES ('MARS-ID-002', 'Bob', 'Goldeneyer',1,2,'medium');
INSERT INTO users VALUES ('MARS-ID-003', 'Carolina', 'Platineyer',1,2,'critical');
INSERT INTO users VALUES ('MARS-ID-004', 'Dirk', 'Endedneyer',1,2,'healthy');
INSERT INTO users VALUES ('MARS-ID-005', 'Elena', 'Reumbersedneyer',1,2,'healthy');
INSERT INTO users VALUES ('MARS-ID-006', 'Florence', 'Nosubneyer',1,2,'healthy');


-- Subscriptions (name, description, price)
INSERT INTO subscriptions VALUES ('Silver', 'Rescue in Domes where a MARS station is present.', 500.00);
INSERT INTO subscriptions VALUES ('Gold', 'Rescue in any Dome, and anywhere on the Surface of Mars.', 750.00);
INSERT INTO subscriptions VALUES ('Platinum', 'Rescue in any Dome, and anywhere on the Surface and Underground of Mars.', 1000.00);


-- User Subscription (user_identifier, subscription_name, price, start_date, end_date, reimbursed)
INSERT INTO user_subscription VALUES ('MARS-ID-001', 'Silver', 500.00, CURRENT_DATE, null, true);
INSERT INTO user_subscription VALUES ('MARS-ID-002', 'Gold', 750.00, CURRENT_DATE, null, false);
INSERT INTO user_subscription VALUES ('MARS-ID-003', 'Platinum', 1000.00, CURRENT_DATE, null, true);
INSERT INTO user_subscription VALUES ('MARS-ID-004', 'Platinum', 1000.00, CURRENT_DATE, CURRENT_DATE, false);
INSERT INTO user_subscription VALUES ('MARS-ID-005', 'Platinum', 1000.00, CURRENT_DATE, CURRENT_DATE, true);


-- Vehicles (identifier, occupied, lat, long)
INSERT INTO vehicles VALUES ('AV-001', false, null, null);
INSERT INTO vehicles VALUES ('AV-002', false, null, null);
INSERT INTO vehicles VALUES ('AV-003', true, null, null);
INSERT INTO vehicles VALUES ('AV-004', true, null, null);
INSERT INTO vehicles VALUES ('AV-005', true, null, null);
INSERT INTO vehicles VALUES ('AV-006', true, null, null);
INSERT INTO vehicles VALUES ('AV-007', true, null, null);
INSERT INTO vehicles VALUES ('AV-008', true, null, null);
INSERT INTO vehicles VALUES ('AV-009', true, null, null);

-- Domes (identifier, size, lat, long)
INSERT INTO domes VALUES ('DOME-001', 1000, 29.64161964165002, 35.408435233550065);
INSERT INTO domes VALUES ('DOME-002', 1000, 29.635330710775417, 35.40980449187471);
INSERT INTO domes VALUES ('DOME-003', 1000, 29.628105341126876, 35.45396513878511);

-- DANGERZONES (lat, long, rad)
INSERT INTO DANGERZONES VALUES ('DZ-001',1,2,3);
INSERT INTO DANGERZONES VALUES ('DZ-001',4,5,6);
INSERT INTO DANGERZONES VALUES ('DZ-001',7,8,9);

-- Dispatches (identifier, source_type, destination_type, source_identifier, destination_identifier)
-- DO NOT POPULATE FOR POC