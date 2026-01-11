CREATE TABLE interviewer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    weekly_capacity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE weekly_availability (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    interviewer_id BIGINT NOT NULL,
    day_of_week TINYINT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_duration_minutes INT NOT NULL DEFAULT 30,
    CONSTRAINT fk_avail_interviewer FOREIGN KEY (interviewer_id) REFERENCES interviewer(id)
);

CREATE TABLE candidate (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE
);

CREATE TABLE interview_slot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    interviewer_id BIGINT NOT NULL,
    start_ts DATETIME NOT NULL,
    end_ts DATETIME NOT NULL,
    capacity INT NOT NULL,
    status ENUM('OPEN','HELD','BOOKED','CLOSED') NOT NULL DEFAULT 'OPEN',
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_slot_interviewer FOREIGN KEY (interviewer_id) REFERENCES interviewer(id),
    CONSTRAINT uniq_slot UNIQUE (interviewer_id, start_ts, end_ts),
    INDEX idx_slot_status_start (status, start_ts)
);

CREATE TABLE interview_booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL,
    slot_id BIGINT NOT NULL,
    status ENUM('BOOKED','CANCELLED','RESCHEDULED') NOT NULL DEFAULT 'BOOKED',
    booked_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_booking_candidate FOREIGN KEY (candidate_id) REFERENCES candidate(id),
    CONSTRAINT fk_booking_slot FOREIGN KEY (slot_id) REFERENCES interview_slot(id),
    CONSTRAINT uniq_candidate_slot UNIQUE (candidate_id, slot_id),
    INDEX idx_booking_slot (slot_id)
);

