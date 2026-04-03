import { createContext, useContext, useState, useEffect } from 'react';
import api from '../api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const saved = localStorage.getItem('cs_user');
    if (saved) {
      setUser(JSON.parse(saved));
    }
    setLoading(false);
  }, []);

  const login = async (nickname, password) => {
    const res = await api.post('/auth/login', { nickname, password });
    const userData = res.data;
    setUser(userData);
    localStorage.setItem('cs_user', JSON.stringify(userData));
    return userData;
  };

  const register = async (nickname, password) => {
    const res = await api.post('/auth/register', { nickname, password });
    const userData = res.data;
    setUser(userData);
    localStorage.setItem('cs_user', JSON.stringify(userData));
    return userData;
  };

  const logout = () => {
    localStorage.removeItem('cs_user');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
