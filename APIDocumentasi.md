# Dokumentasi REST API SkillBridge

Dokumentasi ini mencakup seluruh endpoint REST API aktif pada backend Spring Boot **SkillBridge**.

## Informasi Umum

- **Base URL Lokal:** `http://localhost:8080/api`
- **Format Data:** `application/json`
- **Autentikasi:** Semua endpoint selain `/api/auth/**` memerlukan header HTTP berikut:

```http
Authorization: Bearer <token>
```

### Format Pagination (`Page<T>`)
Endpoint yang mendukung pagination (misalnya daftar proyek dan daftar mahasiswa admin) mengembalikan objek halaman standar Spring Data:

```json
{
  "content": [ ... ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalPages": 1,
  "totalElements": 5,
  "size": 20,
  "number": 0
}
```

### Format Respons Error Standar (`ApiErrorResponse`)
Semua exception dan error validasi ditangani secara terpusat oleh `GlobalExceptionHandler` dan mengembalikan respons dengan format standar:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Data permintaan tidak valid",
  "errors": [
    "email: format email tidak valid",
    "password: minimal 6 karakter"
  ],
  "timestamp": "2026-07-23T21:50:00",
  "path": "/api/auth/register"
}
```

- **Kode Status HTTP Umum:**
  - `200 OK`: Permintaan berhasil.
  - `201 Created`: Resource berhasil dibuat.
  - `204 No Content`: Aksi berhasil tanpa response body.
  - `400 Bad Request`: Validasi gagal atau format data tidak sesuai.
  - `401 Unauthorized`: Token JWT tidak valid atau tidak disediakan.
  - `403 Forbidden`: Hak akses tidak mencukupi.
  - `404 Not Found`: Resource tidak ditemukan.
  - `409 Conflict`: Bentrokan aturan bisnis (misal: email sudah terdaftar, kapasitas tim penuh).

---

## Nilai Enum

| Enum | Nilai yang Didukung |
| --- | --- |
| `Role` | `ADMIN`, `MAHASISWA` |
| `SkillLevel` | `BEGINNER`, `INTERMEDIATE`, `ADVANCED` |
| `ContactPrivacy` | `PUBLIC`, `PRIVATE` |
| `ProjectCategory` | `PKM`, `LOMBA`, `STARTUP`, `PENELITIAN`, `MAGANG`, `OPEN_SOURCE`, `LAINNYA` |
| `ProjectStatus` | `OPEN`, `CLOSED`, `COMPLETED` |
| `ApplicationStatus` | `PENDING`, `ACCEPTED`, `REJECTED` |
| `ContactRequestStatus` | `PENDING`, `APPROVED`, `REJECTED` |
| `NotificationType` | `APPLICATION_RECEIVED`, `APPLICATION_ACCEPTED`, `APPLICATION_REJECTED`, `CONTACT_REQUEST_RECEIVED`, `CONTACT_REQUEST_APPROVED`, `CONTACT_REQUEST_REJECTED`, `CONTACT_AUTO_APPROVED`, `NEW_MESSAGE`, `ACCOUNT_VERIFIED`, `ACCOUNT_UNVERIFIED`, `PROJECT_MODERATED` |

---

## 1. Autentikasi (`/api/auth`)

### `POST /api/auth/register`
Mendaftarkan akun baru (`MAHASISWA` atau `ADMIN`) dan otomatis membuat profil kosong. Tidak memerlukan token.

```json
{
  "email": "salman@student.unimus.ac.id",
  "password": "password123",
  "role": "MAHASISWA"
}
```

**Respons `200 OK`:**
```json
{
  "message": "User berhasil didaftarkan!"
}
```

### `POST /api/auth/login`
Masuk menggunakan email dan password. Tidak memerlukan token.

```json
{
  "email": "salman@student.unimus.ac.id",
  "password": "password123"
}
```

**Respons `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "salman@student.unimus.ac.id",
  "role": "MAHASISWA"
}
```

---

## 2. Profil dan Skill Saya (`/api/profile`)

### `GET /api/profile/me`
Mengambil profil lengkap pengguna yang sedang login.

### `PUT /api/profile/me`
Memperbarui biodata dan kontak pengguna.

```json
{
  "namaLengkap": "M. Salman Alfarizi",
  "nim": "H2A022001",
  "prodi": "Informatika",
  "angkatan": "2022",
  "bio": "Backend developer.",
  "fotoUrl": "https://example.com/foto.jpg",
  "whatsapp": "08123456789",
  "instagram": "salman_alfarizi",
  "linkedin": "https://linkedin.com/in/salman"
}
```

### `PUT /api/profile/me/privacy`
Mengatur visibilitas kontak (`PUBLIC` atau `PRIVATE`).

```json
{
  "contactPrivacy": "PRIVATE"
}
```

### `GET /api/profile/me/skills`
Mengambil daftar skill pengguna.

### `POST /api/profile/me/skills`
Menambahkan skill ke profil pengguna.

```json
{
  "skillId": 1,
  "level": "INTERMEDIATE"
}
```

### `DELETE /api/profile/me/skills/{skillId}`
Menghapus skill dari profil pengguna. Respons `204 No Content`.

### `GET /api/profile/{userId}`
Melihat profil pengguna lain (kontak disembunyikan jika `PRIVATE` dan belum disetujui).

---

## 3. Master Skill (`/api/skills`)

- `GET /api/skills`: Mengambil seluruh master skill.
- `GET /api/skills/search?q={keyword}`: Mencari skill berdasarkan nama.
- `POST /api/skills` *(Admin Only)*: Menambahkan master skill baru.

---

## 4. Proyek dan Rekrutmen (`/api/projects`)

### `GET /api/projects`
Mengambil daftar proyek `OPEN` dengan pagination dan pencarian opsional (`?q=keyword&page=0&size=20`).

### `POST /api/projects`
Membuat proyek baru (pembuat otomatis menjadi Ketua Tim dan anggota pertama).

```json
{
  "title": "Sistem IoT Monitoring",
  "description": "Pengembangan alat IoT.",
  "category": "PKM",
  "maxMembers": 4,
  "requiredSkills": "Arduino, Flutter"
}
```

### `GET /api/projects/my`
Mengambil seluruh proyek yang dibuat oleh pengguna saat ini.

### `GET /api/projects/{id}`
Melihat detail satu proyek.

### `PUT /api/projects/{id}`
Memperbarui proyek (hanya ketua tim).

### `DELETE /api/projects/{id}`
Soft delete proyek (hanya ketua tim). Respons `204 No Content`.

### `POST /api/projects/{id}/apply`
Melamar ke proyek.

```json
{
  "positionApplied": "Frontend Developer",
  "message": "Saya ingin bergabung."
}
```

### `GET /api/projects/{id}/applications`
Melihat daftar lamaran proyek (hanya ketua tim).

### `PUT /api/projects/{id}/applications/{appId}/accept`
Menerima lamaran. Pelamar otomatis ditambahkan ke anggota tim. Jika jumlah anggota telah mencapai `maxMembers`, status proyek otomatis berubah menjadi `CLOSED`.

### `PUT /api/projects/{id}/applications/{appId}/reject`
Menolak lamaran.

### `GET /api/projects/{id}/team`
Melihat daftar anggota resmi tim proyek.

### `GET /api/projects/applications/my`
Melihat seluruh lamaran yang telah dikirim oleh pengguna saat ini.

---

## 5. Pencocokan Kolaborasi (`/api/match`)

- `GET /api/match/projects`: Mengambil proyek `OPEN` yang diurutkan berdasarkan skor kecocokan skill pengguna.
- `GET /api/match/projects/{id}`: Menghitung skor kecocokan pengguna terhadap proyek tertentu.
- `GET /api/match/collaborators?projectId={id}`: Mencari mahasiswa yang paling cocok untuk direkrut ke proyek.

---

## 6. Permintaan Akses Kontak (`/api/contact-requests`)

- `POST /api/contact-requests/{targetUserId}`: Mengirim permintaan akses kontak.
- `GET /api/contact-requests/incoming`: Melihat permintaan kontak masuk berstatus `PENDING`.
- `GET /api/contact-requests/outgoing`: Melihat seluruh permintaan kontak yang dikirim.
- `PUT /api/contact-requests/{requestId}/approve`: Menyetujui permintaan kontak (otomatis membuka akses kontak).
- `PUT /api/contact-requests/{requestId}/reject`: Menolak permintaan kontak.

---

## 7. Pesan Langsung (`/api/messages`)

- `POST /api/messages/{receiverId}`: Mengirim pesan langsung ke pengguna lain.
  ```json
  {
    "content": "Halo, apakah masih ada posisi untuk proyek ini?"
  }
  ```
- `GET /api/messages/conversations`: Mengambil daftar percakapan pengguna (ringkasan percakapan).
- `GET /api/messages/conversations/{partnerId}`: Mengambil seluruh riwayat pesan dengan satu pengguna.

---

## 8. Notifikasi (`/api/notifications`)

- `GET /api/notifications`: Mengambil seluruh notifikasi pengguna saat ini.
- `GET /api/notifications/unread-count`: Mengambil jumlah notifikasi yang belum dibaca (`{"count": 3}`).
- `PUT /api/notifications/{id}/read`: Menandai satu notifikasi sebagai dibaca.
- `PUT /api/notifications/read-all`: Menandai seluruh notifikasi sebagai dibaca.
- `DELETE /api/notifications/{id}`: Menghapus satu notifikasi. Respons `204 No Content`.

---

## 9. Admin Dashboard (`/api/admin`) *(Hanya Role ADMIN)*

- `GET /api/admin/stats`: Statistik menyeluruh platform (pengguna, proyek, lamaran, pesan, notifikasi).
- `GET /api/admin/users`: Daftar seluruh mahasiswa dengan filter keyword & status verifikasi (`?keyword=salman&isVerified=true&page=0&size=20`).
- `GET /api/admin/users/{id}`: Detail lengkap mahasiswa beserta profilnya.
- `PUT /api/admin/users/{id}/verify`: Memverifikasi atau mencabut verifikasi mahasiswa.
  ```json
  {
    "isVerified": true,
    "reason": "Dokumen KTM valid"
  }
  ```
- `GET /api/admin/projects`: Daftar proyek aktif untuk moderasi admin (`?keyword=pkm&page=0&size=20`).
- `DELETE /api/admin/projects/{id}`: Moderasi/hapus proyek yang melanggar ketentuan.
  ```json
  {
    "reason": "Proyek terindikasi spam"
  }
  ```
