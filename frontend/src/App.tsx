import { useMemo, useState } from "react";
import { sendChat } from "./api/chatApi";
import { toUserFacingError } from "./api/client";
import { uploadDataset } from "./api/datasetApi";
import { AnswerPanel } from "./components/AnswerPanel";
import { ConversationHistory } from "./components/ConversationHistory";
import { DatasetInfo } from "./components/DatasetInfo";
import { DatasetUpload } from "./components/DatasetUpload";
import { ErrorMessage } from "./components/ErrorMessage";
import { Header } from "./components/Header";
import { QueryInput } from "./components/QueryInput";
import { ResultChart } from "./components/ResultChart";
import { ResultTable } from "./components/ResultTable";
import { SqlPanel } from "./components/SqlPanel";
import type {
  ChatResponse,
  ConversationTurn,
  DatasetResponse,
} from "./types/api";

function createSessionId(): string {
  return crypto.randomUUID();
}

export default function App() {
  const sessionId = useMemo(createSessionId, []);
  const [dataset, setDataset] = useState<DatasetResponse | null>(null);
  const [question, setQuestion] = useState("");
  const [uploading, setUploading] = useState(false);
  const [analyzing, setAnalyzing] = useState(false);
  const [uploadError, setUploadError] = useState<string | null>(null);
  const [queryError, setQueryError] = useState<string | null>(null);
  const [result, setResult] = useState<ChatResponse | null>(null);
  const [history, setHistory] = useState<ConversationTurn[]>([]);

  async function handleUpload(file: File) {
    setUploading(true);
    setUploadError(null);
    setQueryError(null);
    try {
      const response = await uploadDataset(file);
      setDataset(response);
      setResult(null);
    } catch (error) {
      setUploadError(toUserFacingError(error));
    } finally {
      setUploading(false);
    }
  }

  async function handleAsk() {
    const trimmed = question.trim();
    if (!trimmed || analyzing) {
      return;
    }

    if (!dataset) {
      setQueryError("Invalid dataset. Upload a CSV first.");
      return;
    }

    setAnalyzing(true);
    setQueryError(null);

    try {
      const response = await sendChat({
        sessionId,
        datasetId: dataset.datasetId,
        question: trimmed,
      });
      setResult(response);
      setHistory((current) => [
        ...current,
        {
          id: crypto.randomUUID(),
          question: trimmed,
          answer: response.answer,
        },
      ]);
      setQuestion("");
    } catch (error) {
      setQueryError(toUserFacingError(error));
    } finally {
      setAnalyzing(false);
    }
  }

  return (
    <div className="app">
      <Header dataset={dataset} />
      <main className="workspace">
        <aside className="dataset-panel">
          <DatasetUpload uploading={uploading} onFile={handleUpload} />
          {uploading && (
            <p className="inline-status" style={{ marginTop: 12 }}>
              <span className="spinner" aria-hidden />
              Uploading…
            </p>
          )}
          <ErrorMessage message={uploadError} />
          {dataset && <DatasetInfo dataset={dataset} />}
        </aside>

        <section className="analysis-panel">
          <QueryInput
            question={question}
            disabled={analyzing || uploading}
            loading={analyzing}
            onChange={setQuestion}
            onSubmit={() => void handleAsk()}
          />
          <ErrorMessage message={queryError} />

          {result && (
            <>
              <AnswerPanel answer={result.answer} />
              {result.sql ? <SqlPanel sql={result.sql} /> : null}
              <ResultTable data={result.data ?? []} />
              <ResultChart chart={result.chart} data={result.data ?? []} />
            </>
          )}

          <ConversationHistory turns={history} />
        </section>
      </main>
    </div>
  );
}
