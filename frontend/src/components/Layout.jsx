import { useState } from 'react';
import { Link, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export default function Layout() {
  const location = useLocation();
  const { user, login, logout } = useAuth();
  const [nickname, setNickname] = useState('');
  const [showLogin, setShowLogin] = useState(false);
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    if (!nickname.trim()) return;
    try {
      await login(nickname.trim());
      setShowLogin(false);
      setNickname('');
      setError('');
    } catch {
      setError('로그인에 실패했습니다.');
    }
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
            </nav>
            {user ? (
              <div className="user-info">
                <span className="user-nickname">{user.nickname}</span>
                <button onClick={logout} className="logout-btn">로그아웃</button>
              </div>
            ) : (
              <button onClick={() => setShowLogin(true)} className="login-btn">로그인</button>
            )}
          </div>
        </div>
      </header>

      {showLogin && (
        <div className="login-overlay" onClick={() => setShowLogin(false)}>
          <div className="login-modal" onClick={e => e.stopPropagation()}>
            <h2>로그인</h2>
            <p>닉네임을 입력하세요. 처음이면 자동으로 가입됩니다.</p>
            <form onSubmit={handleLogin}>
              <input
                type="text"
                value={nickname}
                onChange={e => setNickname(e.target.value)}
                placeholder="닉네임"
                className="login-input"
                autoFocus
                maxLength={20}
              />
              {error && <div className="login-error">{error}</div>}
              <button type="submit" className="submit-btn">시작하기</button>
            </form>
          </div>
        </div>
      )}

      <main className="container">
        <Outlet />
      </main>
    </>
  );
}
