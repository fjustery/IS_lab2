ALTER TABLE app_user
    ADD COLUMN role VARCHAR(10) NOT NULL DEFAULT 'USER';

ALTER TABLE app_user
    ADD CONSTRAINT chk_user_role CHECK (role IN ('USER', 'ADMIN'));

INSERT INTO app_user (username, password_hash, role)
VALUES ('admin', '$2a$10$aEKUVKuVEDRO9sXJrAdTbu6VK3VI9IJ.NxsiAFTxLijzMmyd44S7u', 'ADMIN');