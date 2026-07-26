import { useState, useEffect } from "react";
import api from "../services/api";

export default function ManageApplicantsModal({ projectId, onClose }) {
  const [applicants, setApplicants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchApplicants = async () => {
      try {
        // Endpoint asumsi: Mengambil daftar pelamar berdasarkan ID proyek
        const response = await api.get(`/projects/${projectId}/applications`);
        const data = response.data.content || response.data || [];
        setApplicants(data);
      } catch (err) {
        console.error("Gagal mengambil data pendaftar:", err);
        setError("Gagal memuat data pendaftar.");
      } finally {
        setLoading(false);
      }
    };

    fetchApplicants();
  }, [projectId]);

  const handleUpdateStatus = async (applicationId, status) => {
    try {
      // Endpoint asumsi: Mengupdate status aplikasi (ACCEPTED / REJECTED)
      // Sesuaikan payload JSON dengan apa yang diminta backend (misal huruf besar semua)
      await api.put(`/applications/${applicationId}/status`, { status: status });
      
      alert(`Pendaftar berhasil di-${status === 'ACCEPTED' ? 'terima' : 'tolak'}!`);
      
      // Update UI secara lokal agar tidak perlu refresh halaman
      setApplicants((prev) => 
        prev.map((app) => app.id === applicationId ? { ...app, status: status } : app)
      );
    } catch (err) {
      console.error("Gagal mengubah status:", err);
      alert(err.response?.data?.message || "Gagal mengubah status pendaftar.");
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-xl w-full max-w-2xl flex flex-col max-h-[80vh]">
        
        {/* Header Modal */}
        <div className="flex justify-between items-center p-6 border-b border-gray-100">
          <h2 className="text-xl font-bold text-gray-800">Kelola Pendaftar</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-red-500 font-bold text-xl">
            &times;
          </button>
        </div>

        {/* Isi Modal */}
        <div className="p-6 overflow-y-auto flex-grow">
          {loading ? (
            <p className="text-center text-gray-500">Memuat pendaftar...</p>
          ) : error ? (
            <p className="text-center text-red-500 font-medium">{error}</p>
          ) : applicants.length === 0 ? (
            <div className="text-center py-8">
              <p className="text-gray-500">Belum ada mahasiswa yang mendaftar ke proyek ini.</p>
            </div>
          ) : (
            <div className="space-y-4">
              {applicants.map((app) => (
                <div key={app.id} className="border border-gray-200 rounded-lg p-4 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                  <div>
                    {/* Asumsi backend mengirimkan data user di dalam object app.user */}
                    <h3 className="font-bold text-gray-800">{app.user?.namaLengkap || app.user?.email || "Mahasiswa"}</h3>
                    <p className="text-sm text-gray-500 mb-1">Status saat ini: <strong className="text-gray-700">{app.status}</strong></p>
                    {app.message && <p className="text-sm text-gray-600 italic">"{app.message}"</p>}
                  </div>

                  {/* Tombol Aksi (Sembunyikan jika sudah diterima/ditolak) */}
                  {app.status === 'PENDING' && (
                    <div className="flex gap-2 w-full sm:w-auto">
                      <button 
                        onClick={() => handleUpdateStatus(app.id, 'ACCEPTED')}
                        className="flex-1 sm:flex-none bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg text-sm font-semibold transition"
                      >
                        Terima
                      </button>
                      <button 
                        onClick={() => handleUpdateStatus(app.id, 'REJECTED')}
                        className="flex-1 sm:flex-none bg-red-100 hover:bg-red-200 text-red-700 px-4 py-2 rounded-lg text-sm font-semibold transition"
                      >
                        Tolak
                      </button>
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
        
      </div>
    </div>
  );
}