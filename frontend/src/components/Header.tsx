import type { DatasetResponse } from "../types/api";

interface HeaderProps {
  dataset: DatasetResponse | null;
}

export function Header({ dataset }: HeaderProps) {
  return (
    <header className="topbar">
      <div className="brand">
        <h1>AI Data Analyst</h1>
        <p>DuckDB · Cohere</p>
      </div>
      <div className="dataset-status">
        {dataset ? (
          <>
            <strong title={dataset.fileName}>{dataset.fileName}</strong>
            Dataset loaded
          </>
        ) : (
          <>
            <strong>No dataset</strong>
            Upload a CSV to begin
          </>
        )}
      </div>
    </header>
  );
}
