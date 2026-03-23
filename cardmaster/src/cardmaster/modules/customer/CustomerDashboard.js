import React, { useState, useEffect } from 'react';
import apiService from '../../core/api/apiService';
import { SummaryCard, DataTable } from '../../core/components/UIComponents';

const CustomerDashboard = () => {
    const [formData, setFormData] = useState({ name: '', email: '', ssn: '', desiredLimit: '' });
    const [customerDetails, setCustomerDetails] = useState(null);
    const [transactions, setTransactions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            const response = await apiService.get('/api/customers/my-profile');
            setCustomerDetails(response.data.customer);
            setTransactions(response.data.transactions || []);
        } catch (error) {
            console.error("No active profile found.", error);
        }
    };

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true); setErrorMessage(''); setSuccessMessage('');
        try {
            await apiService.post('/api/customers/apply', formData);
            setSuccessMessage("Application submitted and account created successfully!");
            setFormData({ name: '', email: '', ssn: '', desiredLimit: '' });
            await fetchDashboardData();
        } catch (error) {
            setErrorMessage(error.response?.data?.message || "Failed to submit application.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="container-fluid py-4">
            <h2 className="mb-4 text-primary">Customer Workspace</h2>
            {successMessage && <div className="alert alert-success">{successMessage}</div>}
            {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

            {customerDetails && (
                <div className="row mb-4">
                    <SummaryCard title="Card Number" value={`**** ${customerDetails.cardNumber.slice(-4)}`} bg="bg-primary text-white" />
                    <SummaryCard title="Available Limit" value={`$${customerDetails.availableLimit}`} bg="bg-success text-white" />
                    <SummaryCard title="Current Balance" value={`$${customerDetails.balance || 0}`} bg="bg-warning text-dark" />
                </div>
            )}

            <div className="row">
                <div className="col-md-4">
                    <div className="card shadow-sm mb-4">
                        <div className="card-header bg-white"><h5 className="mb-0">Apply for a New Card</h5></div>
                        <div className="card-body">
                            <form onSubmit={handleSubmit}>
                                <div className="mb-3"><label>Full Name</label><input type="text" name="name" className="form-control" value={formData.name} onChange={handleInputChange} required /></div>
                                <div className="mb-3"><label>Email</label><input type="email" name="email" className="form-control" value={formData.email} onChange={handleInputChange} required /></div>
                                <div className="mb-3"><label>SSN</label><input type="text" name="ssn" className="form-control" value={formData.ssn} onChange={handleInputChange} required /></div>
                                <div className="mb-3"><label>Desired Limit ($)</label><input type="number" name="desiredLimit" className="form-control" value={formData.desiredLimit} onChange={handleInputChange} required /></div>
                                <button type="submit" className="btn btn-primary w-100" disabled={isLoading}>{isLoading ? 'Submitting...' : 'Submit Application'}</button>
                            </form>
                        </div>
                    </div>
                </div>
                <div className="col-md-8">
                    <div className="card shadow-sm">
                        <div className="card-header bg-white d-flex justify-content-between align-items-center">
                            <h5 className="mb-0">Recent Transactions</h5>
                            <button className="btn btn-sm btn-outline-primary">View Statements</button>
                        </div>
                        <div className="card-body p-0">
                            {transactions.length > 0 ? (
                                <DataTable headers={['Date', 'Merchant', 'Amount']} data={transactions} keys={['date', 'merchant', 'amount']} />
                            ) : (<div className="text-center p-4 text-muted">No transactions found.</div>)}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
export default CustomerDashboard;
