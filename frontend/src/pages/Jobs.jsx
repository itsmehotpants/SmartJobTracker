import React, { useState, useEffect } from 'react';
import { Search, MapPin, Building, Clock, ChevronRight, Bookmark } from 'lucide-react';
import api from '../api';
import JobApplyModal from '../components/JobApplyModal';

const Jobs = () => {
  const [jobs, setJobs] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  
  // Filtering & Sorting State
  const [jobType, setJobType] = useState('');
  const [sortBy, setSortBy] = useState('createdAt');
  
  // Modal State
  const [selectedJob, setSelectedJob] = useState(null);

  const fetchJobs = async (searchKw = '') => {
    setLoading(true);
    try {
      const typeParam = jobType ? `&jobType=${jobType}` : '';
      let endpoint = searchKw 
        ? `/api/v1/jobs/search?keyword=${encodeURIComponent(searchKw)}&page=${page}&size=10&sort=${sortBy},DESC${typeParam}`
        : `/api/v1/jobs?page=${page}&size=10&sort=${sortBy},DESC${typeParam}`;
      
      const res = await api.get(endpoint);
      setJobs(res.data.data.content);
      setError(null);
    } catch (err) {
      console.error(err);
      setError("Failed to fetch jobs");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchJobs(keyword);
  }, [page, jobType, sortBy]);

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0); // Reset to first page
    fetchJobs(keyword);
  };

  const handleBookmark = async (jobId) => {
    try {
      await api.post('/api/v1/bookmarks/toggle', { jobId });
      alert("Bookmark toggled!"); // Basic alert for now, can be replaced by toast
    } catch (err) {
      alert("Failed to bookmark");
    }
  };

  const handleApply = (job) => {
    setSelectedJob(job);
  };

  return (
    <div className="page-wrapper container animate-fade-in">
      <div className="text-center mb-8">
        <h2>Discover Opportunities</h2>
        <p>Browse elite job listings curated just for you.</p>
      </div>

      <div className="glass-card mb-8" style={{ padding: '16px' }}>
        <form onSubmit={handleSearch} className="flex flex-col gap-4">
          <div className="flex gap-4">
            <div className="relative w-full flex items-center">
              <Search className="absolute ml-3 text-muted" size={20} />
              <input 
                type="text" 
                className="input-field" 
                style={{ paddingLeft: '40px', marginBottom: 0 }}
                placeholder="Search by title, skills, or company..." 
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
              />
            </div>
            <button type="submit" className="btn btn-primary" style={{ padding: '0 32px' }}>
              Search
            </button>
          </div>
          
          <div className="flex gap-4 mt-2">
            <select 
              className="input-field" 
              style={{ padding: '8px 12px', flex: 1 }}
              value={jobType}
              onChange={(e) => setJobType(e.target.value)}
            >
              <option value="">All Job Types</option>
              <option value="FULL_TIME">Full Time</option>
              <option value="PART_TIME">Part Time</option>
              <option value="CONTRACT">Contract</option>
              <option value="INTERNSHIP">Internship</option>
            </select>
            
            <select 
              className="input-field" 
              style={{ padding: '8px 12px', flex: 1 }}
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
            >
              <option value="createdAt">Newest First</option>
              <option value="title">Title (A-Z)</option>
              <option value="company">Company</option>
            </select>
          </div>
        </form>
      </div>

      {loading ? (
        <div className="text-center py-12">Loading jobs...</div>
      ) : error ? (
        <div className="text-center text-danger py-12">{error}</div>
      ) : jobs.length === 0 ? (
        <div className="text-center text-muted py-12">No jobs matched your criteria.</div>
      ) : (
        <div className="flex flex-col gap-4">
          {jobs.map(job => (
            <div key={job.id} className="glass-card flex p-6" style={{ transition: 'all 0.2s ease', position: 'relative' }}>
              <div className="flex-1">
                <div className="flex items-start justify-between mb-2">
                  <h3 className="mb-0" style={{ fontSize: '1.2rem', color: 'var(--primary-color)' }}>{job.title}</h3>
                  <button 
                    onClick={() => handleBookmark(job.id)}
                    style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)' }}
                    title="Toggle Bookmark"
                  >
                    <Bookmark size={20} />
                  </button>
                </div>
                
                <div className="flex items-center gap-4 text-sm text-muted mb-4">
                  <span className="flex items-center gap-1"><Building size={16} /> {job.company}</span>
                  <span className="flex items-center gap-1"><MapPin size={16} /> {job.location}</span>
                  <span className="flex items-center gap-1"><Clock size={16} /> {new Date(job.createdAt).toLocaleDateString()}</span>
                </div>
                
                <div className="flex gap-2 mb-4">
                  <span className="badge badge-purple">{job.jobType.replace('_', ' ')}</span>
                  <span className="badge badge-blue">{job.experienceLevel}</span>
                  <span className="badge badge-green">{job.salary}</span>
                </div>
                
                <p className="text-sm text-muted mb-4" style={{ display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                  {job.description}
                </p>
                
                <div className="flex gap-3" style={{ position: 'relative', zIndex: 10 }}>
                  <button onClick={() => handleApply(job)} className="btn btn-primary" style={{ padding: '8px 16px', fontSize: '0.85rem' }}>
                    Apply Now
                  </button>
                  {job.link && (
                    <a href={job.link} target="_blank" rel="noreferrer" className="btn btn-secondary" style={{ padding: '8px 16px', fontSize: '0.85rem' }}>
                      External Link <ChevronRight size={16} />
                    </a>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Modern Overlay Modal for applying */}
      {selectedJob && (
        <JobApplyModal 
          job={selectedJob} 
          onClose={() => setSelectedJob(null)} 
          onSuccess={() => fetchJobs(keyword)} 
        />
      )}
    </div>
  );
};

export default Jobs;
