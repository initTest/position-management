import React, { useEffect, useState, useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
    TextField,
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Paper,
    Typography,
    SelectChangeEvent,
    Stack,
    Box,
    FormHelperText
} from '@mui/material';
import { Position } from '../types/Position';
import { Recruiter } from '../types/Recruiter';
import { Department } from '../types/Department';
import { positionApi, recruiterApi, departmentApi } from '../services/api';

const initialPosition: Position = {
    title: '',
    description: '',
    location: '',
    status: 'DRAFT',
    recruiter: { id: 0, name: '', email: '' },
    department: { id: 0, name: '', code: '' },
    budget: 0
};

export const PositionForm: React.FC = () => {
    const [position, setPosition] = useState<Position>(initialPosition);
    const [recruiters, setRecruiters] = useState<Recruiter[]>([]);
    const [departments, setDepartments] = useState<Department[]>([]);
    const [errors, setErrors] = useState<Record<string, string>>({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const navigate = useNavigate();
    const { id } = useParams<{ id: string }>();

    const loadPosition = useCallback(async (positionId: number) => {
        try {
            const response = await positionApi.getById(positionId);
            setPosition(response.data);
        } catch (error) {
            console.error('Error loading position:', error);
            navigate('/positions');
        }
    }, [navigate]);

    useEffect(() => {
        const loadRecruitersAndDepartments = async () => {
            try {
                const [recruitersResponse, departmentsResponse] = await Promise.all([
                    recruiterApi.getAll(),
                    departmentApi.getAll()
                ]);
                setRecruiters(recruitersResponse.data);
                setDepartments(departmentsResponse.data);
            } catch (error) {
                console.error('Error loading recruiters and departments:', error);
            }
        };

        loadRecruitersAndDepartments();
        if (id) {
            loadPosition(parseInt(id));
        }
    }, [id, loadPosition]);

    const validateForm = (): boolean => {
        const newErrors: Record<string, string> = {};

        if (!position.title.trim()) {
            newErrors.title = 'Title is required';
        }
        if (!position.description.trim()) {
            newErrors.description = 'Description is required';
        }
        if (!position.location.trim()) {
            newErrors.location = 'Location is required';
        }
        if (position.recruiter.id === 0) {
            newErrors.recruiter = 'Recruiter is required';
        }
        if (position.department.id === 0) {
            newErrors.department = 'Department is required';
        }
        if (!position.budget || position.budget <= 0) {
            newErrors.budget = 'Budget must be greater than 0';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) {
            return;
        }

        setIsSubmitting(true);
        try {
            if (id) {
                await positionApi.update(parseInt(id), position);
            } else {
                await positionApi.create(position);
            }
            navigate('/positions');
        } catch (error) {
            console.error('Error saving position:', error);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent) => {
        const { name, value } = e.target;
        setPosition((prev: Position) => ({
            ...prev,
            [name]: value
        }));
        // Clear error when field is changed
        if (errors[name]) {
            setErrors((prev: Record<string, string>) => ({ ...prev, [name]: '' }));
        }
    };

    const handleRecruiterChange = (e: SelectChangeEvent) => {
        const selectedRecruiter = recruiters.find(r => r.id === Number(e.target.value));
        if (selectedRecruiter) {
            setPosition((prev: Position) => ({
                ...prev,
                recruiter: selectedRecruiter
            }));
            // Clear recruiter error
            if (errors.recruiter) {
                setErrors((prev: Record<string, string>) => ({ ...prev, recruiter: '' }));
            }
        }
    };

    const handleDepartmentChange = (e: SelectChangeEvent) => {
        const selectedDepartment = departments.find(d => d.id === Number(e.target.value));
        if (selectedDepartment) {
            setPosition((prev: Position) => ({
                ...prev,
                department: selectedDepartment
            }));
            // Clear department error
            if (errors.department) {
                setErrors((prev: Record<string, string>) => ({ ...prev, department: '' }));
            }
        }
    };

    return (
        <Paper sx={{ padding: 2 }}>
            <Typography variant="h5" gutterBottom>
                {id ? 'Edit Position' : 'Create New Position'}
            </Typography>
            <form onSubmit={handleSubmit}>
                <Stack spacing={3}>
                    <TextField
                        required
                        fullWidth
                        name="title"
                        label="Title"
                        value={position.title}
                        onChange={handleChange}
                        error={!!errors.title}
                        helperText={errors.title}
                        inputProps={{ maxLength: 100 }}
                    />
                    <TextField
                        required
                        fullWidth
                        multiline
                        rows={4}
                        name="description"
                        label="Description"
                        value={position.description}
                        onChange={handleChange}
                        error={!!errors.description}
                        helperText={errors.description}
                        inputProps={{ maxLength: 1000 }}
                    />
                    <TextField
                        required
                        fullWidth
                        name="location"
                        label="Location"
                        value={position.location}
                        onChange={handleChange}
                        error={!!errors.location}
                        helperText={errors.location}
                    />
                    <FormControl fullWidth required>
                        <InputLabel>Status</InputLabel>
                        <Select
                            name="status"
                            value={position.status}
                            onChange={handleChange}
                            label="Status"
                        >
                            <MenuItem value="DRAFT">Draft</MenuItem>
                            <MenuItem value="OPEN">Open</MenuItem>
                            <MenuItem value="CLOSED">Closed</MenuItem>
                            <MenuItem value="ARCHIVED">Archived</MenuItem>
                        </Select>
                    </FormControl>
                    <FormControl fullWidth required error={!!errors.recruiter}>
                        <InputLabel>Recruiter</InputLabel>
                        <Select
                            value={position.recruiter.id.toString()}
                            onChange={handleRecruiterChange}
                            label="Recruiter"
                        >
                            {recruiters.map(recruiter => (
                                <MenuItem key={recruiter.id} value={recruiter.id}>
                                    {recruiter.name} ({recruiter.email})
                                </MenuItem>
                            ))}
                        </Select>
                        {errors.recruiter && (
                            <FormHelperText>{errors.recruiter}</FormHelperText>
                        )}
                    </FormControl>
                    <FormControl fullWidth required error={!!errors.department}>
                        <InputLabel>Department</InputLabel>
                        <Select
                            value={position.department.id.toString()}
                            onChange={handleDepartmentChange}
                            label="Department"
                        >
                            {departments.map(department => (
                                <MenuItem key={department.id} value={department.id}>
                                    {department.name} ({department.code})
                                </MenuItem>
                            ))}
                        </Select>
                        {errors.department && (
                            <FormHelperText>{errors.department}</FormHelperText>
                        )}
                    </FormControl>
                    <TextField
                        required
                        fullWidth
                        type="number"
                        name="budget"
                        label="Budget"
                        value={position.budget}
                        onChange={handleChange}
                        error={!!errors.budget}
                        helperText={errors.budget}
                        inputProps={{ min: 0 }}
                    />
                    <TextField
                        fullWidth
                        type="date"
                        name="closingDate"
                        label="Closing Date"
                        value={position.closingDate}
                        onChange={handleChange}
                        InputLabelProps={{ shrink: true }}
                    />
                    <Box sx={{ display: 'flex', gap: 1 }}>
                        <Button
                            type="submit"
                            variant="contained"
                            color="primary"
                            disabled={isSubmitting}
                        >
                            {id ? 'Update' : 'Create'}
                        </Button>
                        <Button
                            variant="outlined"
                            onClick={() => navigate('/positions')}
                            disabled={isSubmitting}
                        >
                            Cancel
                        </Button>
                    </Box>
                </Stack>
            </form>
        </Paper>
    );
}; 