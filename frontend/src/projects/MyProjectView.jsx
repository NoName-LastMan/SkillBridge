import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import SearchBar from "./SearchBar";
import FilterBar from "./FilterBar";
import api from "../services/api";
import ManageApplicantsModal from "./ManageApplicantsModal";

export default function MyProjectView() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  
  const [searchQuery, setSearchQuery] = useState("");
  const [filter, setFilter] = useState("ALL");
  const [manageProjectId, setManageProjectId] = useState(null);

  // Dibuat menjadi fungsi terpisah agar bisa dipanggil ulang saat pendaftar diterima
  const fetchMyProjects = async () => {
    setLoading(true);
    try {
      const response = await api.get("/projects/my", {
          params: {
              q: searchQuery !== "" ? searchQuery : undefined
          }
      }); 
      
      const projectData = response.data.content || response.data || [];
      setProjects(projectData);
    } catch (err) {
      console.error("Gagal mengambil proyek saya:", err);
      setError("Gagal memuat daftar proyek. Pastikan koneksi server aman.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMyProjects();
  }, [searchQuery]); 

  const handleAction = (projectId) => {
    setManageProjectId(projectId); 
  };

  const filteredProjects = projects.filter((project) => {
    if (filter === "ALL") return true;
    return project.category === filter;
  });

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* Header */}
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-800">
              Proyek Saya
            </h1>
            <p className="text-gray-500 mt-2">
              Pantau timmu dan kelola mahasiswa yang melamar.
            </p>
          </div>

          <Link
            to="/projects/create"
            className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-lg font-semibold whitespace-nowrap transition shadow-sm"
          >
            + Buat Proyek
          </Link>
        </div>

        {/* Baris Pencarian & Filter */}
        <div className="mb-4">
          <SearchBar 
             value={searchQuery} 
             onSearch={(value) => setSearchQuery(value)} 
          />
        </div>
        <div className="mb-8">
          <FilterBar 
             activeFilter={filter} 
             onFilterChange={(value) => setFilter(value)} 
          />
        </div>

        {/* Area Konten */}
        {loading ? (
          <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 text-center text-gray-500">
            Sedang memuat data proyekmu...
          </div>
        ) : error ? (
          <div className="bg-red-100 text-red-700 rounded-xl shadow-sm p-6 text-center font-medium">
            {error}
          </div>
        ) : filteredProjects.length === 0 ? (
          <div className="bg-white rounded-xl shadow-sm p-12 text-center border border-gray-100">
            <p className="text-gray-500 text-lg mb-2">Belum ada proyek yang sesuai.</p>
            <p className="text-gray-400 text-sm">
              Kamu belum membuat proyek, atau tidak ada yang cocok dengan pencarianmu.
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            
            {/* DESAIN KARTU KHUSUS PEMILIK PROYEK */}
            {filteredProjects.map((project) => (
              <div key={project.id} className="bg-white rounded-xl shadow-md hover:shadow-lg transition flex flex-col h-full border-t-4 border-blue-600">
                <div className="p-6 flex-grow flex flex-col">
                  
                  {/* Badge & Status */}
                  <div className="flex justify-between items-start mb-4">
                    <span className="inline-block bg-blue-100 text-blue-800 text-xs font-bold px-3 py-1 rounded-full">
                      {project.category}
                    </span>
                    <span className={`text-xs font-bold px-3 py-1 rounded-full ${
                      project.status === 'OPEN' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                    }`}>
                      {project.status || 'OPEN'}
                    </span>
                  </div>
                  
                  {/* Judul & Deskripsi Singkat */}
                  <h2 className="text-xl font-bold mb-2 text-gray-800 line-clamp-2">
                    {project.title}
                  </h2>
                  <p className="text-gray-500 text-sm mb-6 line-clamp-2 flex-grow">
                    {project.description}
                  </p>

                  {/* Panel Statistik Internal (Jumlah Anggota & Pelamar) */}
                  <div className="bg-gray-50 rounded-lg p-4 mb-4 grid grid-cols-2 gap-4 border border-gray-100">
                    <div className="text-center border-r border-gray-200">
                      <p className="text-[11px] text-gray-500 font-bold uppercase tracking-wider mb-1">Anggota Tim</p>
                      <p className="text-lg font-extrabold text-gray-800">
                        {project.currentMemberCount || 0} <span className="text-sm text-gray-400 font-medium">/ {project.maxMembers}</span>
                      </p>
                    </div>
                    <div className="text-center">
                      <p className="text-[11px] text-gray-500 font-bold uppercase tracking-wider mb-1">Lamaran Masuk</p>
                      <p className={`text-lg font-extrabold ${project.pendingApplicationCount > 0 ? 'text-orange-600' : 'text-gray-800'}`}>
                        {project.pendingApplicationCount || 0}
                      </p>
                    </div>
                  </div>

                  {/* Tombol Aksi Dinamis */}
                  <button
                    onClick={() => handleAction(project.id)}
                    className={`w-full py-3 rounded-lg font-bold transition flex justify-center items-center gap-2 ${
                      project.pendingApplicationCount > 0 
                        ? 'bg-gray-900 hover:bg-black text-white shadow-md' 
                        : 'bg-white border-2 border-gray-200 text-gray-700 hover:border-gray-900 hover:text-gray-900'
                    }`}
                  >
                    {/* Efek Ping (Titik Berkedip) jika ada pelamar baru */}
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
        
        {/* Modal Kelola Pendaftar dengan Trigger Auto-Refresh */}
        {manageProjectId && (
          <ManageApplicantsModal 
            projectId={manageProjectId} 
            onClose={() => setManageProjectId(null)} 
            onStatusUpdated={fetchMyProjects}
          />
        )}
      </main>
    </div>
  );
}