package com.ayush_singh.ai_data_analyst.Service;

import com.ayush_singh.ai_data_analyst.Model.ChartResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChartService {
    public ChartResponse generateChart(
            List<Map<String, Object>> data
    ) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        if (data.get(0).size() != 2) {
            return null;
        }

        String xAxis =
                data.get(0).keySet()
                        .stream()
                        .findFirst()
                        .orElse(null);

        String yAxis =
                data.get(0).keySet()
                        .stream()
                        .skip(1)
                        .findFirst()
                        .orElse(null);

        if (xAxis == null || yAxis == null) {
            return null;
        }

        return new ChartResponse(
                "bar",
                xAxis,
                yAxis
        );
    }

}