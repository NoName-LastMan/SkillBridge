import { Link, useNavigate } from 'react-router-dom';

export default function Navbar() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token'); // Hapus sesi
        navigate('/login');
    };

    return (
        <nav className="bg-blue-600 text-white shadow-md">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16 items-center">
                    <div className="flex-shrink-0 font-bold text-xl tracking-wider">
                        <Link to="/dashboard">SkillBridge</Link>
                    </div>
                    <div className="flex space-x-4 items-center">
                        <Link to="/dashboard" className="hover:text-blue-200 font-medium">Dashboard</Link>
                        <Link to="/profile" className="hover:text-blue-200 font-medium">Profilku</Link>
                        <button 
                            onClick={handleLogout}
                            className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded-md text-sm font-semibold transition"
                        >
                            Logout
                        </button>
                    </div>
                </div>
            </div>
        </nav>
    );
}