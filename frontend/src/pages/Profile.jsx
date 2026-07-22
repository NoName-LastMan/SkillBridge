import { useEffect, useState } from 'react';
import { getMyProfile, updateProfile } from '../services/profileService';

export default function Profile() {
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
} finally {
setLoading(false);
}
};

const handleSave = async () => {
try {
await updateProfile(profile);
alert('Profil berhasil disimpan');
} catch (error) {
alert('Gagal menyimpan profil');
}
};

if (loading) return <div>Loading...</div>;
if (!profile) return <div>Profil tidak ditemukan</div>;

return ( <div className='max-w-2xl mx-auto p-6'> <h1 className='text-3xl font-bold mb-6'>Profil Saya</h1>

```
  <div className='space-y-4'>
    <input
      className='w-full border p-3 rounded'
      value={profile.namaLengkap || ''}
      onChange={(e) =>
        setProfile({ ...profile, namaLengkap: e.target.value })
      }
      placeholder='Nama Lengkap'
    />

    <input
      className='w-full border p-3 rounded'
      value={profile.nim || ''}
      onChange={(e) => setProfile({ ...profile, nim: e.target.value })}
      placeholder='NIM'
    />

    <button
      onClick={handleSave}
      className='bg-blue-600 text-white px-6 py-3 rounded hover:bg-blue-700'
    >
      Simpan Profil
    </button>
  </div>
</div>

);
}
