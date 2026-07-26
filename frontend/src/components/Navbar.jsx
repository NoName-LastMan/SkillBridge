import { Link, useNavigate } from "react-router-dom";

export default function Navbar() {
  const navigate = useNavigate();

  // Fungsi untuk logout
  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <nav className="bg-white shadow-sm border-b border-gray-100 sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">
        
        {/* Logo / Tombol Home */}
        <Link to="/dashboard" className="text-2xl font-extrabold text-blue-600 tracking-tight hover:text-blue-700 transition">
          SkillBridge.
        </Link>

        {/* Menu Kanan */}
        <div className="flex items-center gap-6">
          
          {/* Ikon Notifikasi (Pesan Lamaran Masuk) */}
          <Link 
            to="/my-projects" 
            className="relative text-gray-500 hover:text-blue-600 transition flex items-center justify-center h-10 w-10 bg-gray-50 rounded-full hover:bg-blue-50"
            title="Lihat pesan lamaran masuk"
          >
            <span className="text-xl">🔔</span>
            {/* Titik/Angka Merah Indikator (Hardcoded untuk MVP, bisa diubah dinamis nanti) */}
            <span className="absolute top-1 right-1 bg-red-500 text-white text-[10px] font-bold px-1.5 py-0.5 rounded-full border-2 border-white">
              !
            </span>
          </Link>

          {/* Menu Navigasi Lainnya */}
          <Link to="/my-projects" className="text-gray-600 hover:text-blue-600 font-medium transition hidden sm:block">
            Proyek Saya
          </Link>

          <Link to="/profile" className="text-gray-600 hover:text-blue-600 font-medium transition hidden sm:block">
            Profil
          </Link>

          {/* Tombol Logout */}
          <button 
            onClick={handleLogout}
            className="ml-2 bg-red-50 text-red-600 hover:bg-red-100 hover:text-red-700 px-4 py-2 rounded-lg font-semibold transition text-sm"
          >
            Keluar
          </button>
        </div>
      </div>
    </nav>
  );
}