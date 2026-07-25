import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function Register() {
    const [formData, setFormData] = useState({
        nama_lengkap: '',
        nim: '',
        email: '',
        password: '',
    });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            // Sesuaikan endpoint ini dengan backend kamu
            await api.post('/auth/register', formData);
            alert('Pendaftaran berhasil! Silakan login.');
            navigate('/login');
        } catch (err) {
            setError('Pendaftaran gagal. NIM atau Email mungkin sudah terdaftar.');
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-md">
                <h2 className="text-2xl font-bold text-center text-blue-600">Daftar SkillBridge</h2>
                
                {error && <p className="text-sm text-red-500 text-center">{error}</p>}
                
                <form onSubmit={handleRegister} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Nama Lengkap</label>
                        <input name="nama_lengkap" type="text" required onChange={handleChange}
                            className="w-full px-3 py-2 mt-1 border rounded-md" />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">NIM</label>
                        <input name="nim" type="text" required onChange={handleChange}
                            className="w-full px-3 py-2 mt-1 border rounded-md" />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Email Kampus</label>
                        <input name="email" type="email" required onChange={handleChange}
                            className="w-full px-3 py-2 mt-1 border rounded-md" 
                            placeholder="nim@student.unimus.ac.id" />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Password</label>
                        <input name="password" type="password" required onChange={handleChange}
                            className="w-full px-3 py-2 mt-1 border rounded-md" />
                    </div>
                    
                    <button type="submit" className="w-full px-4 py-2 text-white bg-green-600 rounded-md hover:bg-green-700">
                        Buat Akun
                    </button>
                </form>
                
                <p className="text-sm text-center text-gray-600">
                    Sudah punya akun? <Link to="/login" className="text-blue-600 hover:underline">Login di sini</Link>
                </p>
            </div>
        </div>
    );
}