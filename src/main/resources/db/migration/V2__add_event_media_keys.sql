-- =========================================
-- MIGRATION: Add poster_key and banner_key to events
-- =========================================

ALTER TABLE events
    ADD COLUMN poster_key VARCHAR(255),
    ADD COLUMN banner_key VARCHAR(255);
