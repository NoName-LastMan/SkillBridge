# Dokumentasi REST API SkillBridge

Dokumentasi ini dibuat berdasarkan endpoint yang aktif pada backend Spring Boot saat ini.

## Informasi Umum

- **Base URL lokal:** `http://localhost:8080/api`
- **Format data:** `application/json`
- **Autentikasi:** endpoint selain `/auth/**` memerlukan header berikut.

```http
Authorization: Bearer <token>
```

- **Catatan respons:** backend mengembalikan DTO atau array DTO secara langsung, bukan pembungkus `success`, `message`, dan `data`.
- **Kode status umum:** `200 OK` (berhasil), `201 Created` (resource dibuat), `204 No Content` (berhasil tanpa body), `400 Bad Request` (validasi/aturan bisnis), `401 Unauthorized` (token tidak ada/tidak valid), dan `403 Forbidden` (hak akses tidak cukup).

## Nilai Enum

| Nama | Nilai yang didukung |
| --- | --- |
| `role` | `ADMIN`, `MAHASISWA` |
| `level` | `BEGINNER`, `INTERMEDIATE`, `ADVANCED` |
| `contactPrivacy` | `PUBLIC`, `PRIVATE` |
| `category` | `PKM`, `LOMBA`, `STARTUP`, `PENELITIAN`, `MAGANG`, `OPEN_SOURCE`, `LAINNYA` |
| `status` proyek | `OPEN`, `CLOSED`, `COMPLETED` |
| `status` lamaran | `PENDING`, `ACCEPTED`, `REJECTED` |
| `status` permintaan kontak | `PENDING`, `APPROVED`, `REJECTED` |

## 1. Autentikasi

### `POST /auth/register`

Mendaftarkan akun dan otomatis membuat profil kosong. Tidak memerlukan token.

```json
{
  "email": "salman@student.unimus.ac.id",
  "password": "password123",
  "role": "MAHASISWA"
}
```

Validasi: `email` harus valid, `password` minimal 6 karakter, dan `role` wajib diisi.

**Respons `200 OK`**

```json
{
  "message": "User berhasil didaftarkan!"
}
```

Jika email telah digunakan, respons `400 Bad Request` berisi pesan `Error: Email sudah terdaftar!`.

### `POST /auth/login`

Masuk menggunakan email dan password. Tidak memerlukan token.

```json
{
  "email": "salman@student.unimus.ac.id",
  "password": "password123"
}
```

