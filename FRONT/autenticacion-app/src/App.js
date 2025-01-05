import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginForm from './components/LoginForm';
import ListaComerciantes from './pages/ListaComerciantes';
import ProtectedRoute from './ProtectedRoute';

function App() {
  return (
    <Router>
      <Routes>
        {/* Ruta para el login */}
        <Route path="/" element={<LoginForm />} />

        {/* Ruta protegida para la lista de comerciantes */}
        <Route
          path="/comerciantes"
          element={
            <ProtectedRoute>
              <ListaComerciantes />
            </ProtectedRoute>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
