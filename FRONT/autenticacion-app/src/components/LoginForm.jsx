import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/login.css';

const LoginForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
  
    const handleSubmit = async (e) => {
      e.preventDefault();
      try {
        const response = await axios.post('http://localhost:8080/api/login', {
          email,
          password,
        });
  
        if (response.data && response.data.token) {
          localStorage.setItem('token', response.data.token);
          navigate('/comerciantes');
        } else {
          setError('Error en el inicio de sesión. Verifique sus credenciales.');
        }
      } catch (err) {
        console.error(err);
        setError('Error en el inicio de sesión. Verifique sus credenciales.');
      }
    };
  
    return (
      <div className="login-container">
        <h2>Iniciar Sesión</h2>
        <form className="login-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Correo Electrónico</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Contraseña</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div className="form-group checkbox-group">
            <input type="checkbox" id="terms" required />
            <label htmlFor="terms">Acepto los términos y condiciones</label>
          </div>
          <button type="submit" className="btn-submit">Iniciar Sesión</button>
          {error && <p className="error-text">{error}</p>}
        </form>
      </div>
    );
  };

export default LoginForm;
