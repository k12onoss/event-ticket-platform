-- =========================================
-- EXTENSIONS
-- =========================================
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =========================================
-- ENUMS
-- =========================================
CREATE TYPE event_status AS ENUM ('DRAFT', 'PUBLISHED', 'CANCELLED', 'COMPLETED');

CREATE TYPE ticket_status AS ENUM ('PURCHASED', 'CANCELLED');

CREATE TYPE qr_code_status AS ENUM ('ACTIVE', 'EXPIRED');

CREATE TYPE ticket_validation_status AS ENUM ('VALID', 'INVALID', 'EXPIRED');

CREATE TYPE ticket_validation_method AS ENUM ('QR_SCAN', 'MANUAL');

-- =========================================
-- USERS TABLE
-- =========================================
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL CHECK (char_length(name) > 0),
    email VARCHAR(255) NOT NULL UNIQUE CHECK (char_length(email) > 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- =========================================
-- EVENTS TABLE
-- =========================================
CREATE TABLE events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL CHECK (char_length(name) > 0),
    event_start TIMESTAMPTZ,
    event_end TIMESTAMPTZ,
    venue VARCHAR(255) NOT NULL CHECK (char_length(venue) > 0),
    sales_start TIMESTAMPTZ,
    sales_end TIMESTAMPTZ,
    status event_status NOT NULL,
    organizer_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_event_organizer FOREIGN KEY (organizer_id)
        REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT chk_event_time CHECK (
        event_start IS NULL OR event_end IS NULL OR event_start <= event_end
    ),
    CONSTRAINT chk_sales_time CHECK (
        sales_start IS NULL OR sales_end IS NULL OR sales_start <= sales_end
    )
);

CREATE INDEX idx_events_organizer_id ON events(organizer_id);

-- =========================================
-- EVENT_ATTENDEE (Many-to-Many)
-- =========================================
CREATE TABLE event_attendee (
    event_id UUID NOT NULL,
    attendee_id UUID NOT NULL,
    PRIMARY KEY (event_id, attendee_id),
    CONSTRAINT fk_event_attendee_event FOREIGN KEY (event_id)
        REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_event_attendee_user FOREIGN KEY (attendee_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_event_attendee_user ON event_attendee(attendee_id);

-- =========================================
-- EVENT_STAFF (Many-to-Many)
-- =========================================
CREATE TABLE event_staff (
    event_id UUID NOT NULL,
    staff_id UUID NOT NULL,
    PRIMARY KEY (event_id, staff_id),
    CONSTRAINT fk_event_staff_event FOREIGN KEY (event_id)
        REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_event_staff_user FOREIGN KEY (staff_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_event_staff_user ON event_staff(staff_id);

-- =========================================
-- TICKET_TYPES TABLE
-- =========================================
CREATE TABLE ticket_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL CHECK (char_length(name) > 0),
    description TEXT,
    price NUMERIC(10,2) NOT NULL CHECK (price >= 0),
    total_available INT CHECK (total_available IS NULL OR total_available >= 0),
    event_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_ticket_type_event FOREIGN KEY (event_id)
        REFERENCES events (id) ON DELETE CASCADE
);

CREATE INDEX idx_ticket_types_event_id ON ticket_types(event_id);

-- =========================================
-- TICKETS TABLE
-- =========================================
CREATE TABLE tickets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status ticket_status NOT NULL,
    ticket_type_id UUID NOT NULL,
    purchaser_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_ticket_ticket_type FOREIGN KEY (ticket_type_id)
        REFERENCES ticket_types (id) ON DELETE CASCADE,
    CONSTRAINT fk_ticket_purchaser FOREIGN KEY (purchaser_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_tickets_ticket_type_id ON tickets(ticket_type_id);
CREATE INDEX idx_tickets_purchaser_id ON tickets(purchaser_id);

-- =========================================
-- QR_CODES TABLE
-- =========================================
CREATE TABLE qr_codes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status qr_code_status NOT NULL,
    token VARCHAR(16) NOT NULL UNIQUE CHECK (char_length(token) = 16),
    ticket_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_qrcode_ticket FOREIGN KEY (ticket_id)
        REFERENCES tickets (id) ON DELETE CASCADE
);

CREATE INDEX idx_qrcodes_ticket_id ON qr_codes(ticket_id);

-- =========================================
-- TICKET_VALIDATIONS TABLE
-- =========================================
CREATE TABLE ticket_validations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status ticket_validation_status NOT NULL,
    validation_method ticket_validation_method NOT NULL,
    ticket_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_ticket_validation_ticket FOREIGN KEY (ticket_id)
        REFERENCES tickets (id) ON DELETE CASCADE
);

CREATE INDEX idx_ticket_validations_ticket_id ON ticket_validations(ticket_id);
