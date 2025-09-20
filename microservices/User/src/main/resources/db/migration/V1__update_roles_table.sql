-- First, update existing NULL values with default names
UPDATE roles SET name = CONCAT('ROLE_', id) WHERE name IS NULL;

-- Then make the column non-nullable
ALTER TABLE roles ALTER COLUMN name SET NOT NULL; 