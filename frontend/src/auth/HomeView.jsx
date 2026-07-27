import { Link } from 'react-router-dom';
import { Users, Search, MessageSquare, Star, Zap, BookOpen, Trophy, ArrowRight, ChevronDown } from "lucide-react";

const problems = [
  "Sulit mencari anggota tim PKM yang tepat",
  "Tidak tahu mahasiswa mana yang punya skill relevan",
  "Susah nyari teman belajar yang serius",
  "Skill kamu tidak dikenal orang lain",
  "Sulit cari mentor dari kakak tingkat",
  "Kolaborasi lintas prodi sangat terbatas",
];

const benefits = [
  {
    icon: Search,
    title: "Temukan Kolaborator",
    desc: "Cari mahasiswa berdasarkan skill, prodi, dan minat. Sistem matching otomatis mempertemukanmu dengan orang yang tepat.",
    color: "#5b21b6",
    bg: "#ede9fe",
  },
  {
    icon: Users,
    title: "Bangun Tim PKM",
    desc: "Rekrut anggota tim PKM, lomba, atau project startup dengan mudah. Buka rekrutmen dan terima lamaran langsung.",
    color: "#0284c7",
    bg: "#e0f2fe",
  },
  {
    icon: BookOpen,
    title: "Cari Teman Belajar",
    desc: "Temukan study partner yang serius dan punya tujuan sama. Belajar bareng jadi lebih produktif.",
    color: "#059669",
    bg: "#d1fae5",
  },
  {
    icon: Star,
    title: "Tampilkan Skillmu",
    desc: "Buat profil skill yang lengkap agar mahasiswa lain bisa menemukanmu. Berhenti jadi talenta tersembunyi.",
    color: "#d97706",
    bg: "#fef3c7",
  },
  {
    icon: MessageSquare,
    title: "Chat Tim Real-time",
    desc: "Koordinasi tim langsung di platform. Tidak perlu pindah-pindah aplikasi lagi.",
    color: "#dc2626",
    bg: "#fee2e2",
  },
  {
    icon: Trophy,
    title: "Riwayat Kolaborasi",
    desc: "Rekam jejak kolaborasimu sebagai portofolio. Buktikan pengalamanmu kepada calon tim berikutnya.",
    color: "#7c3aed",
    bg: "#f3e8ff",
  },
];

