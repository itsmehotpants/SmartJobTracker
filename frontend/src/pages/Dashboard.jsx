import React, { useState, useEffect } from 'react';
import { Bookmark, ClipboardList, Briefcase, TrendingUp } from 'lucide-react';
import api from '../api';

const Dashboard = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const res = await api.get('/api/v1/users/dashboard');
        setData(res.data.data);
      } catch (err) {
        console.error("Failed to load dashboard:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchDashboard();
  }, []);

  if (loading) return <div className="page-wrapper flex justify-center items-center">Loading dashboard...</div>;
  if (!data) return <div className="page-wrapper flex justify-center items-center text-danger">Failed to load data</div>;

  return (
    <div className="page-wrapper container animate-fade-in">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h2>Welcome back, {data.profile.name.split(' ')[0]}! 👋</h2>
          <p>Here's an overview of your job hunting progress.</p>
        </div>
      </div>

      <div className="grid-cards mb-8">
        <div className="glass-card flex items-center justify-between">
          <div>
            <p className="text-sm font-semibold mb-1 color-wishlist">Total Bookmarks</p>
            <h2 className="mb-0">{data.totalBookmarks}</h2>
          </div>
          <div style={{ background: 'rgba(99, 102, 241, 0.1)', padding: '16px', borderRadius: '50%' }}>
            <Bookmark size={28} color="var(--primary-color)" />
          </div>
        </div>
        
        <div className="glass-card flex items-center justify-between">
          <div>
            <p className="text-sm font-semibold mb-1 color-applied">Total Applications</p>
            <h2 className="mb-0">{data.totalApplications}</h2>
          </div>
          <div style={{ background: 'rgba(96, 165, 250, 0.1)', padding: '16px', borderRadius: '50%' }}>
            <ClipboardList size={28} color="#60a5fa" />
          </div>
        </div>

        <div className="glass-card flex items-center justify-between">
          <div>
            <p className="text-sm font-semibold mb-1 text-muted">Platform Active Jobs</p>
            <h2 className="mb-0">{data.totalActiveJobs}</h2>
          </div>
          <div style={{ background: 'rgba(148, 163, 184, 0.1)', padding: '16px', borderRadius: '50%' }}>
            <Briefcase size={28} color="#94a3b8" />
          </div>
        </div>
      </div>

      <div className="grid-cards" style={{ gridTemplateColumns: '1fr 1fr' }}>
        <div className="glass-card">
          <div className="flex items-center gap-2 mb-4">
            <TrendingUp size={20} color="var(--text-main)" />
            <h3 className="mb-0">Applications Pipeline</h3>
          </div>
          {Object.keys(data.applicationsByStatus).length === 0 ? (
            <p className="text-sm text-center py-6">You haven't applied to any jobs yet.</p>
          ) : (
            <div className="flex flex-col gap-3 mt-4">
              {Object.entries(data.applicationsByStatus).map(([status, count]) => (
                <div key={status} className="flex justify-between items-center p-3 rounded-lg" style={{ background: 'rgba(0,0,0,0.2)' }}>
                  <span className={`text-sm font-medium color-${status.toLowerCase()}`}>{status}</span>
                  <span className="badge badge-gray">{count}</span>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="glass-card">
          <h3>Your Profile</h3>
          <div className="mt-4 flex flex-col gap-4">
            <div>
              <span className="text-sm text-muted">Name</span>
              <p className="font-medium text-main">{data.profile.name}</p>
            </div>
            <div>
              <span className="text-sm text-muted">Email</span>
              <p className="font-medium text-main">{data.profile.email}</p>
            </div>
            <div>
              <span className="text-sm text-muted">Role</span>
              <p className="font-medium text-main">
                <span className="badge badge-purple">{data.profile.role}</span>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
