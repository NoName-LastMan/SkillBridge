export default function PrivacyToggle({
  privacy,
  onChange,
}) {
  return (
    <div
      style={{
        margin: "20px 0",
        textAlign: "left",
      }}
    >
      <label>
        <strong>Privasi Kontak</strong>
      </label>

      <br /><br />

      <select
        value={privacy || "PUBLIC"}
        onChange={(e) => onChange(e.target.value)}
      >
        <option value="PUBLIC">
          PUBLIC
        </option>

        <option value="PRIVATE">
          PRIVATE
        </option>
      </select>

      <p
        style={{
          color: "#777",
          fontSize: "14px",
        }}
      >
        PUBLIC = semua orang dapat melihat kontak.
        <br />
        PRIVATE = kontak hanya dapat dilihat setelah disetujui.
      </p>
    </div>
  );
}