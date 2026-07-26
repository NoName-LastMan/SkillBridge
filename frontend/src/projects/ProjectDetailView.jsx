import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import api from "../services/api";

export default function ProjectDetailView() {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProjectDetail = async () => {
      try {
        const response = await api.get(`/projects/${id}`);
        setProject(response.data);
      } catch (err) {
        console.error("Gagal mengambil detail proyek:", err);
        setError("Proyek tidak ditemukan atau terjadi kesalahan server.");
      } finally {
        setLoading(false);
      }
    };

    fetchProjectDetail();
  }, [id]);

  const handleJoinProject = async () => {
    try {
      await api.post(`/projects/${id}/join`);
      alert("Berhasil bergabung ke dalam tim proyek!");
      navigate("/dashboard");
    } catch (err) {
      const msg = err.response?.data?.message || "Gagal bergabung ke proyek.";
      alert(msg);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <div className="max-w-4xl mx-auto p-8">
        <button
          onClick={() => navigate(-1)}
          className="mb-4 text-blue-600 hover:underline font-medium text-sm"
        >
          &larr; Kembali
        </button>

        {loading ? (
          <div className="bg-white rounded-xl shadow-md p-8 text-center text-gray-500">
            Memuat detail proyek...
          </div>
        ) : error ? (
          <div className="bg-red-100 text-red-700 rounded-xl shadow-md p-6 text-center font-medium">
            {error}
          </div>
        ) : project ? (
          <div className="bg-white rounded-xl shadow-md p-8 space-y-6">
            <div className="flex justify-between items-start">
              <div>
                <span className="inline-block bg-blue-100 text-blue-800 text-xs font-bold px-3 py-1 rounded-full mb-3">
                  {project.category}
                </span>
                <h1 className="text-3xl font-bold text-gray-800">{project.title}</h1>
              </div>
              <span className={`text-xs font-bold px-3 py-1 rounded-full ${
                project.status === 'OPEN' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
              }`}>
                {project.status || 'OPEN'}
              </span>
            </div>

            <div className="border-t border-b border-gray-100 py-4 grid grid-cols-2 gap-4 text-sm text-gray-600">
              <div>
                <span className="font-semibold text-gray-700">Kapasitas Anggota:</span>
                <p>Max {project.maxMembers} Orang</p>
              </div>
              <div>
                <span className="font-semibold text-gray-700">Skill Dibutuhkan:</span>
                <p className="font-medium text-gray-800">{project.requiredSkills || "-"}</p>
              </div>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-gray-800 mb-2">Deskripsi Proyek</h3>
              <p className="text-gray-600 leading-relaxed whitespace-pre-line">
                {project.description}
              </p>
            </div>

            <div className="pt-4 border-t border-gray-100 flex justify-end">
              <button
                onClick={handleJoinProject}
                className="bg-green-600 hover:bg-green-700 text-white px-6 py-3 rounded-lg font-bold transition shadow-sm"
              >
                Gabung ke Proyek Ini
              </button>
            </div>
          </div>
        ) : null}
      </div>
    </div>
  );
}