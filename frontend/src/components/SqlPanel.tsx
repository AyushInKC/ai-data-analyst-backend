import { useState } from "react";

interface SqlPanelProps {
  sql: string;
}

export function SqlPanel({ sql }: SqlPanelProps) {
  const [open, setOpen] = useState(true);
  const [copied, setCopied] = useState(false);

  async function copy() {
    await navigator.clipboard.writeText(sql);
    setCopied(true);
    window.setTimeout(() => setCopied(false), 1500);
  }

  return (
    <section className="sql-panel">
      <div className="sql-header">
        <button
          type="button"
          className="toggle"
          onClick={() => setOpen((value) => !value)}
        >
          Generated SQL {open ? "(hide)" : "(show)"}
        </button>
        <button type="button" className="ghost-btn" onClick={() => void copy()}>
          {copied ? "Copied" : "Copy"}
        </button>
      </div>
      {open && <pre className="sql-body">{sql}</pre>}
    </section>
  );
}
