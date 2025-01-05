import React, { useEffect, useState } from 'react';
import Header from '../components/Header';
import axios from 'axios';
import '../styles/ListaComerciantes.css';

const ListaComerciantes = () => {
  const [comerciantes, setComerciantes] = useState([]);
  const token = localStorage.getItem('token');

  useEffect(() => {
    const fetchComerciantes = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/comerciantes', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setComerciantes(response.data);
      } catch (error) {
        console.error('Error al obtener comerciantes:', error);
      }
    };

    fetchComerciantes();
  }, [token]);

  return (
    <div>
      <Header user={{ name: 'John Doe', role: 'Administrador' }} />
      <h2>Lista de Comerciantes</h2>
      <table>
        <thead>
          <tr>
            <th>Razón Social</th>
            <th>Teléfono</th>
            <th>Correo Electrónico</th>
            <th>Fecha Registro</th>
            <th>No. Establecimientos</th>
            <th>Estado</th>
          </tr>
        </thead>
        <tbody>
          {comerciantes.map((comerciante) => (
            <tr key={comerciante.id}>
              <td>{comerciante.nombreRazonSocial}</td>
              <td>{comerciante.telefono}</td>
              <td>{comerciante.correoElectronico}</td>
              <td>{comerciante.fechaRegistro}</td>
              <td>{comerciante.cantidadEstablecimientos}</td>
              <td>{comerciante.estado}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ListaComerciantes;