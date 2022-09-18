package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;


class ShareItTests {

    public static final ObjectMapper objectMapper = JsonMapper.builder()
        .findAndAddModules()
        .build();

    @Test
    void contextLoads() {
        ShareItApp.main(new String[]{});
    }
}
