import { Link, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export default function Layout() {
  const location = useLocation();
  const { user, login, logout } = useAuth();

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
              <Link
                to="/"
                className={location.pathname === '/' ? 'active' : ''}
              >
                홈
              </Link>
              <Link
                to="/stats"
                className={location.pathname === '/stats' ? 'active' : ''}
              >
                통계
              </Link>
            </nav>
            {user ? (
              <div className="user-info">
                <img src={user.profileImageUrl} alt="" className="user-avatar" />
                <span className="user-name">{user.name}</span>
                <button onClick={logout} className="logout-btn">로그아웃</button>
              </div>
            ) : (
              <button onClick={login} className="login-btn">Google 로그인</button>
            )}
          </div>
        </div>
      </header>
      <main className="container">
        <Outlet />
      </main>
    </>
  );
}
