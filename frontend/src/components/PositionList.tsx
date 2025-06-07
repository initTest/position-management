import React, { useEffect, useState } from 'react';
import { 
    Table, 
    TableBody, 
    TableCell, 
    TableContainer, 
    TableHead, 
    TableRow, 
    Paper,
    IconButton,
    Button,
    TablePagination,
    TableSortLabel,
    Box
} from '@mui/material';
import { Delete, Edit } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { Position } from '../types/Position';
import { positionApi, PageParams } from '../services/api';

interface PositionPage {
    content: Position[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}

export const PositionList: React.FC = () => {
    const [positions, setPositions] = useState<Position[]>([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [totalElements, setTotalElements] = useState(0);
    const [sortBy, setSortBy] = useState<string>('id');
    const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('asc');
    const navigate = useNavigate();

    useEffect(() => {
        loadPositions();
    }, [page, rowsPerPage, sortBy, sortDirection]);

    const loadPositions = async () => {
        try {
            const params: PageParams = {
                page,
                size: rowsPerPage,
                sortBy,
                direction: sortDirection
            };
            const response = await positionApi.getAll(params);
            const pageData = response.data as PositionPage;
            setPositions(pageData.content);
            setTotalElements(pageData.totalElements);
        } catch (error) {
            console.error('Error loading positions:', error);
        }
    };

    const handleDelete = async (id: number) => {
        try {
            await positionApi.delete(id);
            loadPositions();
        } catch (error) {
            console.error('Error deleting position:', error);
        }
    };

    const handleChangePage = (event: unknown, newPage: number) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleSort = (column: string) => {
        const isAsc = sortBy === column && sortDirection === 'asc';
        setSortDirection(isAsc ? 'desc' : 'asc');
        setSortBy(column);
    };

    return (
        <div>
            <Button 
                variant="contained" 
                color="primary" 
                onClick={() => navigate('/positions/new')}
                style={{ marginBottom: 20 }}
            >
                Create New Position
            </Button>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>
                                <TableSortLabel
                                    active={sortBy === 'title'}
                                    direction={sortBy === 'title' ? sortDirection : 'asc'}
                                    onClick={() => handleSort('title')}
                                >
                                    Title
                                </TableSortLabel>
                            </TableCell>
                            <TableCell>
                                <TableSortLabel
                                    active={sortBy === 'location'}
                                    direction={sortBy === 'location' ? sortDirection : 'asc'}
                                    onClick={() => handleSort('location')}
                                >
                                    Location
                                </TableSortLabel>
                            </TableCell>
                            <TableCell>
                                <TableSortLabel
                                    active={sortBy === 'status'}
                                    direction={sortBy === 'status' ? sortDirection : 'asc'}
                                    onClick={() => handleSort('status')}
                                >
                                    Status
                                </TableSortLabel>
                            </TableCell>
                            <TableCell>
                                <TableSortLabel
                                    active={sortBy === 'budget'}
                                    direction={sortBy === 'budget' ? sortDirection : 'asc'}
                                    onClick={() => handleSort('budget')}
                                >
                                    Budget
                                </TableSortLabel>
                            </TableCell>
                            <TableCell>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {positions.map((position) => (
                            <TableRow key={position.id}>
                                <TableCell>{position.title}</TableCell>
                                <TableCell>{position.location}</TableCell>
                                <TableCell>{position.status}</TableCell>
                                <TableCell>${position.budget.toLocaleString()}</TableCell>
                                <TableCell>
                                    <IconButton 
                                        onClick={() => navigate(`/positions/edit/${position.id}`)}
                                        color="primary"
                                        data-testid={`edit-button-${position.id}`}
                                    >
                                        <Edit />
                                    </IconButton>
                                    <IconButton 
                                        onClick={() => position.id && handleDelete(position.id)}
                                        color="error"
                                        data-testid={`delete-button-${position.id}`}
                                    >
                                        <Delete />
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <TablePagination
                    component="div"
                    count={totalElements}
                    page={page}
                    onPageChange={handleChangePage}
                    rowsPerPage={rowsPerPage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                    rowsPerPageOptions={[5, 10, 25]}
                />
            </TableContainer>
        </div>
    );
}; 