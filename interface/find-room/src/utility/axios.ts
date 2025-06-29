import axios from "axios";




const axiosInstance = axios.create({
    baseURL: "http://localhost:8080", // Replace with your server URL
});


export default axiosInstance;
