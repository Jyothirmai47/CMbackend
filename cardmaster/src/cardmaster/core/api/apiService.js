import axios from 'axios';

const apiService = axios.create({
    baseURL: 'http://localhost:8082', // Spring Boot Base URL
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor to attach JWT token
apiService.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

export default apiService;
