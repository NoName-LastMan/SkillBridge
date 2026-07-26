import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import api from "../services/api"; // Pastikan path import ini benar

export default function CreateProjectView() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  
  // Menyiapkan state sesuai field yang diminta backend (Dokumentasi API 5.3)
  const [formData, setFormData] = useState({
    title: "",
    category: "PKM",
    description: "",
    maxMembers: 2,
    requiredSkills: ""
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // Mencegah browser me-refresh halaman
    setLoading(true);

    try {
      // Mengirim data ke backend
      await api.post("/projects", formData);
      
      alert("Proyek berhasil dibuat!");
      // Setelah sukses, langsung arahkan kembali ke Dashboard
      navigate("/dashboard");
    } catch (error) {
      console.error("Gagal menyimpan proyek:", error);
      const errorMsg = error.response?.data?.message || "Terjadi kesalahan saat menyimpan proyek.";
      alert(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <div className="max-w-3xl mx-auto p-8">
        <h1 className="text-3xl font-bold mb-6 text-gray-800">Buat Proyek Baru</h1>

        {/* Tambahkan onSubmit pada form */}
        <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow-md p-6 space-y-4">
          
          <div>
            <label className="block mb-2 font-semibold text-gray-700">Judul Proyek</label>
            <input
              name="title"
              type="text"
              required
              value={formData.title}
              onChange={handleChange}
              className="w-full border rounded-lg p-3 focus:ring-2 focus:ring-blue-500 outline-none"
              placeholder="Contoh: Aplikasi AI Deteksi Hama"
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block mb-2 font-semibold text-gray-700">Kategori</label>
              {/* Value disesuaikan dengan Enum ProjectCategory di API */}
              <select
                name="category"
                value={formData.category}
                onChange={handleChange}
                className="w-full border rounded-lg p-3 focus:ring-2 focus:ring-blue-500 outline-none"
              >
                <option value="PKM">PKM</option>
                <option value="LOMBA">LOMBA</option>
                <option value="STARTUP">STARTUP</option>
                <option value="PENELITIAN">PENELITIAN</option>
                <option value="MAGANG">MAGANG</option>
                <option value="OPEN_SOURCE">OPEN SOURCE</option>
                <option value="LAINNYA">LAINNYA</option>
              </select>
            </div>

            <div>
              <label className="block mb-2 font-semibold text-gray-700">Kapasitas Anggota</label>
              <input
                name="maxMembers"
                type="number"
                min="1"
                max="20"
                required
                value={formData.maxMembers}
                onChange={handleChange}
                className="w-full border rounded-lg p-3 focus:ring-2 focus:ring-blue-500 outline-none"
                placeholder="Jumlah anggota dibutuhkan"
              />
            </div>
          </div>

          <div>
            <label className="block mb-2 font-semibold text-gray-700">Skill yang Dibutuhkan</label>
            <input
              name="requiredSkills"
              type="text"
              required
              value={formData.requiredSkills}
              onChange={handleChange}
              className="w-full border rounded-lg p-3 focus:ring-2 focus:ring-blue-500 outline-none"
              placeholder="Contoh: Flutter, Spring Boot, Figma"
            />
          </div>

          <div>
            <label className="block mb-2 font-semibold text-gray-700">Deskripsi Proyek</label>
            <textarea
              name="description"
              rows="5"
              required
              value={formData.description}
              onChange={handleChange}
              className="w-full border rounded-lg p-3 focus:ring-2 focus:ring-blue-500 outline-none"
              placeholder="Ceritakan detail proyekmu dan posisi spesifik yang kamu cari..."
            />
          </div>

          {/* Tombol dengan indikator loading dan tipe submit */}
          <button
            type="submit"
            disabled={loading}
            className={`w-full px-6 py-3 rounded-lg text-white font-bold transition ${
              loading ? "bg-gray-400 cursor-not-allowed" : "bg-blue-600 hover:bg-blue-700"
            }`}
          >
            {loading ? "Menyimpan..." : "Simpan Proyek"}
          </button>
        </form>
      </div>
    </div>
  );
}