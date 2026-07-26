export default function FilterBar({ activeFilter, onFilterChange }) {
  return (
    <select 
      value={activeFilter}
      onChange={(e) => onFilterChange(e.target.value)}
      className="border rounded-lg p-3 text-gray-700 bg-white shadow-sm focus:ring-2 focus:ring-blue-500 outline-none cursor-pointer"
    >
      <option value="ALL">Semua Kategori</option>
      <option value="PKM">PKM</option>
      <option value="LOMBA">LOMBA</option>
      <option value="STARTUP">STARTUP</option>
      <option value="PENELITIAN">PENELITIAN</option>
      <option value="MAGANG">MAGANG</option>
      <option value="OPEN_SOURCE">OPEN SOURCE</option>
      <option value="LAINNYA">LAINNYA</option>
    </select>
  );
}