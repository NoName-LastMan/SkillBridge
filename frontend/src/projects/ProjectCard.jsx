import { Link } from "react-router-dom";

export default function ProjectCard({ project, onJoin }) {
  return (
    <div className="bg-white rounded-xl shadow-md p-5 hover:shadow-lg transition">
      <span className="inline-block bg-blue-100 text-blue-700 text-xs font-semibold px-3 py-1 rounded-full mb-3">
        {project.kategori}
      </span>

      <h2 className="text-xl font-bold mb-3">
        {project.judul_proyek}
      </h2>

      <p className="text-gray-600 mb-4">
        {project.deskripsi}
      </p>

      <div className="mb-4">
        <span className="font-semibold text-green-600">
          Status :
        </span>{" "}
        {project.status}
      </div>

      <div className="flex gap-2">

        <Link
          to={`/projects/${project.id}`}
          className="flex-1 bg-blue-600 text-white text-center py-2 rounded-lg"
        >
          Detail
        </Link>

        <button
          onClick={() => onJoin(project.id)}
          className="flex-1 bg-green-600 text-white py-2 rounded-lg"
        >
          Join
        </button>

      </div>
    </div>
  );
}