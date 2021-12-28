package Udemy;

import Udemy.POJO2.addPlace;
import Udemy.POJO2.location;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class serializeTest {
    public static void main(String[] args) {
        addPlace p=new addPlace();
        p.setAccuracy(50);
        p.setAddress("29, side layout, cohen 09");
        p.setLanguage("English-Ca");
        p.setPhone_number("4371233233");
        p.setWebsite("https://rahulshettyacaemy.com");
        p.setName("my house");
        List<String> myList=new ArrayList<String>();
        myList.addAll(Arrays.asList("shoepark","shoe"));
        p.setTypes(myList);
        location l=new location();
        l.setLat(-38.383494);
        l.setLng(33.427362);
        p.setLocation(l);
        RestAssured.baseURI="https://rahulshettyacademy.com";
       Response res= given().log().all().queryParam("key","qaclick123").body(p)
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).extract().response();
        String responseAsString=res.asString();
        System.out.println(responseAsString);
    }
}
