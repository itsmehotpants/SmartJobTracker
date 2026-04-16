import React, { useState, useContext, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { Briefcase } from 'lucide-react';
import toast from 'react-hot-toast';

const Auth = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({ name: '', email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [loadingText, setLoadingText] = useState('');
  
  const { login, register, user } = useContext(AuthContext);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (user) {
      navigate('/dashboard');
    }
    const params = new URLSearchParams(location.search);
    if (params.get('mode') === 'register') {
      setIsLogin(false);
    }
  }, [user, navigate, location]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    setLoadingText(isLogin ? 'Signing In...' : 'Signing Up...');

    const wakeupTimer = setTimeout(() => {
      setLoadingText('Waking up server... (usually takes ~60s on Free tier)');
      toast('The backend server is waking up. Please be patient!', { icon: '⏳', duration: 6000 });
    }, 4000);

    let res;
    if (isLogin) {
      res = await login(formData.email, formData.password);
    } else {
      res = await register(formData.name, formData.email, formData.password);
    }

    clearTimeout(wakeupTimer);

    if (res.success) {
      toast.success(isLogin ? 'Successfully logged in!' : 'Account created successfully!');
      navigate('/dashboard');
    } else {
      toast.error(res.message || 'Authentication failed');
      setError(res.message);
    }
    setLoading(false);
  };

  return (
    <div className="page-wrapper flex items-center justify-center animate-fade-in" style={{ minHeight: 'calc(100vh - 70px)' }}>
      <div className="glass-card" style={{ width: '100%', maxWidth: '440px', margin: '0 20px', padding: '40px' }}>
        <div className="text-center mb-8">
          <div className="flex justify-center mb-4">
            <div style={{ background: 'rgba(99, 102, 241, 0.1)', padding: '12px', borderRadius: '50%' }}>
              <Briefcase size={32} color="var(--primary-color)" />
            </div>
          </div>
          <h2>{isLogin ? 'Welcome back' : 'Create an account'}</h2>
          <p>{isLogin ? 'Enter your credentials to access your account' : 'Enter your details to get started'}</p>
        </div>

        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid var(--danger-color)', color: '#f87171', padding: '12px', borderRadius: '8px', marginBottom: '20px', fontSize: '0.9rem' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          {!isLogin && (
            <div className="input-group">
              <label className="input-label">Full Name</label>
              <input
                type="text"
                name="name"
                className="input-field"
                placeholder="John Doe"
                value={formData.name}
                onChange={handleChange}
                required
              />
            </div>
          )}
          
          <div className="input-group">
            <label className="input-label">Email</label>
            <input
              type="email"
              name="email"
              className="input-field"
              placeholder="you@example.com"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="input-group">
            <label className="input-label">Password</label>
            <input
              type="password"
              name="password"
              className="input-field"
              placeholder="••••••••"
              value={formData.password}
              onChange={handleChange}
              required
              minLength={6}
            />
          </div>

          <button 
            type="submit" 
            className="btn btn-primary w-full mt-4" 
            style={{ padding: '12px' }}
            disabled={loading}
          >
            {loading ? loadingText : (isLogin ? 'Sign In' : 'Sign Up')}
          </button>
        </form>

        <div className="text-center mt-6 text-sm text-muted">
          {isLogin ? "Don't have an account? " : "Already have an account? "}
          <button 
            type="button" 
            onClick={() => setIsLogin(!isLogin)} 
            style={{ background: 'none', border: 'none', color: 'var(--primary-color)', cursor: 'pointer', fontWeight: 600, fontSize: '0.875rem' }}
          >
            {isLogin ? 'Sign up' : 'Sign in'}
          </button>
        </div>

        {/* Demo credentials hint */}
        <div className="mt-8 pt-6 text-center text-xs" style={{ borderTop: '1px solid var(--border-color)', color: '#64748b' }}>
          <strong>Demo Accounts:</strong><br/>
          Admin: admin@jobtracker.com / admin123<br/>
          User: user@jobtracker.com / user123
        </div>
      </div>
    </div>
  );
};

export default Auth;
