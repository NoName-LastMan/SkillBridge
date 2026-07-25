import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api', // Sesuaikan dengan endpoint controller Spring Boot-mu
});

// Menambahkan token JWT otomatis ke setiap request (jika sudah login)
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;