import Navbar from "../components/Navbar";

export default function CreateProjectView() {
  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <div className="max-w-3xl mx-auto p-8">
        <h1 className="text-3xl font-bold mb-6">
          Buat Proyek Baru
        </h1>

        <form className="bg-white rounded-xl shadow-md p-6 space-y-4">

          <div>
            <label className="block mb-2 font-semibold">
              Judul Proyek
            </label>
            <input
              type="text"
              className="w-full border rounded-lg p-3"
              placeholder="Contoh: PKM AI Kesehatan"
            />
          </div>

          <div>
            <label className="block mb-2 font-semibold">
              Kategori
            </label>

            <select className="w-full border rounded-lg p-3">
              <option>PKM</option>
              <option>LOMBA</option>
              <option>RISET</option>
            </select>
          </div>

          <div>
            <label className="block mb-2 font-semibold">
              Deskripsi
            </label>

            <textarea
              rows="5"
              className="w-full border rounded-lg p-3"
            />
          </div>

          <button
            className="bg-blue-600 text-white px-6 py-3 rounded-lg"
          >
            Simpan Proyek
          </button>

        </form>
      </div>
    </div>
  );
}