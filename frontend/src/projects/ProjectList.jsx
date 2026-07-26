import ProjectCard from "./ProjectCard";

export default function ProjectList({ projects, onJoin }) {
  // Perlindungan jika data kosong
  if (!projects || projects.length === 0) {
    return (
      <div className="col-span-full text-center py-12 bg-white rounded-xl shadow-sm border border-gray-100">
        <p className="text-gray-500 text-lg">Belum ada proyek yang tersedia saat ini.</p>
        <p className="text-gray-400 text-sm mt-1">Jadilah yang pertama membuat proyek!</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {projects.map((project) => (
        <ProjectCard
          key={project.id}
          project={project}
          onJoin={onJoin}
        />
      ))}
    </div>
  );
}