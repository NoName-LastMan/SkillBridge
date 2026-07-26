import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import api from "../services/api";

import SearchBar from "./SearchBar";
import FilterBar from "./FilterBar";
import ProjectList from "./ProjectList";

export default function DashboardView() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  
  // 1. Tambahan state untuk menyimpan ketikan pencarian dan pilihan kategori
  const [searchQuery, setSearchQuery] = useState("");
  const [filter, setFilter] = useState("ALL"); 

  useEffect(() => {
    const fetchProjects = async () => {
      setLoading(true);
      try {
        // 2. Mengirim parameter pencarian (q) ke backend sesuai dokumentasi API
        const response = await api.get("/projects", {
            params: {
                q: searchQuery !== "" ? searchQuery : undefined,
                page: 0,
                size: 20
            }
        });
        
        // 3. PERBAIKAN: Mengambil array dari response.data.content (karena backend menggunakan sistem Pagination)
        setProjects(response.data.content || []); 
      } catch (error) {
        console.error("Gagal mengambil data proyek, menggunakan dummy data:", error);

        // 4. PERBAIKAN: Properti dummy disesuaikan dengan JSON dari dokumentasi API asli (title, description, category)
        setProjects([
          {
            id: 1,
            title: "Tim PKM-K Kewirausahaan Digital",
            category: "PKM",
            description: "Mencari UI/UX Designer untuk aplikasi marketplace mahasiswa.",
            status: "OPEN",
          },
          {
            id: 2,
            title: "Lomba Gemastik Data Mining",
            category: "LOMBA",
            description: "Butuh anggota yang menguasai Python dan Machine Learning.",
            status: "OPEN",
          },
          {
            id: 3,
            title: "IoT Smart Farming",
            category: "PENELITIAN",
            description: "Mencari programmer Arduino dan Flutter untuk monitoring pertanian.",
            status: "OPEN",
          },
        ]);
      } finally {
        setLoading(false);
      }
    };

    // Efek ini akan dipanggil ulang setiap kali searchQuery berubah
    // (Bisa ditambahkan teknik 'debounce' nanti agar tidak memberatkan server)
    fetchProjects();
  }, [searchQuery]);

  const handleJoin = (projectId) => {
    // Nantinya diarahkan ke POST /api/projects/{id}/apply
    alert(
      `Request Join untuk proyek ID: ${projectId} berhasil dikirim!\nMenunggu konfirmasi Ketua Tim.`
    );
  };

  // 5. Logika filter lokal (untuk menyortir data yang sudah diambil berdasarkan kategori Enum)
  const filteredProjects = projects.filter((project) => {
    if (filter === "ALL") return true;
    return project.category === filter;
  });

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* Header */}
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-800">
              SkillBridge Dashboard
            </h1>
            <p className="text-gray-500 mt-2">
              Temukan tim, kolaborator, dan proyek sesuai kemampuanmu.
            </p>
          </div>

          <Link
            to="/projects/create"
            className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-lg font-semibold"
          >
            + Buat Proyek
          </Link>
        </div>

        {/* 6. PERBAIKAN: Mengirim state sebagai props ke komponen anak */}
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

        {/* Loading & Menampilkan data yang sudah difilter */}
        {loading ? (
          <p className="text-center text-gray-500">
            Memuat data proyek...
          </p>
        ) : (
          <ProjectList
            projects={filteredProjects}
            onJoin={handleJoin}
          />
        )}
      </main>
    </div>
  );
}