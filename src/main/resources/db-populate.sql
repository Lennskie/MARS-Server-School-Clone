-- Quotes
INSERT INTO quotes VALUES (0, 'You must accept that you might fail.');
INSERT INTO quotes VALUES (1, 'Everyone enjoys doing the kind of work for which he is best suited.');


-- Users (identifier, firstname, lastname)
INSERT INTO users VALUES ('MARS-ID-001', 'Ana', 'Silveneyer');
INSERT INTO users VALUES ('MARS-ID-002', 'Bob', 'Goldeneyer');
INSERT INTO users VALUES ('MARS-ID-003', 'Carolina', 'Platineyer');
INSERT INTO users VALUES ('MARS-ID-004', 'Dirk', 'Endedneyer');
INSERT INTO users VALUES ('MARS-ID-005', 'Elena', 'Reumbersedneyer');
INSERT INTO users VALUES ('MARS-ID-006', 'Florence', 'Nosubneyer');


-- Subscriptions (name, description, price)
INSERT INTO subscriptions VALUES ('Silver', 'Rescue in Domes where a MARS station is present.', 500.00);
INSERT INTO subscriptions VALUES ('Gold', 'Rescue in any Dome, and anywhere on the Surface of Mars.', 750.00);
INSERT INTO subscriptions VALUES ('Platinum', 'Rescue in any Dome, and anywhere on the Surface and Underground of Mars.', 1000.00);


-- User Subscription (user_identifier, subscription_name, price, start_date, end_date, reimbursed)
INSERT INTO user_subscription VALUES ('MARS-ID-001', 'Silver', 500.00, CURRENT_DATE, null, false);
INSERT INTO user_subscription VALUES ('MARS-ID-002', 'Gold', 750.00, CURRENT_DATE, null, false);
INSERT INTO user_subscription VALUES ('MARS-ID-003', 'Platinum', 1000.00, CURRENT_DATE, null, false);
INSERT INTO user_subscription VALUES ('MARS-ID-004', 'Platinum', 1000.00, CURRENT_DATE, CURRENT_DATE, false);
INSERT INTO user_subscription VALUES ('MARS-ID-005', 'Platinum', 1000.00, CURRENT_DATE, CURRENT_DATE, true);


-- Vehicles (identifier, occupied)
INSERT INTO vehicles VALUES ('AV-001', false);
INSERT INTO vehicles VALUES ('AV-002', false);
INSERT INTO vehicles VALUES ('AV-003', false);

-- Domes (identifier, size)
INSERT INTO domes VALUES ('DOME-001', 1483.23);
INSERT INTO domes VALUES ('DOME-002', 1231.38);
INSERT INTO domes VALUES ('DOME-003', 927.29);
INSERT INTO domes VALUES ('DOME-004', 1120.98);
INSERT INTO domes VALUES ('DOME-005', 923.21);
INSERT INTO domes VALUES ('DOME-006', 827.34);
INSERT INTO domes VALUES ('DOME-007', 1534.32);
INSERT INTO domes VALUES ('DOME-008', 1929.21);
INSERT INTO domes VALUES ('DOME-009', 1739.76);
INSERT INTO domes VALUES ('DOME-010', 1846.23);

-- Vehicles (identifier, source_type, destination_type, source_identifier, destination_identifier)
INSERT INTO dispatches VALUES ('DISPATCH-001', 'Vehicle', 'Client', 'AV-001', 'MARS-ID-001');
INSERT INTO dispatches VALUES ('DISPATCH-002', 'Client', 'Dome', 'MARS-ID-001', 'DOME-001');