import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem('token'); // Obtén el token del almacenamiento local

  if (!token) {
    return <Navigate to="/" />; // Redirige al login si no hay token
  }

  return children; // Muestra la ruta protegida si el token está presente
};

export default ProtectedRoute;