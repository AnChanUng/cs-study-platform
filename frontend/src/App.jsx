import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Layout from './components/Layout';
import Home from './pages/Home';
import CategoryPage from './pages/CategoryPage';
import QuestionPage from './pages/QuestionPage';
import StatsPage from './pages/StatsPage';
import InterviewPracticePage from './pages/InterviewPracticePage';
import MajorExamPage from './pages/MajorExamPage';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<Home />} />
            <Route path="category/:slug" element={<CategoryPage />} />
            <Route path="question/:id" element={<QuestionPage />} />
            <Route path="stats" element={<StatsPage />} />
            <Route path="interview" element={<InterviewPracticePage />} />
            <Route path="major-exam" element={<MajorExamPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
