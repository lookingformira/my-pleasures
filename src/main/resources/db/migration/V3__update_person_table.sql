-- Add person password field
ALTER TABLE person
ADD password text NOT NULL DEFAULT('qwerty');