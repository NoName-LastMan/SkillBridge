import { useState, useEffect } from "react";
import api from "../services/api";

export default function ManageApplicantsModal({ projectId, onClose, onStatusUpdated }) {
  const [applicants, setApplicants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchApplicants = async () => {
    try {
      // Endpoint API 5.3: GET /api/projects/{id}/applications
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

  useEffect(() => {
    fetchApplicants();
  }, [projectId]);

  const handleAccept = async (appId) => {
    try {
      // Endpoint API 5.3: PUT /api/projects/{id}/applications/{appId}/accept
      await api.put(`/projects/${projectId}/applications/${appId}/accept`);
      alert("Pendaftar berhasil DITERIMA menjadi Anggota Tim! 🎉");
      
      // Refresh data lokal di modal
      fetchApplicants();
      
      // Beritahu parent (MyProjectView) untuk merefresh statistik angka tim
      if (onStatusUpdated) onStatusUpdated();
    } catch (err) {
      console.error("Gagal menerima pendaftar:", err);
      alert(err.response?.data?.message || "Gagal menerima pendaftar.");
    }
  };

  const handleReject = async (appId) => {
    try {
      // Endpoint API 5.3: PUT /api/projects/{id}/applications/{appId}/reject
      await api.put(`/projects/${projectId}/applications/${appId}/reject`);
      alert("Pendaftar telah ditolak.");
      
      fetchApplicants();
      if (onStatusUpdated) onStatusUpdated();
    } catch (err) {
      console.error("Gagal menolak pendaftar:", err);
      alert(err.response?.data?.message || "Gagal menolak pendaftar.");
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-xl w-full max-w-2xl flex flex-col max-h-[80vh]">
        
        {/* Header Modal */}
        <div className="flex justify-between items-center p-6 border-b border-gray-100">
          <div>
            <h2 className="text-xl font-bold text-gray-800">Kelola Pelamar & Anggota</h2>
            <p className="text-xs text-gray-500 mt-0.5">Setujui pelamar untuk memasukkan mereka ke dalam tim proyekmu.</p>
          </div>
          <button onClick={onClose} className="text-gray-400 hover:text-red-500 font-bold text-2xl">
            &times;
          </button>
        </div>

        {/* Isi Modal */}
        <div className="p-6 overflow-y-auto flex-grow">
          {loading ? (
            <p className="text-center text-gray-500 py-4">Memuat daftar pelamar...</p>
          ) : error ? (
            <p className="text-center text-red-500 font-medium py-4">{error}</p>
          ) : applicants.length === 0 ? (
            <div className="text-center py-8">
              <p className="text-gray-500">Belum ada mahasiswa yang melamar ke proyek ini.</p>
            </div>
          ) : (
            <div className="space-y-4">
              {applicants.map((app) => (
                <div 
                  key={app.id} 
                  className={`border rounded-xl p-4 transition ${
                    app.status === 'ACCEPTED' 
                      ? 'border-green-300 bg-green-50/30' 
                      : 'border-gray-200 hover:border-blue-200'
                  }`}
                >
                  <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-2">
                    <div>
                      <div className="flex items-center gap-2">
                        <h3 className="font-bold text-lg text-gray-800">
                          {app.applicantName || "Mahasiswa"}
                        </h3>
                        {app.status === 'ACCEPTED' && (
                          <span className="bg-green-600 text-white text-[10px] font-bold px-2 py-0.5 rounded">
                            ANGGOTA TIM
                          </span>
                        )}
                      </div>
                      <p className="text-xs text-blue-600 font-semibold mt-0.5">
                        Posisi: {app.positionApplied || "-"}
                      </p>
                    </div>

                    {/* Status Badge */}
                    <span className={`text-xs font-bold px-3 py-1 rounded-full ${
                      app.status === 'ACCEPTED' ? 'bg-green-100 text-green-700' :
                      app.status === 'REJECTED' ? 'bg-red-100 text-red-700' :
                      'bg-yellow-100 text-yellow-700'
                    }`}>
                      {app.status === 'ACCEPTED' ? 'DITERIMA' : app.status === 'REJECTED' ? 'DITOLAK' : 'MENUNGGU'}
                    </span>
                  </div>

                  {/* Pesan Pelamar */}
                  {app.message && (
                    <div className="bg-white p-3 rounded-lg text-sm text-gray-600 my-3 border border-gray-100">
                      <p className="italic">"{app.message}"</p>
                    </div>
                  )}

                  {/* Tombol Aksi (Hanya muncul jika status masih PENDING) */}
                  {app.status === 'PENDING' && (
                    <div className="flex gap-2 justify-end pt-2 border-t border-gray-100">
                      <button 
                        onClick={() => handleReject(app.id)}
                        className="bg-red-50 hover:bg-red-100 text-red-600 px-4 py-2 rounded-lg text-sm font-semibold transition"
                      >
                        Tolak
                      </button>
                      <button 
                        onClick={() => handleAccept(app.id)}
                        className="bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded-lg text-sm font-semibold transition shadow-sm"
                      >
                        Terima Ke Tim
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