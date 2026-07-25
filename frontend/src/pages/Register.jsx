import { useState } from "react";
import { Link } from "react-router-dom";

export default function Register() {
  const [form, setForm] = useState({
    nama: "",
    nim: "",
    prodi: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (form.password !== form.confirmPassword) {
      alert("Password tidak sama!");
      return;
    }

    alert("Register berhasil (sementara)");
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        background: "#f4f4f4",
      }}
    >
      <div
        style={{
          width: "400px",
          background: "white",
          padding: "30px",
          borderRadius: "10px",
          boxShadow: "0 0 10px rgba(0,0,0,0.2)",
        }}
      >
        <h2 style={{ textAlign: "center" }}>
          Register SkillBridge
        </h2>

        <form onSubmit={handleSubmit}>

          <input
            name="nama"
            placeholder="Nama Lengkap"
            value={form.nama}
            onChange={handleChange}
            style={input}
          />

          <input
            name="nim"
            placeholder="NIM"
            value={form.nim}
            onChange={handleChange}
            style={input}
          />

          <input
            name="prodi"
            placeholder="Program Studi"
            value={form.prodi}
            onChange={handleChange}
            style={input}
          />

          <input
            name="email"
            placeholder="Email"
            type="email"
            value={form.email}
            onChange={handleChange}
            style={input}
          />

          <input
            name="password"
            placeholder="Password"
            type="password"
            value={form.password}
            onChange={handleChange}
            style={input}
          />

          <input
            name="confirmPassword"
            placeholder="Konfirmasi Password"
            type="password"
            value={form.confirmPassword}
            onChange={handleChange}
            style={input}
          />

          <button style={button}>
            Register
          </button>

        </form>

        <p style={{ textAlign: "center", marginTop: "15px" }}>
          Sudah punya akun?
          <Link to="/login"> Login</Link>
        </p>

      </div>
    </div>
  );
}

const input = {
  width: "100%",
  padding: "12px",
  marginTop: "10px",
  marginBottom: "10px",
  boxSizing: "border-box",
};

const button = {
  width: "100%",
  padding: "12px",
  background: "#2563eb",
  color: "white",
  border: "none",
  cursor: "pointer",
};