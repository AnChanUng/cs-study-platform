import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

export default function OAuthCallback() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const token = searchParams.get('token');
    if (token) {
      localStorage.setItem('token', token);
    }
    navigate('/', { replace: true });
  }, [searchParams, navigate]);

  return (
    <div className="loading">
      <div className="loading-spinner" />
      로그인 중...
    </div>
  );
}
