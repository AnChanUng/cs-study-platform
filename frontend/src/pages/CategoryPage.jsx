import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api';

const difficultyClass = (d) => `badge badge-${d.toLowerCase()}`;

export default function CategoryPage() {
  const { slug } = useParams();
  const [questions, setQuestions] = useState([]);
  const [category, setCategory] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.get(`/categories/${slug}`),
      api.get(`/questions/category/${slug}`),
    ])
      .then(([catRes, qRes]) => {
        setCategory(catRes.data);
        setQuestions(qRes.data);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [slug]);

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <>
      <div className="page-header">
        <Link to="/" className="back-link">← Back</Link>
        {category && (
          <>
            <h1>{category.iconEmoji} {category.name}</h1>
            <p>{category.description}</p>
          </>
        )}
      </div>
      {questions.length === 0 ? (
        <div className="empty">No questions yet</div>
      ) : (
        <div className="question-list">
          {questions.map(q => (
            <Link to={`/question/${q.id}`} key={q.id} className="question-item">
              <h3>{q.title}</h3>
              <div className="question-meta">
                <span className={difficultyClass(q.difficulty)}>{q.difficulty}</span>
                {q.studyCount > 0 && (
                  <span className="badge badge-study">Studied {q.studyCount}x</span>
                )}
                {q.bookmarked && (
                  <span className="badge badge-bookmark">Bookmarked</span>
                )}
              </div>
            </Link>
          ))}
        </div>
      )}
    </>
  );
}
