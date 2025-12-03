import React, { useState } from 'react';
import DocumentUpload from './components/DocumentUpload';
import DocumentHistory from './components/DocumentHistory';
import AnimatedBackground from './components/AnimatedBackground';
import './App.css';

function App() {
  const [currentPage, setCurrentPage] = useState('upload');

  return (
    <div className="App">
      <AnimatedBackground />
      <nav className="app-nav">
        <button 
          onClick={() => setCurrentPage('upload')}
          className={currentPage === 'upload' ? 'active' : ''}
        >
          Upload
        </button>
        <button 
          onClick={() => setCurrentPage('history')}
          className={currentPage === 'history' ? 'active' : ''}
        >
          History
        </button>
      </nav>
      
      {currentPage === 'upload' ? <DocumentUpload /> : <DocumentHistory />}
    </div>
  );
}

export default App;
