import React, { useState } from 'react';
import './DocumentHistory.css';

const DocumentHistory = () => {
  // Sample data - replace with actual data from your backend
  const [documents, setDocuments] = useState([
    {
      id: 1,
      name: 'Financial_Report_2023.pdf',
      uploadDate: '2023-12-01',
      size: 2450,
      status: 'Analyzed'
    },
    {
      id: 2,
      name: 'Contract_Agreement.docx',
      uploadDate: '2023-12-02',
      size: 856,
      status: 'Processing'
    },
    {
      id: 3,
      name: 'Research_Paper.pdf',
      uploadDate: '2023-12-03',
      size: 3200,
      status: 'Analyzed'
    }
  ]);

  const handleView = (id) => {
    console.log('Viewing document:', id);
    // TODO: Implement view logic
  };

  const handleDelete = (id) => {
    if (window.confirm('Are you sure you want to delete this document?')) {
      setDocuments(documents.filter(doc => doc.id !== id));
    }
  };

  const getFileIcon = (filename) => {
    if (filename.endsWith('.pdf')) return '📄';
    if (filename.endsWith('.doc') || filename.endsWith('.docx')) return '📝';
    if (filename.endsWith('.txt')) return '📃';
    return '📄';
  };

  const getStatusClass = (status) => {
    return status === 'Analyzed' ? 'status-analyzed' : 'status-processing';
  };

  return (
    <div className="history-page">
      <div className="history-container">
        <div className="history-header">
          <h1>Document History</h1>
          <p className="subtitle">View and manage your uploaded documents</p>
        </div>

        {documents.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">📭</div>
            <p>No documents uploaded yet</p>
            <p className="empty-subtext">Upload your first document to get started</p>
          </div>
        ) : (
          <div className="documents-list">
            {documents.map((doc) => (
              <div key={doc.id} className="document-card">
                <div className="document-icon">
                  {getFileIcon(doc.name)}
                </div>
                <div className="document-info">
                  <h3 className="document-name">{doc.name}</h3>
                  <div className="document-meta">
                    <span className="document-date">
                      📅 {new Date(doc.uploadDate).toLocaleDateString()}
                    </span>
                    <span className="document-size">
                      💾 {(doc.size / 1024).toFixed(2)} KB
                    </span>
                    <span className={`document-status ${getStatusClass(doc.status)}`}>
                      {doc.status}
                    </span>
                  </div>
                </div>
                <div className="document-actions">
                  <button 
                    onClick={() => handleView(doc.id)} 
                    className="view-btn"
                    title="View document"
                  >
                    View
                  </button>
                  <button 
                    onClick={() => handleDelete(doc.id)} 
                    className="delete-btn"
                    title="Delete document"
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default DocumentHistory;