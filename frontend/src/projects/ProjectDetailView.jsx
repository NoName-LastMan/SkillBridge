import { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import api from "../services/api";

export default function ProjectDetailView() {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const [project, setProject] = useState(null);
  const [teamMembers, setTeamMembers] = useState([]); // State baru untuk menyimpan tim
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [showApplyForm, setShowApplyForm] = useState(false);
  const [applyData, setApplyData] = useState({
    positionApplied: "",
    message: ""
  });
  const [isApplying, setIsApplying] = useState(false);

  useEffect(() => {
    const fetchProjectAndTeam = async () => {
      try {
        // 1. Ambil Detail Proyek
        const projectRes = await api.get(`/projects/${id}`);
        setProject(projectRes.data);

        // 2. Ambil Daftar Anggota Tim (API 5.3 Poin 12)
        try {
          const teamRes = await api.get(`/projects/${id}/team`);
          // Memastikan data yang disimpan adalah array
          setTeamMembers(teamRes.data.content || teamRes.data || []);
        } catch (teamErr) {
          console.warn("Gagal mengambil data tim, mungkin belum ada anggota atau akses ditolak", teamErr);
        }

      } catch (err) {
        console.error("Gagal mengambil detail proyek:", err);
        setError("Proyek tidak ditemukan atau terjadi kesalahan server.");
      } finally {
        setLoading(false);
      }
    };

    fetchProjectAndTeam();
  }, [id]);

  const handleApplySubmit = async (e) => {
    e.preventDefault();
    setIsApplying(true);
    
    try {
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
    <div className="min-h-screen bg-[#F3F2EF]">
      <Navbar />

      <div className="max-w-4xl mx-auto p-4 sm:p-8 space-y-6">
        <button
          onClick={() => navigate(-1)}
          className="text-gray-500 hover:text-blue-600 hover:underline font-bold text-sm flex items-center gap-2 transition"
        >
          &larr; Kembali ke Feed
        </button>

        {loading ? (
          <div className="bg-white rounded-xl shadow-sm p-12 text-center text-gray-500 border border-gray-200">
            <span className="animate-pulse">Mengumpulkan informasi proyek & tim...</span>
          </div>
        ) : error ? (
          <div className="bg-red-50 text-red-700 rounded-xl shadow-sm p-8 text-center font-bold border border-red-100">
            {error}
          </div>
        ) : project ? (
          <>
            {/* =========================================
                BAGIAN 1: DETAIL PROYEK
                ========================================= */}
            <div className="bg-white rounded-xl shadow-sm p-6 sm:p-8 border border-gray-200">
              <div className="flex justify-between items-start">
                <div>
                  <span className="inline-block bg-blue-100 text-blue-800 text-xs font-bold px-3 py-1 rounded-full mb-4">
                    {project.category}
                  </span>
                  <h1 className="text-3xl font-bold text-gray-900 leading-tight">{project.title}</h1>
                </div>
                <span className={`text-xs font-bold px-4 py-1.5 rounded-full shadow-sm ${
                  project.status === 'OPEN' ? 'bg-green-100 text-green-700 border border-green-200' : 'bg-red-100 text-red-700 border border-red-200'
                }`}>
                  {project.status || 'OPEN'}
                </span>
              </div>

              <div className="mt-8 border-y border-gray-100 py-5 grid grid-cols-2 gap-4 text-sm">
                <div>
                  <p className="text-gray-500 font-bold uppercase tracking-wider text-[11px] mb-1">Kapasitas Tim</p>
                  <p className="font-bold text-gray-800 text-lg">
                    {project.currentMemberCount || 0} <span className="text-gray-400 font-medium text-sm">/ {project.maxMembers} Anggota</span>
                  </p>
                </div>
                <div>
                  <p className="text-gray-500 font-bold uppercase tracking-wider text-[11px] mb-1">Keahlian Dicari</p>
                  <p className="font-bold text-blue-600">{project.requiredSkills || "Terbuka untuk umum"}</p>
                </div>
              </div>

              <div className="mt-6">
                <h3 className="text-lg font-bold text-gray-800 mb-3">Tentang Proyek Ini</h3>
                <p className="text-gray-600 leading-relaxed whitespace-pre-line text-justify">
                  {project.description}
                </p>
              </div>

              {/* Area Form Lamaran */}
              <div className="mt-8 pt-6 border-t border-gray-100 bg-gray-50 -mx-6 sm:-mx-8 -mb-6 sm:-mb-8 p-6 sm:p-8 rounded-b-xl">
                {!showApplyForm ? (
                  <div className="flex flex-col sm:flex-row justify-between items-center gap-4">
                    <p className="text-sm text-gray-600 font-medium text-center sm:text-left">
                      {project.status === 'OPEN' 
                        ? "Tertarik dengan ide ini? Ajukan dirimu dan lengkapi tim mereka!" 
                        : "Mohon maaf, rekrutmen untuk proyek ini telah ditutup."}
                    </p>
                    <button
                      onClick={() => setShowApplyForm(true)}
                      disabled={project.status !== 'OPEN'}
                      className={`w-full sm:w-auto px-8 py-3 rounded-lg font-bold transition shadow-md whitespace-nowrap ${
                        project.status === 'OPEN' 
                          ? "bg-blue-600 hover:bg-blue-700 text-white" 
                          : "bg-gray-300 text-gray-500 cursor-not-allowed shadow-none"
                      }`}
                    >
                      {project.status === 'OPEN' ? "Gabung Proyek" : "Ditutup"}
                    </button>
                  </div>
                ) : (
                  <form onSubmit={handleApplySubmit} className="bg-white p-6 rounded-xl shadow-sm border border-gray-200 animate-fade-in-up">
                    <h3 className="text-lg font-bold text-gray-800 mb-5 border-b pb-3">Formulir Pengajuan Diri</h3>
                    
                    <div className="mb-4">
                      <label className="block mb-2 text-sm font-bold text-gray-700">Posisi yang Diinginkan <span className="text-red-500">*</span></label>
                      <input
                        type="text"
                        required
                        value={applyData.positionApplied}
                        onChange={(e) => setApplyData({...applyData, positionApplied: e.target.value})}
                        className="w-full border border-gray-300 rounded-lg p-3 text-sm focus:ring-2 focus:ring-blue-500 outline-none bg-gray-50 focus:bg-white transition"
                        placeholder="Contoh: Frontend Dev, UI/UX, Penulis Laporan..."
                      />
                    </div>

                    <div className="mb-5">
                      <label className="block mb-2 text-sm font-bold text-gray-700">Pesan Pendek / Cover Letter <span className="text-red-500">*</span></label>
                      <textarea
                        required
                        rows="3"
                        value={applyData.message}
                        onChange={(e) => setApplyData({...applyData, message: e.target.value})}
                        className="w-full border border-gray-300 rounded-lg p-3 text-sm focus:ring-2 focus:ring-blue-500 outline-none bg-gray-50 focus:bg-white transition"
                        placeholder="Halo, saya sangat tertarik dengan proyek ini karena..."
                      />
                    </div>

                    <div className="flex justify-end gap-3">
                      <button
                        type="button"
                        onClick={() => setShowApplyForm(false)}
                        className="px-5 py-2.5 text-gray-600 hover:bg-gray-100 rounded-lg font-bold transition"
                      >
                        Batal
                      </button>
                      <button
                        type="submit"
                        disabled={isApplying}
                        className={`px-6 py-2.5 rounded-lg font-bold text-white transition shadow-sm ${
                          isApplying ? "bg-gray-400 cursor-not-allowed" : "bg-blue-600 hover:bg-blue-700"
                        }`}
                      >
                        {isApplying ? "Mengirim..." : "Kirim Pengajuan"}
                      </button>
                    </div>
                  </form>
                )}
              </div>
            </div>

            {/* =========================================
                BAGIAN 2: DAFTAR ANGGOTA TIM (THE BRIDGE)
                ========================================= */}
            <div className="bg-white rounded-xl shadow-sm p-6 sm:p-8 border border-gray-200">
              <div className="mb-6 border-b border-gray-100 pb-4">
                <h2 className="text-xl font-bold text-gray-800 flex items-center gap-2">
                  <span>👥</span> Susunan Tim Proyek
                </h2>
                <p className="text-sm text-gray-500 mt-1">Kenali kolaborator yang sudah bergabung di proyek ini.</p>
              </div>

              {teamMembers.length === 0 ? (
                <div className="text-center py-8 bg-gray-50 rounded-xl border border-dashed border-gray-300">
                  <p className="text-gray-500 font-medium">Belum ada anggota tim yang terdaftar.</p>
                  <p className="text-sm text-gray-400 mt-1">Jadilah yang pertama untuk bergabung!</p>
                </div>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {teamMembers.map((member) => (
                    <div key={member.id} className="group flex items-center gap-4 p-4 rounded-xl border border-gray-100 hover:border-blue-300 hover:shadow-md transition bg-gray-50 hover:bg-white cursor-pointer">
                      {/* Avatar Inisial */}
                      <div className="w-14 h-14 shrink-0 bg-gradient-to-br from-blue-500 to-cyan-400 rounded-full flex items-center justify-center text-white text-xl font-bold shadow-sm">
                        {member.userName ? member.userName.charAt(0).toUpperCase() : 'U'}
                      </div>
                      
                      <div className="flex-grow min-w-0">
                        {/* Menghubungkan nama ke halaman profil publik */}
                        <Link 
                          to={`/profile/${member.userId}`} 
                          className="block text-lg font-bold text-gray-800 truncate group-hover:text-blue-600 transition"
                        >
                          {member.userName || "Pengguna Anonim"}
                        </Link>
                        
                        <div className="flex items-center gap-2 mt-1">
                          <span className={`text-[10px] font-bold px-2 py-0.5 rounded uppercase tracking-wider ${
                            member.teamRole === 'Ketua Tim' ? 'bg-blue-100 text-blue-700' : 'bg-green-100 text-green-700'
                          }`}>
                            {member.teamRole || "Anggota"}
                          </span>
                          <span className="text-xs text-gray-500 truncate">
                            {member.userNim ? `NIM: ${member.userNim}` : ''}
                          </span>
                        </div>
                      </div>

                      {/* Ikon panah kecil penanda bisa diklik */}
                      <div className="text-gray-300 group-hover:text-blue-500 transition">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                          <path fillRule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clipRule="evenodd" />
                        </svg>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </>
        ) : null}
      </div>
    </div>
  );
}