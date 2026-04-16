import React, { useContext } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { Briefcase, LayoutDashboard, Bookmark, ClipboardList, LogOut, User } from 'lucide-react';

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const isActive = (path) => location.pathname === path;

  return (
    <nav className="glass-nav">
      <div className="container flex items-center justify-between py-4">
        <Link to="/" className="flex items-center gap-2" style={{ textDecoration: 'none' }}>
          <Briefcase size={28} color="var(--primary-color)" />
          <span style={{ fontSize: '1.25rem', fontWeight: 700, color: 'var(--text-main)', letterSpacing: '-0.5px' }}>
            JobTracker
          </span>
        </Link>
        
        <div className="flex items-center gap-6">
          {user ? (
            <>
              <Link to="/jobs" className={`nav-link ${isActive('/jobs') ? 'active' : ''}`}>
                Find Jobs
              </Link>
              <Link to="/dashboard" className={`nav-link flex items-center gap-2 ${isActive('/dashboard') ? 'active' : ''}`}>
                <LayoutDashboard size={18} />
                Dashboard
              </Link>
              <Link to="/applications" className={`nav-link flex items-center gap-2 ${isActive('/applications') ? 'active' : ''}`}>
                <ClipboardList size={18} />
                Applications
              </Link>
              <Link to="/bookmarks" className={`nav-link flex items-center gap-2 ${isActive('/bookmarks') ? 'active' : ''}`}>
                <Bookmark size={18} />
                Bookmarks
              </Link>
              
              <div className="h-6 w-px bg-slate-700 mx-2"></div>
              
              <div className="flex items-center gap-4">
                <div className="flex items-center gap-2 text-sm text-slate-300">
                  <User size={16} />
                  {user.email}
                </div>
                <button onClick={handleLogout} className="btn btn-secondary">
                  <LogOut size={16} />
                  Logout
                </button>
              </div>
            </>
          ) : (
            <>
              <Link to="/auth" className="btn btn-secondary">Sign In</Link>
              <Link to="/auth?mode=register" className="btn btn-primary">Get Started</Link>
            </>
          )}
        </div>
      </div>
      
      <style>{`
        .nav-link {
          color: var(--text-muted);
          text-decoration: none;
          font-weight: 500;
          font-size: 0.95rem;
          transition: color 0.2s;
        }
        .nav-link:hover {
          color: white;
        }
        .nav-link.active {
          color: var(--primary-color);
        }
      `}</style>
    </nav>
  );
};

export default Navbar;
