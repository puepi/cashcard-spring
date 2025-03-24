package example.cashcard;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json;

    @Autowired
    private JacksonTester<CashCard[]> jsonList;

    private CashCard[] cashCards;

    @BeforeEach
    void setUp() {
        cashCards = Arrays.array(
                new CashCard(99L, 123.45),
                new CashCard(100L, 1.00),
                new CashCard(101L, 150.00));
    }

    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashcard=cashCards[0];
        assertThat(json.write(cashcard))
                .isStrictlyEqualToJson("expected.json");
        assertThat(json.write(cashcard))
                .hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashcard))
                .extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(json.write(cashcard))
                .hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashcard))
                .extractingJsonPathNumberValue("@.amount")
                .isEqualTo(123.45);

        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void cashCardDeserializationTest() throws IOException {
        String expected="""
                        {
                                "id":99,
                                "amount":123.45
                        }
                        """;
        

        String expected_= """
                [
                    { "id": 99, "amount": 123.45 },
                    { "id": 100, "amount": 1.00 },
                    { "id": 101, "amount": 150.00 }
                ]
                """;
        assertThat(json.parse(expected))
                .isEqualTo(new CashCard(99L,123.45));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);

        assertThat(jsonList.parse(expected_)).isEqualTo(cashCards);
    }

}
