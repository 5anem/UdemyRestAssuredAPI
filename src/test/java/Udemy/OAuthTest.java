package Udemy;

import Udemy.POJO.GetCourse;
import Udemy.POJO.api;
import Udemy.POJO.webAutomation;
import Udemy.util.ConfigReader;
import Udemy.util.Driver;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

//Section 10
//we are putting steps in postman in to automation process here
public class OAuthTest {
    public static void main(String[] args) throws InterruptedException {
        Driver.getDriver().get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
        Driver.getDriver().findElement(By.id("identifierId")).sendKeys(ConfigReader.getProperty("username"));
        Driver.getDriver().findElement(By.xpath("//button[@class='VfPpkd-LgbsSe VfPpkd-LgbsSe-OWXEXe-k8QpJ VfPpkd-LgbsSe-OWXEXe-dgl2Hf nCP5yc AjY5Oe DuMIQc qIypjc TrZEUc lw1w4b']")).click();
        Thread.sleep(5000);
        Driver.getDriver().findElement(By.xpath("//input[@type='password']")).sendKeys(ConfigReader.getProperty("password"), Keys.ENTER);
        Thread.sleep(4000);
        String currentUrl = Driver.getDriver().getCurrentUrl();

        String partialcode = currentUrl.split("code=")[1];
        String actualcode = partialcode.split("&scope")[0];
        System.out.println("currentUrl = " + currentUrl);
        //String code = "4%2F0AX4XfWh24LUgENnvJJ3QOixFRBfwYoiD3klqnmeLTp7-aJaxrbrAvPbkjk9n_gGpJj0U9Q";
        //we are using url encoding enabled false because we dont want % to be converted to numeric value in code
        // thats why we need it
        String accessTokenResponse = given().queryParams("code", actualcode).urlEncodingEnabled(false)
                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type", "authorization_code")
                .when().log().all()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();
        JsonPath js = new JsonPath(accessTokenResponse);
        String accessToken = js.getString("access_token");

//        String response = given().queryParam("access_token", accessToken)
//                .when().get("https://rahulshettyacademy.com/getCourse.php").asString();
        //same response after POJO class introduced
        //if you Content-Type header is application/json you dont need to put defatultParser(Parser.JSON)
        //if it is other than that you need to explicitly tell java that it should be a json format by parsing it
        GetCourse responseJSON = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
        System.out.println("responseJSON.getLinkedIn() = " + responseJSON.getLinkedIn());
        System.out.println(responseJSON.getInstructor());
        System.out.println(responseJSON.getCourses().getMobile().get(0).getPrice());
        System.out.println(responseJSON.getCourses().getApi().get(1).getCourseTitle());
        List<api> apiCourses=responseJSON.getCourses().getApi();
        for (int i=0; i<apiCourses.size(); i++){
            if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")){
                System.out.println("apiCourses.get(i).getPrice() = " + apiCourses.get(i).getPrice());
            }
        }
        List<webAutomation> webAutomationCourses=responseJSON.getCourses().getWebAutomation();
        for (int i=0; i<webAutomationCourses.size(); i++){
            System.out.println(webAutomationCourses.get(i).getCourseTitle());
            System.out.println(webAutomationCourses.get(i).getPrice());
        }
        // System.out.println(response);
    }
}
