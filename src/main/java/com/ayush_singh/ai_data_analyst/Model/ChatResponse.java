package com.ayush_singh.ai_data_analyst.Model;

import java.util.List;
import java.util.Map;

public record ChatResponse<data>(String answer,
                                 String sql,
                        List<Map<String, Object>> data,
                                 ChartResponse chart) {
}
