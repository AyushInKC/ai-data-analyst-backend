package com.ayush_singh.ai_data_analyst;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"cohere.api-key=test-api-key",
		"cohere.model=command-a-plus-05-2026"
})
class AiDataAnalystApplicationTests {

	@Test
	void contextLoads() {
	}

}
