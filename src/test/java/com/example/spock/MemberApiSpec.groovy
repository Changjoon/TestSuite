package com.example.spock

import com.jpabook.jpashop.controller.MemberForm
import io.restassured.RestAssured
import io.restassured.http.ContentType
import spock.lang.Specification
import spock.lang.Unroll

class MemberApiSpec extends Specification {

    @Unroll
    def "Create a new member with name #name, city #city, street #street, and zipcode #zipcode"() {
        given:
        def memberForm = new MemberForm()
        memberForm.setName(name)
        memberForm.setCity(city)
        memberForm.setStreet(street)
        memberForm.setZipcode(zipcode)

        when:
        def response = RestAssured.given()
                .baseUri(baseURI)
                .contentType(ContentType.JSON)
                .body(memberForm)
                .post(endpoint)

        then:
        response.statusCode == statusCode
        response.body.asString().contains(message)

        where:
        name       | city   | street        | zipcode | endpoint          | baseURI               | statusCode | message
        "John Doe" | "Seoul" | "123 Main St" | "12345" | "/members/member" | "http://localhost:8091" | 201       | "Member created successfully"
        "Jane Doe" | "Busan" | "456 Market St" | "67890" | "/members/member" | "http://localhost:8092" | 201       | "Member created successfully"
    }

    @Unroll
    def "Check the latest member with name #name, city #city, street #street, and zipcode #zipcode"() {
        given:
        def latestMemberEndpoint = "/members/latest"

        when:
        def response = RestAssured.given()
                .baseUri(baseURI)
                .get(latestMemberEndpoint)

        then:
        response.statusCode == 200
        def latestMember = response.jsonPath().getMap("")
        latestMember.name == name
        latestMember.address.city == city
        latestMember.address.street == street
        latestMember.address.zipcode == zipcode

        where:
        name       | city   | street        | zipcode | baseURI
        "John Doe" | "Seoul" | "123 Main St" | "12345" | "http://localhost:8091"
        "Jane Doe" | "Busan" | "456 Market St" | "67890" | "http://localhost:8092"
    }
}