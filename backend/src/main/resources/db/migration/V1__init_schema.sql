-- ============================================================================
-- V1 — Skema awal SkillBridge
-- Dibuat oleh Flyway untuk menggantikan ddl-auto=update.
-- Script ini idempotent (menggunakan IF NOT EXISTS).
-- ============================================================================

-- ── 1. USERS ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(100) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL CHECK (role IN ('MAHASISWA', 'ADMIN')),
    is_verified BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_users_email UNIQUE (email)
);

-- ── 2. PROFILES ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS profiles (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT       NOT NULL,
    nama_lengkap    VARCHAR(150),
    nim             VARCHAR(20),
    prodi           VARCHAR(100),
    angkatan        VARCHAR(10),
    bio             TEXT,
    foto_url        VARCHAR(500),
    whatsapp        VARCHAR(20),
    instagram       VARCHAR(100),
    linkedin        VARCHAR(200),
    contact_privacy VARCHAR(20) NOT NULL DEFAULT 'PUBLIC'
                    CHECK (contact_privacy IN ('PUBLIC', 'PRIVATE')),
    created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_profiles_user     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_profiles_user_id  UNIQUE (user_id)
);

-- ── 3. SKILLS (master tabel) ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS skills (
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    category VARCHAR(100),
    CONSTRAINT uk_skills_name UNIQUE (name)
);

-- ── 4. USER_SKILLS (relasi many-to-many user ↔ skill) ────────────────────────
CREATE TABLE IF NOT EXISTS user_skills (
    user_id  BIGINT      NOT NULL,
    skill_id BIGINT      NOT NULL,
    level    VARCHAR(20) NOT NULL DEFAULT 'BEGINNER'
             CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    CONSTRAINT pk_user_skills        PRIMARY KEY (user_id, skill_id),
    CONSTRAINT fk_user_skills_user   FOREIGN KEY (user_id)  REFERENCES users(id)  ON DELETE CASCADE,
    CONSTRAINT fk_user_skills_skill  FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

-- ── 5. PROJECTS ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS projects (
    id              BIGSERIAL    PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    category        VARCHAR(50)  NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'OPEN'
                    CHECK (status IN ('OPEN', 'CLOSED', 'COMPLETED')),
    max_members     INTEGER,
    required_skills TEXT,
    created_by      BIGINT       NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_projects_creator FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- ── 6. APPLICATIONS ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS applications (
    id               BIGSERIAL    PRIMARY KEY,
    project_id       BIGINT       NOT NULL,
    applicant_id     BIGINT       NOT NULL,
    position_applied VARCHAR(150),
    message          TEXT,
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDING'
                     CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_applications_project   FOREIGN KEY (project_id)   REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_applications_applicant FOREIGN KEY (applicant_id) REFERENCES users(id)    ON DELETE CASCADE,
    -- Satu user hanya bisa melamar ke satu proyek sekali
    CONSTRAINT uk_project_applicant      UNIQUE (project_id, applicant_id)
);

-- ── 7. TEAM_MEMBERS ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS team_members (
    id         BIGSERIAL    PRIMARY KEY,
    project_id BIGINT       NOT NULL,
    user_id    BIGINT       NOT NULL,
    team_role  VARCHAR(100),
    joined_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_team_members_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_team_members_user    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    -- Satu user hanya bisa jadi anggota satu proyek sekali
    CONSTRAINT uk_project_user         UNIQUE (project_id, user_id)
);

-- ── 8. CONTACT_REQUESTS ───────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS contact_requests (
    id           BIGSERIAL   PRIMARY KEY,
    requester_id BIGINT      NOT NULL,
    target_id    BIGINT      NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                 CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    created_at   TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_contact_req_requester FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_contact_req_target    FOREIGN KEY (target_id)    REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_contact_req_pair      UNIQUE (requester_id, target_id)
);

-- ── 9. MESSAGES ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS messages (
    id          BIGSERIAL   PRIMARY KEY,
    sender_id   BIGINT      NOT NULL,
    receiver_id BIGINT      NOT NULL,
    content     TEXT        NOT NULL,
    is_read     BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_messages_sender   FOREIGN KEY (sender_id)   REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_receiver FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ── 10. NOTIFICATIONS ─────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS notifications (
    id             BIGSERIAL    PRIMARY KEY,
    user_id        BIGINT       NOT NULL,
    type           VARCHAR(50)  NOT NULL,
    title          VARCHAR(200) NOT NULL,
    message        TEXT,
    is_read        BOOLEAN      NOT NULL DEFAULT FALSE,
    reference_id   BIGINT,
    reference_type VARCHAR(50),
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ── INDEXES untuk performa query umum ─────────────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_users_email        ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role         ON users(role);
CREATE INDEX IF NOT EXISTS idx_projects_status    ON projects(status);
CREATE INDEX IF NOT EXISTS idx_projects_creator   ON projects(created_by);
CREATE INDEX IF NOT EXISTS idx_applications_proj  ON applications(project_id);
CREATE INDEX IF NOT EXISTS idx_applications_appl  ON applications(applicant_id);
CREATE INDEX IF NOT EXISTS idx_messages_sender    ON messages(sender_id);
CREATE INDEX IF NOT EXISTS idx_messages_receiver  ON messages(receiver_id);
CREATE INDEX IF NOT EXISTS idx_notif_user         ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_notif_user_read    ON notifications(user_id, is_read);