**Respons `200 OK`**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "salman@student.unimus.ac.id",
  "role": "MAHASISWA"
}
```

## 2. Profil dan Skill Saya

Semua endpoint pada bagian ini memerlukan Bearer token.

### `GET /profile/me`

Mengambil profil lengkap pengguna yang sedang login, termasuk kontak dan daftar skill.

**Respons `200 OK`:** objek profil dengan `id`, `userId`, `email`, `namaLengkap`, `nim`, `prodi`, `angkatan`, `bio`, `fotoUrl`, `whatsapp`, `instagram`, `linkedin`, `contactPrivacy`, dan `skills`.

### `PUT /profile/me`

Memperbarui biodata dan kontak. Semua field opsional; batas `nim` 20 karakter, `prodi` 100, dan `angkatan` 10.

```json
{
  "namaLengkap": "M. Salman Alfarizi",
  "nim": "H2A022001",
  "prodi": "Informatika",
  "angkatan": "2022",
  "bio": "Backend developer dan anggota tim PKM.",
  "fotoUrl": "https://example.com/foto.jpg",
  "whatsapp": "08123456789",
  "instagram": "salman_alfarizi",
  "linkedin": "https://linkedin.com/in/salman"
}
```

**Respons `200 OK`:** objek profil lengkap seperti `GET /profile/me`.

### `PUT /profile/me/privacy`

Mengatur visibilitas seluruh kontak profil.

```json
{
  "contactPrivacy": "PRIVATE"
}
```

**Respons `200 OK`:** objek profil lengkap dengan nilai `contactPrivacy` terbaru.

### `GET /profile/me/skills`

Mengambil daftar skill milik pengguna.

**Respons `200 OK`**

```json
[
  {
    "skillId": 1,
    "skillName": "Spring Boot",
    "skillCategory": "Backend",
    "level": "INTERMEDIATE"
  }
]
```

### `POST /profile/me/skills`

Menambahkan skill dari master skill ke profil pengguna.

```json
{
  "skillId": 1,
  "level": "INTERMEDIATE"
}
```

**Respons `200 OK`:** satu objek skill dengan field yang sama seperti item pada `GET /profile/me/skills`.

### `DELETE /profile/me/skills/{skillId}`

Menghapus skill dari profil pengguna.

**Respons `204 No Content`** tanpa body.

### `GET /profile/{userId}`

Melihat profil pengguna lain. Bila kontak target `PRIVATE` dan belum disetujui, nilai `whatsapp`, `instagram`, dan `linkedin` bernilai `null`.

**Respons `200 OK`**

```json
{
  "userId": 12,
  "namaLengkap": "Rina Putri",
  "nim": "H2A022010",
  "prodi": "Informatika",
  "angkatan": "2022",
  "bio": "UI/UX designer.",
  "fotoUrl": null,
  "whatsapp": null,
  "instagram": null,
  "linkedin": null,
  "contactPrivacy": "PRIVATE",
  "contactVisible": false,
  "myRequestStatus": "PENDING",
  "skills": []
}
```

## 3. Master Skill

### `GET /skills`

Mengambil semua skill pada master. Memerlukan Bearer token.

### `GET /skills/search?q={keyword}`

Mencari skill berdasarkan nama. Parameter `q` wajib diisi. Memerlukan Bearer token.

Kedua endpoint mengembalikan `200 OK` berupa array berikut:

```json
[
  {
    "id": 1,
    "name": "Spring Boot",
    "category": "Backend"
  }
]
```

### `POST /skills`

Membuat skill master. Memerlukan Bearer token dengan role `ADMIN`.

```json
{
  "name": "Spring Boot",
  "category": "Backend"
}
```

`name` wajib diisi dan maksimal 100 karakter; `category` maksimal 100 karakter. Respons `200 OK` mengembalikan objek skill yang dibuat.

## 4. Proyek dan Rekrutmen

Semua endpoint pada bagian ini memerlukan Bearer token.

### Struktur respons proyek

Endpoint proyek mengembalikan objek dengan field `id`, `title`, `description`, `category`, `status`, `maxMembers`, `requiredSkills`, `createdById`, `createdByEmail`, `createdByName`, `memberCount`, `pendingApplicationCount`, `hasApplied`, `isOwner`, `createdAt`, dan `updatedAt`.

### `POST /projects`

Membuat proyek baru; pembuat otomatis menjadi ketua tim.

```json
{
  "title": "Sistem Monitoring Kesehatan",
  "description": "Mencari anggota untuk pengembangan IoT.",
  "category": "PKM",
  "maxMembers": 5,
  "requiredSkills": "Arduino, IoT, Flutter"
}
```

`title`, `category`, dan `maxMembers` wajib diisi. Respons: `201 Created` dengan objek proyek.

### `GET /projects`

Mengambil proyek berstatus `OPEN`. Gunakan query opsional `?q=keyword` untuk pencarian. Respons `200 OK` berupa array objek proyek.

### `GET /projects/my`

Mengambil semua proyek yang dibuat pengguna sebagai ketua tim. Respons `200 OK` berupa array objek proyek.

### `GET /projects/{id}`

Mengambil detail satu proyek. Respons `200 OK` berupa objek proyek.

### `PUT /projects/{id}`

Memperbarui proyek; hanya ketua tim. Semua field opsional, tetapi jika dikirim `maxMembers` minimal bernilai 1.

```json
{
  "title": "Sistem Monitoring Kesehatan PKM",
  "description": "Deskripsi terbaru.",
  "category": "PKM",
  "status": "OPEN",
  "maxMembers": 6,
  "requiredSkills": "Arduino, IoT, Flutter, UI/UX"
}
```

Respons `200 OK` berupa objek proyek.

### `DELETE /projects/{id}`

Menghapus proyek; hanya ketua tim. Respons `204 No Content` tanpa body.

### `POST /projects/{id}/apply`

Mengirim lamaran ke proyek.

```json
{
  "positionApplied": "Flutter Developer",
  "message": "Saya berpengalaman membangun aplikasi Flutter."
}
```

Kedua field wajib diisi dan `positionApplied` maksimal 150 karakter. Respons `201 Created` berupa objek lamaran.

### `GET /projects/{id}/applications`

Mengambil semua lamaran untuk proyek; hanya ketua tim. Respons `200 OK` berupa array objek lamaran.

### `PUT /projects/{id}/applications/{appId}/accept`

Menerima lamaran; pelamar otomatis menjadi anggota tim. Hanya ketua tim. Respons `200 OK` berupa objek lamaran dengan `status: "ACCEPTED"`.

### `PUT /projects/{id}/applications/{appId}/reject`

Menolak lamaran. Hanya ketua tim. Respons `200 OK` berupa objek lamaran dengan `status: "REJECTED"`.

### `GET /projects/{id}/team`

Mengambil anggota resmi tim. Respons `200 OK` berupa array dengan `id`, `projectId`, `userId`, `userEmail`, `userName`, `userNim`, `teamRole`, dan `joinedAt`.

### `GET /projects/applications/my`

Mengambil semua lamaran yang telah dikirim pengguna. Respons `200 OK` berupa array objek lamaran.

Objek lamaran berisi `id`, `projectId`, `projectTitle`, `applicantId`, `applicantEmail`, `applicantName`, `applicantNim`, `positionApplied`, `message`, `status`, `createdAt`, dan `updatedAt`.

## 5. Pencocokan

Semua endpoint pencocokan memerlukan Bearer token.

### `GET /match/projects`

Mengambil proyek `OPEN` yang diurutkan berdasarkan kecocokan skill pengguna.

### `GET /match/projects/{id}`

Menghitung kecocokan pengguna terhadap satu proyek.

Kedua endpoint mengembalikan `200 OK` berupa satu objek atau array objek dengan `projectId`, `title`, `description`, `category`, `status`, `requiredSkills`, `maxMembers`, `createdById`, `createdByName`, `createdByEmail`, `currentMemberCount`, `createdAt`, `matchScore`, `matchedSkills`, `missingSkills`, `totalRequired`, dan `totalMatched`.

### `GET /match/collaborators?projectId={id}`

Mencari calon kolaborator untuk kebutuhan proyek. Parameter `projectId` wajib diisi.

**Respons `200 OK`** berupa array dengan field `userId`, `email`, `namaLengkap`, `nim`, `prodi`, `angkatan`, `fotoUrl`, `matchScore`, `matchedSkills`, `allSkills`, dan `alreadyMember`. Nilai `allSkills` menggunakan struktur skill pengguna pada bagian profil.

## 6. Permintaan Akses Kontak

Semua endpoint memerlukan Bearer token.

### `POST /contact-requests/{targetUserId}`

Mengirim permintaan melihat kontak pengguna lain yang mengatur kontak sebagai `PRIVATE`. Tidak memiliki request body.

**Respons `200 OK`:** objek permintaan kontak.

### `GET /contact-requests/incoming`

Mengambil permintaan kontak masuk berstatus `PENDING` untuk pengguna saat ini.

### `GET /contact-requests/outgoing`

Mengambil seluruh permintaan kontak yang dikirim pengguna saat ini.

### `PUT /contact-requests/{requestId}/approve`

Menyetujui permintaan kontak. Hanya pemilik kontak target yang dapat menjalankannya.

### `PUT /contact-requests/{requestId}/reject`

Menolak permintaan kontak. Hanya pemilik kontak target yang dapat menjalankannya.

Keempat endpoint di atas mengembalikan `200 OK` berupa satu objek atau array objek dengan field `id`, `requesterId`, `requesterName`, `requesterEmail`, `requesterNim`, `targetId`, `targetName`, `status`, `createdAt`, dan `updatedAt`.

## Catatan Implementasi

- Saat ini belum ada controller HTTP untuk pesan dan notifikasi, sehingga belum tersedia endpoint REST untuk kedua fitur tersebut.
- Tanggal/waktu respons menggunakan format JSON `LocalDateTime`, misalnya `2026-07-21T13:45:00`.
