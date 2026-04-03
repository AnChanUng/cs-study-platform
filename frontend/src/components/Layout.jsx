import { Link, Outlet } from 'react-router-dom';

export default function Layout() {
  return (
    <>
      <header className="header">
        <div className="container header-inner">
          <Link to="/" className="logo">CS Study</Link>
          <nav className="nav-links">
            <Link to="/">Home</Link>
            <Link to="/stats">Stats</Link>
          </nav>
        </div>
      </header>
      <main className="container">
        <Outlet />
      </main>
    </>
  );
}
