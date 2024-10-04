import io.restassured.RestAssured;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.Order;
import praktikum.ResponseCreate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest {
    private final String colorsOptions;

    public OrderTest(String colorsOptions) {
        this.colorsOptions = colorsOptions;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters(name = "Проверка возвращаемого значения в зависимости от количества цветов самоката. Тестовые данные: {0} {1} {2}")
    public static Object[][] createOrderParams() {
            return new Object[][]{
                    {"BLACK"},
                    {"BLACK, GREY"},
                    {""}
            };
    }

    @Test
    public void createOrderParamColors(){
        String[] colors = new String[2];
        colors[0] = colorsOptions;
        Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 7, "2020-06-06", "Saske, come back to Konoha", colors);
        ResponseCreate responseCreate = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders")
                .body()
                .as(ResponseCreate.class);
        MatcherAssert.assertThat(responseCreate.getTrack(), notNullValue());
    }
}