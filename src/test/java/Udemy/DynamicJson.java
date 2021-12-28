package Udemy;

import Udemy.Files.Payload;
import Udemy.Files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJson {
    //Library API
    @Test(dataProvider = "BooksData")
    public void addBook(String isbn,String aisle){
        RestAssured.baseURI="http://216.10.245.166";
      String response=  given().header("Content-Type","application/json")
                .body(Payload.AddBook(isbn,aisle)).when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js=ReusableMethods.rawToJSon(response);
       String id= js.get("ID");
        System.out.println("id = " + id);
    }
    @DataProvider(name = "BooksData")
    public Object [][] getData(){
        return new Object [][]{
                {"sdfjsd","1233"},
                {"scfjsd","1433"},
                {"srfjsd","1533"}};
    }
}
