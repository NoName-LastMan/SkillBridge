import { Link } from "react-router-dom";

export default function Dashboard() {
  return (
    <div style={{ padding: "40px" }}>
      <h1>Dashboard SkillBridge</h1>

      <p>Selamat datang di aplikasi SkillBridge.</p>

      <div style={{ marginTop: "20px" }}>
        <Link to="/projects">
          <button>Lihat Project</button>
        </Link>

        <Link to="/profile">
          <button style={{ marginLeft: "10px" }}>
            Profil Saya
          </button>
        </Link>
      </div>
    </div>
  );
}