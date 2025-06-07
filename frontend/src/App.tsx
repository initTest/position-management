import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Container, CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import { PositionList } from './components/PositionList';
import { PositionForm } from './components/PositionForm';

const theme = createTheme();

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Container maxWidth="lg" style={{ marginTop: 20 }}>
          <Routes>
            <Route path="/positions" element={<PositionList />} />
            <Route path="/positions/new" element={<PositionForm />} />
            <Route path="/positions/edit/:id" element={<PositionForm />} />
            <Route path="/" element={<Navigate to="/positions" replace />} />
          </Routes>
        </Container>
      </Router>
    </ThemeProvider>
  );
}

export default App;
