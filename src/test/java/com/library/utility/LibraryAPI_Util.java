package com.library.utility;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LibraryAPI_Util {


    /**
     * Return TOKEN as String by using provided username from /token endpoint
     * @param userType
     * @return
     */
    public static String getToken(String userType){

        String email=ConfigurationReader.getProperty(userType+"_username");
        String password="libraryUser";

        return getToken(email,password);
    }

    public static String getToken(String email,String password){


        return given()
                .contentType(ContentType.URLENC)
                .formParam("email" , email)
                .formParam("password" , password).
                when()
                .post(ConfigurationReader.getProperty("library.baseUri")+"/login")
                //.prettyPeek()
                .then().statusCode(200)
                .extract().jsonPath().getString("token");



    }
    public static Map<String,Object> getRandomDataMap(String keyword){
        Faker faker = new Faker() ;
        Map<String,Object> map = new LinkedHashMap<>();
        switch (keyword){
            case "book":
                String randomBookName = faker.book().title() + faker.number().numberBetween(0, 10);
                map.put("name", randomBookName);
                map.put("isbn", faker.code().isbn10()   );
                map.put("year", String.valueOf(faker.number().numberBetween(1000,2021))   );
                map.put("author",faker.book().author()   );
                map.put("book_category_id", String.valueOf(faker.number().numberBetween(1,20))   );  // in library app valid category_id is 1-20
                map.put("description", faker.chuckNorris().fact() );
                break;
            case "user":
                String fullName = faker.name().fullName();
                String email=fullName.substring(0,fullName.indexOf(" "))+"@library";
                System.out.println(email);
                map.put("full_name", fullName );
                map.put("email", email);
                map.put("password", "libraryUser");
                map.put("user_group_id","2");
                map.put("status", "ACTIVE");
                map.put("start_date", "2023-03-11");
                map.put("end_date", "2024-03-11");
                map.put("address", faker.address().cityName());
                break;
        }

        return map;
    }
    public static Map<String,Object> getRandomBookMap(){

        Faker faker = new Faker() ;
        Map<String,Object> bookMap = new LinkedHashMap<>();
        String randomBookName = faker.book().title() + faker.number().numberBetween(0, 10);
        bookMap.put("name", randomBookName);
        bookMap.put("isbn", faker.code().isbn10()   );
        bookMap.put("year", String.valueOf(faker.number().numberBetween(1000,2021))   );
        bookMap.put("author",faker.book().author()   );
        bookMap.put("book_category_id", String.valueOf(faker.number().numberBetween(1,20) )  );  // in library app valid category_id is 1-20
        bookMap.put("description", faker.chuckNorris().fact() );

        return bookMap ;
    }

    public static Map<String,Object> getRandomUserMap(){

        Faker faker = new Faker() ;
        Map<String,Object> userMap = new LinkedHashMap<>();
        String fullName = faker.name().fullName();
        String email=fullName.substring(0,fullName.indexOf(" "))+"@library";
        System.out.println(email);
        userMap.put("full_name", fullName );
        userMap.put("email", email);
        userMap.put("password", "libraryUser");
        // 2 is librarian as role
        userMap.put("user_group_id","2");
        userMap.put("status", "ACTIVE");
        userMap.put("start_date", "2023-03-11");
        userMap.put("end_date", "2024-03-11");
        userMap.put("address", faker.address().cityName());

        return userMap ;
    }





}
