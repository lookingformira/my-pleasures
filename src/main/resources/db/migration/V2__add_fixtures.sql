-- Add user fixtures
INSERT INTO person (first_name, last_name, address, phone, email)
VALUES ('Emily', 'Elizabeth', '1 Birdwell Island, New York, NY', '212-215-1928', 'emily@bigreddog.com'),
       ('Sherlock', 'Holmes', '221B Baker St, London, England, UK', '+44-20-7224-3688', 'sherlock@sherlockholmes.com'),
       ('Fern', 'Arable', 'Arable Farm, Brooklin, ME', '207-711-1899', 'fern@charlottesweb.com'),
       ('Elizabeth', 'Hunter', 'Ontario, Canada', '807-511-1918', 'elizabeth@incrediblejourney.com'),
       ('Peter', 'Hunter', 'Ontario, Canada', '807-511-1918', 'peter@incrediblejourney.com'),
       ('Jim', 'Hunter', 'Ontario, Canada', '807-511-1918', 'jim@incrediblejourney.com'),
       ('Harry', 'Potter', '4 Privet Drive, Little Whinging, Surrey, UK', '+44-20-7224-3688', 'harry@hogwarts.edu'),
       ('Jon', 'Arbuckle', '711 Maple St, Muncie, IN', '812-728-1945', 'jon@garfield.com');


-- Add pleasure fixtures
INSERT INTO pleasure (name, link, description, person_id)
VALUES ('Keyboard', 'https://geekboards.ru/product/varmilo-minilo', 'varmilo keyboard', (SELECT id FROM person WHERE email = 'emily@bigreddog.com')),
       ('Cellphone', 'https://www.apple.com/iphone-14-pro/', 'iphone 14', (SELECT id FROM person WHERE email = 'sherlock@sherlockholmes.com')),
       ('Car', 'https://www.lamborghini.com/en-en/models/urus', 'lamborghini urus', (SELECT id FROM person WHERE email = 'fern@charlottesweb.com'));
