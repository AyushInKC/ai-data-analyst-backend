import { ApiError, type ErrorResponse } from "../types/api";

const BASE_URL = (import.meta.env.VITE_API_BASE_URL ?? "").replace(/\/$/, "");

export function apiUrl(path: string): string {
  return `${BASE_URL}${path}`;
}

export async function readError(response: Response): Promise<ApiError> {
  let payload: ErrorResponse | undefined;

  try {
    payload = (await response.json()) as ErrorResponse;
  } catch {
    payload = undefined;
  }

  const message =
    payload?.message?.trim() ||
    (response.status === 0
      ? "Network error"
      : `Backend error (${response.status})`);

  return new ApiError(message, response.status, payload?.error);
}

export function toUserFacingError(error: unknown): string {
  if (error instanceof ApiError) {
    if (error.message.toLowerCase().includes("dataset not found")) {
      return error.message;
    }
    if (error.message.toLowerCase().includes("dataset id is required")) {
      return error.message;
    }
    return error.message;
  }

  if (error instanceof TypeError) {
    return "Network error. Confirm the backend is running.";
  }

  if (error instanceof Error && error.message) {
    return error.message;
  }

  return "An unexpected error occurred";
}
