import Navbar from "../components/Navbar";

export default function PublicProfileView() {
  const profile = {
    nama: "Alif Najwan",
    prodi: "Informatika",
    angkatan: "2023",
    bio: "Frontend Developer & React Enthusiast",
    skills: [
      "React",
      "Spring Boot",
      "Java",
      "UI/UX",
    ],
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />

      <div className="max-w-4xl mx-auto p-8">
        <div className="bg-white rounded-xl shadow p-8">

          <h1 className="text-3xl font-bold">
            {profile.nama}
          </h1>

          <p className="text-gray-600 mt-2">
            {profile.prodi} • Angkatan {profile.angkatan}
          </p>

          <p className="mt-5">
            {profile.bio}
          </p>

          <h2 className="text-xl font-semibold mt-8 mb-4">
            Skill
          </h2>

          <div className="flex flex-wrap gap-2">
            {profile.skills.map((skill) => (
              <span
                key={skill}
                className="bg-blue-100 text-blue-700 px-3 py-1 rounded-full"
              >
                {skill}
              </span>
            ))}
          </div>

        </div>
      </div>
    </div>
  );
}