import { useState } from 'react';
import { Link, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export default function Layout() {
  const location = useLocation();
  const { user, login, register, logout } = useAuth();
  const [nickname, setNickname] = useState('');
  const [password, setPassword] = useState('');
  const [showLogin, setShowLogin] = useState(false);
  const [isRegister, setIsRegister] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!nickname.trim() || !password.trim()) return;
    try {
      if (isRegister) {
        await register(nickname.trim(), password.trim());
      } else {
        await login(nickname.trim(), password.trim());
      }
      setShowLogin(false);
      setNickname('');
      setPassword('');
      setError('');
    } catch (err) {
      setError(err.response?.data?.error || '요청에 실패했습니다.');
    }
  };

  const closeModal = () => {
    setShowLogin(false);
    setError('');
    setNickname('');
    setPassword('');
  };

  return (
    <>
      <header className="header">
        <div className="container header-inner">
          <Link to="/" className="logo">
            <span className="logo-dot" />
            CS Study
          </Link>
          <div className="header-right">
            <nav className="nav-links">
              <Link to="/" className={location.pathname === '/' ? 'active' : ''}>
                홈
              </Link>
              <Link to="/stats" className={location.pathname === '/stats' ? 'active' : ''}>
                통계
              </Link>
              <Link to="/interview" className={location.pathname === '/interview' ? 'active' : ''}>
                면접연습
              </Link>
            </nav>
            {user ? (
              <div className="user-info">
                <span className="user-nickname">{user.nickname}</span>
                <button onClick={logout} className="logout-btn">로그아웃</button>
              </div>
            ) : (
              <button onClick={() => { setShowLogin(true); setIsRegister(false); }} className="login-btn">로그인</button>
            )}
          </div>
        </div>
      </header>

      {showLogin && (
        <div className="login-overlay" onClick={closeModal}>
          <div className="login-modal" onClick={e => e.stopPropagation()}>
            <h2>{isRegister ? '회원가입' : '로그인'}</h2>
            <form onSubmit={handleSubmit}>
              <input
                type="text"
                value={nickname}
                onChange={e => setNickname(e.target.value)}
                placeholder="닉네임"
                className="login-input"
                autoFocus
                maxLength={20}
              />
              <input
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                placeholder="비밀번호"
                className="login-input"
                maxLength={30}
              />
              {error && <div className="login-error">{error}</div>}
              <button type="submit" className="submit-btn">
                {isRegister ? '가입하기' : '로그인'}
              </button>
            </form>
            <div className="login-switch">
              {isRegister ? (
                <span>이미 계정이 있나요? <button onClick={() => { setIsRegister(false); setError(''); }} className="switch-btn">로그인</button></span>
              ) : (
                <span>처음이신가요? <button onClick={() => { setIsRegister(true); setError(''); }} className="switch-btn">회원가입</button></span>
              )}
            </div>
          </div>
        </div>
      )}

      <main className="container">
        <Outlet />
      </main>
    </>
  );
}
