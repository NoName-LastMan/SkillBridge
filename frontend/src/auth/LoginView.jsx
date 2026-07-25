import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            // Mengirim data ke backend
            const response = await api.post('/auth/login', { email, password });
            
            // Simpan token JWT ke local storage
            localStorage.setItem('token', response.data.token);
            
            // Arahkan ke halaman utama/dashboard setelah sukses
            navigate('/dashboard');
        } catch (err) {
            setError('Login gagal. Periksa kembali email dan passwordmu.');
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-md">
                <h2 className="text-2xl font-bold text-center text-blue-600">Login SkillBridge</h2>
                
                {error && <p className="text-sm text-red-500 text-center">{error}</p>}
                
                <form onSubmit={handleLogin} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Email Kampus</label>
                        <input 
                            type="email" 
                            required
                            className="w-full px-3 py-2 mt-1 border rounded-md focus:ring-blue-500 focus:border-blue-500"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="nim@student.unimus.ac.id"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Password</label>
                        <input 
                            type="password" 
                            required
                            className="w-full px-3 py-2 mt-1 border rounded-md focus:ring-blue-500 focus:border-blue-500"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <button 
                        type="submit" 
                        className="w-full px-4 py-2 text-white bg-blue-600 rounded-md hover:bg-blue-700"
                    >
                        Masuk
                    </button>
                </form>
                
                <p className="text-sm text-center text-gray-600">
                    Belum punya akun? <Link to="/register" className="text-blue-600 hover:underline">Daftar di sini</Link>
                </p>
            </div>
        </div>
    );
}