Dokumentasi REST API - SkillBridge 🚀

Platform Kolaborasi dan Pertukaran Skill Mahasiswa UNIMUS

Dokumentasi API ini berfungsi sebagai kontrak jembatan antara tim Backend (Spring Boot) dan tim Frontend (React JS). Semua komunikasi data menggunakan format JSON dengan base URL yang disepakati di bawah ini.

📌 Informasi Umum

Base URL Lokal: http://localhost:8080/api

Base URL Production: https://api.skillbridge.unimus.ac.id/api (contoh)

Format Data: JSON (Content-Type: application/json)

Autentikasi: Bearer Token JWT (dimasukkan dalam Header Authorization: Bearer <token>)

Standard Response Format

🟢 Response Sukses

{
  "success": true,
  "message": "Pesan status operasi berhasil",
  "data": {} // Bisa berupa objek {} atau array []
}


🔴 Response Error

{
  "success": false,
  "message": "Penjelasan detail kenapa error terjadi",
  "errors": null // Opsional, bisa berisi list error validasi form
}


1. Autentikasi & Akun (/auth)

1.1 Register Mahasiswa Baru

Menggunakan email kampus berakhiran @student.unimus.ac.id.

Endpoint: POST /auth/register

Autentikasi: No (Public)

Request Body:

{
  "email": "salman@student.unimus.ac.id",
  "password": "password123",
  "role": "mahasiswa"
}


Response Sukses (201 Created):

{
  "success": true,
  "message": "Registrasi berhasil. Silakan tunggu verifikasi admin kampus.",
  "data": {
    "id": 1,
    "email": "salman@student.unimus.ac.id",
    "role": "mahasiswa",
    "is_verified": false,
    "created_at": "2026-07-04T11:30:00"
  }
}


1.2 Login Akun

Endpoint: POST /auth/login

Autentikasi: No (Public)

Request Body:

{
  "email": "salman@student.unimus.ac.id",
  "password": "password123"
}


Response Sukses (200 OK):

{
  "success": true,
  "message": "Login berhasil",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "salman@student.unimus.ac.id",
      "role": "mahasiswa",
      "is_verified": true
    }
  }
}


2. Profil Mahasiswa (/profiles)

2.1 Buat / Update Profil Skill (Langkah 2 dari 3)

Endpoint: PUT /profiles/me

Autentikasi: Yes (Bearer Token)

Request Body:

{
  "nama_lengkap": "M. Salman Alfarizi",
  "nim": "H2A022001",
  "prodi": "Informatika",
  "bio": "Suka riset backend development dan optimasi database.",
  "skills": ["Spring Boot", "PostgreSQL", "React", "Docker"],
  "minat": ["PKM", "Startup", "Kompetisi"],
  "sedang_mencari": ["Anggota Tim PKM", "Teman Belajar"],
  "no_telepon": "08123456789",
  "instagram": "salman_alfarizi",
  "phone_privacy": "PRIVATE", // Nilai: PUBLIC / PRIVATE
  "ig_privacy": "PUBLIC"
}


Response Sukses (200 OK):

{
  "success": true,
  "message": "Profil berhasil diperbarui",
  "data": {
    "id": 1,
    "nama_lengkap": "M. Salman Alfarizi",
    "nim": "H2A022001",
    "prodi": "Informatika",
    "bio": "Suka riset backend development dan optimasi database.",
    "skills": ["Spring Boot", "PostgreSQL", "React"],
    "minat": ["PKM", "Startup"],
    "sedang_mencari": ["Anggota Tim PKM"],
    "no_telepon": "08123456789",
    "instagram": "salman_alfarizi",
    "phone_privacy": "PRIVATE",
    "ig_privacy": "PUBLIC"
  }
}


2.2 Dapatkan Profil Pribadi (Untuk Dashboard)

Endpoint: GET /profiles/me

Autentikasi: Yes (Bearer Token)

Response Sukses (200 OK):

{
  "success": true,
  "message": "Profil ditemukan",
  "data": {
    "id": 1,
    "nama_lengkap": "M. Salman Alfarizi",
    "nim": "H2A022001",
    "prodi": "Informatika",
    "bio": "Suka riset backend development dan optimasi database.",
    "skills": ["Spring Boot", "React"],
    "is_verified": true
  }
}


2.3 Pencarian & Matching Kolaborator (Algoritma Pencocokan)

Mengembalikan calon kolaborator terdekat berdasarkan kesamaan minat, skill, dan prodi.

Endpoint: GET /profiles

Query Params:

search: (String) untuk pencarian nama/skill/prodi secara manual.

sort: matching_score (default tertinggi) atau latest.

Autentikasi: Yes (Bearer Token)

Request URL Contoh: GET /api/profiles?search=React&sort=matching_score

Response Sukses (200 OK):

