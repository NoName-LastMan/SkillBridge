import { useState, useEffect } from "react";
import api from "../services/api";

export default function ManageApplicantsModal({ projectId, onClose, onStatusUpdated }) {
  const [applicants, setApplicants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Tab state untuk memisahkan tampilan
  const [activeTab, setActiveTab] = useState("TEAM"); // "TEAM" atau "APPLICANTS"

  const fetchApplicants = async () => {
    try {
      const response = await api.get(`/projects/${projectId}/applications`);
      const data = response.data.content || response.data || [];
      setApplicants(data);
    } catch (err) {
      console.error("Gagal mengambil data:", err);
      setError("Gagal memuat data tim dan pendaftar.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchApplicants();
  }, [projectId]);

  const handleAccept = async (appId) => {
    try {
      await api.put(`/projects/${projectId}/applications/${appId}/accept`);
      alert("Pendaftar berhasil DITERIMA menjadi Anggota Tim! 🎉");
      fetchApplicants();
      if (onStatusUpdated) onStatusUpdated();
    } catch (err) {
      alert(err.response?.data?.message || "Gagal menerima pendaftar.");
    }
  };

  const handleReject = async (appId) => {
    try {
      await api.put(`/projects/${projectId}/applications/${appId}/reject`);
      fetchApplicants();
      if (onStatusUpdated) onStatusUpdated();
    } catch (err) {
      alert(err.response?.data?.message || "Gagal menolak pendaftar.");
    }
  };

  // Mengelompokkan data
  const teamMembers = applicants.filter(app => app.status === 'ACCEPTED');
  const pendingApplicants = applicants.filter(app => app.status === 'PENDING');

  // URL WhatsApp Group (Untuk MVP kita buat statis/dummy dulu. Nanti bisa ditambahkan ke backend)
  const handleOpenWhatsApp = () => {
    window.open("https://chat.whatsapp.com/GrupDummyMVP", "_blank");
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-xl w-full max-w-2xl flex flex-col max-h-[85vh]">
        
        {/* Header Modal */}
        <div className="flex justify-between items-center p-6 border-b border-gray-100">
          <div>
            <h2 className="text-2xl font-bold text-gray-800">Manajemen Tim Proyek</h2>
          </div>
          <button onClick={onClose} className="text-gray-400 hover:text-red-500 font-bold text-2xl">
            &times;
          </button>
        </div>

        {/* Tab Navigasi */}
        <div className="flex border-b border-gray-200 px-6 mt-2">
          <button 
            onClick={() => setActiveTab("TEAM")}
            className={`pb-3 px-4 font-semibold text-sm transition border-b-2 ${
              activeTab === "TEAM" ? "border-blue-600 text-blue-600" : "border-transparent text-gray-500 hover:text-gray-700"
            }`}
          >
            Anggota Tim ({teamMembers.length})
          </button>
          <button 
            onClick={() => setActiveTab("APPLICANTS")}
            className={`pb-3 px-4 font-semibold text-sm transition border-b-2 flex items-center gap-2 ${
              activeTab === "APPLICANTS" ? "border-blue-600 text-blue-600" : "border-transparent text-gray-500 hover:text-gray-700"
            }`}
          >
            Antrean Pelamar 
            {pendingApplicants.length > 0 && (
              <span className="bg-orange-500 text-white text-[10px] px-2 py-0.5 rounded-full">
                {pendingApplicants.length}
              </span>
            )}
          </button>
        </div>

        {/* Isi Modal */}
        <div className="p-6 overflow-y-auto flex-grow bg-gray-50">
          {loading ? (
            <p className="text-center text-gray-500 py-8">Memuat data...</p>
          ) : error ? (
            <p className="text-center text-red-500 font-medium py-8">{error}</p>
          ) : (
            <>
              {/* === TAB 1: ANGGOTA TIM === */}
              {activeTab === "TEAM" && (
                <div className="space-y-4 animate-fade-in">
                  
                  {/* Panel WhatsApp Group */}
                  <div className="bg-green-50 border border-green-200 rounded-xl p-4 flex flex-col sm:flex-row justify-between items-center gap-4 mb-6 shadow-sm">
                    <div>
                      <h3 className="font-bold text-green-800 text-lg flex items-center gap-2">
                        <span>💬</span> Ruang Diskusi Tim
                      </h3>
                      <p className="text-sm text-green-700 mt-1">
                        Arahkan anggota tim yang sudah diterima ke grup WhatsApp ini.
                      </p>
                    </div>
                    <button 
                      onClick={handleOpenWhatsApp}
                      className="bg-green-600 hover:bg-green-700 text-white px-5 py-2.5 rounded-lg font-bold transition shadow-sm w-full sm:w-auto whitespace-nowrap flex items-center justify-center gap-2"
                    >
                      Buka Grup WA
                    </button>
                  </div>

                  {teamMembers.length === 0 ? (
                    <div className="text-center py-8 bg-white rounded-xl border border-gray-100">
                      <p className="text-gray-500">Belum ada anggota tim yang bergabung.</p>
                    </div>
                  ) : (
                    <div className="grid gap-3">
                      {teamMembers.map((member) => (
                        <div key={member.id} className="bg-white border border-gray-200 rounded-xl p-4 flex justify-between items-center shadow-sm">
                          <div>
                            <h4 className="font-bold text-gray-800 text-lg">{member.applicantName}</h4>
                            <p className="text-sm text-blue-600 font-medium mt-0.5">{member.positionApplied}</p>
                          </div>
                          <span className="bg-green-100 text-green-700 text-xs font-bold px-3 py-1 rounded-full">
                            ANGGOTA
                          </span>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}

              {/* === TAB 2: PELAMAR === */}
              {activeTab === "APPLICANTS" && (
                <div className="space-y-4 animate-fade-in">
                  {pendingApplicants.length === 0 ? (
                    <div className="text-center py-12 bg-white rounded-xl border border-gray-100">
                      <p className="text-gray-500">Tidak ada lamaran baru yang menunggu.</p>
                    </div>
                  ) : (
                    pendingApplicants.map((app) => (
                      <div key={app.id} className="bg-white border border-gray-200 rounded-xl p-5 shadow-sm hover:border-blue-300 transition">
                        <div className="flex justify-between items-start mb-3">
                          <div>
                            <h3 className="font-bold text-lg text-gray-800">{app.applicantName}</h3>
                            <p className="text-sm font-semibold text-gray-500 mt-0.5">
                              Melamar sebagai: <span className="text-blue-600">{app.positionApplied}</span>
                            </p>
                          </div>
                          <span className="bg-yellow-100 text-yellow-700 text-xs font-bold px-3 py-1 rounded-full">
                            MENUNGGU
                          </span>
                        </div>

                        <div className="bg-gray-50 p-3 rounded-lg text-sm text-gray-600 my-3 border border-gray-100 italic">
                          "{app.message}"
                        </div>

                        <div className="flex gap-2 justify-end pt-3 border-t border-gray-100 mt-4">
                          <button 
                            onClick={() => handleReject(app.id)}
                            className="bg-red-50 hover:bg-red-100 text-red-600 px-5 py-2 rounded-lg text-sm font-bold transition"
                          >
                            Tolak
                          </button>
                          <button 
                            onClick={() => handleAccept(app.id)}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg text-sm font-bold transition shadow-sm"
                          >
                            Terima ke Tim
                          </button>
                        </div>
                      </div>
                    ))
                  )}
                </div>
              )}
            </>
          )}
        </div>

      </div>
    </div>
  );
}