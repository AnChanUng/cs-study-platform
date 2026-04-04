import { useState } from 'react';
import { Link } from 'react-router-dom';

const notes = [
  {
    id: 1,
    title: '변수와 스레드',
    content: `**변수**
- code : 실행할 프로그램 코드

**스레드**
스레드가 독립적으로 가지는 것 : stack, PC(Program Counter), 레지스터
스레드가 공유하는 것 : heap, data, code`,
  },
];

export default function MajorExamPage() {
  const [openId, setOpenId] = useState(null);

  return (
    <div className="fade-in">
      <div className="page-header">
        <Link to="/" className="back-link">← 홈으로</Link>
        <h1>📝 전공필기 요약노트</h1>
        <p>코스콤, 금융결제원</p>
      </div>

      <div className="note-list">
        {notes.map(note => (
          <div key={note.id} className="note-card">
            <button
              className={`note-header ${openId === note.id ? 'open' : ''}`}
              onClick={() => setOpenId(openId === note.id ? null : note.id)}
            >
              <span className="note-title">{note.title}</span>
              <span className="note-toggle">{openId === note.id ? '−' : '+'}</span>
            </button>
            {openId === note.id && (
              <div className="note-body">
                {note.content.split('\n').map((line, i) => {
                  if (line.startsWith('**') && line.endsWith('**')) {
                    return <h3 key={i} className="note-subtitle">{line.replace(/\*\*/g, '')}</h3>;
                  }
                  if (line.startsWith('- ')) {
                    return <div key={i} className="note-bullet">{line}</div>;
                  }
                  if (line.trim() === '') {
                    return <div key={i} className="note-spacer" />;
                  }
                  return <div key={i} className="note-line">{line}</div>;
                })}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
