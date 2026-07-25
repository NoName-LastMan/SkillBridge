import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function RegisterView() {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        confirmPassword: '',
        role: 'MAHASISWA' // Sesuai dengan Enum Role API
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

        // Validasi sederhana: pastikan password dan konfirmasi password sama
        if (formData.password !== formData.confirmPassword) {
            setError('Password dan Konfirmasi Password tidak cocok!');
            return;
        }

        setLoading(true);

        try {
            // Menyiapkan payload bersih sesuai dokumentasi API (tanpa confirmPassword)
            const payload = {
                email: formData.email,
                password: formData.password,
                role: formData.role
            };

            await api.post('/auth/register', payload);
            
            alert('Registrasi berhasil! Silakan login menggunakan akun baru Anda.');
            navigate('/login');
        } catch (err) {
            // Menangkap pesan error dari backend atau menampilkan fallback
            const errorMsg = err.response?.data?.message || 'Pendaftaran gagal. Periksa kembali email kampus atau koneksi server.';
            setError(errorMsg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-md">
                <div>
                    <h2 className="text-2xl font-bold text-center text-blue-600">Daftar Akun SkillBridge</h2>
                    <p className="text-sm text-center text-gray-500 mt-1">Platform Kolaborasi & Pertukaran Skill Mahasiswa</p>
                </div>
                
                {error && (
                    <div className="p-3 text-sm text-red-700 bg-red-100 rounded-md text-center">
                        {error}
                    </div>
                )}
                
                <form onSubmit={handleRegister} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Email Kampus</label>
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
                    
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Password</label>
                        <input 
                            name="password" 
                            type="password" 
                            required 
                            value={formData.password}
                            onChange={handleChange}
                            className="w-full px-3 py-2 mt-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" 
                            placeholder="Minimal 6-8 karakter" 
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Konfirmasi Password</label>
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
                    
                    <button 
                        type="submit" 
                        disabled={loading}
                        className={`w-full px-4 py-2 text-white rounded-md font-medium transition ${
                            loading ? 'bg-gray-400 cursor-not-allowed' : 'bg-green-600 hover:bg-green-700'
                        }`}
                    >
                        {loading ? 'Memproses pendaftaran...' : 'Buat Akun'}
                    </button>
                </form>
                
                <p className="text-sm text-center text-gray-600">
                    Sudah punya akun? <Link to="/login" className="text-blue-600 hover:underline font-medium">Login di sini</Link>
                </p>
            </div>
        </div>
    );
}