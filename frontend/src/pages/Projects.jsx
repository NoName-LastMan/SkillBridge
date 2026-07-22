import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getProjects } from '../services/projectService';

export default function Projects() {
const [projects, setProjects] = useState([]);
const [keyword, setKeyword] = useState('');

useEffect(() => {
loadProjects();
}, []);

const loadProjects = async (q = '') => {
try {
const data = await getProjects(q);
setProjects(data);
} catch (error) {
console.error(error);
}
};

return ( <div className='p-6'> <h1 className='text-3xl font-bold mb-4'>Daftar Proyek</h1>

```
  <input
    className='border p-3 rounded w-full mb-4'
    placeholder='Cari proyek...'
    value={keyword}
    onChange={(e) => setKeyword(e.target.value)}
  />

  <div className='space-y-4'>
    {projects.map((project) => (
      <div key={project.id} className='border rounded p-4 bg-white'>
        <h2 className='text-xl font-bold'>{project.title}</h2>
        <p>{project.description}</p>

        <Link
          to={`/projects/${project.id}`}
          className='text-blue-600 underline'
        >
          Lihat Detail
        </Link>
      </div>
    ))}
  </div>
</div>

);
}
