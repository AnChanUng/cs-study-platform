import { useState, useEffect, useRef, useCallback } from 'react';

const QUESTIONS = [
  { id: 1, category: '인성', text: '자기소개를 해주세요.' },
  { id: 2, category: '인성', text: '본인의 장점과 단점을 말해주세요.' },
  { id: 3, category: '인성', text: '스트레스를 어떻게 관리하나요?' },
  { id: 4, category: '인성', text: '5년 후 자신의 모습은 어떨 것 같나요?' },
  { id: 5, category: '인성', text: '살면서 가장 힘들었던 경험과 극복 방법을 말해주세요.' },
  { id: 6, category: '지원동기', text: '지원 동기가 무엇인가요?' },
  { id: 7, category: '지원동기', text: '왜 개발자가 되고 싶은가요?' },
  { id: 8, category: '지원동기', text: '우리 회사에 대해 알고 있는 것이 있나요?' },
  { id: 9, category: '지원동기', text: '본인이 지원한 직무에서 가장 중요한 역량은 무엇이라고 생각하나요?' },
  { id: 10, category: '경험', text: '가장 어려웠던 프로젝트 경험을 말해주세요.' },
  { id: 11, category: '경험', text: '팀 프로젝트에서 갈등이 생겼을 때 어떻게 해결했나요?' },
  { id: 12, category: '경험', text: '실패 경험과 그로부터 배운 점을 말해주세요.' },
  { id: 13, category: '경험', text: '리더십을 발휘한 경험이 있나요?' },
  { id: 14, category: '경험', text: '이전 프로젝트에서 본인의 역할은 무엇이었나요?' },
  { id: 15, category: '경험', text: '협업할 때 가장 중요하게 생각하는 것은 무엇인가요?' },
  { id: 16, category: '기술', text: '본인이 가장 자신 있는 기술 스택은 무엇인가요?' },
  { id: 17, category: '기술', text: '최근에 관심을 가지고 공부한 기술이 있나요?' },
  { id: 18, category: '기술', text: '개발자로서 성장하기 위해 어떤 노력을 하고 있나요?' },
  { id: 19, category: '기술', text: '객체지향 프로그래밍이란 무엇인가요?' },
  { id: 20, category: '마무리', text: '마지막으로 하고 싶은 말이 있나요?' },
];

const CATEGORIES = ['전체', '인성', '지원동기', '경험', '기술', '마무리'];

