import { createContext, useContext, useEffect, useState } from 'react';



const AuthContext = createContext(null);


export const DEMO_USERS = [
  { username: 'admin', name: 'Vaibhav Bisht', role: 'Admin' },
  { username: 'manager', name: 'Anurag Maurya', role: 'Manager' },
  { username: 'staff', name: 'Vedant Gandhewar', role: 'Staff' },
];

const DEMO_PASSWORD = 'demo';
const STORAGE_KEY = 'ims.auth.user';


const PERMISSIONS = {
  Admin: ['view', 'create', 'edit', 'adjust', 'manageUsers'],
  Manager: ['view', 'create', 'edit', 'adjust'],
  Staff: ['view'],
};

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  });

  useEffect(() => {
    if (user) localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
    else localStorage.removeItem(STORAGE_KEY);
  }, [user]);

  const login = (username, password) => {
    const found = DEMO_USERS.find((u) => u.username === username.trim().toLowerCase());
    if (!found || password !== DEMO_PASSWORD) {
      return { ok: false, error: 'Invalid username or password.' };
    }
    setUser(found);
    return { ok: true };
  };

  const logout = () => setUser(null);

  const can = (action) => !!user && PERMISSIONS[user.role]?.includes(action);

  return (
    <AuthContext.Provider value={{ user, login, logout, can }}>{children}</AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within an AuthProvider');
  return ctx;
}
