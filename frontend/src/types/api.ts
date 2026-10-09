export interface DatasetResponse {
  datasetId: string;
  fileName: string;
  rowCount: number;
  columnCount: number;
  columns: string[];
}

export interface ChatRequest {
  sessionId: string;
  datasetId: string;
  question: string;
}

export interface ChartResponse {
  type: string;
  xAxis: string;
  yAxis: string;
}

export type ResultRow = Record<string, unknown>;

export interface ChatResponse {
  answer: string;
  sql: string;
  data: ResultRow[];
  chart: ChartResponse | null;
}

export interface ErrorResponse {
  error: string;
  message: string;
  timestamp: string;
}

export class ApiError extends Error {
  status: number;
  errorCode?: string;

  constructor(message: string, status: number, errorCode?: string) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.errorCode = errorCode;
  }
}

export interface ConversationTurn {
  id: string;
  question: string;
  answer: string;
}
