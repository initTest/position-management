import axios from 'axios';
import { Position } from '../types/Position';
import { Recruiter } from '../types/Recruiter';
import { Department } from '../types/Department';
import env from '../config/env';

const axiosInstance = axios.create({
    baseURL: env.API_URL,
    headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': env.API_KEY
    }
});

export interface PageParams {
    page?: number;
    size?: number;
    sortBy?: string;
    direction?: 'asc' | 'desc';
}

export const positionApi = {
    getAll: (params: PageParams = {}) => 
        axiosInstance.get<any>('/positions', { 
            params: {
                page: params.page || 0,
                size: params.size || 10,
                sortBy: params.sortBy || 'id',
                direction: params.direction || 'asc'
            }
        }),
    getById: (id: number) => axiosInstance.get<Position>(`/positions/${id}`),
    create: (position: Position) => axiosInstance.post<Position>('/positions', position),
    update: (id: number, position: Position) => axiosInstance.put<Position>(`/positions/${id}`, position),
    delete: (id: number) => axiosInstance.delete(`/positions/${id}`)
};

export const recruiterApi = {
    getAll: () => axiosInstance.get<Recruiter[]>('/recruiters'),
    getById: (id: number) => axiosInstance.get<Recruiter>(`/recruiters/${id}`)
};

export const departmentApi = {
    getAll: () => axiosInstance.get<Department[]>('/departments'),
    getById: (id: number) => axiosInstance.get<Department>(`/departments/${id}`)
}; 