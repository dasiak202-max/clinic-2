import axios from "axios";

const BASE_URL = "http://localhost:8082/api/offices";

export const getAllOffices = () => axios.get(`${BASE_URL}/all`);


export const deleteOffice = (id) => axios.delete(`${BASE_URL}/delete/${id}`);