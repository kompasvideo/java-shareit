package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;


class ShareItTests {

	public static final ObjectMapper objectMapper = JsonMapper.builder()
		.findAndAddModules()
		.build();

	@Test
	void contextLoads() {
		ShareItApp.main(new String[]{});
	}
}
