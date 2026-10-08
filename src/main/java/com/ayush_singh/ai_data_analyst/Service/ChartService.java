package com.ayush_singh.ai_data_analyst.Service;

import com.ayush_singh.ai_data_analyst.Model.ChartResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChartService {

    public ChartResponse generateChart(List<Map<String, Object>> data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        Map<String, Object> firstRow = data.get(0);

        if (firstRow.size() < 2) {
            return null;
        }

        String xAxis = null;
        String yAxis = null;

        for (Map.Entry<String, Object> entry : firstRow.entrySet()) {

            Object value = entry.getValue();

            if (value instanceof Number && yAxis == null) {
                yAxis = entry.getKey();
            } else if (xAxis == null) {
                xAxis = entry.getKey();
            }
        }

        if (xAxis == null || yAxis == null) {
            return null;
        }

        String chartType = "bar";

        String xAxisLower = xAxis.toLowerCase();

        if (xAxisLower.contains("date")
                || xAxisLower.contains("month")
                || xAxisLower.contains("year")
                || xAxisLower.contains("time")) {
            chartType = "line";
        }

        return new ChartResponse(
                chartType,
                xAxis,
                yAxis
        );
    }
}