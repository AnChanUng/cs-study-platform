import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api';
import { hardcodedCategories, hardcodedQuestions } from '../hardcodedData';

const difficultyLabel = {
  BASIC: '기초',
  INTERMEDIATE: '중급',
  ADVANCED: '심화',
};

export default function CategoryPage() {
  const { slug } = useParams();
  const [questions, setQuestions] = useState([]);
  const [category, setCategory] = useState(null);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('ALL');

  useEffect(() => {
    setLoading(true);
    Promise.all([
      api.get(`/categories/${slug}`),
      api.get(`/questions/category/${slug}`),
    ])
      .then(([catRes, qRes]) => {
        setCategory(catRes.data || hardcodedCategories.find(c => c.slug === slug));
        setQuestions(qRes.data?.length ? qRes.data : hardcodedQuestions.filter(q => q.categorySlug === slug));
      })
      .catch(() => {
        setCategory(hardcodedCategories.find(c => c.slug === slug) || null);
        setQuestions(hardcodedQuestions.filter(q => q.categorySlug === slug));
      })
      .finally(() => setLoading(false));
  }, [slug]);

  if (loading) {
    return (
      <div className="loading">
        <div className="loading-spinner" />
        불러오는 중...
      </div>
    );
  }

  const filtered = filter === 'ALL'
    ? questions
    : questions.filter(q => q.difficulty === filter);

  const diffCounts = questions.reduce((acc, q) => {
    acc[q.difficulty] = (acc[q.difficulty] || 0) + 1;
    return acc;
  }, {});

  return (
    <div className="fade-in">
      <div className="page-header">
        <Link to="/" className="back-link">← 홈으로</Link>
        {category && (
          <>
            <h1>{category.iconEmoji} {category.name}</h1>
            <p>{category.description}</p>
          </>
        )}
      </div>

      <div className="filter-row">
        <button
          className={`filter-chip ${filter === 'ALL' ? 'active' : ''}`}
          onClick={() => setFilter('ALL')}
        >
          전체 {questions.length}
        </button>
        {['BASIC', 'INTERMEDIATE', 'ADVANCED'].map(d => (
          diffCounts[d] ? (
            <button
              key={d}
              className={`filter-chip ${filter === d ? 'active' : ''}`}
              onClick={() => setFilter(d)}
            >
              {difficultyLabel[d]} {diffCounts[d]}
            </button>
          ) : null
        ))}
      </div>

      {filtered.length === 0 ? (
        <div className="empty">
          <div className="empty-icon">📋</div>
          아직 질문이 없습니다
        </div>
      ) : (
        <div className="question-list">
          {filtered.map((q, idx) => (
            <Link to={`/question/${q.id}`} key={q.id} className="question-item">
              <div className="question-number">{idx + 1}</div>
              <div className="question-item-body">
                <h3>{q.title}</h3>
                <div className="question-meta">
                  <span className={`badge badge-${q.difficulty.toLowerCase()}`}>
                    {difficultyLabel[q.difficulty] || q.difficulty}
                  </span>
                  {q.studyCount > 0 && (
                    <span className="badge badge-study">
                      학습 {q.studyCount}회
                    </span>
                  )}
                  {q.bookmarked && (
                    <span className="badge badge-bookmark">북마크</span>
                  )}
                </div>
              </div>
              <span className="question-item-arrow">›</span>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
