import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { PositionList } from '../PositionList';
import { positionApi } from '../../services/api';

// Suppress React Router warnings for tests
const originalConsoleWarn = console.warn;
beforeAll(() => {
    console.warn = (...args: any[]) => {
        if (args[0]?.includes?.('React Router')) return;
        originalConsoleWarn.apply(console, args);
    };
});

afterAll(() => {
    console.warn = originalConsoleWarn;
});

// Mock the API
jest.mock('../../services/api', () => ({
    positionApi: {
        getAll: jest.fn(),
        delete: jest.fn()
    }
}));

// Mock useNavigate
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate
}));

describe('PositionList', () => {
    const mockPosition = {
        id: 1,
        title: 'Software Engineer',
        description: 'Test description',
        location: 'Remote',
        status: 'OPEN',
        recruiter: { id: 1, name: 'John Doe', email: 'john@example.com' },
        department: { id: 1, name: 'Engineering', code: 'ENG' },
        budget: 100000
    };

    const mockPageResponse = {
        data: {
            content: [mockPosition],
            totalElements: 1,
            totalPages: 1,
            size: 10,
            number: 0
        }
    };

    beforeEach(() => {
        jest.clearAllMocks();
        (positionApi.getAll as jest.Mock).mockResolvedValue(mockPageResponse);
    });

    afterEach(() => {
        jest.resetModules();
    });

    it('renders position list and handles navigation', async () => {
        render(
            <MemoryRouter>
                <PositionList />
            </MemoryRouter>
        );

        // Wait for the position to be loaded and displayed
        await waitFor(() => {
            expect(positionApi.getAll).toHaveBeenCalledWith({
                page: 0,
                size: 10,
                sortBy: 'id',
                direction: 'asc'
            });
        });

        // Verify the position is displayed
        expect(await screen.findByText('Software Engineer')).toBeInTheDocument();
        expect(await screen.findByText('Remote')).toBeInTheDocument();
        expect(await screen.findByText('$100,000')).toBeInTheDocument();

        // Test navigation
        fireEvent.click(screen.getByText('Create New Position'));
        expect(mockNavigate).toHaveBeenCalledWith('/positions/new');

        const editButton = await screen.findByTestId('edit-button-1');
        fireEvent.click(editButton);
        expect(mockNavigate).toHaveBeenCalledWith('/positions/edit/1');
    });

    it('handles position deletion', async () => {
        (positionApi.delete as jest.Mock).mockResolvedValue({});
        
        render(
            <MemoryRouter>
                <PositionList />
            </MemoryRouter>
        );

        // Wait for the position to be loaded
        await waitFor(() => {
            expect(positionApi.getAll).toHaveBeenCalledWith({
                page: 0,
                size: 10,
                sortBy: 'id',
                direction: 'asc'
            });
        });

        // Verify the delete button is present and click it
        const deleteButton = await screen.findByTestId('delete-button-1');
        fireEvent.click(deleteButton);

        // Verify the deletion was called and data was reloaded
        await waitFor(() => {
            expect(positionApi.delete).toHaveBeenCalledWith(1);
            expect(positionApi.getAll).toHaveBeenCalledTimes(2);
        });
    });

    it('handles API errors gracefully', async () => {
        const consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation(() => {});
        (positionApi.getAll as jest.Mock).mockRejectedValue(new Error('API Error'));

        render(
            <MemoryRouter>
                <PositionList />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(consoleErrorSpy).toHaveBeenCalledWith('Error loading positions:', expect.any(Error));
        });

        consoleErrorSpy.mockRestore();
    });
}); 