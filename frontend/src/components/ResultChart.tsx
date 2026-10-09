import type { ChartResponse, ResultRow } from "../types/api";

interface ResultChartProps {
  chart: ChartResponse | null;
  data: ResultRow[];
}

function toNumber(value: unknown): number | null {
  if (typeof value === "number" && Number.isFinite(value)) {
    return value;
  }
  if (typeof value === "string" && value.trim() !== "") {
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : null;
  }
  return null;
}

function label(value: unknown): string {
  if (value === null || value === undefined) {
    return "";
  }
  return String(value);
}

export function ResultChart({ chart, data }: ResultChartProps) {
  if (!chart || data.length === 0) {
    return null;
  }

  const points = data
    .map((row) => ({
      x: label(row[chart.xAxis]),
      y: toNumber(row[chart.yAxis]),
    }))
    .filter((point): point is { x: string; y: number } => point.y !== null);

  if (points.length === 0) {
    return null;
  }

  const type = chart.type === "line" ? "line" : "bar";
  const width = 640;
  const height = 220;
  const pad = { top: 12, right: 12, bottom: 36, left: 48 };
  const innerW = width - pad.left - pad.right;
  const innerH = height - pad.top - pad.bottom;
  const maxY = Math.max(...points.map((point) => point.y), 0);
  const minY = Math.min(...points.map((point) => point.y), 0);
  const span = maxY - minY || 1;

  const xFor = (index: number) => {
    if (points.length === 1) {
      return pad.left + innerW / 2;
    }
    return pad.left + (index / (points.length - (type === "line" ? 1 : 0))) * innerW;
  };

  const yFor = (value: number) =>
    pad.top + innerH - ((value - minY) / span) * innerH;

  const barWidth = Math.max(8, (innerW / points.length) * 0.62);

  return (
    <section className="chart-wrap">
      <h3>
        {type === "line" ? "Line" : "Bar"} · {chart.xAxis} / {chart.yAxis}
      </h3>
      <svg className="chart-svg" viewBox={`0 0 ${width} ${height}`} role="img">
        <line
          x1={pad.left}
          y1={pad.top}
          x2={pad.left}
          y2={pad.top + innerH}
          stroke="#b9b5aa"
        />
        <line
          x1={pad.left}
          y1={pad.top + innerH}
          x2={pad.left + innerW}
          y2={pad.top + innerH}
          stroke="#b9b5aa"
        />
        <text className="chart-axis" x={4} y={pad.top + 8}>
          {maxY.toLocaleString()}
        </text>
        <text className="chart-axis" x={4} y={pad.top + innerH}>
          {minY.toLocaleString()}
        </text>
        {type === "bar"
          ? points.map((point, index) => {
              const x =
                pad.left +
                (index + 0.5) * (innerW / points.length) -
                barWidth / 2;
              const y = yFor(point.y);
              const h = Math.max(1, pad.top + innerH - y);
              return (
                <g key={`${point.x}-${index}`}>
                  <rect x={x} y={y} width={barWidth} height={h} fill="#215a8e" />
                  <text
                    className="chart-axis"
                    x={x + barWidth / 2}
                    y={height - 12}
                    textAnchor="middle"
                  >
                    {point.x}
                  </text>
                </g>
              );
            })
          : (
            <>
              <polyline
                fill="none"
                stroke="#215a8e"
                strokeWidth="2"
                points={points
                  .map((point, index) => `${xFor(index)},${yFor(point.y)}`)
                  .join(" ")}
              />
              {points.map((point, index) => (
                <g key={`${point.x}-${index}`}>
                  <circle cx={xFor(index)} cy={yFor(point.y)} r="3" fill="#215a8e" />
                  <text
                    className="chart-axis"
                    x={xFor(index)}
                    y={height - 12}
                    textAnchor="middle"
                  >
                    {point.x}
                  </text>
                </g>
              ))}
            </>
          )}
      </svg>
    </section>
  );
}
