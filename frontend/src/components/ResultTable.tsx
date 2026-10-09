import type { ResultRow } from "../types/api";

interface ResultTableProps {
  data: ResultRow[];
}

function formatHeader(key: string): string {
  return key
    .replace(/_/g, " ")
    .replace(/\b\w/g, (char) => char.toUpperCase());
}

function formatCell(value: unknown): string {
  if (value === null || value === undefined) {
    return "";
  }
  if (typeof value === "number") {
    return Number.isInteger(value)
      ? value.toLocaleString()
      : value.toLocaleString(undefined, { maximumFractionDigits: 4 });
  }
  if (typeof value === "object") {
    return JSON.stringify(value);
  }
  return String(value);
}

export function ResultTable({ data }: ResultTableProps) {
  if (data.length === 0) {
    return <p className="empty-note">No results</p>;
  }

  const columns = Object.keys(data[0]);

  return (
    <section>
      <p className="section-label">Results</p>
      <div className="table-wrap">
        <table className="result-table">
          <thead>
            <tr>
              {columns.map((column) => (
                <th key={column}>{formatHeader(column)}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {data.map((row, index) => (
              <tr key={index}>
                {columns.map((column) => {
                  const value = row[column];
                  const empty = value === null || value === undefined;
                  return (
                    <td key={column} className={empty ? "null-cell" : undefined}>
                      {empty ? "null" : formatCell(value)}
                    </td>
                  );
                })}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
