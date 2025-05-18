-- Add profile_picture column to users table if it doesn't exist
ALTER TABLE users ADD COLUMN IF NOT EXISTS profile_picture VARCHAR(255);
