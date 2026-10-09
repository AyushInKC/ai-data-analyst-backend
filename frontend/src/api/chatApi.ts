import type { ChatRequest, ChatResponse } from "../types/api";
import { apiUrl, readError } from "./client";

export async function sendChat(request: ChatRequest): Promise<ChatResponse> {
  let response: Response;
  try {
    response = await fetch(apiUrl("/api/chat"), {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(request),
    });
  } catch {
    throw new TypeError("Network error");
  }

  if (!response.ok) {
    throw await readError(response);
  }

  return (await response.json()) as ChatResponse;
}
