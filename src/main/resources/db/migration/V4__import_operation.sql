CREATE TABLE import_operation (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(10) NOT NULL,
    added_count INT,
    user_id BIGINT NOT NULL REFERENCES app_user(id),

    CONSTRAINT chk_import_status CHECK (status IN ('SUCCESS', 'FAILED')),
    CONSTRAINT chk_import_added_count CHECK (
      (status = 'SUCCESS' AND added_count IS NOT NULL AND added_count >= 0)
      OR
      (status = 'FAILED' AND added_count IS NULL)
    )
);