export default function HomeView() {
  return (
    <div
      className="min-h-screen overflow-x-hidden bg-slate-50"
      style={{ fontFamily: "'Plus Jakarta Sans', sans-serif" }}
    >
      {/* Navbar Gabungan (Logo custom & Tombol Router) */}
      <nav className="fixed top-0 left-0 right-0 z-50 flex items-center justify-between px-6 py-4"
        style={{ background: "rgba(247,245,255,0.85)", backdropFilter: "blur(12px)", borderBottom: "1px solid rgba(91,33,182,0.1)" }}>
        <div className="flex items-center gap-3">
          <img 
            src="/logo.png" 
            alt="SkillBridge Logo" 
            className="h-9 w-9 object-cover rounded-lg shadow-sm" 
          />
          <span style={{ fontFamily: "'JetBrains Mono', monospace", fontWeight: 700, color: "#5b21b6", fontSize: "1.2rem" }}>
            SkillBridge.
          </span>
        </div>
        <div className="flex items-center gap-4">
          <Link to="/login" className="hidden sm:block text-sm font-bold transition hover:text-blue-700" style={{ color: "#6b7280" }}>
            Masuk
          </Link>
          <Link
            to="/register"
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg text-sm transition-all hover:opacity-90 active:scale-95 shadow-sm"
            style={{ background: "#5b21b6", color: "#fff", fontWeight: 600 }}
          >
            Daftar <ArrowRight className="w-4 h-4 hidden sm:block" />
          </Link>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="relative min-h-screen flex flex-col items-center justify-center px-6 pt-24 pb-16 overflow-hidden">
        {/* Dekorasi Geometris dari Temanmu */}
        <div className="absolute top-20 left-8 w-24 h-24 rounded-full opacity-20 pointer-events-none" style={{ background: "#5b21b6" }} />
        <div className="absolute top-40 right-12 w-16 h-16 rounded-lg rotate-12 opacity-15 pointer-events-none" style={{ background: "#f59e0b" }} />
        <div className="absolute bottom-32 left-16 w-20 h-20 rounded-full opacity-10 pointer-events-none" style={{ background: "#0284c7" }} />
        <div className="absolute bottom-16 right-8 w-32 h-32 rotate-45 opacity-10 pointer-events-none" style={{ background: "#059669", borderRadius: "8px" }} />
        <div className="absolute top-64 left-1/4 w-6 h-6 rounded-full opacity-40 pointer-events-none" style={{ background: "#f59e0b" }} />
        <div className="absolute top-1/3 right-1/4 w-8 h-8 rotate-45 opacity-30 pointer-events-none" style={{ background: "#dc2626", borderRadius: "2px" }} />

        <div className="relative max-w-4xl mx-auto text-center space-y-8">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full text-sm shadow-sm"
            style={{ background: "#ede9fe", color: "#5b21b6", border: "1px solid #c4b5fd", fontWeight: 600 }}>
            <span>⚡</span> Platform Kolaborasi Mahasiswa UNIMUS
          </div>

          <div>
            <h1 className="leading-tight tracking-tight" style={{ fontSize: "clamp(2.5rem, 7vw, 5rem)", fontWeight: 800, color: "#0f0e1a", lineHeight: 1.1 }}>
              Hubungkan{" "}
              <span style={{ color: "#5b21b6", position: "relative", display: "inline-block" }}>
                Skill
                <span className="absolute -bottom-1 left-0 right-0 h-1 rounded-full" style={{ background: "#f59e0b" }} />
              </span>{" "}
              Kamu<br />
              dengan{" "}
              <span style={{ color: "#0284c7" }}>Peluang</span>{" "}
              Nyata
            </h1>
          </div>

          <p className="max-w-2xl mx-auto leading-relaxed" style={{ fontSize: "1.1rem", color: "#4b5563" }}>
            SkillBridge mempertemukan mahasiswa UNIMUS lintas prodi untuk PKM, lomba, belajar bersama, dan mentoring. Satu platform, ribuan kolaborasi.
          </p>

          <div className="flex flex-wrap justify-center gap-8 py-4">
            {[["500+", "Mahasiswa Aktif"], ["120+", "Tim PKM Terbentuk"], ["15+", "Prodi Terdaftar"]].map(([num, label]) => (
              <div key={label} className="text-center">
                <div style={{ fontSize: "2rem", fontWeight: 800, color: "#5b21b6" }}>{num}</div>
                <div style={{ fontSize: "0.85rem", color: "#6b7280" }}>{label}</div>
              </div>
            ))}
          </div>

          <div className="flex flex-wrap justify-center gap-4">
            <Link
              to="/register"
              className="flex items-center gap-2 px-8 py-4 rounded-xl transition-all hover:opacity-90 active:scale-95 shadow-lg"
              style={{ background: "#5b21b6", color: "#fff", fontWeight: 700, fontSize: "1rem" }}
            >
              Mulai Kolaborasi <ArrowRight className="w-4 h-4" />
            </Link>
            <a
              href="#demo-section"
              className="flex items-center gap-2 px-8 py-4 rounded-xl transition-all hover:bg-gray-50 active:scale-95"
              style={{ background: "#fff", color: "#5b21b6", fontWeight: 700, fontSize: "1rem", border: "2px solid #5b21b6" }}
            >
              Lihat Cara Kerja
            </a>
          </div>
        </div>

        <div className="absolute bottom-8 left-1/2 -translate-x-1/2 flex flex-col items-center gap-1 opacity-50 animate-bounce">
          <span style={{ fontSize: "0.75rem", color: "#6b7280", fontWeight: 'bold' }}>Scroll</span>
          <ChevronDown className="w-4 h-4" style={{ color: "#6b7280" }} />
        </div>
      </section>

      {/* Problem Section */}
      <section className="py-24 px-6" style={{ background: "#0f0e1a" }}>
        <div className="max-w-5xl mx-auto">
          <div className="text-center mb-16">
            <div className="inline-block px-3 py-1 rounded-md mb-4"
              style={{ background: "#dc2626", color: "#fff", fontFamily: "'JetBrains Mono', monospace", fontSize: "0.8rem", fontWeight: 600 }}>
              MASALAH NYATA
            </div>
            <h2 style={{ fontSize: "clamp(1.8rem, 4vw, 2.5rem)", fontWeight: 800, color: "#f9fafb", lineHeight: 1.2 }}>
              Mahasiswa Sering Mengalami Ini
            </h2>
          </div>

          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {problems.map((p, i) => (
              <div key={i} className="flex items-start gap-3 p-5 rounded-xl hover:bg-white/10 transition"
                style={{ background: "rgba(255,255,255,0.05)", border: "1px solid rgba(255,255,255,0.08)" }}>
                <span className="mt-0.5 flex-shrink-0 w-6 h-6 rounded-full flex items-center justify-center text-sm"
                  style={{ background: "#dc2626", color: "#fff" }}>✕</span>
                <span style={{ color: "#d1d5db", fontSize: "0.95rem", lineHeight: 1.5 }}>{p}</span>
              </div>
            ))}
          </div>

          <div className="mt-12 text-center p-8 rounded-2xl" style={{ background: "rgba(91,33,182,0.2)", border: "1px solid rgba(124,58,237,0.3)" }}>
            <p style={{ color: "#c4b5fd", fontSize: "1.1rem" }}>
              <strong style={{ color: "#a78bfa" }}>SkillBridge</strong> hadir untuk menyelesaikan semua masalah ini dengan satu platform terpadu.
            </p>
          </div>
        </div>
      </section>

      {/* Benefits Section */}
      <section id="demo-section" className="py-24 px-6" style={{ background: "#f7f5ff" }}>
        <div className="max-w-6xl mx-auto">
          <div className="text-center mb-16">
            <div className="inline-block px-3 py-1 rounded-md mb-4"
              style={{ background: "#5b21b6", color: "#fff", fontFamily: "'JetBrains Mono', monospace", fontSize: "0.8rem", fontWeight: 600 }}>
              FITUR & MANFAAT
            </div>
            <h2 style={{ fontSize: "clamp(1.8rem, 4vw, 2.5rem)", fontWeight: 800, color: "#0f0e1a", lineHeight: 1.2 }}>
              Semua yang Kamu Butuhkan<br />
              <span style={{ color: "#5b21b6" }}>Ada di Sini</span>
            </h2>
          </div>

          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {benefits.map((b, i) => {
              const Icon = b.icon;
              return (
                <div key={i} className="p-8 rounded-2xl transition-all hover:-translate-y-2 hover:shadow-lg"
                  style={{ background: "#fff", border: "1px solid rgba(91,33,182,0.1)", boxShadow: "0 4px 20px rgba(91,33,182,0.05)" }}>
                  <div className="w-14 h-14 rounded-xl flex items-center justify-center mb-6"
                    style={{ background: b.bg }}>
                    <Icon className="w-7 h-7" style={{ color: b.color }} />
                  </div>
                  <h3 style={{ fontWeight: 800, color: "#0f0e1a", marginBottom: "0.75rem", fontSize: "1.2rem" }}>{b.title}</h3>
                  <p style={{ color: "#6b7280", fontSize: "0.95rem", lineHeight: 1.6 }}>{b.desc}</p>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* CTA Bottom Section */}
      <section className="py-24 px-6" style={{ background: "#5b21b6", position: "relative", overflow: "hidden" }}>
        <div className="absolute top-0 right-0 w-64 h-64 rounded-full opacity-10 pointer-events-none"
          style={{ background: "#f59e0b", transform: "translate(30%, -30%)" }} />
        <div className="absolute bottom-0 left-0 w-48 h-48 rounded-full opacity-10 pointer-events-none"
          style={{ background: "#0284c7", transform: "translate(-30%, 30%)" }} />

        <div className="relative max-w-3xl mx-auto text-center space-y-8">
          <h2 style={{ fontSize: "clamp(2rem, 5vw, 3rem)", fontWeight: 800, color: "#fff", lineHeight: 1.2 }}>
            Siap Mulai Kolaborasi?
          </h2>
          <p style={{ color: "#c4b5fd", fontSize: "1.2rem", maxWidth: "600px", margin: "0 auto" }}>
            Buat profil skillmu sekarang dan mulai temukan kolaborator yang tepat di kampusmu.
          </p>
          <Link
            to="/register"
            className="inline-flex items-center gap-2 px-10 py-5 rounded-2xl transition-all hover:scale-105 shadow-xl"
            style={{ background: "#f59e0b", color: "#0f0e1a", fontWeight: 800, fontSize: "1.1rem" }}
          >
            Buat Akun Sekarang <ArrowRight className="w-5 h-5" />
          </Link>
          <p style={{ color: "#a78bfa", fontSize: "0.9rem", fontWeight: "bold" }}>Gratis • Khusus Mahasiswa</p>
        </div>
      </section>

      {/* Footer */}
      <footer className="py-10 px-6 text-center" style={{ background: "#0f0e1a", borderTop: "1px solid rgba(255,255,255,0.05)" }}>
        <div className="flex items-center justify-center gap-2 mb-4">
          <img src="/logo.png" alt="Logo Footer" className="w-6 h-6 rounded object-cover grayscale opacity-70" />
          <span style={{ fontFamily: "'JetBrains Mono', monospace", fontWeight: 700, color: "#a78bfa", fontSize: "1.1rem" }}>SkillBridge</span>
        </div>
        <p style={{ color: "#4b5563", fontSize: "0.9rem" }}>
          © 2026 SkillBridge — Platform Kolaborasi Mahasiswa.
        </p>
      </footer>
    </div>
  );
}