// Generic responsive table. Horizontal scroll on small screens via .table-wrap.
// columns: [{ key, header, render? }]
export default function DataTable({ columns, rows, empty = 'No data.' }) {
  if (!rows || rows.length === 0) {
    return <p className="muted">{empty}</p>;
  }
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            {columns.map((c) => (
              <th key={c.key}>{c.header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((row, i) => (
            <tr key={row.id ?? i}>
              {columns.map((c) => (
                <td key={c.key}>{c.render ? c.render(row) : row[c.key]}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
