import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
    return (
        <div className="container text-center pt-5">
            <h1 className="display-4 text-primary mb-3">CardMaster System</h1>
            <p className="lead text-muted mb-5">Enterprise Credit Card Management Platform</p>
            
            <div className="d-flex justify-content-center gap-3 mb-5">
                <Link to="/customer" className="btn btn-primary btn-lg px-5">Customer Login</Link>
                <Link to="/underwriter" className="btn btn-outline-primary btn-lg px-5">Underwriter Login</Link>
            </div>

            <div className="row mt-5">
                <div className="col-md-4">
                    <div className="card border-0 shadow-sm p-4 h-100">
                        <h4 className="text-primary">Issuance & Risk</h4>
                        <p className="text-muted">Streamlined application and underwriting flow.</p>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="card border-0 shadow-sm p-4 h-100 bg-primary text-white">
                        <h4>Real-time Billing</h4>
                        <p>Immediate transaction processing and generation.</p>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="card border-0 shadow-sm p-4 h-100">
                        <h4 className="text-primary">Secure Admin</h4>
                        <p className="text-muted">Complete control over fees and card products.</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Home;
