package Udemy;

import Udemy.Files.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

public class complexJsonParse {
    public static void main(String[] args) {
        JsonPath js=new JsonPath(Payload.CoursePrice());// we are using dummy json cuz
        //we assume that develpoer hasnt developed api yet thats why we are using dummy which to be changed
        //with actual one later
        //Print no of courses returned by api
//        1. Print No of courses returned by API
      int count=  js.getInt("courses.size()"); // size is a method that is used in json path class
        System.out.println(count);
//        2.Print Purchase Amount
       int purchaseAmount= js.getInt("dashboard.purchaseAmount");
        System.out.println("purchaseAmount = " + purchaseAmount);
//        3. Print Title of the first course
       String titleofFirstCourse= js.get("courses[0].title");
        System.out.println("titleofFirstCourse = " + titleofFirstCourse);
//        4. Print All course titles and their respective Prices
        for (int i=0; i<count; i++){
            String courseTitles=js.get("courses["+i+"].title");
          //  System.out.println(js.get("courses[" + i + "].price").toString());
            int prices= js.get("courses["+i+"].price");
            System.out.println(courseTitles+"'s price is : "+prices);

        }
//        5. Print no of copies sold by RPA Course
        for (int i=0; i<count; i++){
            String courseTitles=js.get("courses["+i+"].title");
            if (courseTitles.equalsIgnoreCase("RPA")){
                int copies=js.get("courses[" + i + "].copies");
                System.out.println("copies = " + copies);
                break;
            }

        }
//        6. Verify if Sum of all Course prices matches with Purchase Amount
        int total=0;
        for(int i=0;i<count;i++){
            int price = js.getInt("courses["+i+"].price");
            int copies = js.getInt("courses["+i+"].copies");
            int priceCopiestop=price*copies;
            total+=priceCopiestop;
        }

        System.out.println(total);
        Assert.assertEquals(total,purchaseAmount);

    }
}
