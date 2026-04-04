import { useState, useRef, useEffect } from 'react';
import api from '../api';
import { hardcodedQuestions } from '../hardcodedData';

export default function ChaosChat() {
  const [open, setOpen] = useState(false);
  const [messages, setMessages] = useState([
    { role: 'assistant', content: '안냥! 나는 카오스 고양이다냥~ 🐱\nCS 면접 관련 궁금한 거 아무거나 물어봐!' }
  ]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const bottomRef = useRef(null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const localFallback = (msg) => {
    const lower = msg.toLowerCase();
    const keywords = lower.split(/\s+/);
    const matched = hardcodedQuestions.filter(q =>
      keywords.some(k =>
        q.title.toLowerCase().includes(k) ||
        q.tags.toLowerCase().includes(k) ||
        q.content.toLowerCase().includes(k)
      )
    ).slice(0, 3);

    if (matched.length === 0) {
      return '냥... 그 질문은 아직 잘 모르겠다냥! 😿 다른 CS 키워드로 물어봐줘!';
    }

    let reply = '냥! 관련 내용을 찾았다냥~ 🐱\n\n';
    matched.forEach(q => {
      reply += `**${q.title}**\n`;
      const answer = q.answer.length > 300 ? q.answer.substring(0, 300) + '...' : q.answer;
      reply += answer + '\n\n';
    });
    reply += '더 자세한 내용은 해당 질문 페이지에서 확인해봐냥!';
    return reply;
  };

  const send = async () => {
    const msg = input.trim();
    if (!msg || loading) return;

    const userMsg = { role: 'user', content: msg };
    setMessages(prev => [...prev, userMsg]);
    setInput('');
    setLoading(true);

    try {
      const history = messages
        .filter(m => m.role !== 'system')
        .slice(-10)
        .map(m => ({ role: m.role, content: m.content }));

      const res = await api.post('/chat', { message: msg, history });
      setMessages(prev => [...prev, { role: 'assistant', content: res.data.reply }]);
    } catch {
      const fallbackReply = localFallback(msg);
      setMessages(prev => [...prev, { role: 'assistant', content: fallbackReply }]);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      send();
    }
  };

  return (
    <>
      {/* Floating Button */}
      <button className="chaos-fab" onClick={() => setOpen(!open)}>
        {open ? '✕' : '🐱'}
      </button>

      {/* Chat Panel */}
      {open && (
        <div className="chaos-panel">
          <div className="chaos-header">
            <span className="chaos-avatar">🐱</span>
            <div>
              <div className="chaos-name">카오스 고양이</div>
              <div className="chaos-status">CS 면접 도우미냥~</div>
            </div>
          </div>

          <div className="chaos-messages">
            {messages.map((m, i) => (
              <div key={i} className={`chaos-msg ${m.role}`}>
                {m.role === 'assistant' && <span className="chaos-msg-avatar">🐱</span>}
                <div className="chaos-bubble">{m.content}</div>
              </div>
            ))}
            {loading && (
              <div className="chaos-msg assistant">
                <span className="chaos-msg-avatar">🐱</span>
                <div className="chaos-bubble chaos-typing">
                  <span /><span /><span />
                </div>
              </div>
            )}
            <div ref={bottomRef} />
          </div>

          <div className="chaos-input-row">
            <input
              type="text"
              className="chaos-input"
              placeholder="궁금한 거 물어봐냥~"
              value={input}
              onChange={e => setInput(e.target.value)}
              onKeyDown={handleKeyDown}
              disabled={loading}
            />
            <button className="chaos-send" onClick={send} disabled={loading || !input.trim()}>
              ↑
            </button>
          </div>
        </div>
      )}
    </>
  );
}
