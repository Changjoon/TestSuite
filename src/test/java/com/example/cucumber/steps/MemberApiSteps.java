package com.example.cucumber.steps;

import com.jpabook.jpashop.controller.MemberForm;
import com.jpabook.jpashop.domain.Member;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class MemberApiSteps {

    private MemberForm memberForm;
    private Response response;

    @Given("I have a member with name {string}, city {string}, street {string}, and zipcode {string}")
    public void i_have_a_member_with_name_city_street_and_zipcode(String name, String city, String street, String zipcode) {
        memberForm = new MemberForm();
        memberForm.setName(name);
        memberForm.setCity(city);
        memberForm.setStreet(street);
        memberForm.setZipcode(zipcode);
    }

    @When("I send a POST request to {string} with base URI {string}")
    public void i_send_a_post_request_to_with_base_URI(String endpoint, String baseURI) {
        response = RestAssured.given()
                .baseUri(baseURI)
                .contentType(ContentType.JSON)
                .body(memberForm)
                .when()
                .post(endpoint);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("the response should contain the message {string}")
    public void the_response_should_contain_the_message(String message) {
        String responseBody = response.getBody().asString();
        assertThat(responseBody, containsString(message));
    }

    @Then("the latest member should have name {string}, city {string}, street {string}, and zipcode {string} with {string}")
    public void the_latest_member_should_have_name_city_street_and_zipcode(String name, String city, String street, String zipcode, String baseURI) {
        // GET 요청을 통해 최신 멤버 확인
        Response latestMemberResponse = RestAssured.given()
                .baseUri(baseURI)
                .when()
                .get("/members/latest");

        latestMemberResponse.then().statusCode(200);

        Member latestMember = latestMemberResponse.as(Member.class);
        assertThat(latestMember.getName(), equalTo(name));
        assertThat(latestMember.getAddress().getCity(), equalTo(city));
        assertThat(latestMember.getAddress().getStreet(), equalTo(street));
        assertThat(latestMember.getAddress().getZipcode(), equalTo(zipcode));
    }
}