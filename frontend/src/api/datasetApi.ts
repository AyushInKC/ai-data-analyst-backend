import type { DatasetResponse } from "../types/api";
import { apiUrl, readError } from "./client";

export async function uploadDataset(file: File): Promise<DatasetResponse> {
  const formData = new FormData();
  formData.append("file", file);

  let response: Response;
  try {
    response = await fetch(apiUrl("/api/dataset/upload"), {
      method: "POST",
      body: formData,
    });
  } catch {
    throw new TypeError("Network error");
  }

  if (!response.ok) {
    throw await readError(response);
  }

  return (await response.json()) as DatasetResponse;
}
