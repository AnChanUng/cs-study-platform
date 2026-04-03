import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api';

export default function QuestionPage() {
  const { id } = useParams();
  const [question, setQuestion] = useState(null);
  const [showAnswer, setShowAnswer] = useState(false);
  const [userAnswer, setUserAnswer] = useState('');
  const [gradeResult, setGradeResult] = useState(null);
  const [grading, setGrading] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get(`/questions/${id}`)
      .then(res => setQuestion(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
    api.post(`/questions/${id}/study`).catch(() => {});
  }, [id]);

  const handleGrade = async () => {
    if (!userAnswer.trim()) return;
    setGrading(true);
    try {
      const res = await api.post(`/questions/${id}/grade`, { userAnswer });
      setGradeResult(res.data);
    } catch (e) {
      console.error(e);
    } finally {
      setGrading(false);
    }
  };

  const handleBookmark = async () => {
    try {
      const res = await api.post(`/questions/${id}/bookmark`);
      setQuestion(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (!question) return <div className="empty">Question not found</div>;

  return (
    <div className="question-detail">
      <div className="page-header">
        <Link to={`/category/${question.categorySlug}`} className="back-link">
          ← {question.categoryName}
        </Link>
        <h1>{question.title}</h1>
        <div className="question-meta">
          <span className={`badge badge-${question.difficulty.toLowerCase()}`}>
            {question.difficulty}
          </span>
          <span className="badge badge-study">Studied {question.studyCount}x</span>
        </div>
      </div>

      <div className="action-row">
        <button
          className={`action-btn ${question.bookmarked ? 'active' : ''}`}
          onClick={handleBookmark}
        >
          {question.bookmarked ? '★ Bookmarked' : '☆ Bookmark'}
        </button>
      </div>

      {question.content && (
        <div className="question-content">{question.content}</div>
      )}

      <div className="grading-section">
        <h2>Write your answer</h2>
        <textarea
          className="answer-input"
          placeholder="Write your answer here..."
          value={userAnswer}
          onChange={e => setUserAnswer(e.target.value)}
        />
        <button
          className="submit-btn"
          onClick={handleGrade}
          disabled={grading || !userAnswer.trim()}
        >
          {grading ? 'Grading...' : 'Submit Answer'}
        </button>

        {gradeResult && (
          <div className="grade-result">
            <div className={`grade-score grade-${gradeResult.grade}`}>
              {gradeResult.grade} ({gradeResult.score}pts)
            </div>
            <div className="grade-feedback">{gradeResult.feedback}</div>
            {gradeResult.matchedKeywords?.length > 0 && (
              <div>
                <strong>Matched keywords:</strong>
                <div className="keywords">
                  {gradeResult.matchedKeywords.map((k, i) => (
                    <span key={i} className="keyword-matched">{k}</span>
                  ))}
                </div>
              </div>
            )}
            {gradeResult.missedKeywords?.length > 0 && (
              <div style={{ marginTop: 12 }}>
                <strong>Missed keywords:</strong>
                <div className="keywords">
                  {gradeResult.missedKeywords.map((k, i) => (
                    <span key={i} className="keyword-missed">{k}</span>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}
      </div>

      <div className="answer-section">
        <h2>Model Answer</h2>
        <button className="toggle-btn" onClick={() => setShowAnswer(!showAnswer)}>
          {showAnswer ? 'Hide Answer' : 'Show Answer'}
        </button>
        {showAnswer && question.answer && (
          <div className="answer-box" style={{ marginTop: 12 }}>
            {question.answer}
          </div>
        )}
      </div>
    </div>
  );
}
