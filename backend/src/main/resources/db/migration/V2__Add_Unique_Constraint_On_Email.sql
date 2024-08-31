ALTER TABLE customer
    ADD CONSTRAINT unique_email_constraint UNIQUE (email);