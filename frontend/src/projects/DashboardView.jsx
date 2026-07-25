import { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import api from '../services/api'; // Pastikan path ini mengarah ke file axios kamu

export default function DashboardView() {
    const [projects, setProjects] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Fungsi untuk mengambil data lowongan tim dari backend
        const fetchProjects = async () => {
            try {
                // Asumsi endpoint Spring Boot kamu adalah /api/projects
                const response = await api.get('/projects');
                setProjects(response.data);
            } catch (error) {
                console.error("Gagal mengambil data proyek:", error);
                // Data dummy sementara jika backend belum mengembalikan data
                setProjects([
                    { id: 1, judul_proyek: 'Tim PKM-K Kewirausahaan Digital', kategori: 'PKM', deskripsi: 'Mencari UI/UX Designer untuk aplikasi marketplace mahasiswa.', status: 'OPEN' },
                    { id: 2, judul_proyek: 'Lomba Gemastik Data Mining', kategori: 'LOMBA', deskripsi: 'Butuh anggota yang menguasai Python dan Machine Learning.', status: 'OPEN' }
                ]);
            } finally {
                setLoading(false);
            }
        };

        fetchProjects();
    }, []);

    const handleJoin = (projectId) => {
        // Nanti ini dihubungkan ke POST /api/applications
        alert(`Request Join untuk proyek ID: ${projectId} berhasil dikirim! Menunggu konfirmasi Ketua Tim.`);
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <Navbar />
            
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div className="flex justify-between items-center mb-6">
                    <h1 className="text-2xl font-bold text-gray-800">Cari Tim & Kolaborator</h1>
                    <button className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md font-medium shadow-sm transition">
                        + Buat Proyek Baru
                    </button>
                </div>

                {loading ? (
                    <p className="text-center text-gray-500">Memuat data proyek...</p>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {projects.map((project) => (
                            <div key={project.id} className="bg-white border rounded-xl shadow-sm hover:shadow-md transition p-5 flex flex-col justify-between">
                                <div>
                                    <span className="inline-block px-3 py-1 bg-blue-100 text-blue-800 text-xs font-semibold rounded-full mb-3">
                                        {project.kategori}
                                    </span>
                                    <h2 className="text-xl font-bold text-gray-900 mb-2">{project.judul_proyek}</h2>
                                    <p className="text-gray-600 text-sm mb-4 line-clamp-3">
                                        {project.deskripsi}
                                    </p>
                                </div>
                                
                                <button 
                                    onClick={() => handleJoin(project.id)}
                                    className="w-full mt-4 bg-green-500 hover:bg-green-600 text-white font-medium py-2 rounded-lg transition"
                                >
                                    Ajukan Join Tim
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </main>
        </div>
    );
}