import axios from "axios";

const axiosInstance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BASE_URL || "http://localhost:8081", // Replace with your server URL
});


export default axiosInstance;