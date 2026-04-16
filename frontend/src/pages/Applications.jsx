import React, { useState, useEffect } from 'react';
import { Building, Calendar, Edit2, Link as LinkIcon } from 'lucide-react';
import api from '../api';

const Applications = () => {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchApplications = async () => {
    try {
      const res = await api.get('/api/v1/applications');
      setApplications(res.data.data.content);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchApplications();
  }, []);

  const handleUpdateStatus = async (id, currentStatus) => {
    const statuses = ['WISHLIST', 'APPLIED', 'SCREENING', 'INTERVIEW', 'OFFERED', 'ACCEPTED', 'REJECTED', 'WITHDRAWN'];
    const newStatus = prompt(`Current status is ${currentStatus}. Enter new status:\nOptions: ${statuses.join(', ')}`, currentStatus);
    
    if (newStatus && statuses.includes(newStatus.toUpperCase())) {
      try {
        await api.patch(`/api/v1/applications/${id}/status`, {
          status: newStatus.toUpperCase(),
          notes: "Status updated from pipeline"
        });
        fetchApplications(); // Refresh list to get updated metadata
      } catch (err) {
        alert(err.response?.data?.message || "Failed to update status");
      }
    } else if (newStatus) {
      alert("Invalid status provided");
    }
  };

  const handleExportCsv = async () => {
    const res = await api.get('/api/v1/applications/export', { responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'my_applications.csv');
    document.body.appendChild(link);
    link.click();
  };

  return (
    <div className="page-wrapper container animate-fade-in">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h2>My Pipeline</h2>
          <p>Track your applications and move them through the funnel.</p>
        </div>
        <button onClick={handleExportCsv} className="btn btn-secondary">
          Export full CSV
        </button>
      </div>

      {loading ? (
        <div className="text-center py-12">Loading pipeline...</div>
      ) : applications.length === 0 ? (
        <div className="glass-card text-center py-12">
          <p className="mb-4">Your application pipeline is currently empty.</p>
          <a href="/jobs" className="btn btn-primary">Find Jobs</a>
        </div>
      ) : (
        <div className="glass-card p-0" style={{ overflow: 'hidden' }}>
          <table className="w-full text-left" style={{ borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid var(--border-color)', backgroundColor: 'rgba(0,0,0,0.2)' }}>
                <th className="p-4 font-medium text-sm text-muted">Role & Company</th>
                <th className="p-4 font-medium text-sm text-muted">Applied On</th>
                <th className="p-4 font-medium text-sm text-muted">Current Status</th>
                <th className="p-4 font-medium text-sm text-muted">Actions</th>
              </tr>
            </thead>
            <tbody>
              {applications.map(app => (
                <tr key={app.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
                  <td className="p-4">
                    <p className="font-semibold mb-1" style={{ color: 'var(--text-main)' }}>{app.job.title}</p>
                    <p className="text-xs text-muted flex items-center gap-1"><Building size={12}/> {app.job.company}</p>
                  </td>
                  <td className="p-4">
                    <p className="text-sm flex items-center gap-1"><Calendar size={14}/> {new Date(app.appliedDate).toLocaleDateString()}</p>
                  </td>
                  <td className="p-4">
                    <span className={`badge border color-${app.status.toLowerCase()}`} style={{ borderColor: 'currentColor', background: 'transparent' }}>
                      {app.status}
                    </span>
                    <p className="text-xs text-muted mt-1">Last updated: {new Date(app.lastUpdated).toLocaleDateString()}</p>
                  </td>
                  <td className="p-4">
                    <div className="flex items-center gap-2">
                      <button 
                        onClick={() => handleUpdateStatus(app.id, app.status)}
                        className="btn btn-secondary" 
                        style={{ padding: '6px 10px', fontSize: '0.8rem' }}
                        title="Update Status"
                      >
                        <Edit2 size={14} /> Update
                      </button>
                      {app.resumeLink && (
                        <a href={app.resumeLink} target="_blank" rel="noreferrer" className="btn" style={{ padding: '6px', background: 'var(--border-color)', color: 'white' }}>
                          <LinkIcon size={14} />
                        </a>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default Applications;
