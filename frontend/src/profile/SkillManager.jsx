import { useState } from "react";

export default function SkillManager() {
  const [skills, setSkills] = useState([
    {
      id: 1,
      nama: "React",
      level: "Intermediate",
    },
    {
      id: 2,
      nama: "Spring Boot",
      level: "Beginner",
    },
  ]);

  const [skill, setSkill] = useState("");
  const [level, setLevel] = useState("Beginner");

  const tambahSkill = () => {
    if (!skill) return;

    setSkills([
      ...skills,
      {
        id: Date.now(),
        nama: skill,
        level,
      },
    ]);

    setSkill("");
    setLevel("Beginner");
  };

  const hapusSkill = (id) => {
    setSkills(skills.filter((item) => item.id !== id));
  };

  return (
    <div className="bg-white rounded-xl shadow p-6">
      <h2 className="text-xl font-bold mb-4">
        Skill Saya
      </h2>

      <div className="flex gap-3 mb-4">
        <input
          className="border rounded p-2 flex-1"
          placeholder="Contoh: React"
          value={skill}
          onChange={(e) => setSkill(e.target.value)}
        />

        <select
          className="border rounded p-2"
          value={level}
          onChange={(e) => setLevel(e.target.value)}
        >
          <option>Beginner</option>
          <option>Intermediate</option>
          <option>Advanced</option>
        </select>

        <button
          onClick={tambahSkill}
          className="bg-blue-600 text-white px-4 rounded"
        >
          Tambah
        </button>
      </div>

      <div className="space-y-2">
        {skills.map((item) => (
          <div
            key={item.id}
            className="flex justify-between border rounded p-3"
          >
            <div>
              <strong>{item.nama}</strong>

              <p className="text-gray-500">
                {item.level}
              </p>
            </div>

            <button
              onClick={() => hapusSkill(item.id)}
              className="text-red-500"
            >
              Hapus
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}