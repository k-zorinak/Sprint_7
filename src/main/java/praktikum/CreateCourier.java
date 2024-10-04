package praktikum;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateCourier {
  public static Response createCourier(Courier courier){
      return given()
              .header("Content-type", "application/json")
              .and()
              .body(courier)
              .when()
              .post("/api/v1/courier");
  }
}
