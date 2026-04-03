import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

export default function StatsPage() {
  const [stats, setStats] = useState(null);
  const [gradingStats, setGradingStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.get('/stats'),
      api.get('/grading/stats'),
    ])
      .then(([sRes, gRes]) => {
        setStats(sRes.data);
        setGradingStats(gRes.data);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="loading">Loading...</div>;

  const maxCat = stats?.byCategory
    ? Math.max(...Object.values(stats.byCategory), 1)
    : 1;

  return (
    <div style={{ paddingBottom: 60 }}>
      <div className="page-header">
        <Link to="/" className="back-link">← Back</Link>
        <h1>Study Statistics</h1>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-value">{stats?.totalQuestions || 0}</div>
          <div className="stat-label">Total Questions</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{stats?.studiedQuestions || 0}</div>
          <div className="stat-label">Studied</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{gradingStats?.totalAnswered || 0}</div>
          <div className="stat-label">Answers Submitted</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">
            {gradingStats?.averageScore ? gradingStats.averageScore.toFixed(1) : 0}
          </div>
          <div className="stat-label">Avg Score</div>
        </div>
      </div>

      {stats?.byCategory && Object.keys(stats.byCategory).length > 0 && (
        <div className="stats-section">
          <h2>By Category</h2>
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

      {stats?.byDifficulty && (
        <div className="stats-section">
          <h2>By Difficulty</h2>
          <div className="stats-grid">
            {Object.entries(stats.byDifficulty).map(([diff, count]) => (
              <div className="stat-card" key={diff}>
                <div className="stat-value">{count}</div>
                <div className="stat-label">{diff}</div>
              </div>
            ))}
          </div>
        </div>
      )}

      {gradingStats?.gradeDistribution && (
        <div className="stats-section">
          <h2>Grade Distribution</h2>
          <div className="stats-grid">
            {['A', 'B', 'C', 'D', 'F'].map(grade => (
              <div className="stat-card" key={grade}>
                <div className={`stat-value grade-${grade}`}>
                  {gradingStats.gradeDistribution[grade] || 0}
                </div>
                <div className="stat-label">Grade {grade}</div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
