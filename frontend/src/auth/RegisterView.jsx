import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function RegisterView() {
    const [formData, setFormData] = useState({
        // Data Wajib (Autentikasi)
        email: '',
        password: '',
        confirmPassword: '',
        role: 'MAHASISWA',
        
        // Data Profil Tambahan
        namaLengkap: '',
        nim: '',
        prodi: ''
    });
    
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        setError('');

        if (formData.password !== formData.confirmPassword) {
            setError('Password dan Konfirmasi Password tidak cocok!');
            return;
        }

        setLoading(true);

        try {
            // LANGKAH 1: Daftarkan Akun (Hanya data Auth)
            await api.post('/auth/register', {
                email: formData.email,
                password: formData.password,
                role: formData.role
            });
            
            // LANGKAH 2: Langsung Login untuk mendapatkan Token
            const loginResponse = await api.post('/auth/login', {
                email: formData.email,
                password: formData.password
            });
            
            const token = loginResponse.data.token;
            localStorage.setItem('token', token); // Simpan token agar axios interceptor bisa memakainya
            
            // LANGKAH 3: Jika user mengisi nama, otomatis update profilnya!
            if (formData.namaLengkap.trim() !== '') {
                try {
                    // Endpoint untuk update profil (membutuhkan Auth)
                    await api.put('/profile/me', {
                        namaLengkap: formData.namaLengkap,
                        nim: formData.nim,
                        prodi: formData.prodi
                    }, {
                        // Memastikan token terkirim khusus untuk request ini
                        headers: { Authorization: `Bearer ${token}` }
                    });
                } catch (profileErr) {
                    console.warn("Registrasi berhasil, tapi gagal menyimpan data profil awal:", profileErr);
                    // Kita biarkan saja, karena akun utamanya sudah berhasil dibuat
                }
            }
            
            alert('Registrasi berhasil! Selamat datang di tim.');
            navigate('/dashboard');
            
        } catch (err) {
            const errorMsg = err.response?.data?.message || 'Pendaftaran gagal. Periksa kembali datamu atau pastikan email belum terdaftar.';
            setError(errorMsg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100 py-10">
            <div className="w-full max-w-lg p-8 space-y-6 bg-white rounded-xl shadow-md">
                <div>
                    <h2 className="text-2xl font-bold text-center text-blue-600">Daftar SkillBridge</h2>
                    <p className="text-sm text-center text-gray-500 mt-1">Platform Kolaborasi Mahasiswa</p>
                </div>
                
                {error && (
                    <div className="p-3 text-sm text-red-700 bg-red-100 rounded-md text-center border border-red-200">
                        {error}
                    </div>
                )}
                
                <form onSubmit={handleRegister} className="space-y-6">
                    
                    {/* --- BAGIAN 1: INFORMASI AKUN (WAJIB) --- */}
                    <div className="space-y-4">
                        <h3 className="text-sm font-bold text-gray-400 uppercase tracking-wider border-b pb-2">1. Informasi Akun</h3>
                        
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Email Kampus <span className="text-red-500">*</span></label>
                            <input 
                                name="email" 
                                type="email" 
                                required 
                                value={formData.email}
                                onChange={handleChange}
                                className="w-full px-3 py-2 mt-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" 
                                placeholder="nama@student.unimus.ac.id" 
                            />
                        </div>
                        
                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Password <span className="text-red-500">*</span></label>
                                <input 
                                    name="password" 
                                    type="password" 
                                    required 
                                    value={formData.password}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 mt-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" 
                                    placeholder="Minimal 6 karakter" 
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Ulangi Password <span className="text-red-500">*</span></label>
                                <input 
                                    name="confirmPassword" 
                                    type="password" 
                                    required 
                                    value={formData.confirmPassword}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 mt-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" 
                                    placeholder="Ulangi password" 
                                />
                            </div>
                        </div>
                    </div>

                    {/* --- BAGIAN 2: INFORMASI PROFIL (OPSIONAL/BISA DI-SKIP) --- */}
                    <div className="space-y-4 pt-2">
                        <div className="flex justify-between items-center border-b pb-2">
                            <h3 className="text-sm font-bold text-gray-400 uppercase tracking-wider">2. Data Diri</h3>
                            <span className="text-xs text-blue-500 bg-blue-50 px-2 py-1 rounded">-----</span>
                        </div>
                        
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Nama Lengkap</label>
                            <input 
                                name="namaLengkap" 
                                type="text" 
                                value={formData.namaLengkap}
                                onChange={handleChange}
                                className="w-full px-3 py-2 mt-1 border border-gray-200 bg-gray-50 rounded-md focus:bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 transition" 
                                placeholder="Contoh: Budi Santoso" 
                            />
                        </div>

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700">NIM</label>
                                <input 
                                    name="nim" 
                                    type="text" 
                                    value={formData.nim}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 mt-1 border border-gray-200 bg-gray-50 rounded-md focus:bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 transition" 
                                    placeholder="Nomor Induk Mahasiswa" 
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Program Studi</label>
                                <input 
                                    name="prodi" 
                                    type="text" 
                                    value={formData.prodi}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 mt-1 border border-gray-200 bg-gray-50 rounded-md focus:bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 transition" 
                                    placeholder="Contoh: Teknik Informatika" 
                                />
                            </div>
                        </div>
                    </div>
                    
                    <button 
                        type="submit" 
                        disabled={loading}
                        className={`w-full px-4 py-3 text-white rounded-lg font-bold text-lg transition shadow-md mt-4 ${
                            loading ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-600 hover:bg-blue-700'
                        }`}
                    >
                        {loading ? 'Memproses Pendaftaran...' : 'Buat Akun Sekarang'}
                    </button>
                </form>
                
                <p className="text-sm text-center text-gray-600">
                    Sudah punya akun? <Link to="/login" className="text-blue-600 hover:underline font-bold">Login di sini</Link>
                </p>
            </div>
        </div>
    );
}