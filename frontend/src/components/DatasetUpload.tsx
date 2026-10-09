interface DatasetUploadProps {
  uploading: boolean;
  onFile: (file: File) => void;
}

export function DatasetUpload({ uploading, onFile }: DatasetUploadProps) {
  return (
    <div>
      <p className="section-label">Dataset</p>
      <label>
        <input
          className="file-input"
          type="file"
          accept=".csv,text/csv"
          disabled={uploading}
          onChange={(event) => {
            const file = event.target.files?.[0];
            event.target.value = "";
            if (file) {
              onFile(file);
            }
          }}
        />
        <span className="upload-btn" aria-disabled={uploading}>
          {uploading ? "Uploading…" : "Upload CSV"}
        </span>
      </label>
      <p className="hint">CSV only. The file is sent to the existing upload API.</p>
    </div>
  );
}
