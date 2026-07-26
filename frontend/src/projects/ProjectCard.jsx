import { Link } from "react-router-dom";

export default function ProjectCard({ project, onJoin }) {
  return (
    <div className="bg-white rounded-xl shadow-md p-5 hover:shadow-lg transition flex flex-col h-full">
      <div className="flex justify-between items-start mb-3">
        {/* Ubah .kategori menjadi .category */}
        <span className="inline-block bg-blue-100 text-blue-700 text-xs font-semibold px-3 py-1 rounded-full">
          {project.category}
        </span>
        <span className={`text-xs font-bold px-3 py-1 rounded-full ${
          project.status === 'OPEN' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
        }`}>
          {project.status || 'OPEN'}
        </span>
      </div>

      {/* Ubah .judul_proyek menjadi .title */}
      <h2 className="text-xl font-bold mb-3 text-gray-800 line-clamp-2">
        {project.title}
      </h2>

      {/* Ubah .deskripsi menjadi .description */}
      <p className="text-gray-600 mb-4 line-clamp-3 flex-grow">
        {project.description}
      </p>

      {/* Tambahan Info Max Member */}
      {project.maxMembers && (
        <div className="mb-4 text-sm text-gray-500 font-medium">
          👥 Max {project.maxMembers} Anggota
        </div>
      )}

      <div className="flex gap-2 mt-auto">
        <Link
          to={`/projects/${project.id}`}
          className="flex-1 bg-blue-600 hover:bg-blue-700 transition text-white text-center py-2 rounded-lg font-medium"
        >
          Detail
        </Link>
        <button
          onClick={() => onJoin(project.id)}
          className="flex-1 bg-green-600 hover:bg-green-700 transition text-white py-2 rounded-lg font-medium"
        >
          Join
        </button>
      </div>
    </div>
  );
}