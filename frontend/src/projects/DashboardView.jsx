import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import api from "../services/api";

// Import Ikon dari Lucide-React agar seragam dengan desain temanmu
import { Zap, Users, Flame, BookMarked, PlusCircle, Compass, ChevronRight } from "lucide-react";

import SearchBar from "./SearchBar";
import FilterBar from "./FilterBar";
import ProjectList from "./ProjectList";

// BANK DATA DUMMY (Akan muncul jika database backend masih kosong)
const DUMMY_PROJECTS = [
  {
    id: 101,
    title: "Tim PKM-K: Kewirausahaan Digital UMKM",
    category: "PKM",
    description: "Mencari 2 orang (UI/UX Designer & Copywriter) untuk proposal PKM Kewirausahaan pembuatan platform kasir digital UMKM lokal.",
    status: "OPEN",
    currentMemberCount: 1,
    maxMembers: 3,
  },
  {
    id: 102,
    title: "Hackathon Gemastik 2026: Divisi Data Mining",
    category: "LOMBA",
    description: "Membutuhkan mahasiswa prodi Statistik atau Informatika yang paham Python, Pandas, dan Machine Learning dasar.",
    status: "OPEN",
    currentMemberCount: 2,
    maxMembers: 3,
  },
  {
    id: 103,
    title: "Startup Edutech: KampusKu Apps",
    category: "STARTUP",
    description: "Sedang merintis startup pendidikan. Butuh Frontend Developer (React/Next.js) dan satu orang Marketing.",
    status: "OPEN",
    currentMemberCount: 3,
    maxMembers: 5,
  },
  {
    id: 104,
    title: "Penelitian IoT Smart Farming Dosen",
    category: "PENELITIAN",
    description: "Dibutuhkan asisten peneliti untuk merangkai sensor Arduino dan membuat aplikasi mobile Flutter untuk monitoring suhu tanaman.",
    status: "OPEN",
    currentMemberCount: 1,
    maxMembers: 4,
  },
  {
    id: 105,
    title: "Open Source: Sistem Perpustakaan Desa",
    category: "OPEN_SOURCE",
    description: "Proyek sosial membangun sistem perpustakaan desa. Siapapun yang mau belajar Spring Boot dan React dipersilakan bergabung!",
    status: "OPEN",
    currentMemberCount: 4,
    maxMembers: 10,
  }
];

