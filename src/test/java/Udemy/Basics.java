package Udemy;

import Udemy.Files.Payload;
import Udemy.Files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import org.testng.Assert;
import org.testng.asserts.Assertion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Basics {
    public static void main(String[] args) throws IOException {
        //Add - validate if add place api is working as expected
        //given - all input details that you need
        // , when submit the Api - resource and http methods come here
        // , then - validate response
        //ADD place - update place with new address >> get place to validate if new address is present in response
        //we use log.all only if we want to see more details about execution in terminal
        RestAssured.baseURI = "https://rahulshettyacademy.com"; //burasi bizim calistigim ana website
        // soru isaretinden sonra bir tane bilgi varsa queryparam birden fazla varsa query params kullaniliyor.
        //header istege bagli istersen kullanma
        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get("C:\\Users\\oralr\\Desktop\\bootcamp\\API\\addPlace.json"))))
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
        //Files class helps us to import external files such as JSON format and we can use its path directly
        //here without copying all information
        //We can either use it as it is above or below
        //Content of the file to String -> content of file can convert into Byte -> Byte Data to String we created
        //new String Object
//        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
//                .body(Payload.AddPlace())
//                .when().post("/maps/api/place/add/json")
//                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
//                .header("server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();

        System.out.println(response);
        JsonPath js = new JsonPath(response); // we used thid to parse response as in json format

        String placeId = js.getString("place_id");  //latitude would be location/lat
        System.out.println(placeId);

        //update place
        String newAddress = "70 Summer Walk Africa";
        given().log().all().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + placeId + "\",\n" +
                        "\"address\":\"" + newAddress + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}\n")
                .when().put("/maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200)
                .body("msg", equalTo("Address successfully updated"));

        //Get Place
//        Map<String, String> queryparams=new LinkedHashMap<>();
//        queryparams.put("key","qaclick123");
//        queryparams.put("place_id",placeId);
        //
        String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when().get("/maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200)
                .extract().response().asString();
        JsonPath js2 = ReusableMethods.rawToJSon(getPlaceResponse);
        String actualAddress = js2.getString("address");
        System.out.println(actualAddress);
        //Cucumber, JUnit and TestNG are both testing framework
        Assert.assertEquals(actualAddress, newAddress);
    }
}
