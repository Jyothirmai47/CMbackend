import React, { useState, useEffect } from 'react';
import apiService from '../../core/api/apiService';

const UnderwriterDashboard = () => {
    const [applications, setApplications] = useState([]);
    const [notes, setNotes] = useState('');
    const MAX_CHARS = 250;

    useEffect(() => {
        apiService.get('/api/underwriting/pending').then(res => setApplications(res.data)).catch(err => console.log(err));
    }, []);

    const processDecision = (appId, decision) => {
        apiService.post(`/api/underwriting/${appId}/decision`, { decision, notes })
            .then(() => {
                alert(`Application ${decision}`);
                setApplications(apps => apps.filter(app => app.id !== appId));
                setNotes('');
            });
    }

    return (
        <div className="container-fluid py-4">
            <h2 className="mb-4 text-primary">Underwriter Review Queue</h2>
            {applications.length === 0 && <p className="text-muted">No pending applications.</p>}
            {applications.map(app => (
                <div key={app.id} className="card mb-3 shadow-sm">
                    <div className="card-body d-flex justify-content-between">
                        <div>
                            <h5>{app.customerName} - {app.productType}</h5>
                            <span className="badge bg-info text-dark">Credit Score: {app.creditScore}</span>
                        </div>
                        <div style={{width: '350px'}}>
                            <label className="form-label d-flex justify-content-between">
                                <span>Underwriter Notes</span>
                                <small className="text-muted">{notes.length}/{MAX_CHARS}</small>
                            </label>
                            <textarea className="form-control mb-2" rows="2" value={notes} maxLength={MAX_CHARS} onChange={(e) => setNotes(e.target.value)} />
                            <div className="d-flex gap-2">
                                <button className="btn btn-success w-50" onClick={() => processDecision(app.id, 'APPROVED')}>Approve</button>
                                <button className="btn btn-danger w-50" onClick={() => processDecision(app.id, 'REJECTED')}>Reject</button>
                            </div>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};
export default UnderwriterDashboard;
