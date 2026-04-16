import React, { useState, useEffect } from 'react';
import { BookmarkMinus, MapPin, Building } from 'lucide-react';
import api from '../api';

const Bookmarks = () => {
  const [bookmarks, setBookmarks] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchBookmarks = async () => {
    try {
      const res = await api.get('/api/v1/bookmarks');
      setBookmarks(res.data.data.content);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBookmarks();
  }, []);

  const handleRemove = async (id) => {
    try {
      await api.delete(`/api/v1/bookmarks/${id}`);
      fetchBookmarks(); // Refresh list
    } catch (err) {
      alert("Failed to remove bookmark");
    }
  };

  const handleExportCsv = async () => {
    const res = await api.get('/api/v1/bookmarks/export', { responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'my_bookmarks.csv');
    document.body.appendChild(link);
    link.click();
  };

  return (
    <div className="page-wrapper container animate-fade-in">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h2>Saved Jobs</h2>
          <p>Jobs you've bookmarked for later consideration.</p>
        </div>
        <button onClick={handleExportCsv} className="btn btn-secondary">
          Export CSV
        </button>
      </div>

      {loading ? (
        <div className="text-center py-12">Loading bookmarks...</div>
      ) : bookmarks.length === 0 ? (
        <div className="glass-card text-center py-12">
          <p className="mb-4">You have no saved jobs.</p>
          <a href="/jobs" className="btn btn-primary">Discover Jobs</a>
        </div>
      ) : (
        <div className="grid-cards">
          {bookmarks.map(bookmark => (
            <div key={bookmark.id} className="glass-card flex flex-col justify-between">
              <div>
                <h3 className="mb-2" style={{ fontSize: '1.2rem', color: 'var(--primary-color)' }}>{bookmark.job.title}</h3>
                <div className="flex items-center gap-3 text-sm text-muted mb-4">
                  <span className="flex items-center gap-1"><Building size={14} /> {bookmark.job.company}</span>
                  <span className="flex items-center gap-1"><MapPin size={14} /> {bookmark.job.location}</span>
                </div>
                
                <p className="text-sm text-main mb-4" style={{ background: 'rgba(0,0,0,0.2)', padding: '12px', borderRadius: '8px' }}>
                  <strong>Notes:</strong> {bookmark.notes || "No notes provided."}
                </p>
                <p className="text-xs text-muted mb-6">
                  Saved on: {new Date(bookmark.createdAt).toLocaleDateString()}
                </p>
              </div>
              
              <div className="flex justify-between items-center">
                <a href={`/jobs`} className="btn btn-primary" style={{ padding: '8px 16px', fontSize: '0.85rem' }}>View the Job</a>
                <button 
                  onClick={() => handleRemove(bookmark.id)} 
                  className="btn text-danger pl-0 pr-0" 
                  style={{ background: 'transparent' }}
                  title="Remove from bookmarks"
                >
                  <BookmarkMinus size={20} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Bookmarks;
