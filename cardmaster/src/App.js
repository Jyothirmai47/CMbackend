import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

import Home from './cardmaster/pages/Home';
import CustomerDashboard from './cardmaster/modules/customer/CustomerDashboard';
import UnderwriterDashboard from './cardmaster/modules/underwriter/UnderwriterDashboard';

const App = () => {
    // Basic Layout wrapper with a Topbar
    const Layout = ({ children }) => (
        <div className="bg-light min-vh-100">
            <nav className="navbar navbar-expand-lg navbar-dark bg-primary px-4 shadow-sm">
                <a className="navbar-brand fw-bold" href="/">CardMaster</a>
                <div className="ms-auto">
                    <button className="btn btn-outline-light btn-sm" onClick={() => { localStorage.clear(); window.location.href='/'; }}>Logout</button>
                </div>
            </nav>
            <div className="container-fluid py-4">{children}</div>
        </div>
    );

    return (
        <Router>
            <Routes>
                {/* Public */}
                <Route path="/" element={<Home />} />
                
                {/* Protected / Dashboard routes */}
                <Route path="/customer" element={<Layout><CustomerDashboard /></Layout>} />
                <Route path="/underwriter" element={<Layout><UnderwriterDashboard /></Layout>} />
                
                {/* Fallback */}
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </Router>
    );
}

export default App;
