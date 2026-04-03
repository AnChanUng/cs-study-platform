import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api';

const difficultyLabel = {
  BASIC: '기초',
  INTERMEDIATE: '중급',
  ADVANCED: '심화',
};

export default function QuestionPage() {
  const { id } = useParams();
  const [question, setQuestion] = useState(null);
  const [showAnswer, setShowAnswer] = useState(false);
  const [userAnswer, setUserAnswer] = useState('');
  const [gradeResult, setGradeResult] = useState(null);
  const [grading, setGrading] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    setShowAnswer(false);
    setUserAnswer('');
    setGradeResult(null);
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

  if (loading) {
    return (
      <div className="loading">
        <div className="loading-spinner" />
        불러오는 중...
      </div>
    );
  }

  if (!question) {
    return (
      <div className="empty">
        <div className="empty-icon">❓</div>
        질문을 찾을 수 없습니다
      </div>
    );
  }

  return (
    <div className="question-detail fade-in">
      <div className="page-header">
        <Link to={`/category/${question.categorySlug}`} className="back-link">
          ← {question.categoryName}
        </Link>
        <h1>{question.title}</h1>
        <div className="question-meta" style={{ marginTop: 4 }}>
          <span className={`badge badge-${question.difficulty.toLowerCase()}`}>
            {difficultyLabel[question.difficulty] || question.difficulty}
          </span>
          {question.studyCount > 0 && (
            <span className="badge badge-study">학습 {question.studyCount}회</span>
          )}
        </div>
      </div>

      <div className="action-row">
        <button
          className={`action-btn ${question.bookmarked ? 'active' : ''}`}
          onClick={handleBookmark}
        >
          {question.bookmarked ? '★ 북마크됨' : '☆ 북마크'}
        </button>
      </div>

      {question.content && (
        <div className="question-content">{question.content}</div>
      )}

      {/* Grading Section */}
      <div className="grading-section">
        <h2>✏️ 답변 작성</h2>
        <textarea
          className="answer-input"
          placeholder="여기에 답변을 작성하세요..."
          value={userAnswer}
          onChange={e => setUserAnswer(e.target.value)}
        />
        <button
          className="submit-btn"
          onClick={handleGrade}
          disabled={grading || !userAnswer.trim()}
        >
          {grading ? '채점 중...' : '채점하기'}
        </button>

        {gradeResult && (
          <div className={`grade-result grade-${gradeResult.grade}`}>
            <div className="grade-score-wrapper">
              <div className="grade-score-circle">
                {gradeResult.grade}
              </div>
              <div className="grade-points">{gradeResult.score}점</div>
            </div>
            <div className="grade-feedback">{gradeResult.feedback}</div>

            {gradeResult.matchedKeywords?.length > 0 && (
              <div className="keywords-section">
                <div className="keywords-label">✅ 포함된 키워드</div>
                <div className="keywords">
                  {gradeResult.matchedKeywords.map((k, i) => (
                    <span key={i} className="keyword-matched">{k}</span>
                  ))}
                </div>
              </div>
            )}
            {gradeResult.missedKeywords?.length > 0 && (
              <div className="keywords-section" style={{ marginTop: 14 }}>
                <div className="keywords-label">❌ 누락된 키워드</div>
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

      {/* Model Answer Section */}
      <div className="answer-section">
        <h2>📖 모범답안</h2>
        <button className="toggle-btn" onClick={() => setShowAnswer(!showAnswer)}>
          {showAnswer ? '모범답안 숨기기' : '모범답안 보기'}
        </button>
        {showAnswer && question.answer && (
          <div className="answer-box">
            {question.answer}
          </div>
        )}
      </div>
    </div>
  );
}
