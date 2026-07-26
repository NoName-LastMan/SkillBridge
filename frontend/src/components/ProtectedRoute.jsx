import { Navigate } from 'react-router-dom';

export default function ProtectedRoute({ children }) {
    const token = localStorage.getItem('token');
    
    // Jika tidak ada token, paksa kembali ke halaman login
    if (!token) {
        return <Navigate to="/login" replace />;
    }
    
    // Jika ada token, izinkan masuk ke halaman yang dituju
    return children;
}