import type { ConversationTurn } from "../types/api";

interface ConversationHistoryProps {
  turns: ConversationTurn[];
}

export function ConversationHistory({ turns }: ConversationHistoryProps) {
  if (turns.length === 0) {
    return null;
  }

  return (
    <section className="history">
      <p className="section-label">Session</p>
      {turns.map((turn) => (
        <div className="turn" key={turn.id}>
          <div className="who">User</div>
          <p>{turn.question}</p>
          <div className="who" style={{ marginTop: 8 }}>
            Assistant
          </div>
          <p>{turn.answer}</p>
        </div>
      ))}
    </section>
  );
}
