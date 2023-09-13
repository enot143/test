package t1.test;

import openapi.model.InputString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApiTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("Valid input string")
    @ParameterizedTest
    @MethodSource("provideStringsForValid")
    public void testValid(String input, String output)  {
        assertEquals(200, getResponseEntity(input).getStatusCodeValue());
        assertEquals(output, getResponseEntity(input).getBody());
    }

    @DisplayName("Invalid size of input string")
    @ParameterizedTest
    @ValueSource(strings = {"", "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuva"})
    public void testInvalidSize(String string) {
        assertEquals(400, getResponseEntity(string).getStatusCodeValue());
    }

    @DisplayName("Valid pattern of input string")
    @ParameterizedTest
    @ValueSource(strings = {"sS", "/", "!!!", "4995", "()("})
    public void testValidPattern(String string) {
        assertEquals(200, getResponseEntity(string).getStatusCodeValue());
    }

    @DisplayName("Invalid pattern of input string")
    @ParameterizedTest
    @ValueSource(strings = {"\n", "\t", "\b", " "})
    public void testInvalidPattern(String string) {
        assertEquals(400, getResponseEntity(string).getStatusCodeValue());
    }

    @Test
    @DisplayName("Nullable input")
    public void testNullableInput() {
        assertEquals(400, getResponseEntity(null).getStatusCodeValue());
    }

    private ResponseEntity<String> getResponseEntity(String string)  {
        InputString inputString = new InputString();
        inputString.setInputString(string);
        return this.restTemplate
                .postForEntity("http://localhost:" + port + "/test", inputString, String.class);
    }

    private static Stream<Arguments> provideStringsForValid() {
        return Stream.of(
                Arguments.of("aaaaabcccc", "\"a\":5,\"c\":4,\"b\":1"),
                Arguments.of("hhaabcdeffzxy", "\"a\":2,\"f\":2,\"h\":2,\"b\":1,\"c\":1,\"d\":1,\"e\":1,\"x\":1,\"y\":1,\"z\":1"),
                Arguments.of("yxzffedcbaahh", "\"a\":2,\"f\":2,\"h\":2,\"b\":1,\"c\":1,\"d\":1,\"e\":1,\"x\":1,\"y\":1,\"z\":1"),
                Arguments.of("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuv",
                        "\"a\":4,\"b\":4,\"c\":4,\"d\":4,\"e\":4,\"f\":4,\"g\":4," +
                                "\"h\":4,\"i\":4,\"j\":4,\"k\":4,\"l\":4,\"m\":4," +
                                "\"n\":4,\"o\":4,\"p\":4,\"q\":4,\"r\":4,\"s\":4," +
                                "\"t\":4,\"u\":4,\"v\":4,\"w\":3,\"x\":3,\"y\":3,\"z\":3"));
    }
}
