import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import api from "../services/api";
import { ArrowLeft, Save } from "lucide-react";

export default function EditProjectView() {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState({
    title: "",
    category: "PKM",
    maxMembers: 3,
    requiredSkills: "",
    description: "",
    status: "OPEN"
  });
  
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    // Tarik data proyek yang lama untuk diisi ke dalam form
    const fetchProject = async () => {
      try {
        const response = await api.get(`/projects/${id}`);
        setFormData({
          title: response.data.title || "",
          category: response.data.category || "PKM",
          maxMembers: response.data.maxMembers || 3,
          requiredSkills: response.data.requiredSkills || "",
          description: response.data.description || "",
          status: response.data.status || "OPEN"
        });
      } catch (error) {
        alert("Gagal memuat data proyek.");
        navigate("/my-projects");
      } finally {
        setLoading(false);
      }
    };
    fetchProject();
  }, [id, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      // Endpoint PUT untuk menyimpan perubahan
      await api.put(`/projects/${id}`, formData);
      alert("Proyek berhasil diperbarui! 🎉");
      navigate("/my-projects");
    } catch (error) {
      alert("Gagal menyimpan perubahan.");
    } finally {
      setSaving(false);
    }
  };

  const cardStyle = {
    background: "#fff", border: "1px solid rgba(91,33,182,0.1)", boxShadow: "0 4px 20px rgba(91,33,182,0.05)"
  };

  if (loading) return <div className="min-h-screen bg-[#f7f5ff] pt-20 text-center text-gray-500">Memuat formulir...</div>;

  return (
    <div className="min-h-screen flex flex-col" style={{ background: "#f7f5ff", fontFamily: "'Plus Jakarta Sans', sans-serif" }}>
      <Navbar />
      <main className="max-w-2xl mx-auto px-4 py-8 w-full">
        <button onClick={() => navigate(-1)} className="flex items-center gap-2 text-sm font-bold transition hover:opacity-70 mb-6" style={{ color: "#5b21b6" }}>
          <ArrowLeft className="w-4 h-4" /> Kembali
        </button>

        <div className="rounded-2xl p-6 sm:p-8" style={cardStyle}>
          <h1 className="text-2xl font-bold text-gray-900 mb-6 border-b pb-4" style={{ borderColor: "rgba(91,33,182,0.1)" }}>
            Edit Proyek
          </h1>

          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="block text-sm font-bold text-gray-700 mb-1.5">Judul Proyek</label>
              <input type="text" required value={formData.title} onChange={e => setFormData({...formData, title: e.target.value})}
                className="w-full px-4 py-2.5 rounded-xl border focus:ring-2 outline-none transition bg-gray-50 focus:bg-white"
                style={{ borderColor: "rgba(91,33,182,0.2)" }} />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-bold text-gray-700 mb-1.5">Kategori</label>
                <select value={formData.category} onChange={e => setFormData({...formData, category: e.target.value})}
                  className="w-full px-4 py-2.5 rounded-xl border focus:ring-2 outline-none transition bg-gray-50 focus:bg-white" style={{ borderColor: "rgba(91,33,182,0.2)" }}>
                  <option value="PKM">PKM</option>
                  <option value="LOMBA">Lomba</option>
                  <option value="STARTUP">Startup</option>
                  <option value="PENELITIAN">Penelitian</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-bold text-gray-700 mb-1.5">Status Rekrutmen</label>
                <select value={formData.status} onChange={e => setFormData({...formData, status: e.target.value})}
                  className="w-full px-4 py-2.5 rounded-xl border focus:ring-2 outline-none transition bg-gray-50 focus:bg-white" style={{ borderColor: "rgba(91,33,182,0.2)" }}>
                  <option value="OPEN">Buka (OPEN)</option>
                  <option value="CLOSED">Tutup (CLOSED)</option>
                </select>
              </div>
            </div>

            <div>
              <label className="block text-sm font-bold text-gray-700 mb-1.5">Total Kebutuhan Anggota</label>
              <input type="number" min="1" required value={formData.maxMembers} onChange={e => setFormData({...formData, maxMembers: parseInt(e.target.value)})}
                className="w-full px-4 py-2.5 rounded-xl border focus:ring-2 outline-none transition bg-gray-50 focus:bg-white" style={{ borderColor: "rgba(91,33,182,0.2)" }} />
            </div>

            <div>
              <label className="block text-sm font-bold text-gray-700 mb-1.5">Keahlian yang Dicari</label>
              <input type="text" placeholder="Contoh: UI/UX, Python, Copywriting" value={formData.requiredSkills} onChange={e => setFormData({...formData, requiredSkills: e.target.value})}
                className="w-full px-4 py-2.5 rounded-xl border focus:ring-2 outline-none transition bg-gray-50 focus:bg-white" style={{ borderColor: "rgba(91,33,182,0.2)" }} />
            </div>

            <div>
              <label className="block text-sm font-bold text-gray-700 mb-1.5">Deskripsi Lengkap</label>
              <textarea rows="5" required value={formData.description} onChange={e => setFormData({...formData, description: e.target.value})}
                className="w-full px-4 py-2.5 rounded-xl border focus:ring-2 outline-none transition bg-gray-50 focus:bg-white" style={{ borderColor: "rgba(91,33,182,0.2)" }} />
            </div>

            <div className="pt-4 flex justify-end">
              <button type="submit" disabled={saving} className="flex items-center gap-2 px-6 py-3 rounded-xl font-bold text-white transition hover:opacity-90 disabled:opacity-50" style={{ background: "#5b21b6" }}>
                <Save className="w-4 h-4" /> {saving ? "Menyimpan..." : "Simpan Perubahan"}
              </button>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
}