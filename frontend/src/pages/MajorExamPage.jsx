import { useState } from 'react';
import { Link } from 'react-router-dom';

const subjects = [
  {
    id: 'os',
    name: '운영체제',
    emoji: '🖥️',
    notes: [
      {
        title: '변수와 스레드',
        lines: [
          { type: 'h', text: '변수' },
          { type: 'li', text: 'code : 실행할 프로그램 코드' },
          { type: 'br' },
          { type: 'h', text: '스레드' },
          { type: 'p', text: '스레드가 독립적으로 가지는 것 : stack, PC(Program Counter), 레지스터' },
          { type: 'p', text: '스레드가 공유하는 것 : heap, data, code' },
        ],
      },
    ],
  },
  {
    id: 'ds',
    name: '자료구조',
    emoji: '🏗️',
    notes: [],
  },
  {
    id: 'db',
    name: '데이터베이스',
    emoji: '🗄️',
    notes: [],
  },
  {
    id: 'se',
    name: '소프트웨어공학',
    emoji: '📐',
    notes: [],
  },
  {
    id: 'dc',
    name: '데이터통신',
    emoji: '📡',
    notes: [],
  },
  {
    id: 'nw',
    name: '네트워크',
    emoji: '🌐',
    notes: [],
  },
];

export default function MajorExamPage() {
  const [activeTab, setActiveTab] = useState('os');
  const current = subjects.find(s => s.id === activeTab);

  return (
    <div className="fade-in">
      <div className="page-header">
        <Link to="/" className="back-link">← 홈으로</Link>
        <h1>📝 전공필기 요약노트</h1>
        <p>금융공기업(코스콤, 금융결제원) 대비</p>
      </div>

      <div className="major-tabs">
        {subjects.map(s => (
          <button
            key={s.id}
            className={`major-tab ${activeTab === s.id ? 'active' : ''}`}
            onClick={() => setActiveTab(s.id)}
          >
            <span className="major-tab-emoji">{s.emoji}</span>
            {s.name}
          </button>
        ))}
      </div>

      <div className="major-content">
        {current && current.notes.length > 0 ? (
          current.notes.map((note, idx) => (
            <div key={idx} className="major-note-section">
              <h2 className="major-note-title">{note.title}</h2>
              <div className="major-note-body">
                {note.lines.map((line, i) => {
                  if (line.type === 'h') return <h3 key={i} className="note-subtitle">{line.text}</h3>;
                  if (line.type === 'li') return <div key={i} className="note-bullet">- {line.text}</div>;
                  if (line.type === 'p') return <div key={i} className="note-line">{line.text}</div>;
                  if (line.type === 'br') return <div key={i} className="note-spacer" />;
                  return null;
                })}
              </div>
            </div>
          ))
        ) : (
          <div className="empty">
            <div className="empty-icon">📋</div>
            아직 정리된 내용이 없습니다
          </div>
        )}
      </div>
    </div>
  );
}
