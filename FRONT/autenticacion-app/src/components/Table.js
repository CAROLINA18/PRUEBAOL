import React from "react";

const Table = ({ comerciantes }) => {
  return (
<table>
  <thead>
    <tr>
      <th>Razón Social</th>
      <th>Teléfono</th>
      <th>Correo Electrónico</th>
      <th>Fecha Registro</th>
      <th>No. Establecimientos</th>
      <th>Estado</th>
      <th>Acciones</th>
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
        <td>
          <span
            className={
              comerciante.estado === "Activo"
                ? "estado-activo"
                : "estado-inactivo"
            }
          >
            {comerciante.estado}
          </span>
        </td>
        <td>
          <button className="edit">Editar</button>
          <button className="toggle-status">Activar/Inactivar</button>
          <button className="delete">Eliminar</button>
        </td>
      </tr>
    ))}
  </tbody>
</table>
    
  );
  
};

export default Table;
