import { Recruiter } from './Recruiter';
import { Department } from './Department';

export interface Position {
    id?: number;
    title: string;
    description: string;
    location: string;
    status: 'DRAFT' | 'OPEN' | 'CLOSED' | 'ARCHIVED';
    recruiter: Recruiter;
    department: Department;
    budget: number;
    closingDate?: string;
} 