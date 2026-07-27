import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import SearchBar from "./SearchBar";
import FilterBar from "./FilterBar";
import api from "../services/api";
import ManageApplicantsModal from "./ManageApplicantsModal";
import { Edit, Trash2 } from "lucide-react"; // Ditambahkan ikon Edit & Hapus

export default function MyProjectView() {
  const [myProjects, setMyProjects] = useState([]);
  const [loadingProjects, setLoadingProjects] = useState(true);
  
  const [myApplications, setMyApplications] = useState([]);
  const [loadingApps, setLoadingApps] = useState(true);

  const [error, setError] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [filter, setFilter] = useState("ALL");
  const [manageProjectId, setManageProjectId] = useState(null);

  const fetchMyProjects = async () => {
    try {
      const response = await api.get("/projects/my", {
          params: { q: searchQuery !== "" ? searchQuery : undefined }
      }); 
      setMyProjects(response.data.content || response.data || []);
    } catch (err) {
      console.error("Gagal mengambil proyek saya:", err);
      setError("Gagal memuat data. Pastikan koneksi server aman.");
    } finally {
      setLoadingProjects(false);
    }
  };

  const fetchMyApplications = async () => {
    try {
      const response = await api.get("/projects/applications/my");
      setMyApplications(response.data.content || response.data || []);
    } catch (err) {
      console.error("Gagal mengambil lamaran saya:", err);
    } finally {
      setLoadingApps(false);
    }
  };

  useEffect(() => {
    setLoadingProjects(true);
    fetchMyProjects();
    fetchMyApplications();
  }, [searchQuery]); 

  const filteredProjects = myProjects.filter((project) => {
    if (filter === "ALL") return true;
    return project.category === filter;
  });

  const handleDelete = async (id) => {
    if (window.confirm("Apakah kamu yakin ingin menghapus proyek ini?")) {
      try {
        await api.delete(`/projects/${id}`);
        alert("Proyek berhasil dihapus!");
        setMyProjects(myProjects.filter(p => p.id !== id));
      } catch (error) {
        alert("Gagal menghapus proyek.");
      }
    }
  };

  const handleOpenWhatsApp = () => {
    window.open("https://chat.whatsapp.com/GrupDummyMVP", "_blank");
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <main className="max-w-7xl mx-auto px-6 py-8 space-y-12">
        
        {/* =========================================
            BAGIAN 1: PROYEK YANG SAYA PIMPIN (KETUA)
            ========================================= */}
        <section>
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
            <div>
              <h1 className="text-3xl font-bold text-gray-800">Proyek Saya</h1>
              <p className="text-gray-500 mt-2">Pantau timmu dan kelola mahasiswa yang melamar.</p>
            </div>
            <Link
              to="/projects/create"
              className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-lg font-semibold whitespace-nowrap transition shadow-sm"
            >
              + Buat Proyek
            </Link>
          </div>

          <div className="mb-4">
            <SearchBar value={searchQuery} onSearch={setSearchQuery} />
          </div>
          <div className="mb-8">
            <FilterBar activeFilter={filter} onFilterChange={setFilter} />
          </div>

          {loadingProjects ? (
            <div className="bg-white rounded-xl shadow-sm p-6 text-center text-gray-500">Memuat data proyekmu...</div>
          ) : error ? (
            <div className="bg-red-100 text-red-700 rounded-xl shadow-sm p-6 text-center font-medium">{error}</div>
          ) : filteredProjects.length === 0 ? (
            <div className="bg-white rounded-xl shadow-sm p-12 text-center border border-gray-100">
              <p className="text-gray-500 text-lg mb-2">Belum ada proyek yang kamu pimpin.</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {filteredProjects.map((project) => (
                <div key={project.id} className="bg-white rounded-xl shadow-md hover:shadow-lg transition flex flex-col h-full border-t-4 border-blue-600">
                  <div className="p-6 flex-grow flex flex-col">
                    <div className="flex justify-between items-start mb-4">
                      <span className="inline-block bg-blue-100 text-blue-800 text-xs font-bold px-3 py-1 rounded-full">{project.category}</span>
                      
                      {/* TOMBOL EDIT & HAPUS DI KARTU PROYEK */}
                      <div className="flex items-center gap-1.5">
                        <Link 
                          to={`/projects/edit/${project.id}`} 
                          className="p-1.5 text-blue-600 hover:bg-blue-50 rounded-lg transition"
                          title="Edit Proyek"
                        >
                          <Edit className="w-4 h-4" />
                        </Link>
                        <button 
                          onClick={() => handleDelete(project.id)} 
                          className="p-1.5 text-red-600 hover:bg-red-50 rounded-lg transition"
                          title="Hapus Proyek"
                        >
                          <Trash2 className="w-4 h-4" />
                        </button>
                        <span className={`text-xs font-bold px-3 py-1 rounded-full ml-1 ${project.status === 'OPEN' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                          {project.status || 'OPEN'}
                        </span>
                      </div>

                    </div>
                    <h2 className="text-xl font-bold mb-2 text-gray-800 line-clamp-2">{project.title}</h2>
                    <p className="text-gray-500 text-sm mb-6 line-clamp-2 flex-grow">{project.description}</p>

                    <div className="bg-gray-50 rounded-lg p-4 mb-4 grid grid-cols-2 gap-4 border border-gray-100">
                      <div className="text-center border-r border-gray-200">
                        <p className="text-[11px] text-gray-500 font-bold uppercase mb-1">Anggota Tim</p>
                        <p className="text-lg font-extrabold text-gray-800">
                          {project.currentMemberCount || 0} <span className="text-sm text-gray-400 font-medium">/ {project.maxMembers}</span>
                        </p>
                      </div>
                      <div className="text-center">
                        <p className="text-[11px] text-gray-500 font-bold uppercase mb-1">Lamaran Masuk</p>
                        <p className={`text-lg font-extrabold ${project.pendingApplicationCount > 0 ? 'text-orange-600' : 'text-gray-800'}`}>
                          {project.pendingApplicationCount || 0}
                        </p>
                      </div>
                    </div>

                    <button
                      onClick={() => setManageProjectId(project.id)}
                      className={`w-full py-3 rounded-lg font-bold transition flex justify-center items-center gap-2 ${
                        project.pendingApplicationCount > 0 ? 'bg-gray-900 hover:bg-black text-white shadow-md' : 'bg-white border-2 border-gray-200 text-gray-700'
                      }`}
                    >
                      {project.pendingApplicationCount > 0 && (
                         <span className="relative flex h-3 w-3">
                           <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-orange-400 opacity-75"></span>
                           <span className="relative inline-flex rounded-full h-3 w-3 bg-orange-500"></span>
                         </span>
                      )}
                      Kelola Pendaftar
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>

        {/* =========================================
            BAGIAN 2: STATUS LAMARAN SAYA (PENDAFTAR)
            ========================================= */}
        <section className="pt-8 border-t border-gray-200">
          <h2 className="text-2xl font-bold text-gray-800 mb-6">Status Lamaran Saya</h2>
          
          {loadingApps ? (
            <p className="text-gray-500">Memuat status lamaran...</p>
          ) : myApplications.length === 0 ? (
            <div className="bg-white rounded-xl shadow-sm p-8 text-center border border-gray-100">
              <p className="text-gray-500">Kamu belum melamar ke proyek mana pun.</p>
              <Link to="/dashboard" className="text-blue-600 hover:underline text-sm font-medium mt-2 inline-block">Cari proyek sekarang &rarr;</Link>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {myApplications.map((app) => (
                <div key={app.id} className="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex flex-col gap-4 hover:shadow-md transition">
                  <div className="flex justify-between items-start">
                    <div>
                      <Link 
                        to={`/projects/${app.projectId}`} 
                        className="font-bold text-lg text-gray-800 hover:text-blue-600 transition"
                      >
                        {app.projectTitle || "Nama Proyek"}
                      </Link>
                      <p className="text-sm text-gray-500 mt-1">Posisi: <span className="font-medium text-gray-700">{app.positionApplied}</span></p>
                    </div>
                    
                    <span className={`text-xs font-bold px-4 py-2 rounded-lg ${
                      app.status === 'ACCEPTED' ? 'bg-green-100 text-green-700 border border-green-200' :
                      app.status === 'REJECTED' ? 'bg-red-100 text-red-700 border border-red-200' :
                      'bg-yellow-100 text-yellow-700 border border-yellow-200'
                    }`}>
                      {app.status === 'ACCEPTED' ? 'DITERIMA' : app.status === 'REJECTED' ? 'DITOLAK' : 'MENUNGGU'}
                    </span>
                  </div>

                  {app.status === 'ACCEPTED' && (
                    <div className="bg-green-50 p-4 rounded-lg border border-green-100 flex flex-col sm:flex-row justify-between items-center gap-4 mt-2">
                      <p className="text-sm text-green-800 font-medium text-center sm:text-left">
                        Selamat, kamu resmi bergabung! Silakan masuk ke grup tim.
                      </p>
                      <button
                        onClick={handleOpenWhatsApp}
                        className="bg-green-600 hover:bg-green-700 text-white px-5 py-2.5 rounded-lg text-sm font-bold transition shadow-sm flex items-center justify-center gap-2 w-full sm:w-auto whitespace-nowrap"
                      >
                        <span>💬</span> Masuk Grup WA
                      </button>
                    </div>
                  )}

                </div>
              ))}
            </div>
          )}
        </section>

        {manageProjectId && (
          <ManageApplicantsModal 
            projectId={manageProjectId} 
            onClose={() => setManageProjectId(null)} 
            onStatusUpdated={() => {
              setTimeout(() => {
                fetchMyProjects();
                fetchMyApplications();
              }, 500);
            }}
          />
        )}
      </main>
    </div>
  );
}