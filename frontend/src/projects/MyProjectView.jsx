import Navbar from "../components/Navbar";

export default function MyProjectView() {
  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <div className="max-w-5xl mx-auto p-8">
        <h1 className="text-3xl font-bold mb-6">
          Proyek Saya
        </h1>

        <div className="bg-white rounded-xl shadow-md p-6">
          Belum ada proyek yang dibuat.
        </div>
      </div>
    </div>
  );
}