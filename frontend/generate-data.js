import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const contentDir = path.resolve(__dirname, '..', 'content');

const categories = [
  { id: 1, name: '데이터베이스', slug: 'database', description: 'Database concepts and SQL', displayOrder: 1, iconEmoji: '🗄️' },
  { id: 2, name: '자료구조', slug: 'data-structure', description: 'Data structures', displayOrder: 2, iconEmoji: '🏗️' },
  { id: 3, name: '운영체제', slug: 'operating-system', description: 'Operating system concepts', displayOrder: 3, iconEmoji: '🖥️' },
  { id: 4, name: '네트워크', slug: 'network', description: 'Network and protocols', displayOrder: 4, iconEmoji: '🌐' },
  { id: 5, name: '알고리즘', slug: 'algorithm', description: 'Algorithms and problem solving', displayOrder: 5, iconEmoji: '⚡' },
  { id: 6, name: '소프트웨어공학', slug: 'software-engineering', description: 'Software engineering principles', displayOrder: 6, iconEmoji: '📐' },
  { id: 7, name: '스프링', slug: 'spring', description: 'Spring and Spring Boot', displayOrder: 7, iconEmoji: '🌱' },
  { id: 8, name: '자바', slug: 'java', description: 'Java programming language', displayOrder: 8, iconEmoji: '☕' },
  { id: 9, name: '쿠버네티스', slug: 'kubernetes', description: 'Kubernetes container orchestration', displayOrder: 9, iconEmoji: '⎈' },
  { id: 10, name: '도커', slug: 'docker', description: 'Docker containerization', displayOrder: 10, iconEmoji: '🐳' },
  { id: 11, name: '전공필기', slug: 'major-exam', description: '코스콤, 금융결제원', displayOrder: 11, iconEmoji: '📝' },
];

function parseFrontmatter(content) {
  const fm = {};
  if (!content.startsWith('---')) return fm;
  const end = content.indexOf('---', 3);
  if (end === -1) return fm;
  const lines = content.substring(3, end).trim().split('\n');
  for (const line of lines) {
    const idx = line.indexOf(':');
    if (idx > 0) {
      const key = line.substring(0, idx).trim();
      let val = line.substring(idx + 1).trim();
      if (val.startsWith('"') && val.endsWith('"')) val = val.slice(1, -1);
      fm[key] = val;
    }
  }
  return fm;
}

function extractBody(content) {
  if (!content.startsWith('---')) return content;
  const end = content.indexOf('---', 3);
  return end === -1 ? content : content.substring(end + 3).trim();
}

function extractSection(body, sectionName) {
  const marker = '## ' + sectionName;
  let start = body.indexOf(marker);
  if (start === -1) return '';
  start = body.indexOf('\n', start);
  if (start === -1) return '';
  start++;
  const end = body.indexOf('\n## ', start);
  return end === -1 ? body.substring(start).trim() : body.substring(start, end).trim();
}

const catSlugToId = {};
categories.forEach(c => { catSlugToId[c.slug] = c.id; });

const catSlugToName = {};
categories.forEach(c => { catSlugToName[c.slug] = c.name; });

const questions = [];
let questionId = 1;

const dirs = fs.readdirSync(contentDir).filter(d => fs.statSync(path.join(contentDir, d)).isDirectory());

for (const dir of dirs.sort()) {
  const dirPath = path.join(contentDir, dir);
  const files = fs.readdirSync(dirPath).filter(f => f.endsWith('.md')).sort();

  for (const file of files) {
    const filePath = path.join(dirPath, file);
    const content = fs.readFileSync(filePath, 'utf-8');
    const fm = parseFrontmatter(content);
    const body = extractBody(content);

    if (!fm.title) continue;

    const categorySlug = fm.category || 'database';
    const difficulty = (fm.difficulty || 'BASIC').toUpperCase();
    const tags = fm.tags || '';

    let questionContent = extractSection(body, '질문');
    let answerContent = extractSection(body, '답변');
    if (!questionContent) questionContent = body;
    if (!answerContent) answerContent = body;

    questions.push({
      id: questionId,
      title: fm.title,
      content: questionContent,
      answer: answerContent,
      difficulty,
      tags,
      categorySlug,
      categoryName: catSlugToName[categorySlug] || categorySlug,
      categoryId: catSlugToId[categorySlug] || 1,
      studyCount: 0,
      bookmarked: false,
    });
    questionId++;
  }
}

const output = `// Auto-generated hardcoded data from content/ markdown files
// Generated: ${new Date().toISOString()}

export const hardcodedCategories = ${JSON.stringify(categories, null, 2)};

export const hardcodedQuestions = ${JSON.stringify(questions, null, 2)};
`;

fs.writeFileSync(path.join(__dirname, 'src', 'hardcodedData.js'), output, 'utf-8');
console.log(`Generated ${categories.length} categories and ${questions.length} questions.`);
