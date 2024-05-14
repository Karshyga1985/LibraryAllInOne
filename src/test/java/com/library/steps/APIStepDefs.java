package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.DashboardPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.gherkin.internal.com.eclipsesource.json.Json;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class APIStepDefs {
    RequestSpecification givenPart;
    Response response;
    ValidatableResponse thenPart;
    LoginPage loginPage;
    BookPage bookPage;
    String id;
    /*
     *  US - 01
     */
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {
        givenPart = given()//.log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));
    }
    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeader) {
        givenPart.accept(acceptHeader);
    }
    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
         response = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint);
                //.prettyPeek();
       thenPart = response.then();
    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {
        thenPart.statusCode(statusCode);
    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        thenPart.contentType(contentType);
    }
    @Then("Each {string} field should not be null")
    public void each_field_should_not_be_null(String path) {
        thenPart.body(path, everyItem(notNullValue()));
    }

    /*
     * USER STORY 2
     */
    String idValue;
    @And("Path param {string} is {string}")
    public void pathParamIs(String key, String value) {
        givenPart.pathParam(key, value);
        idValue = value;
    }
    @And("{string} field should be same with path param")
    public void fieldShouldBeSameWithPathParam(String expectedId) {
        thenPart.body(expectedId, equalTo(idValue));
    }
    @And("following fields should not be null")
    public void followingFieldsShouldNotBeNull(List<String> expectedFields) {
        for (String eachField : expectedFields) {
            thenPart.body(eachField, is(notNullValue()));
        }
    }
    /*
     * USER STORY 3
     */
    @And("Request Content Type header is {string}")
    public void requestContentTypeHeaderIs(String requestContentTypeHeader) {
        givenPart.contentType(requestContentTypeHeader);
    }
    Map <String, Object> requestBody;
    Map<String, Object> apiData;

    @And("I create a random {string} as request body")
    public void iCreateARandomAsRequestBody(String randomData) {
        requestBody = new LinkedHashMap<>();
        switch (randomData){
            case "book":
                requestBody = LibraryAPI_Util.getRandomBookMap();
                break;
            case "user":
                requestBody = LibraryAPI_Util.getRandomUserMap();
                break;
            default:
                throw new RuntimeException("Unexpected Data: " + randomData);
        }
        givenPart.formParams(requestBody);
        apiData = requestBody;
    }
    @When("I send POST request to {string} endpoint")
    public void iSendPOSTRequestToEndpoint(String endpoint) {
        response = givenPart.when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint);//.prettyPeek();
        thenPart = response.then();
    }
    @And("the field value for {string} path should be equal to {string}")
    public void theFieldValueForPathShouldBeEqualTo(String path, String value) {
        thenPart.body(path, is(value));
    }
    @And("{string} field should not be null")
    public void fieldShouldNotBeNull(String idPath) {
        thenPart.body(idPath, is(notNullValue()));
    }
    /*
     * USER STORY 3-2
     */
    @And("UI, Database and API created book information must match")
    public void uiDatabaseAndAPICreatedBookInformationMustMatch() {
        bookPage = new BookPage();
        id = response.path("book_id");
        // retreiving database data
        String query = "select name, isbn, year, author, book_category_id, description from books\n" +
                "where id = " + id;
        DB_Util.runQuery(query);

        Map<String, Object> dbData = DB_Util.getRowMap(1);
        System.out.println("dbData = " + dbData);
        // verifying api data and database data
        Assert.assertEquals(apiData, dbData);

        String expectedBookNameAPI = (String) apiData.get("name");

        bookPage.search.sendKeys(expectedBookNameAPI + Keys.ENTER);
        BrowserUtil.waitFor(5);
        bookPage.editBook(expectedBookNameAPI).click();
        BrowserUtil.waitFor(3);

        Map<String, Object> uiData = new LinkedHashMap<>();

        String actualBookNameUI = bookPage.bookName.getAttribute("value");
        uiData.put("name", actualBookNameUI);
        String actualIsbnUI = bookPage.isbn.getAttribute("value");
        uiData.put("isbn", actualIsbnUI);
        String actualYearUI = bookPage.year.getAttribute("value");
        uiData.put("year", actualYearUI);
        String actualAuthorUI = bookPage.author.getAttribute("value");
        uiData.put("author", actualAuthorUI);

        //retrieve book category id
        String selectedCategoryUI = BrowserUtil.getSelectedOption(bookPage.categoryDropdown);
        DB_Util.runQuery("select id from book_categories where name = '" + selectedCategoryUI + "'");
        String actualBookCategoryIdUI = DB_Util.getFirstRowFirstColumn();
        uiData.put("book_category_id", actualBookCategoryIdUI);

        String actualDescUI = bookPage.description.getAttribute("value");
        uiData.put("description", actualDescUI);

        Assert.assertEquals(apiData, uiData);

    }
    /*
     * USER STORY 4-2
     */
    @And("created user information should match with Database")
    public void createdUserInformationShouldMatchWithDatabase() {
        id = response.path("user_id");
        //holding original apiData in the another map to use in the next step
        //Map<String , Object> apiData2 = new LinkedHashMap<>(apiData);
        //remove password from apiData to do assertion
        String password= (String) apiData.remove("password");
        // retrieve information from database
        String query = "select full_name, email, user_group_id, status, start_date, end_date, address from users\n" +
                "where id = " + id;
        DB_Util.runQuery(query);
        Map<String, Object> mapDB = DB_Util.getRowMap(1);
        Assert.assertEquals(apiData, mapDB);

        apiData.put("password",password);
    }
    @And("created user should be able to login Library UI")
    public void createdUserShouldBeAbleToLoginLibraryUI() {
        loginPage = new LoginPage();
        String email = (String) apiData.get("email");
        String password = (String) apiData.get("password");
        loginPage.login(email, password);
        BrowserUtil.waitFor(5);
    }
    @And("created user name should appear in Dashboard Page")
    public void createdUserNameShouldAppearInDashboardPage() {
        bookPage = new BookPage();
        BrowserUtil.waitFor(5);
        String expectedFullNameAPI = (String) apiData.get("full_name");
        String actualUserNameUI = bookPage.accountHolderName.getText();
        Assert.assertEquals(expectedFullNameAPI, actualUserNameUI);
    }
    /*
     * USER STORY 5
     */
    String token;
    @Given("I logged Library api with credentials {string} and {string}")
    public void iLoggedLibraryApiWithCredentialsAnd(String email, String password) {
        token = LibraryAPI_Util.getToken(email, password);
        givenPart = given().log().uri();
    }
    @And("I send token information as request body")
    public void iSendTokenInformationAsRequestBody() {
      givenPart.formParam("token", token);
    }
    /*
     * USER STORY 6
     */
    List<Map<String, Object>> categoriesAPI;
    @And("UI, Database and API book categories information must match")
    public void uiDatabaseAndAPIBookCategoriesInformationMustMatch() {
        //retrieving info from API response
        categoriesAPI = response.path("");
        System.out.println(categoriesAPI);

        //retrieving book categories from database
        String query = "select id, name from book_categories";
        DB_Util.runQuery(query);
        List<Map<String, Object>> categoriesDB = DB_Util.getAllRowAsListOfMap();
        System.out.println(categoriesDB);

        //verifying API and DB
        Assert.assertEquals(categoriesAPI, categoriesDB);

        BrowserUtil.waitForPageToLoad(5);

        //retrieving info form UI
        bookPage = new BookPage();
        Select select = new Select(bookPage.mainCategoryElement);
        List<WebElement> categoriesUI = select.getOptions();
        List<String> categoriesNameUI = new ArrayList<>();
        for (WebElement each : categoriesUI) {
            if(!each.getText().equals("ALL")) {
                categoriesNameUI.add(each.getText());
            }
        }
        System.out.println(categoriesNameUI);
        //retrieving only names from API response map
        List<String> categoriesNameAPI = new ArrayList<>();
        for (Map<String, Object> each : categoriesAPI) {
            categoriesNameAPI.add((String) each.get("name"));
        }
        System.out.println(categoriesNameAPI);

        //verifying API and UI
        Assert.assertEquals(categoriesNameAPI, categoriesNameUI);
    }
    /*
     * USER STORY 7
     */
    @And("{string} field should be same with path param values")
    public void fieldShouldBeSameWithPathParamValues(String expectedIds) {
        String actualIds = response.path("id");
        System.out.println(actualIds);
        Assert.assertEquals(expectedIds, actualIds);
    }
    /*
     * USER STORY 8
     */
    @And("I provide {string} and update {string} as a request body")
    public void iProvideAndUpdateAsARequestBody(String bookId, String updatingInfo) {
        requestBody = new LinkedHashMap<>();
        requestBody.put("id", bookId);
        requestBody.put("name", updatingInfo);
        givenPart.body(requestBody);
    }
    @When("I send PATCH request to {string} endpoint")
    public void iSendPATCHRequestToEndpoint(String endpoint) {
        response = givenPart.when().patch(ConfigurationReader.getProperty("library.baseUri")+ endpoint);
        thenPart = response.then();
    }
    /*
     * USER STORY 9
     */
    @When("I send DELETE request to {string} endpoint")
    public void iSendDELETERequestToEndpoint(String endpoint) {
        response = givenPart.when().delete(ConfigurationReader.getProperty("library.baseUri") + endpoint);
        thenPart = response.then();
    }
    /*
     * USER STORY 10
     */
    @And("API response should match with Database")
    public void apiResponseShouldMatchWithDatabase() {
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> actualListAPI = jsonPath.getList("");
        System.out.println(actualListAPI);

        //retrieving data from database
        String query = "select bb.id, bb.book_id, bb.user_id, bb.borrowed_date, bb.planned_return_date, bb.returned_date, bb.is_returned, b.name from books b\n" +
                "inner join book_borrow bb\n" +
                "on b.id = bb.book_id\n" +
                "where user_id = " + idValue;
        DB_Util.runQuery(query);
        List<Map<String, Object>> expectedListDB = DB_Util.getAllRowAsListOfMap();
        System.out.println(expectedListDB);
        Assert.assertEquals(expectedListDB, actualListAPI);
    }
    /*
     * USER STORY 12
     */
    @And("I provide borrow_id {string} as a request body")
    public void iProvideBorrow_idAsARequestBody(String id) {
        givenPart.formParam("id", id);
    }
    /*
     * USER STORY 13
     */
    @And("I provide {string} as {string} and {string} as {string} as a request body")
    public void iProvideAsAndAsAsARequestBody(String value1, String key1, String value2, String key2) {
        givenPart.formParam(key1, value1)
                .formParam(key2, value2);
    }
    @And("{string} should match with DB record and is_returned should be {string}")
    public void shouldMatchWithDBRecordAndIs_returnedShouldBe(String bookId, String isReturnedResult) {
        Map<String, Object> expectedResult = new LinkedHashMap<>();
        expectedResult.put("book_id", bookId);
        expectedResult.put("is_returned", isReturnedResult);

        //retrieving data form database
        String query = "select book_id, is_returned from book_borrow\n" +
                "where book_id = " + bookId;
        DB_Util.runQuery(query);
        Map<String, Object> actualResultDB = DB_Util.getRowMap(1);
        Assert.assertEquals(expectedResult, actualResultDB);
    }
    /*
     * USER STORY 14
     */
    @And("API response should match with UI")
    public void apiResponseShouldMatchWithUI() {
        JsonPath jsonPath = response.jsonPath();
        Map<String, Object> responseAPI = jsonPath.getMap("");
        System.out.println(responseAPI);

        loginPage = new LoginPage();
        loginPage.login(ConfigurationReader.getProperty("librarian_username"), ConfigurationReader.getProperty("librarian_password"));

        DashboardPage dashboardPage = new DashboardPage();
        Map<String, Object> actualUI = new LinkedHashMap<>();
        actualUI.put("book_count", dashboardPage.books.getText());
        actualUI.put("borrowed_books", dashboardPage.borrowedBooks.getText());
        actualUI.put("users", dashboardPage.users.getText());
        System.out.println(actualUI);
    }
    /*
     * USER STORY 15
     */
    @And("user groups API response should match with Database")
    public void userGroupsAPIResponseShouldMatchWithDatabase() {
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> actualResultAPI = jsonPath.getList("");
        System.out.println(actualResultAPI);

        String query = "select id, group_name from user_groups";
        DB_Util.runQuery(query);
        List<Map<String, Object>> expectedResultDB = DB_Util.getAllRowAsListOfMap();
        System.out.println(expectedResultDB);

        Assert.assertEquals(expectedResultDB, actualResultAPI);

    }
    /*
     * USER STORY 16
     */
    @Given("I start an API request")
    public void iStartAnAPIRequest() {
        givenPart = given();
    }


    @And("I update one of the following fields {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string},  the user information as request body")
    public void iUpdateOneOfTheFollowingFieldsTheUserInformationAsRequestBody(String fullName, String email, String password, String userGroupId, String status, String startDate, String endDate, String address, String id) {
    requestBody = new LinkedHashMap<>();
    requestBody.put("full_name", fullName);
    requestBody.put("email", email);
    requestBody.put("password", password);
    requestBody.put("user_group_id", userGroupId);
    requestBody.put("status", status);
    requestBody.put("start_date", startDate);
    requestBody.put("end_date", endDate);
    requestBody.put("address", address);
    requestBody.put("id", id);

    givenPart.formParams(requestBody);
    }
}
