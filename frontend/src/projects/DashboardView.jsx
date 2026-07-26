import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import api from "../services/api";

// Tambahkan import ini
import SearchBar from "./SearchBar";
import FilterBar from "./FilterBar";
import ProjectList from "./ProjectList";

export default function DashboardView() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const response = await api.get("/projects");
        setProjects(response.data);
      } catch (error) {
        console.error("Gagal mengambil data proyek:", error);

        // Dummy data jika backend belum siap
        setProjects([
          {
            id: 1,
            judul_proyek: "Tim PKM-K Kewirausahaan Digital",
            kategori: "PKM",
            deskripsi:
              "Mencari UI/UX Designer untuk aplikasi marketplace mahasiswa.",
            status: "OPEN",
          },
          {
            id: 2,
            judul_proyek: "Lomba Gemastik Data Mining",
            kategori: "LOMBA",
            deskripsi:
              "Butuh anggota yang menguasai Python dan Machine Learning.",
            status: "OPEN",
          },
          {
            id: 3,
            judul_proyek: "IoT Smart Farming",
            kategori: "RISET",
            deskripsi:
              "Mencari programmer Arduino dan Flutter untuk monitoring pertanian.",
            status: "OPEN",
          },
        ]);
      } finally {
        setLoading(false);
      }
    };

    fetchProjects();
  }, []);

  const handleJoin = (projectId) => {
    alert(
      `Request Join untuk proyek ID: ${projectId} berhasil dikirim!\nMenunggu konfirmasi Ketua Tim.`
    );
  };

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

        {/* Search */}
        <div className="mb-4">
          <SearchBar />
        </div>

        {/* Filter */}
        <div className="mb-8">
          <FilterBar />
        </div>

        {/* Loading */}
        {loading ? (
          <p className="text-center text-gray-500">
            Memuat data proyek...
          </p>
        ) : (
          <ProjectList
            projects={projects}
            onJoin={handleJoin}
          />
        )}
      </main>
    </div>
  );
}