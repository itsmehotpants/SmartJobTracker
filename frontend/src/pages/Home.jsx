import React from 'react';
import { Link } from 'react-router-dom';
import { Search, Briefcase, TrendingUp, ShieldCheck } from 'lucide-react';

const Home = () => {
  return (
    <div className="page-wrapper" style={{ padding: '0' }}>
      {/* Hero Section */}
      <section style={{ padding: '80px 24px', textAlign: 'center', position: 'relative', overflow: 'hidden' }}>
        {/* Abstract background blobs */}
        <div style={{
          position: 'absolute', top: '-10%', left: '10%', width: '400px', height: '400px',
          background: 'rgba(99, 102, 241, 0.15)', filter: 'blur(80px)', borderRadius: '50%', zIndex: '-1'
        }} />
        <div style={{
          position: 'absolute', bottom: '-10%', right: '10%', width: '300px', height: '300px',
          background: 'rgba(168, 85, 247, 0.15)', filter: 'blur(80px)', borderRadius: '50%', zIndex: '-1'
        }} />
        
        <div className="container animate-fade-in" style={{ maxWidth: '800px' }}>
          <div className="badge badge-purple mb-4">🚀 The Future of Job Hunting</div>
          <h1 style={{ fontSize: '4rem', fontWeight: 800, letterSpacing: '-1px', marginBottom: '1.5rem' }}>
            Land Your Dream Role with <span className="text-gradient">Smart Tracking</span>
          </h1>
          <p style={{ fontSize: '1.25rem', color: 'var(--text-muted)', marginBottom: '3rem', maxWidth: '600px', margin: '0 auto 3rem' }}>
            A premium platform to organize your job applications, discover top opportunities, and accelerate your tech career.
          </p>
          
          <div className="flex justify-center gap-4">
            <Link to="/auth?mode=register" className="btn btn-primary" style={{ padding: '14px 32px', fontSize: '1.1rem' }}>
              Get Started Free
            </Link>
            <Link to="/auth" className="btn btn-secondary" style={{ padding: '14px 32px', fontSize: '1.1rem' }}>
              Sign In
            </Link>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section style={{ padding: '80px 24px', backgroundColor: 'var(--surface-color)' }}>
        <div className="container">
          <div className="text-center mb-8">
            <h2>Why Choose JobTracker?</h2>
            <p>Everything you need to manage your career in one place.</p>
          </div>
          
          <div className="grid-cards" style={{ marginTop: '3rem' }}>
            <div className="glass-card">
              <div style={{ background: 'rgba(99, 102, 241, 0.2)', width: '50px', height: '50px', borderRadius: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1rem' }}>
                <Search color="#6366f1" size={24} />
              </div>
              <h3 style={{ fontSize: '1.25rem' }}>Smart Discovery</h3>
              <p className="text-sm">Filter through thousands of jobs with advanced parameters. Find exactly what fits your skills.</p>
            </div>
            
            <div className="glass-card">
              <div style={{ background: 'rgba(16, 185, 129, 0.2)', width: '50px', height: '50px', borderRadius: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1rem' }}>
                <TrendingUp color="#10b981" size={24} />
              </div>
              <h3 style={{ fontSize: '1.25rem' }}>Pipeline Analytics</h3>
              <p className="text-sm">Track your applications from wishlist to offer. Watch your interview and offer rates soar.</p>
            </div>
            
            <div className="glass-card">
              <div style={{ background: 'rgba(245, 158, 11, 0.2)', width: '50px', height: '50px', borderRadius: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1rem' }}>
                <ShieldCheck color="#fbbf24" size={24} />
              </div>
              <h3 style={{ fontSize: '1.25rem' }}>Enterprise Security</h3>
              <p className="text-sm">Your data is secured with AES BCrypt hashing and stateless JWT token rotation.</p>
            </div>
          </div>
        </div>
      </section>
      
      {/* Footer */}
      <footer style={{ padding: '40px 24px', textAlign: 'center', borderTop: '1px solid var(--border-color)' }}>
        <div className="flex items-center justify-center gap-2 text-muted">
          <Briefcase size={20} />
          <span style={{ fontWeight: 600 }}>Smart Job Tracker</span>
        </div>
        <p className="mt-4 text-sm text-muted">© 2024. Built for ambitious professionals.</p>
      </footer>
    </div>
  );
};

export default Home;