export default function DashboardView() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  
  const [searchQuery, setSearchQuery] = useState("");
  const [filter, setFilter] = useState("ALL"); 

  useEffect(() => {
    const fetchProjects = async () => {
      setLoading(true);
      try {
        const response = await api.get("/projects", {
            params: {
                q: searchQuery !== "" ? searchQuery : undefined,
                page: 0,
                size: 20
            }
        });
        
        const realData = response.data.content || [];
        
        if (realData.length === 0 && searchQuery === "") {
            setProjects(DUMMY_PROJECTS);
        } else {
            setProjects(realData); 
        }
        
      } catch (error) {
        console.error("API gagal, fallback ke dummy data:", error);
        setProjects(DUMMY_PROJECTS);
      } finally {
        setLoading(false);
      }
    };

    fetchProjects();
  }, [searchQuery]);

  const handleJoin = (projectId) => {
    alert(
      `Request Join untuk proyek ID: ${projectId} berhasil dikirim!\nMenunggu konfirmasi Ketua Tim.`
    );
  };

  const filteredProjects = projects.filter((project) => {
    if (filter === "ALL") return true;
    return project.category === filter;
  });

  // --- STYLING CONSTANTS (DNA Desain Temanmu) ---
  const cardStyle = {
    background: "#fff", 
    border: "1px solid rgba(91,33,182,0.1)", 
    boxShadow: "0 4px 20px rgba(91,33,182,0.05)"
  };

  return (
    <div className="min-h-screen" style={{ background: "#f7f5ff", fontFamily: "'Plus Jakarta Sans', sans-serif" }}>
      <Navbar />

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">

          {/* ==========================================
              KOLOM KIRI: Mini Profil & Navigasi
              ========================================== */}
          <div className="hidden lg:block lg:col-span-3 space-y-6">
             {/* Profile Card */}
             <div className="rounded-2xl overflow-hidden transition-all hover:-translate-y-1" style={cardStyle}>
                <div className="h-20" style={{ background: "linear-gradient(135deg,#5b21b6 0%,#7c3aed 100%)" }}></div>
                <div className="px-5 pb-5 relative flex flex-col items-center text-center">
                   <div className="w-16 h-16 rounded-2xl bg-white border-4 border-white shadow-sm -mt-8 flex items-center justify-center text-3xl z-10 relative">
                     🎓
                   </div>
                   <h2 className="mt-3 font-bold text-gray-900 text-lg">Siap Berkolaborasi?</h2>
                   <p className="text-xs text-gray-500 mt-1.5 leading-relaxed" style={{ fontFamily: "'Inter', sans-serif" }}>
                     Lengkapi profil dan keahlianmu agar mudah ditemukan oleh ketua proyek di platform ini.
                   </p>
                </div>
                <div className="border-t p-4 text-center" style={{ borderColor: "rgba(91,33,182,0.1)" }}>
                   <Link to="/profile" className="text-sm font-bold transition flex items-center justify-center gap-1.5 hover:opacity-80" style={{ color: "#5b21b6" }}>
                     Update Profil Saya <ChevronRight className="w-4 h-4" />
                   </Link>
                </div>
             </div>

             {/* Stats Card */}
             <div className="rounded-2xl p-5" style={cardStyle}>
               <h3 className="text-xs font-bold uppercase tracking-widest mb-4" style={{ color: "#9ca3af", fontFamily: "'JetBrains Mono', monospace" }}>
                 Statistik Jaringan
               </h3>
               <div className="space-y-4 text-sm font-medium" style={{ color: "#4b5563" }}>
                 <div className="flex justify-between items-center group cursor-pointer">
                   <div className="flex items-center gap-2">
                     <Users className="w-4 h-4" style={{ color: "#5b21b6" }} />
                     <span className="group-hover:text-blue-600 transition">Koneksi Kampus</span>
                   </div>
                   <span className="font-bold" style={{ color: "#5b21b6" }}>24</span>
                 </div>
                 <div className="flex justify-between items-center group cursor-pointer">
                   <div className="flex items-center gap-2">
                     <BookMarked className="w-4 h-4" style={{ color: "#0284c7" }} />
                     <span className="group-hover:text-blue-600 transition">Proyek Disimpan</span>
                   </div>
                   <span className="font-bold" style={{ color: "#0284c7" }}>8</span>
                 </div>
               </div>
             </div>
          </div>

          {/* ==========================================
              KOLOM TENGAH: Area Feed Utama
              ========================================== */}
          <div className="col-span-1 md:col-span-8 lg:col-span-6 space-y-5">
            
            {/* Create Project Prompt */}
            <div className="rounded-2xl p-4 flex items-center gap-3 transition-shadow hover:shadow-md" style={cardStyle}>
               <div className="w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0" style={{ background: "#ede9fe", color: "#5b21b6" }}>
                 <Zap className="w-6 h-6" />
               </div>
               <Link 
                  to="/projects/create" 
                  className="flex-grow text-left px-5 py-3.5 rounded-xl font-medium transition-all hover:opacity-80"
                  style={{ background: "#f5f3ff", color: "#6b7280", border: "1px solid rgba(91,33,182,0.15)", fontSize: "0.95rem" }}
               >
                 Punya ide proyek? Buat rekrutmen sekarang...
               </Link>
            </div>

            {/* Divider */}
            <div className="flex items-center py-2 opacity-70">
              <div className="flex-grow h-px" style={{ background: "rgba(91,33,182,0.15)" }}></div>
              <div className="flex items-center gap-2 px-4">
                <Compass className="w-4 h-4" style={{ color: "#5b21b6" }} />
                <span className="text-xs font-bold uppercase tracking-widest" style={{ color: "#5b21b6", fontFamily: "'JetBrains Mono', monospace" }}>
                  Feed Proyek
                </span>
              </div>
              <div className="flex-grow h-px" style={{ background: "rgba(91,33,182,0.15)" }}></div>
            </div>

            {/* Search & Filter Wrappers */}
            <div className="rounded-2xl p-5 space-y-4" style={cardStyle}>
                <SearchBar 
                  value={searchQuery} 
                  onSearch={(value) => setSearchQuery(value)} 
                />
                <FilterBar 
                  activeFilter={filter} 
                  onFilterChange={(value) => setFilter(value)} 
                />
            </div>

            {/* Project List */}
            {loading ? (
              <div className="rounded-2xl p-10 text-center" style={cardStyle}>
                <p className="font-medium animate-pulse" style={{ color: "#6b7280" }}>Mencari kecocokan proyek...</p>
              </div>
            ) : filteredProjects.length === 0 ? (
              <div className="rounded-2xl p-12 text-center" style={cardStyle}>
                <div className="w-16 h-16 mx-auto mb-4 bg-gray-100 rounded-full flex items-center justify-center text-gray-400">
                  <Search className="w-8 h-8" />
                </div>
                <h3 className="text-lg font-bold text-gray-800 mb-1">Tidak Ditemukan</h3>
                <p style={{ color: "#6b7280", fontSize: "0.9rem" }}>Belum ada postingan proyek yang sesuai dengan pencarianmu.</p>
              </div>
            ) : (
              <ProjectList
                projects={filteredProjects}
                onJoin={handleJoin}
              />
            )}
          </div>

          {/* ==========================================
              KOLOM KANAN: Trending & Info
              ========================================== */}
          <div className="hidden lg:block lg:col-span-3 space-y-6">
             <div className="rounded-2xl p-6" style={cardStyle}>
               <h3 className="font-bold text-gray-900 mb-5 flex items-center gap-2" style={{ fontSize: "1.1rem" }}>
                 <Flame className="w-5 h-5" style={{ color: "#f59e0b" }} /> 
                 Sedang Tren
               </h3>
               <ul className="space-y-4">
                  {[
                    { tag: "#PKM2026", count: 42, color: "#5b21b6", bg: "#ede9fe" },
                    { tag: "#Gemastik", count: 18, color: "#0284c7", bg: "#e0f2fe" },
                    { tag: "#StartupMahasiswa", count: 9, color: "#059669", bg: "#d1fae5" }
                  ].map((trend, i) => (
                    <li key={i} className="cursor-pointer group flex items-start justify-between">
                      <div>
                        <p className="font-bold text-gray-800 group-hover:text-blue-600 transition" style={{ fontSize: "0.95rem" }}>
                          {trend.tag}
                        </p>
                        <p style={{ color: "#6b7280", fontSize: "0.75rem", fontFamily: "'Inter', sans-serif", marginTop: "2px" }}>
                          {trend.count} rekrutmen baru
                        </p>
                      </div>
                      <div className="w-8 h-8 rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity" style={{ background: trend.bg, color: trend.color }}>
                        <ChevronRight className="w-4 h-4" />
                      </div>
                    </li>
                  ))}
               </ul>
             </div>

             {/* Footer Links */}
             <div className="text-center px-4 pt-2">
                <div className="flex flex-wrap justify-center gap-x-3 gap-y-2 text-xs font-semibold mb-3" style={{ color: "#6b7280" }}>
                  <Link to="#" className="hover:text-blue-600 transition">Tentang</Link>
                  <Link to="#" className="hover:text-blue-600 transition">Aksesibilitas</Link>
                  <Link to="#" className="hover:text-blue-600 transition">Bantuan</Link>
                  <Link to="#" className="hover:text-blue-600 transition">Privasi & Ketentuan</Link>
                </div>
                <p style={{ color: "#9ca3af", fontSize: "0.75rem", fontFamily: "'JetBrains Mono', monospace" }}>
                  SkillBridge Corporation © 2026
                </p>
             </div>
          </div>

        </div>
      </main>
    </div>
  );
}