export default function InterviewPracticePage() {
  const [selectedCategory, setSelectedCategory] = useState('전체');
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isRecording, setIsRecording] = useState(false);
  const [recordings, setRecordings] = useState({});
  const [cameraOn, setCameraOn] = useState(false);
  const [cameraError, setCameraError] = useState('');
  const [recordingTime, setRecordingTime] = useState(0);

  const videoRef = useRef(null);
  const streamRef = useRef(null);
  const mediaRecorderRef = useRef(null);
  const chunksRef = useRef([]);
  const timerRef = useRef(null);

  const filteredQuestions = selectedCategory === '전체'
    ? QUESTIONS
    : QUESTIONS.filter(q => q.category === selectedCategory);

  const currentQuestion = filteredQuestions[currentIndex];

  const startCamera = useCallback(async () => {
    try {
      setCameraError('');
      const stream = await navigator.mediaDevices.getUserMedia({
        video: { width: 640, height: 480, facingMode: 'user' },
        audio: true,
      });
      streamRef.current = stream;
      if (videoRef.current) {
        videoRef.current.srcObject = stream;
      }
      setCameraOn(true);
    } catch (err) {
      setCameraError('카메라/마이크 접근 권한이 필요합니다.');
      console.error('Camera error:', err);
    }
  }, []);

  const stopCamera = useCallback(() => {
    if (streamRef.current) {
      streamRef.current.getTracks().forEach(t => t.stop());
      streamRef.current = null;
    }
    if (videoRef.current) {
      videoRef.current.srcObject = null;
    }
    setCameraOn(false);
  }, []);

  useEffect(() => {
    return () => {
      stopCamera();
      if (timerRef.current) clearInterval(timerRef.current);
    };
  }, [stopCamera]);

  const startRecording = () => {
    if (!streamRef.current) return;
    chunksRef.current = [];
    const recorder = new MediaRecorder(streamRef.current, { mimeType: 'audio/webm' });
    recorder.ondataavailable = (e) => {
      if (e.data.size > 0) chunksRef.current.push(e.data);
    };
    recorder.onstop = () => {
      const blob = new Blob(chunksRef.current, { type: 'audio/webm' });
      const url = URL.createObjectURL(blob);
      setRecordings(prev => ({ ...prev, [currentQuestion.id]: url }));
    };
    recorder.start();
    mediaRecorderRef.current = recorder;
    setIsRecording(true);
    setRecordingTime(0);
    timerRef.current = setInterval(() => {
      setRecordingTime(prev => prev + 1);
    }, 1000);
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current && mediaRecorderRef.current.state === 'recording') {
      mediaRecorderRef.current.stop();
    }
    setIsRecording(false);
    if (timerRef.current) {
      clearInterval(timerRef.current);
      timerRef.current = null;
    }
  };

  const deleteRecording = (questionId) => {
    setRecordings(prev => {
      const next = { ...prev };
      if (next[questionId]) {
        URL.revokeObjectURL(next[questionId]);
        delete next[questionId];
      }
      return next;
    });
  };

  const formatTime = (seconds) => {
    const m = Math.floor(seconds / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    return `${m}:${s}`;
  };

  const goTo = (direction) => {
    if (isRecording) stopRecording();
    setCurrentIndex(prev => {
      if (direction === 'prev') return Math.max(0, prev - 1);
      return Math.min(filteredQuestions.length - 1, prev + 1);
    });
  };

  useEffect(() => {
    setCurrentIndex(0);
  }, [selectedCategory]);

  return (
    <div className="interview-page">
      <div className="interview-header">
        <h1 className="interview-title">면접 공통질문 연습</h1>
        <p className="interview-subtitle">카메라와 마이크를 켜고 실전처럼 연습하세요</p>
      </div>

      <div className="interview-categories">
        {CATEGORIES.map(cat => (
          <button
            key={cat}
            className={`interview-cat-btn ${selectedCategory === cat ? 'active' : ''}`}
            onClick={() => setSelectedCategory(cat)}
          >
            {cat}
          </button>
        ))}
      </div>

      <div className="interview-main">
        <div className="interview-video-section">
          <div className="interview-video-wrapper">
            {cameraOn ? (
              <video
                ref={videoRef}
                autoPlay
                playsInline
                muted
                className="interview-video"
              />
            ) : (
              <div className="interview-video-placeholder">
                {cameraError ? (
                  <p className="interview-camera-error">{cameraError}</p>
                ) : (
                  <p>카메라를 켜서 시작하세요</p>
                )}
              </div>
            )}
            {isRecording && (
              <div className="interview-recording-indicator">
                <span className="interview-rec-dot" />
                REC {formatTime(recordingTime)}
              </div>
            )}
          </div>
          <button
            className={`interview-camera-btn ${cameraOn ? 'on' : ''}`}
            onClick={cameraOn ? stopCamera : startCamera}
          >
            {cameraOn ? '카메라 끄기' : '카메라 켜기'}
          </button>
        </div>

        {currentQuestion && (
          <div className="interview-question-section">
            <div className="interview-question-meta">
              <span className="interview-question-cat">{currentQuestion.category}</span>
              <span className="interview-question-num">
                {currentIndex + 1} / {filteredQuestions.length}
              </span>
            </div>
            <h2 className="interview-question-text">{currentQuestion.text}</h2>

            <div className="interview-controls">
              {cameraOn && !isRecording && (
                <button className="interview-record-btn start" onClick={startRecording}>
                  녹음 시작
                </button>
              )}
              {isRecording && (
                <button className="interview-record-btn stop" onClick={stopRecording}>
                  녹음 중지
                </button>
              )}
            </div>

            {recordings[currentQuestion.id] && (
              <div className="interview-playback">
                <p className="interview-playback-label">녹음된 답변</p>
                <audio controls src={recordings[currentQuestion.id]} />
                <button
                  className="interview-delete-btn"
                  onClick={() => deleteRecording(currentQuestion.id)}
                >
                  삭제 후 다시 녹음
                </button>
              </div>
            )}

            <div className="interview-nav">
              <button
                className="interview-nav-btn"
                onClick={() => goTo('prev')}
                disabled={currentIndex === 0}
              >
                이전 질문
              </button>
              <button
                className="interview-nav-btn"
                onClick={() => goTo('next')}
                disabled={currentIndex === filteredQuestions.length - 1}
              >
                다음 질문
              </button>
            </div>
          </div>
        )}
      </div>

      <div className="interview-question-list">
        <h3 className="interview-list-title">질문 목록</h3>
        <div className="interview-list-items">
          {filteredQuestions.map((q, idx) => (
            <button
              key={q.id}
              className={`interview-list-item ${idx === currentIndex ? 'active' : ''} ${recordings[q.id] ? 'recorded' : ''}`}
              onClick={() => {
                if (isRecording) stopRecording();
                setCurrentIndex(idx);
              }}
            >
              <span className="interview-list-num">{idx + 1}</span>
              <span className="interview-list-text">{q.text}</span>
              {recordings[q.id] && <span className="interview-list-check">&#10003;</span>}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
