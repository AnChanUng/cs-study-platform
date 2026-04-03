import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

export default function Home() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/categories')
      .then(res => setCategories(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <>
      <div className="hero">
        <h1>CS Interview Prep</h1>
        <p>CS interview questions by category, practice and track your progress</p>
      </div>
      <div className="categories-grid">
        {categories.map(cat => (
          <Link to={`/category/${cat.slug}`} key={cat.id} className="category-card">
            <div className="category-icon">{cat.iconEmoji}</div>
            <h3>{cat.name}</h3>
            <p>{cat.description}</p>
          </Link>
        ))}
      </div>
    </>
  );
}
