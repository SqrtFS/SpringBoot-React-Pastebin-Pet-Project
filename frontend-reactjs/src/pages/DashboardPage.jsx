import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Dashboard.css';

const DashboardPage = () => {
  const [posts, setPosts] = useState([]);
  const [newPostText, setNewPostText] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  
  const navigate = useNavigate();
  const token = localStorage.getItem('token');


  useEffect(() => {
    if (!token) {
      navigate('/login');
      return;
    }
    fetchPosts();
  }, [token, navigate]);

  const fetchPosts = async () => {
    try {
      const response = await fetch('/api/posts', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.status === 403 || response.status === 401) {
        handleLogout();
        return;
      }

      if (response.ok) {
        const data = await response.json();
        setPosts(data);
      } else {
        setError('Failed to load posts');
      }
    } catch (err) {
      setError('Could not connect to server');
    } finally {
      setLoading(false);
    }
  };

  // 3. Create New Post
  const handleCreatePost = async (e) => {
    e.preventDefault();
    if (!newPostText.trim()) return;

    try {
      const formData = new FormData();
      formData.append('text', newPostText);

      const response = await fetch('/api/posts', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
        body: formData,
      });

      if (response.ok) {
        setNewPostText(''); 
        fetchPosts(); 
      } else {
        alert('Failed to create post');
      }
    } catch (err) {
      alert('Error creating post');
    }
  };

  // 4. Delete Post
  const handleDeletePost = async (id) => {
    if (!window.confirm("Are you sure you want to delete this paste?")) return;

    try {
      const response = await fetch(`/api/posts/${id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        setPosts(posts.filter(post => post.id !== id));
      }
    } catch (err) {
      console.error("Error deleting post", err);
    }
  };

  // 5. Download Post (НОВАЯ ФУНКЦИЯ)
  const handleDownload = async (id, title) => {
    try {
        const response = await fetch(`/api/posts/${id}/content`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (response.ok) {
            const blob = await response.blob();
            
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
        
            link.setAttribute('download', `paste-${id}.txt`);
            
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
        } else {
            alert("Failed to download file. It might not exist in Minio anymore.");
        }
    } catch (error) {
        console.error("Download error:", error);
        alert("Error downloading file");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <nav className="navbar">
        <div className="nav-brand">Pastebin Dashboard</div>
        <div className="nav-user">
          <button onClick={handleLogout} className="logout-btn">Logout</button>
        </div>
      </nav>

      <div className="content-wrapper">
        {/* Left Side: Create Post */}
        <div className="editor-section">
          <div className="card">
            <h3>Create New Post</h3>
            <form onSubmit={handleCreatePost}>
              <textarea
                className="paste-input"
                placeholder="Paste your code or text here..."
                value={newPostText}
                onChange={(e) => setNewPostText(e.target.value)}
                required
              ></textarea>
              <button type="submit" className="create-btn">Save Paste</button>
            </form>
          </div>
        </div>

        {/* Right Side: Post History */}
        <div className="history-section">
          <h3>Your Posts</h3>
          {loading ? (
            <p>Loading...</p>
          ) : error ? (
            <p className="error-text">{error}</p>
          ) : posts.length === 0 ? (
            <p className="empty-text">No posts found. Create one!</p>
          ) : (
            <div className="post-list">
              {posts.map((post) => (
                <div key={post.id} className="post-card">
                  <div className="post-header">
                    <span className="post-title">{post.title || `Paste #${post.id}`}</span>
                    <button 
                      className="delete-btn"
                      onClick={() => handleDeletePost(post.id)}
                      title="Delete paste"
                    >
                      ×
                    </button>
                  </div>
                  <p className="post-desc">{post.description}...</p>
                  
                  <div className="post-actions" style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                     <span className="post-date">ID: {post.id}</span>
                     
                     {/* Кнопка скачивания */}
                     <button 
                        className="download-btn" 
                        onClick={() => handleDownload(post.id, post.title)}
                        style={{
                            background: '#28a745', 
                            color: 'white', 
                            border: 'none', 
                            padding: '5px 10px', 
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontSize: '0.8rem'
                        }}
                     >
                        Download .txt
                     </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;