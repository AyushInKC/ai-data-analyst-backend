const SUGGESTIONS = [
  "Show total revenue by region",
  "Which region has the highest revenue?",
  "Show revenue by date",
];

interface QueryInputProps {
  question: string;
  disabled: boolean;
  loading: boolean;
  onChange: (value: string) => void;
  onSubmit: () => void;
}

export function QueryInput({
  question,
  disabled,
  loading,
  onChange,
  onSubmit,
}: QueryInputProps) {
  return (
    <form
      onSubmit={(event) => {
        event.preventDefault();
        onSubmit();
      }}
    >
      <p className="section-label">Question</p>
      <textarea
        className="query-box"
        value={question}
        disabled={disabled}
        placeholder="Ask your dataset a question..."
        onChange={(event) => onChange(event.target.value)}
        onKeyDown={(event) => {
          if (event.key === "Enter" && (event.metaKey || event.ctrlKey)) {
            event.preventDefault();
            onSubmit();
          }
        }}
      />
      <div className="suggestions">
        {SUGGESTIONS.map((item) => (
          <button
            key={item}
            type="button"
            disabled={disabled}
            onClick={() => onChange(item)}
          >
            {item}
          </button>
        ))}
      </div>
      <div className="query-actions">
        <button className="primary-btn" type="submit" disabled={disabled || !question.trim()}>
          Run analysis
        </button>
        {loading && (
          <span className="inline-status">
            <span className="spinner" aria-hidden />
            Running analysis…
          </span>
        )}
      </div>
    </form>
  );
}
