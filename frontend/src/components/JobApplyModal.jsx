import React, { useState } from 'react';
import { Sparkles, X, FileText, CheckCircle2 } from 'lucide-react';
import api from '../api';

const JobApplyModal = ({ job, onClose, onSuccess }) => {
  const [mode, setMode] = useState('STANDARD'); // 'STANDARD' or 'SMART'
  const [formData, setFormData] = useState({ notes: '', resumeLink: '', resumeText: '' });
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null); // Will hold the AI response

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    if (mode === 'SMART') {
      try {
        const res = await api.post('/api/v1/applications/auto-apply', {
          jobId: job.id,
          resumeText: formData.resumeText
        });
        setResult(res.data.data); // Capture AutoApplyResponse
      } catch (err) {
        alert(err.response?.data?.message || "Failed to auto-apply");
      } finally {
        setLoading(false);
      }
    } else {
      try {
        await api.post('/api/v1/applications', {
          jobId: job.id,
          notes: formData.notes,
          resumeLink: formData.resumeLink
        });
        alert("Standard application submitted!");
        onSuccess();
        onClose();
      } catch (err) {
        alert(err.response?.data?.message || "Failed to apply");
        setLoading(false);
      }
    }
  };

  return (
    <div className="modal-backdrop" style={{
      position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh',
      background: 'rgba(0,0,0,0.6)', backdropFilter: 'blur(4px)',
      display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000
    }}>
      <div className="glass-card animate-fade-in" style={{ width: '100%', maxWidth: '600px', margin: '20px', padding: '0', overflow: 'hidden' }}>
        
        {/* Header */}
        <div className="flex justify-between items-center p-6" style={{ borderBottom: '1px solid var(--border-color)' }}>
          <h3 className="mb-0">Apply to {job.company}</h3>
          <button onClick={onClose} style={{ background: 'none', border: 'none', color: 'var(--text-muted)', cursor: 'pointer' }}>
            <X size={24} />
          </button>
        </div>

        <div className="p-6">
          {!result ? (
            <>
              {/* Mode Toggle */}
              <div className="flex gap-4 mb-6" style={{ background: 'var(--bg-color)', padding: '6px', borderRadius: '12px' }}>
                <button 
                  type="button"
                  className={`flex-1 py-2 px-4 rounded-lg font-medium transition-all ${mode === 'STANDARD' ? 'bg-primary text-white' : 'text-muted'}`}
                  style={{ background: mode === 'STANDARD' ? 'var(--primary-color)' : 'transparent', border: 'none', cursor: 'pointer', color: mode === 'STANDARD' ? 'white' : 'var(--text-muted)' }}
                  onClick={() => setMode('STANDARD')}
                >
                  Standard Default
                </button>
                <button 
                  type="button"
                  className={`flex-1 py-2 px-4 rounded-lg font-medium transition-all flex items-center justify-center gap-2`}
                  style={{ background: mode === 'SMART' ? 'linear-gradient(135deg, #a855f7 0%, #6366f1 100%)' : 'transparent', border: 'none', cursor: 'pointer', color: mode === 'SMART' ? 'white' : 'var(--text-muted)' }}
                  onClick={() => setMode('SMART')}
                >
                  <Sparkles size={18} />
                  AI Smart Scan
                </button>
              </div>

              <form onSubmit={handleSubmit}>
                {mode === 'STANDARD' ? (
                  <div className="animate-fade-in">
                    <div className="input-group">
                      <label className="input-label">Resume Link (Optional)</label>
                      <input type="url" name="resumeLink" value={formData.resumeLink} onChange={handleChange} className="input-field" placeholder="https://drive.google.com/..." />
                    </div>
                    <div className="input-group">
                      <label className="input-label">Cover Letter / Notes (Optional)</label>
                      <textarea name="notes" value={formData.notes} onChange={handleChange} className="input-field" rows="4" placeholder="Tell them why you're a great fit..."></textarea>
                    </div>
                  </div>
                ) : (
                  <div className="animate-fade-in">
                    <p className="text-sm text-muted mb-4 flex items-start gap-2">
                      <Sparkles size={16} style={{ marginTop: '2px', color: '#a855f7' }} />
                      Our AI will scan your resume against the job description, calculate a fit score, and auto-apply internally.
                    </p>
                    <div className="input-group">
                      <label className="input-label flex items-center gap-2"><FileText size={18}/> Paste Resume Content</label>
                      <textarea 
                        name="resumeText" 
                        value={formData.resumeText} 
                        onChange={handleChange} 
                        className="input-field" 
                        rows="8" 
                        style={{ fontFamily: 'monospace', fontSize: '0.9rem' }}
                        placeholder="Paste plain text of your resume here..."
                        required
                      ></textarea>
                    </div>
                  </div>
                )}

                <div className="mt-8 flex justify-end gap-3">
                  <button type="button" onClick={onClose} className="btn btn-secondary">Cancel</button>
                  <button type="submit" disabled={loading} className="btn btn-primary">
                    {loading ? (mode === 'SMART' ? 'Scanning AI...' : 'Submitting...') : 'Submit Application'}
                  </button>
                </div>
              </form>
            </>
          ) : (
            // Results View
            <div className="animate-fade-in text-center py-4">
              <div className="flex justify-center mb-4">
                <div style={{ background: 'rgba(16, 185, 129, 0.2)', padding: '20px', borderRadius: '50%' }}>
                  <CheckCircle2 size={48} color="#10b981" />
                </div>
              </div>
              <h2 className="text-gradient">Application Submitted!</h2>
              
              <div className="mt-6 text-left" style={{ background: 'var(--bg-color)', padding: '20px', borderRadius: '12px' }}>
                <h4 className="flex items-center justify-between mb-4">
                  Match Score
                  <span className={`badge ${result.matchScore > 70 ? 'badge-green' : 'badge-yellow'}`} style={{ fontSize: '1rem' }}>
                    {result.matchScore}%
                  </span>
                </h4>
                <p className="text-sm text-muted mb-4">{result.analysisSummary}</p>
                
                {result.missingKeywords?.length > 0 && (
                  <div className="mb-3">
                    <span className="text-xs font-semibold color-rejected uppercase">Missing Keywords:</span>
                    <div className="flex flex-wrap gap-2 mt-2">
                      {result.missingKeywords.map(k => <span key={k} className="badge badge-red">{k}</span>)}
                    </div>
                  </div>
                )}
                
                {result.matchedKeywords?.length > 0 && (
                  <div>
                    <span className="text-xs font-semibold color-offered uppercase">Matched Hits:</span>
                    <div className="flex flex-wrap gap-2 mt-2">
                      {result.matchedKeywords.map(k => <span key={k} className="badge badge-green">{k}</span>)}
                    </div>
                  </div>
                )}
              </div>

              <button className="btn btn-primary w-full mt-6" onClick={() => { onSuccess(); onClose(); }}>
                Return to Jobs
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default JobApplyModal;
