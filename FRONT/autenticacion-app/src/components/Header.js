import React from "react";

const Header = ({ user, onLogout }) => {
  return (
    <header style={{ display: "flex", justifyContent: "space-between", padding: "10px", background: "#f4f4f4" }}>
      <h1>Lista Comerciantes</h1>
      <div>
        <span>Bienvenido, {user.name} ({user.role})</span>
        <button onClick={onLogout} style={{ marginLeft: "10px" }}>
          Cerrar Sesión
        </button>
      </div>
    </header>
  );
};

export default Header;