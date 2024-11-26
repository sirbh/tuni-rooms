import axios from "axios";

const axiosInstance = axios.create({
    baseURL: process.env.MAP_PROCESSOR_URL,
    });

console.log(process.env.MAP_PROCESSOR_URL);

export default axiosInstance;