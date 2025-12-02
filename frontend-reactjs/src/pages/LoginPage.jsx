import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Auth.css';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('/api/v1/auth/authenticate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });

      const data = await response.json();

      if (response.ok) {
        localStorage.setItem('token', data.token);
        navigate('/dashboard');
      } else {
        setError(data.message || 'Login failed');
      }
    } catch (err) {
      setError('Server error. Please try again later.');
    }
  };

  return (
    <div className="auth-container">
      <div className="brand-title">Pastebin from Sultanmurat Yeldar</div>

      <div className="auth-card">
        <h2 className="auth-title">Sign in</h2>

        {error && <p className="error-msg">{error}</p>}

        <form onSubmit={handleLogin}>
          <div className="input-group">
            <label>Enter your username or email address</label>
            <input
              type="text"
              className="input-field"
              placeholder="Username or email address"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>

          <div className="input-group">
            <label>Enter your Password</label>
            <input
              type="password"
              className="input-field"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <a href="#" className="forgot-password">Forgot Password</a>
          </div>

          <button type="submit" className="auth-btn">Sign in</button>
        </form>

        <div className="divider">OR</div>

        <div className="social-login">
          <div className="social-icon" style={{background: 'red'}}></div>
          <div className="social-icon" style={{background: 'blue'}}></div>
        </div>

        <div className="switch-auth">
          No Account? <Link to="/register">Sign up</Link>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;