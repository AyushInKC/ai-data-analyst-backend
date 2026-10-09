import type { DatasetResponse } from "../types/api";

interface DatasetInfoProps {
  dataset: DatasetResponse;
}

export function DatasetInfo({ dataset }: DatasetInfoProps) {
  return (
    <div className="meta-card">
      <h2>Dataset loaded</h2>
      <dl>
        <div className="meta-row">
          <dt>Filename</dt>
          <dd>{dataset.fileName}</dd>
        </div>
        <div className="meta-row">
          <dt>Dataset ID</dt>
          <dd>{dataset.datasetId}</dd>
        </div>
        <div className="meta-row">
          <dt>Rows</dt>
          <dd>{dataset.rowCount}</dd>
        </div>
        <div className="meta-row">
          <dt>Columns</dt>
          <dd>{dataset.columnCount}</dd>
        </div>
      </dl>
      {dataset.columns?.length > 0 && (
        <ul className="columns">
          {dataset.columns.map((column) => (
            <li key={column}>{column}</li>
          ))}
        </ul>
      )}
    </div>
  );
}
