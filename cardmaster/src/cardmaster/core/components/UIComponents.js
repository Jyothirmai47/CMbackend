import React from 'react';

export const SummaryCard = ({ title, value, bg }) => (
    <div className="col-md-4">
        <div className={`card shadow-sm ${bg}`}>
            <div className="card-body">
                <h6 className="card-title opacity-75">{title}</h6>
                <h3 className="mb-0">{value}</h3>
            </div>
        </div>
    </div>
);

export const DataTable = ({ headers, data, keys }) => (
    <table className="table table-hover mb-0">
        <thead className="table-light">
            <tr>{headers.map((h, i) => <th key={i}>{h}</th>)}</tr>
        </thead>
        <tbody>
            {data.map((row, i) => (
                <tr key={i}>{keys.map((k, j) => <td key={j}>{row[k]}</td>)}</tr>
            ))}
        </tbody>
    </table>
);

export default SummaryCard;
