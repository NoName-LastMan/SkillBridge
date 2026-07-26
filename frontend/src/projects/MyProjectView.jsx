import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import ProjectCard from "./ProjectCard";
import SearchBar from "./SearchBar";
import FilterBar from "./FilterBar";
import api from "../services/api";

export default function MyProjectView() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  
  // State untuk pencarian dan filter
  const [searchQuery, setSearchQuery] = useState("");
  const [filter, setFilter] = useState("ALL");

  useEffect(() => {
    const fetchMyProjects = async () => {
      setLoading(true);
      try {
        // Mengirimkan parameter pencarian (jika backend mendukungnya di endpoint ini)
        const response = await api.get("/projects/me", {
            params: {
                q: searchQuery !== "" ? searchQuery : undefined
            }
        }); 
        
        // Menyesuaikan dengan format paginasi Spring Boot (Page<Project>)
        const projectData = response.data.content || response.data || [];
        setProjects(projectData);
      } catch (err) {
        console.error("Gagal mengambil proyek saya:", err);
        setError("Gagal memuat daftar proyek. Pastikan koneksi server aman.");
      } finally {
        setLoading(false);
      }
    };

    fetchMyProjects();
  }, [searchQuery]); // Akan memanggil ulang API jika kata kunci pencarian berubah

  const handleAction = (projectId) => {
    // Karena ini proyek sendiri, tombolnya jadi fitur Kelola/Edit
    alert(`Fitur kelola untuk proyek ID: ${projectId} akan segera datang!`);
  };

  // Filter lokal berdasarkan kategori (Enum)
  const filteredProjects = projects.filter((project) => {
    if (filter === "ALL") return true;
    return project.category === filter;
  });

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* Header mirip Dashboard */}
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-800">
              Proyek Saya
            </h1>
            <p className="text-gray-500 mt-2">
              Kelola proyek yang sedang kamu pimpin.
            </p>
          </div>

          <Link
            to="/projects/create"
            className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-lg font-semibold whitespace-nowrap transition"
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
            {filteredProjects.map((project) => (
              <ProjectCard
                key={project.id}
                project={project}
                onJoin={handleAction} 
              />
            ))}
          </div>
        )}
      </main>
    </div>
  );
}