ALTER TABLE users MODIFY COLUMN created_at timestamp;
ALTER TABLE users MODIFY COLUMN updated_at timestamp;

ALTER TABLE posts MODIFY COLUMN created_at timestamp;
ALTER TABLE posts MODIFY COLUMN updated_at timestamp;