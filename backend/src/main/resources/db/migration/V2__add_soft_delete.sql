-- ============================================================================
-- V2 — Tambah kolom soft delete untuk projects
-- Proyek yang dihapus admin tidak benar-benar hilang dari DB sehingga
-- audit trail tetap terjaga.
-- ============================================================================

-- Tambah kolom soft delete ke tabel projects
ALTER TABLE projects
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP DEFAULT NULL;

-- Index untuk memudahkan query "proyek aktif" (deleted_at IS NULL)
CREATE INDEX IF NOT EXISTS idx_projects_deleted_at ON projects(deleted_at)
    WHERE deleted_at IS NULL;
