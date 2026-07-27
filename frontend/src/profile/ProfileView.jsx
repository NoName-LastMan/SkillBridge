import { useEffect, useState } from "react";
import { getMyProfile, updateProfile } from "../services/profileService";
import PrivacyToggle from "./PrivacyToggle";
import Navbar from "../components/Navbar"; // Tambahkan Navbar agar bisa kembali ke menu utama

export default function ProfileView() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false); // State untuk efek tombol loading

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    try {
      const data = await getMyProfile();
      setProfile(data);
    } catch (error) {
      console.error(error);
      alert("Gagal mengambil data profil");
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setIsSaving(true);
    try {
      await updateProfile(profile);
      alert("Profil berhasil diperbarui! 🎉");
    } catch (error) {
      console.error(error);
      alert("Gagal menyimpan profil. Pastikan koneksi aman.");
    } finally {
      setIsSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col">
        <Navbar />
        <div className="flex-grow flex items-center justify-center">
          <p className="text-gray-500 font-medium">Memuat data profil...</p>
        </div>
      </div>
    );
  }

  if (!profile) {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col">
        <Navbar />
        <div className="flex-grow flex items-center justify-center">
          <p className="text-red-500 font-medium">Profil tidak ditemukan.</p>
        </div>
      </div>
    );
  }

  // Generate inisial untuk placeholder jika foto tidak ada
  const fallbackImage = `https://ui-avatars.com/api/?name=${profile.namaLengkap || 'User'}&background=0D8ABC&color=fff&size=150`;

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <Navbar />

      <main className="max-w-4xl mx-auto px-6 py-10 w-full">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-800">Profil Saya</h1>
          <p className="text-gray-500 mt-2">Kelola informasi publik dan kontak yang akan dilihat oleh pengguna lain.</p>
        </div>

        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
          <form onSubmit={handleSave} className="space-y-8">
            
            {/* --- FOTO PROFIL --- */}
            <div className="flex flex-col sm:flex-row items-center gap-6 pb-6 border-b border-gray-100">
              <img
                src={profile.fotoUrl || fallbackImage}
                alt="Foto Profil"
                className="w-32 h-32 rounded-full object-cover shadow-sm border-4 border-white outline outline-2 outline-gray-100"
              />
              <div className="flex-grow w-full">
                <label className="block text-sm font-bold text-gray-700 mb-2">URL Foto Profil</label>
                <input
                  type="text"
                  placeholder="https://contoh.com/foto.jpg"
                  value={profile.fotoUrl || ""}
                  onChange={(e) => setProfile({ ...profile, fotoUrl: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 bg-gray-50 focus:bg-white transition"
                />
                <p className="text-xs text-gray-400 mt-2">Untuk MVP, cukup paste link gambar publik (misal dari Google Drive/Imgur).</p>
              </div>
            </div>

            {/* --- DATA AKADEMIK & DIRI --- */}
            <div>
              <h3 className="text-lg font-bold text-gray-800 mb-4">Informasi Akademik</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Nama Lengkap</label>
                  <input
                    type="text"
                    value={profile.namaLengkap || ""}
                    onChange={(e) => setProfile({ ...profile, namaLengkap: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">NIM</label>
                  <input
                    type="text"
                    value={profile.nim || ""}
                    onChange={(e) => setProfile({ ...profile, nim: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Program Studi</label>
                  <input
                    type="text"
                    value={profile.prodi || ""}
                    onChange={(e) => setProfile({ ...profile, prodi: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Tahun Angkatan</label>
                  <input
                    type="text"
                    placeholder="Contoh: 2022"
                    value={profile.angkatan || ""}
                    onChange={(e) => setProfile({ ...profile, angkatan: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
              </div>
            </div>

            {/* --- BIO --- */}
            <div>
              <label className="block text-sm font-bold text-gray-800 mb-2">Bio / Tentang Saya</label>
              <textarea
                rows="4"
                placeholder="Ceritakan singkat tentang minat dan keahlianmu..."
                value={profile.bio || ""}
                onChange={(e) => setProfile({ ...profile, bio: e.target.value })}
                className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            {/* --- KONTAK --- */}
            <div className="pt-6 border-t border-gray-100">
              <h3 className="text-lg font-bold text-gray-800 mb-4">Sosial Media & Kontak</h3>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">WhatsApp</label>
                  <input
                    type="text"
                    placeholder="0812..."
                    value={profile.whatsapp || ""}
                    onChange={(e) => setProfile({ ...profile, whatsapp: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Instagram</label>
                  <input
                    type="text"
                    placeholder="@username"
                    value={profile.instagram || ""}
                    onChange={(e) => setProfile({ ...profile, instagram: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-pink-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">LinkedIn URL</label>
                  <input
                    type="text"
                    placeholder="https://linkedin.com/in/..."
                    value={profile.linkedin || ""}
                    onChange={(e) => setProfile({ ...profile, linkedin: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
                  />
                </div>
              </div>

              {/* Komponen PrivacyToggle bawaan temanmu */}
              <div className="bg-gray-50 p-4 rounded-xl border border-gray-200 flex items-center justify-between">
                <div>
                  <p className="font-bold text-gray-800">Privasi Kontak</p>
                  <p className="text-sm text-gray-500">Atur siapa saja yang bisa melihat kontakmu.</p>
                </div>
                <PrivacyToggle
                  privacy={profile.contactPrivacy}
                  onChange={(value) => setProfile({ ...profile, contactPrivacy: value })}
                />
              </div>
            </div>

            {/* --- TOMBOL SIMPAN --- */}
            <div className="flex justify-end pt-4">
              <button 
                type="submit" 
                disabled={isSaving}
                className={`px-8 py-3 rounded-lg font-bold text-white transition shadow-sm ${
                  isSaving ? "bg-gray-400 cursor-not-allowed" : "bg-blue-600 hover:bg-blue-700"
                }`}
              >
                {isSaving ? "Menyimpan..." : "Simpan Profil"}
              </button>
            </div>

          </form>
        </div>
      </main>
    </div>
  );
}