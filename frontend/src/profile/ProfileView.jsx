import { useEffect, useState } from "react";
import {
  getMyProfile,
  updateProfile,
} from "../services/profileService";

import PrivacyToggle from "./PrivacyToggle";

export default function ProfileView() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    try {
      const data = await getMyProfile();
      setProfile(data);
    } catch (error) {
      console.error(error);
      alert("Gagal mengambil data profil");
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    try {
      await updateProfile(profile);
      alert("Profil berhasil disimpan");
    } catch (error) {
      console.error(error);
      alert("Gagal menyimpan profil");
    }
  };

  if (loading) return <h2>Loading...</h2>;

  if (!profile) return <h2>Profil tidak ditemukan</h2>;

  return (
    <div style={{ maxWidth: "700px", margin: "40px auto" }}>
      <h1>Profil Saya</h1>

      <div style={{ marginBottom: "20px" }}>
        <img
          src={
            profile.fotoUrl ||
            "https://via.placeholder.com/150"
          }
          alt="Foto Profil"
          width="150"
          style={{
            borderRadius: "50%",
            objectFit: "cover",
          }}
        />
      </div>

      <input
        type="text"
        placeholder="URL Foto"
        value={profile.fotoUrl || ""}
        onChange={(e) =>
          setProfile({
            ...profile,
            fotoUrl: e.target.value,
          })
        }
      />

      <br /><br />

      <input
        type="text"
        placeholder="Nama Lengkap"
        value={profile.namaLengkap || ""}
        onChange={(e) =>
          setProfile({
            ...profile,
            namaLengkap: e.target.value,
          })
        }
      />

      <br /><br />

      <input
        type="text"
        placeholder="NIM"
        value={profile.nim || ""}
        onChange={(e) =>
          setProfile({
            ...profile,
            nim: e.target.value,
          })
        }
      />

      <br /><br />

      <input
        type="text"
        placeholder="Program Studi"
        value={profile.prodi || ""}
        onChange={(e) =>
          setProfile({
            ...profile,
            prodi: e.target.value,
          })
        }
      />

      <br /><br />

      <input
        type="text"
        placeholder="Angkatan"
        value={profile.angkatan || ""}
        onChange={(e) =>
          setProfile({
            ...profile,
            angkatan: e.target.value,
          })
        }
      />

      <br /><br />

      <textarea
        rows="5"
        placeholder="Bio"
        value={profile.bio || ""}
        onChange={(e) =>
          setProfile({
            ...profile,
            bio: e.target.value,
          })
        }
      />

      <br /><br />

      <input
        type="text"
        placeholder="WhatsApp"
        value={profile.whatsapp || ""}
        onChange={(e) =>
          setProfile({
            ...profile,
            whatsapp: e.target.value,
          })
        }
      />

      <br /><br />

      <input
        type="text"
        placeholder="Instagram"
        value={profile.instagram || ""}
        onChange={(e) =>
          setProfile({
            ...profile,
            instagram: e.target.value,
          })
        }
      />

      <br /><br />

      <input
  type="text"
  placeholder="LinkedIn"
  value={profile.linkedin || ""}
  onChange={(e) =>
    setProfile({
      ...profile,
      linkedin: e.target.value,
    })
  }
/>

<br /><br />

<PrivacyToggle
  privacy={profile.contactPrivacy}
  onChange={(value) =>
    setProfile({
      ...profile,
      contactPrivacy: value,
    })
  }
/>

<br /><br />

<button onClick={handleSave}>
  Simpan Profil
</button>
    </div>
  );
}