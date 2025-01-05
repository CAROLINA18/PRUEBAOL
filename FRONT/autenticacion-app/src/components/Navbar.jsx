import React from 'react';
import { useSelector } from 'react-redux';

const Navbar = () => {
  const user = useSelector((state) => state.user);

  return (
    <nav style={styles.nav}>
      <div>OL Plataforma</div>
      {user && (
        <div>
          {user.name} ({user.role})
        </div>
      )}
    </nav>
  );
};

const styles = {
  nav: {
    backgroundColor: '#fff',
    padding: '10px 20px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.1)',
  },
};

export default Navbar;