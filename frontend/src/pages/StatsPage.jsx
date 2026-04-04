import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';
import { hardcodedCategories, hardcodedQuestions } from '../hardcodedData';

const difficultyLabel = {
  BASIC: '기초',
  INTERMEDIATE: '중급',
  ADVANCED: '심화',
};

export default function StatsPage() {
  const [stats, setStats] = useState(null);
  const [gradingStats, setGradingStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fallbackStats = () => {
      const byCategory = {};
      const byDifficulty = {};
      hardcodedQuestions.forEach(q => {
        const catName = hardcodedCategories.find(c => c.slug === q.categorySlug)?.name || q.categorySlug;
        byCategory[catName] = (byCategory[catName] || 0) + 1;
        byDifficulty[q.difficulty] = (byDifficulty[q.difficulty] || 0) + 1;
      });
      return { totalQuestions: hardcodedQuestions.length, studiedQuestions: 0, byCategory, byDifficulty };
    };

    Promise.all([
      api.get('/stats'),
      api.get('/grading/stats'),
    ])
      .then(([sRes, gRes]) => {
        setStats(sRes.data?.totalQuestions ? sRes.data : fallbackStats());
        setGradingStats(gRes.data);
      })
      .catch(() => {
        setStats(fallbackStats());
        setGradingStats(null);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="loading">
        <div className="loading-spinner" />
        불러오는 중...
      </div>
    );
  }

  const maxCat = stats?.byCategory
    ? Math.max(...Object.values(stats.byCategory), 1)
    : 1;

  const progressPercent = stats?.totalQuestions
    ? Math.round((stats.studiedQuestions / stats.totalQuestions) * 100)
    : 0;

  return (
    <div className="page-bottom fade-in">
      <div className="page-header">
        <Link to="/" className="back-link">← 홈으로</Link>
        <h1>📊 학습 통계</h1>
        <p>나의 학습 현황을 한 눈에 확인하세요</p>
      </div>

      {/* Overview Stats */}
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-value">{stats?.totalQuestions || 0}</div>
          <div className="stat-label">전체 질문</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{stats?.studiedQuestions || 0}</div>
          <div className="stat-label">학습 완료</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{gradingStats?.totalAnswered || 0}</div>
          <div className="stat-label">채점 완료</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">
            {gradingStats?.averageScore
              ? gradingStats.averageScore.toFixed(1)
              : '-'}
          </div>
          <div className="stat-label">평균 점수</div>
        </div>
      </div>

      {/* Progress */}
      {stats?.totalQuestions > 0 && (
        <div className="stats-section">
          <h2>학습 진행률</h2>
          <div className="bar-chart">
            <div className="bar-row">
              <div className="bar-label">진행률</div>
              <div className="bar-track">
                <div
                  className="bar-fill"
                  style={{
                    width: `${Math.max(progressPercent, 5)}%`,
                    background: progressPercent >= 80
                      ? 'var(--accent-green)'
                      : progressPercent >= 50
                        ? 'var(--accent-blue)'
                        : 'var(--accent-orange)',
                  }}
                >
                  {progressPercent}%
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* By Category */}
      {stats?.byCategory && Object.keys(stats.byCategory).length > 0 && (
        <div className="stats-section">
          <h2>카테고리별 질문 수</h2>
          <div className="bar-chart">
            {Object.entries(stats.byCategory).map(([name, count]) => (
              <div className="bar-row" key={name}>
                <div className="bar-label">{name}</div>
                <div className="bar-track">
                  <div
                    className="bar-fill"
                    style={{ width: `${(count / maxCat) * 100}%` }}
                  >
                    {count}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* By Difficulty */}
      {stats?.byDifficulty && Object.keys(stats.byDifficulty).length > 0 && (
        <div className="stats-section">
          <h2>난이도별</h2>
          <div className="difficulty-cards">
            {Object.entries(stats.byDifficulty).map(([diff, count]) => (
              <div className="difficulty-card" key={diff}>
                <div className="stat-value" style={{
                  color: diff === 'BASIC'
                    ? 'var(--accent-green)'
                    : diff === 'INTERMEDIATE'
                      ? 'var(--accent-orange)'
                      : 'var(--accent-red)',
                }}>
                  {count}
                </div>
                <div className="stat-label">{difficultyLabel[diff] || diff}</div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Grade Distribution */}
      {gradingStats?.gradeDistribution && (
        <div className="stats-section">
          <h2>등급 분포</h2>
          <div className="grade-cards">
            {['A', 'B', 'C', 'D', 'F'].map(grade => (
              <div className="grade-card" key={grade}>
                <div className={`stat-value grade-${grade}`}>
                  {gradingStats.gradeDistribution[grade] || 0}
                </div>
                <div className="stat-label">{grade}등급</div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Recent Scores */}
      {gradingStats?.recentScores?.length > 0 && (
        <div className="stats-section">
          <h2>최근 채점 기록</h2>
          <div className="bar-chart">
            {gradingStats.recentScores.slice(0, 10).map((score, idx) => (
              <div className="bar-row" key={idx}>
                <div className="bar-label">#{idx + 1}</div>
                <div className="bar-track">
                  <div
                    className="bar-fill"
                    style={{
                      width: `${score}%`,
                      background: score >= 80
                        ? 'var(--accent-green)'
                        : score >= 60
                          ? 'var(--accent-blue)'
                          : score >= 40
                            ? 'var(--accent-orange)'
                            : 'var(--accent-red)',
                    }}
                  >
                    {score}점
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