{
  "success": true,
  "message": "Berhasil mendapatkan rekomendasi kolaborator",
  "data": [
    {
      "user_id": 12,
      "nama_lengkap": "Rina Putri",
      "prodi": "Akuntansi",
      "angkatan": "2022",
      "bio": "Mahasiswi akuntansi yang passionate di bidang bisnis dan startup.",
      "skills": ["Analisis Keuangan", "Penyusunan Anggaran", "Riset Pasar"],
      "sedang_mencari": ["Programmer untuk Tim", "Anggota Tim PKM"],
      "matching_score": 95,
      "status_hubungan": "BELUM_TERHUBUNG" // BELUM_TERHUBUNG, PENDING, TERHUBUNG
    },
    {
      "user_id": 15,
      "nama_lengkap": "Bima Satrio",
      "prodi": "Sistem Informasi",
      "angkatan": "2021",
      "bio": "Suka riset dan data science.",
      "skills": ["Python", "Machine Learning", "Analisis Data"],
      "sedang_mencari": ["Anggota Tim PKM", "Teman Belajar"],
      "matching_score": 88,
      "status_hubungan": "PENDING"
    }
  ]
}


3. Manajemen Proyek & Tim (/projects)

3.1 Buat Lowongan Proyek Baru (Ketua Tim)

Endpoint: POST /projects

Autentikasi: Yes (Bearer Token)

Request Body:

{
  "judul_proyek": "Sistem Monitoring Kesehatan PKM-KC",
  "deskripsi": "Kami mencari developer Internet of Things (IoT) dan mobile app designer untuk ikut serta PKM tahun ini.",
  "kategori": "PKM", // Nilai: PKM, LOMBA, BELAJAR, STARTUP
  "skills_dibutuhkan": ["Arduino", "IoT", "Flutter"]
}


Response Sukses (201 Created):

{
  "success": true,
  "message": "Lowongan proyek berhasil dipublikasikan",
  "data": {
    "id": 101,
    "creator_id": 1,
    "judul_proyek": "Sistem Monitoring Kesehatan PKM-KC",
    "kategori": "PKM",
    "status": "OPEN",
    "created_at": "2026-07-04T11:35:00"
  }
}


3.2 Ajukan Bergabung ke Tim (Request Join)

Endpoint: POST /projects/{id}/apply

Autentikasi: Yes (Bearer Token)

Request Body:

{
  "pesan_apply": "Halo! Saya Fahmi Nugraha, tertarik mengisi bagian Flutter Developer karena sudah berpengalaman 1 tahun menggunakan Flutter."
}


Response Sukses (201 Created):

{
  "success": true,
  "message": "Permintaan bergabung berhasil dikirim. Menunggu persetujuan Ketua Tim.",
  "data": {
    "application_id": 50,
    "project_id": 101,
    "applicant_id": 3,
    "status": "PENDING"
  }
}


3.3 Kelola Lamaran Masuk (Khusus Ketua Tim)

Menyetujui atau menolak pendaftar tim.

Endpoint: PUT /applications/{id}/status

Autentikasi: Yes (Bearer Token)

Request Body:

{
  "status": "ACCEPTED", // Nilai: ACCEPTED / REJECTED
  "role_tim": "Mobile Developer" // Diisi jika ACCEPTED, mencatat riwayat resmi tim
}


Response Sukses (200 OK):

{
  "success": true,
  "message": "Pendaftar berhasil diterima bergabung di dalam tim.",
  "data": {
    "application_id": 50,
    "status": "ACCEPTED",
    "role_tim": "Mobile Developer"
  }
}


4. Chat & Akses Kontak (/messages & /contacts)

4.1 Kirim & Ambil Chat Internal Proyek (Real-Time Chat)

Hanya bisa diakses oleh anggota tim resmi (tercatat di tabel team_members).

Ambil Riwayat Chat

Endpoint: GET /projects/{project_id}/messages

Autentikasi: Yes (Bearer Token)

Response Sukses (200 OK):

{
  "success": true,
  "message": "Pesan obrolan berhasil dimuat",
  "data": [
    {
      "id": 501,
      "sender_id": 1,
      "nama_pengirim": "M. Salman Alfarizi",
      "pesan": "Halo tim, besok kita kumpul jam 10 pagi di perpus ya untuk bahas proposal.",
      "created_at": "2026-07-04T10:00:00"
    },
    {
      "id": 502,
      "sender_id": 3,
      "nama_pengirim": "Fahmi Nugraha",
      "pesan": "Siap mas, saya bawa draf rancangan sirkuit IoT nya.",
      "created_at": "2026-07-04T10:05:00"
    }
  ]
}


Kirim Chat Baru

Endpoint: POST /projects/{project_id}/messages

Autentikasi: Yes (Bearer Token)

Request Body:

{
  "pesan": "Oke mas, nanti saya kabari Alif juga."
}


4.2 Minta Akses Kontak Privat (Contact Request System)

Jika profil target di-set PRIVATE nomor HP atau Instagram-nya, pengguna lain harus mengajukan izin akses.

Ajukan Permintaan Akses Kontak

Endpoint: POST /contacts/request

Autentikasi: Yes (Bearer Token)

Request Body:

{
  "owner_id": 12, // ID mahasiswa pemilik kontak privat
  "contact_type": "PHONE" // Nilai: PHONE, INSTAGRAM, ALL
}


Response Sukses (201 Created):

{
  "success": true,
  "message": "Permintaan akses kontak berhasil diajukan.",
  "data": {
    "request_id": 88,
    "status": "PENDING"
  }
}
