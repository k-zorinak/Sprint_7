import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import praktikum.Courier;
import praktikum.CreateCourier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourierTest() {
        String login = RandomStringUtils.random(10, true, false);
        String password = RandomStringUtils.random(10, true, true);
        String firstName = RandomStringUtils.random(10, true, false);

        Courier courier = new Courier(login, password, firstName);

        Response createCourier = CreateCourier.createCourier(courier);

        createCourier
                .then().statusCode(201).assertThat().body("ok",equalTo(true));
    }

    @Test
    public void createCourierWithoutOneParameter() {
        String login = RandomStringUtils.random(10, true, false);
        String password = "";
        String firstName = RandomStringUtils.random(10, true, false);

        Courier courier = new Courier(login, password, firstName);

        Response createCourier = CreateCourier.createCourier(courier);

        createCourier
                .then().statusCode(400).assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createIdenticalCouriersReturnError(){
        String login = RandomStringUtils.random(10, true, false);
        String password = RandomStringUtils.random(10, true, true);
        String firstName = RandomStringUtils.random(10, true, false);

        Courier courier = new Courier(login, password, firstName);

        CreateCourier.createCourier(courier);

        Response createCourier = CreateCourier.createCourier(courier);

        createCourier
                .then().statusCode(409).assertThat().body("message",equalTo("Этот логин уже используется"));
    }

    @Test
    public void loginCourier(){
        String login = RandomStringUtils.random(10, true, false);
        String password = RandomStringUtils.random(10, true, true);
        String firstName = RandomStringUtils.random(10, true, false);
        Courier courier = new Courier(login, password, firstName);

        CreateCourier.createCourier(courier);

        Courier loginCourier = new Courier(login, password);
        Response response =
                given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourier)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void enterNonExistentLogin(){
        String login = RandomStringUtils.random(10, true, false);
        String password = RandomStringUtils.random(10, true, true);

        Courier loginCourier = new Courier(login, password);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message",equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    public void enterLoginWithoutLogin(){
        String login = "";
        String password = RandomStringUtils.random(10, true, true);

        Courier loginCourier = new Courier(login, password);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message",equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }
}