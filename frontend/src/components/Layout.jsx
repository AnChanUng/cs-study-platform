import { Link, Outlet, useLocation } from 'react-router-dom';

export default function Layout() {
  const location = useLocation();

  return (
    <>
      <header className="header">
        <div className="container header-inner">
          <Link to="/" className="logo">
            <span className="logo-dot" />
            CS Study
          </Link>
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
        </div>
      </header>
      <main className="container">
        <Outlet />
      </main>
    </>
  );
}
