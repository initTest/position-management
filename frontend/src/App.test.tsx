import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders create position button', () => {
  render(<App />);
  const buttonElement = screen.getByText(/Create New Position/i);
  expect(buttonElement).toBeInTheDocument();
});
