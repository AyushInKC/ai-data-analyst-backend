interface AnswerPanelProps {
  answer: string;
}

export function AnswerPanel({ answer }: AnswerPanelProps) {
  return (
    <section className="answer">
      <p className="section-label">Answer</p>
      <p>{answer}</p>
    </section>
  );
}
