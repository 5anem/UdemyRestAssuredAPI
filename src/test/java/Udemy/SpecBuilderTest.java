package Udemy;

import Udemy.POJO2.addPlace;
import Udemy.POJO2.location;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SpecBuilderTest {
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
        RequestSpecification req =new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key","qaclick123")
                .setContentType(ContentType.JSON).build();
       // given - request attached, when- where we are sending, then - assertion
        ResponseSpecification resspec= new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
        RequestSpecification res= given().spec(req).body(p);

         Response response=res.when().post("/maps/api/place/add/json")
                .then().spec(resspec).extract().response();
        String responseAsString=response.asString();
        System.out.println(responseAsString);
    }
}
