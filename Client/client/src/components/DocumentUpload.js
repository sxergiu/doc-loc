import React, { useState, useRef } from 'react';
import './DocumentUpload.css';


const DocumentUpload = () => {
  const [dragActive, setDragActive] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const fileInputRef = useRef(null);

  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === "dragenter" || e.type === "dragover") {
      setDragActive(true);
    } else if (e.type === "dragleave") {
      setDragActive(false);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      setSelectedFile(e.dataTransfer.files[0]);
    }
  };

  const handleChange = (e) => {
    e.preventDefault();
    if (e.target.files && e.target.files[0]) {
      setSelectedFile(e.target.files[0]);
    }
  };

  const handleButtonClick = () => {
    fileInputRef.current?.click();
  };

  const handleCancel = () => {
    setSelectedFile(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  const handleUpload = () => {
    if (selectedFile) {
      // TODO: Implement upload logic
      console.log('Uploading:', selectedFile.name);
      alert(`Uploading: ${selectedFile.name}`);
    }
  };

  return (
    <div className="upload-page">
      <div className="upload-container">
        <div className="upload-header">
          <h1>Document Analysis</h1>
          <p className="subtitle">Upload your document for AI-powered analysis</p>
        </div>
        
        <div
          className={`drop-zone ${dragActive ? 'active' : ''}`}
          onDragEnter={handleDrag}
          onDragLeave={handleDrag}
          onDragOver={handleDrag}
          onDrop={handleDrop}
        >
          <input
            ref={fileInputRef}
            type="file"
            onChange={handleChange}
            style={{ display: 'none' }}
            accept=".pdf,.doc,.docx,.txt"
          />
          {selectedFile ? (
            <div className="file-info">
              <div className="file-icon">📄</div>
              <p className="file-name">{selectedFile.name}</p>
              <p className="file-size">{(selectedFile.size / 1024).toFixed(2)} KB</p>
              <div className="file-actions">
                <button onClick={handleCancel} className="cancel-btn">
                  ✕ Cancel
                </button>
                <button onClick={handleButtonClick} className="change-btn">
                  🔄 Choose Another
                </button>
              </div>
            </div>
          ) : (
            <div className="drop-zone-content">
              <div className="upload-icon">📁</div>
              <p className="main-text">Drag and drop your document here</p>
              <div className="divider">
                <span>or</span>
              </div>
              <button onClick={handleButtonClick} className="choose-btn">
                Choose Document
              </button>
              <p className="supported-formats">Supported formats: PDF, DOC, DOCX, TXT</p>
            </div>
          )}
        </div>
        
        {selectedFile && (
          <button onClick={handleUpload} className="upload-btn">
            <span>⬆</span> Upload & Analyze Document
          </button>
        )}
      </div>
    </div>
  );
};

export default DocumentUpload;