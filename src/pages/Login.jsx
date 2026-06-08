import { useState } from 'react';
import { useNavigate, useLocation, Navigate } from 'react-router-dom';
import { useAuth, DEMO_USERS } from '../auth/AuthContext';

export default function Login() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  // Already signed in → send to where they were headed (or the dashboard).
  const from = location.state?.from?.pathname || '/';
  if (user) return <Navigate to={from} replace />;

  const submit = (e) => {
    e.preventDefault();
    const res = login(username, password);
    if (res.ok) navigate(from, { replace: true });
    else setError(res.error);
  };

  // Click a demo account to prefill the form.
  const pick = (u) => {
    setUsername(u.username);
    setPassword('demo');
    setError('');
  };

  return (
    <div className="login-screen">
      <div className="login-card">
        <div className="login-brand">📦 IMS</div>
        <h1>Sign in</h1>
        <p className="muted">Inventory Management System</p>

        <form onSubmit={submit} className="login-form">
          <div className="form-field">
            <label htmlFor="username">Username</label>
            <input
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="admin / manager / staff"
              autoComplete="username"
            />
          </div>
          <div className="form-field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="demo"
              autoComplete="current-password"
            />
          </div>
          {error && <div className="login-error">{error}</div>}
          <button type="submit" style={{ width: '100%' }}>
            Sign in
          </button>
        </form>

        <div className="login-demo">
          <span className="muted">Demo accounts (password: <code>demo</code>)</span>
          <div className="login-roles">
            {DEMO_USERS.map((u) => (
              <button key={u.username} type="button" className="secondary" onClick={() => pick(u)}>
                {u.role}
              </button>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
