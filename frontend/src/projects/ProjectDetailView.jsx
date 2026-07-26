import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import api from "../services/api";

export default function ProjectDetailView() {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // State untuk form lamaran
  const [showApplyForm, setShowApplyForm] = useState(false);
  const [applyData, setApplyData] = useState({
    positionApplied: "",
    message: ""
  });
  const [isApplying, setIsApplying] = useState(false);

  useEffect(() => {
    const fetchProjectDetail = async () => {
      try {
        const response = await api.get(`/projects/${id}`);
        setProject(response.data);
      } catch (err) {
        console.error("Gagal mengambil detail proyek:", err);
        setError("Proyek tidak ditemukan atau terjadi kesalahan server.");
      } finally {
        setLoading(false);
      }
    };

    fetchProjectDetail();
  }, [id]);

  const handleApplySubmit = async (e) => {
    e.preventDefault();
    setIsApplying(true);
    
    try {
      // Mengirimkan payload sesuai Dokumentasi API MVP
      await api.post(`/projects/${id}/apply`, applyData);
      
      alert("Lamaran berhasil dikirim! Menunggu persetujuan ketua tim.");
      setShowApplyForm(false);
      navigate("/dashboard");
    } catch (err) {
      const msg = err.response?.data?.message || "Gagal mengirim lamaran.";
      alert(msg);
    } finally {
      setIsApplying(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <div className="max-w-4xl mx-auto p-8">
        <button
          onClick={() => navigate(-1)}
          className="mb-4 text-blue-600 hover:underline font-medium text-sm"
        >
          &larr; Kembali
        </button>

        {loading ? (
          <div className="bg-white rounded-xl shadow-md p-8 text-center text-gray-500">
            Memuat detail proyek...
          </div>
        ) : error ? (
          <div className="bg-red-100 text-red-700 rounded-xl shadow-md p-6 text-center font-medium">
            {error}
          </div>
        ) : project ? (
          <div className="bg-white rounded-xl shadow-md p-8 space-y-6">
            <div className="flex justify-between items-start">
              <div>
                <span className="inline-block bg-blue-100 text-blue-800 text-xs font-bold px-3 py-1 rounded-full mb-3">
                  {project.category}
                </span>
                <h1 className="text-3xl font-bold text-gray-800">{project.title}</h1>
                <p className="text-sm text-gray-500 mt-2">Dibuat oleh: <span className="font-semibold text-gray-700">{project.createdByName || "Pemilik Proyek"}</span></p>
              </div>
              <span className={`text-xs font-bold px-3 py-1 rounded-full ${
                project.status === 'OPEN' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
              }`}>
                {project.status || 'OPEN'}
              </span>
            </div>

            <div className="border-t border-b border-gray-100 py-4 grid grid-cols-2 gap-4 text-sm text-gray-600">
              <div>
                <span className="font-semibold text-gray-700">Kapasitas Anggota:</span>
                <p>{project.currentMemberCount || 0} / {project.maxMembers} Terisi</p>
              </div>
              <div>
                <span className="font-semibold text-gray-700">Skill Dibutuhkan:</span>
                <p className="font-medium text-gray-800">{project.requiredSkills || "-"}</p>
              </div>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-gray-800 mb-2">Deskripsi Proyek</h3>
              <p className="text-gray-600 leading-relaxed whitespace-pre-line">
                {project.description}
              </p>
            </div>

            {/* Area Tombol Join & Form Lamaran */}
            <div className="pt-4 border-t border-gray-100 mt-6">
              {!showApplyForm ? (
                <div className="flex justify-end">
                  <button
                    onClick={() => setShowApplyForm(true)}
                    disabled={project.status !== 'OPEN'}
                    className={`px-6 py-3 rounded-lg font-bold transition shadow-sm ${
                      project.status === 'OPEN' 
                        ? "bg-green-600 hover:bg-green-700 text-white" 
                        : "bg-gray-300 text-gray-500 cursor-not-allowed"
                    }`}
                  >
                    {project.status === 'OPEN' ? "Gabung ke Proyek Ini" : "Rekrutmen Ditutup"}
                  </button>
                </div>
              ) : (
                <form onSubmit={handleApplySubmit} className="bg-gray-50 p-6 rounded-xl border border-gray-200 mt-4 animate-fade-in-up">
                  <h3 className="text-lg font-bold text-gray-800 mb-4">Form Pengajuan Diri</h3>
                  
                  <div className="mb-4">
                    <label className="block mb-2 text-sm font-semibold text-gray-700">Posisi yang Dilamar</label>
                    <input
                      type="text"
                      required
                      value={applyData.positionApplied}
                      onChange={(e) => setApplyData({...applyData, positionApplied: e.target.value})}
                      className="w-full border rounded-lg p-3 text-sm focus:ring-2 focus:ring-green-500 outline-none"
                      placeholder="Contoh: Frontend Developer / Data Analyst"
                    />
                  </div>

                  <div className="mb-4">
                    <label className="block mb-2 text-sm font-semibold text-gray-700">Pesan Singkat (Alasan & Skill)</label>
                    <textarea
                      required
                      rows="3"
                      value={applyData.message}
                      onChange={(e) => setApplyData({...applyData, message: e.target.value})}
                      className="w-full border rounded-lg p-3 text-sm focus:ring-2 focus:ring-green-500 outline-none"
                      placeholder="Ceritakan sedikit kenapa kamu cocok untuk proyek ini..."
                    />
                  </div>

                  <div className="flex justify-end gap-3">
                    <button
                      type="button"
                      onClick={() => setShowApplyForm(false)}
                      className="px-4 py-2 text-gray-600 hover:bg-gray-200 rounded-lg font-medium transition"
                    >
                      Batal
                    </button>
                    <button
                      type="submit"
                      disabled={isApplying}
                      className={`px-6 py-2 rounded-lg font-bold text-white transition ${
                        isApplying ? "bg-gray-400 cursor-not-allowed" : "bg-green-600 hover:bg-green-700"
                      }`}
                    >
                      {isApplying ? "Mengirim..." : "Kirim Lamaran"}
                    </button>
                  </div>
                </form>
              )}
            </div>
            
          </div>
        ) : null}
      </div>
    </div>
  );
}