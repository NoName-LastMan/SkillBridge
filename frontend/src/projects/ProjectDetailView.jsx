import { useParams } from "react-router-dom";
import Navbar from "../components/Navbar";

export default function ProjectDetailView() {
  const { id } = useParams();

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <div className="max-w-4xl mx-auto p-8">
        <h1 className="text-3xl font-bold mb-6">
          Detail Proyek
        </h1>

        <div className="bg-white rounded-xl shadow-md p-6">
          <p><b>ID:</b> {id}</p>
          <p><b>Judul:</b> Contoh Proyek</p>
          <p><b>Kategori:</b> PKM</p>
          <p className="mt-4">
            Halaman detail proyek sementara untuk presentasi.
          </p>
        </div>
      </div>
    </div>
  );
}