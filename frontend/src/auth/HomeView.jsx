import { Link } from 'react-router-dom';

export default function HomeView() {
  return (
    <div className="min-h-screen bg-slate-50 font-sans text-gray-800 flex flex-col">
      
      {/* NAVBAR LANDING PAGE */}
      <nav className="bg-white shadow-sm py-4 px-6 md:px-12 flex justify-between items-center sticky top-0 z-50">
        <div className="flex items-center gap-3">
          {/* PLACEHOLDER LOGO PNG - Nanti URL src ini kamu ganti dengan lokasi logomu */}
          <img 
            src="/logo.png" 
            alt="SkillBridge Logo" 
            className="h-10 w-10 object-cover rounded-xl shadow-sm border border-gray-100" 
          />
          <span className="text-2xl font-extrabold text-blue-600 tracking-tight">SkillBridge.</span>
        </div>
        <div className="flex gap-2 sm:gap-4 items-center">
          <Link to="/login" className="text-gray-600 hover:text-blue-600 font-bold px-3 py-2 transition text-sm sm:text-base">
            Masuk
          </Link>
          <Link to="/register" className="bg-blue-600 hover:bg-blue-700 text-white px-4 sm:px-6 py-2 sm:py-2.5 rounded-lg font-bold transition shadow-sm text-sm sm:text-base">
            Daftar <span className="hidden sm:inline">Sekarang</span>
          </Link>
        </div>
      </nav>

      {/* HERO SECTION (Bagian Utama) */}
      <main className="flex-grow flex flex-col items-center justify-center px-6 py-16 text-center">
        <div className="max-w-4xl mx-auto space-y-8 animate-fade-in-up">
          
          <div className="inline-block bg-blue-100 text-blue-800 font-bold px-4 py-1.5 rounded-full text-sm mb-4 shadow-sm border border-blue-200">
            🚀 MVP Release v1.0
          </div>
          
          <h1 className="text-5xl md:text-6xl font-extrabold text-gray-900 tracking-tight leading-tight">
            Wujudkan Ide Besarmu, <br className="hidden md:block"/>
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-cyan-500">
              Temukan Tim Hebatmu.
            </span>
          </h1>
          
          <p className="text-lg md:text-xl text-gray-500 max-w-2xl mx-auto leading-relaxed">
            Platform kolaborasi khusus mahasiswa. Cari rekan proyek, temukan lowongan tim, bangun portofolio, dan ciptakan karya luar biasa bersama-sama.
          </p>
          
          <div className="flex flex-col sm:flex-row gap-4 justify-center pt-6">
            <Link to="/register" className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-4 rounded-xl font-bold text-lg transition shadow-lg hover:shadow-xl transform hover:-translate-y-1">
              Mulai Kolaborasi Sekarang
            </Link>
            <Link to="/login" className="bg-white hover:bg-gray-50 text-gray-700 border-2 border-gray-200 px-8 py-4 rounded-xl font-bold text-lg transition shadow-sm">
              Eksplor Proyek
            </Link>
          </div>
        </div>

        {/* FITUR SECTION (3 Kolom) */}
        <div className="max-w-6xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8 mt-24 px-4">
          <div className="bg-white p-8 rounded-2xl shadow-sm border border-gray-100 hover:shadow-md transition">
            <div className="text-4xl mb-4">🔍</div>
            <h3 className="text-xl font-bold text-gray-800 mb-2">Cari Proyek Sesuai Skill</h3>
            <p className="text-gray-500 text-sm leading-relaxed">
              Temukan proyek PKM, Lomba, atau Startup yang sedang mencari anggota dengan keahlian yang kamu miliki.
            </p>
          </div>
          <div className="bg-white p-8 rounded-2xl shadow-sm border border-gray-100 hover:shadow-md transition">
            <div className="text-4xl mb-4">🤝</div>
            <h3 className="text-xl font-bold text-gray-800 mb-2">Rekrut Talenta Kampus</h3>
            <p className="text-gray-500 text-sm leading-relaxed">
              Punya ide cemerlang tapi kurang tenaga? Buat proyekmu dan rekrut mahasiswa berbakat dari berbagai prodi.
            </p>
          </div>
          <div className="bg-white p-8 rounded-2xl shadow-sm border border-gray-100 hover:shadow-md transition">
            <div className="text-4xl mb-4">📈</div>
            <h3 className="text-xl font-bold text-gray-800 mb-2">Bangun Portofolio Nyata</h3>
            <p className="text-gray-500 text-sm leading-relaxed">
              Setiap proyek yang berhasil kamu selesaikan akan menjadi jejak rekam portofolio profesional sebelum lulus.
            </p>
          </div>
        </div>
      </main>

      {/* FOOTER */}
      <footer className="bg-white border-t border-gray-200 py-8 text-center mt-12">
        <p className="text-gray-400 text-sm font-medium">
          © 2026 SkillBridge Platform. Dibuat untuk Presentasi MVP.
        </p>
      </footer>

    </div>
  );
}