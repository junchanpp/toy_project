package com.example.toy_project;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;

@Slf4j
public class DetectUndefinedValueTest {

    @Nested
    class TestWithDefaultObjectMapper {

        ObjectMapper objectMapper = new ObjectMapper();

        @Test
        void useObjectMapper() throws JsonProcessingException {
            String json = "{\"name\": null}";
            CustomRequest request = new CustomRequest(Optional.of("test_name"),
                    Optional.of("test_nickname"));

            ObjectReader objectReader = objectMapper.readerForUpdating(request);
            CustomRequest updatedRequest = objectReader.readValue(json);

            log.info("updatedRequest: {}", updatedRequest);

            assertThat(updatedRequest.getName()).isEqualTo(Optional.empty());
            assertThat(updatedRequest.getNickname()).isEqualTo(Optional.of("test_nickname"));
        }

        @Test
        void useObjectMapperWithUndefinedValue() throws JsonProcessingException {
            String json = "{\"name\": null}";

            CustomRequest updatedRequest = objectMapper.readerWithView(CustomRequest.class)
                    .forType(CustomRequest.class).readValue(json);

            log.info("updatedRequest: {}", updatedRequest);

            assertThat(updatedRequest.getName()).isEqualTo(Optional.empty());
            assertThat(updatedRequest.getNickname()).isEqualTo(null);
        }

        @Getter
        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        private static class CustomRequest {

            private Optional<String> name;
            private Optional<String> nickname;

            public void setName(String name) {
                this.name = Optional.ofNullable(name);
            }

            public void setNickname(String nickname) {
                this.nickname = Optional.ofNullable(nickname);
            }
        }
    }

    @Nested
    class testWithJsonNullableObjectMapper {

        ObjectMapper objectMapper = new ObjectMapper();

        {
            objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS,
                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.registerModules(new JsonNullableModule(), new JavaTimeModule());
        }

        @Test
        void useJsonNullableAllUndefined() throws JsonProcessingException {
            var json = "{}";
            var object = new CustomRequestForJsonNullable(JsonNullable.undefined(),
                    JsonNullable.undefined());

            var result = objectMapper.readValue(json, CustomRequestForJsonNullable.class);

            assertEquals(result, object);
        }

        @Test
        void useJsonNullableWithNull() throws JsonProcessingException {
            var json = "{\"name\":null}";
            var object = new CustomRequestForJsonNullable(JsonNullable.of(null),
                    JsonNullable.undefined());

            var result = objectMapper.readValue(json, CustomRequestForJsonNullable.class);

            assertEquals(result, object);
        }

        @Test
        void useJsonNullableWithEmptyString() throws JsonProcessingException {
            var json = "{\"name\":\"\"}";
            var object = new CustomRequestForJsonNullable(JsonNullable.of(""),
                    JsonNullable.undefined());

            var result = objectMapper.readValue(json, CustomRequestForJsonNullable.class);

            assertEquals(result, object);
        }

        private record CustomRequestForJsonNullable(JsonNullable<String> name,
                                                    JsonNullable<String> nickname) {

        }
    }


}
