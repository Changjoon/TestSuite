package com.example.springframework;

import com.jpabook.jpashop.controller.MemberForm;
import com.jpabook.jpashop.domain.Member;

import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

//@SpringBootTest   // It makes too slow. So, it is not necessary.w
public class MemberApiTests {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testCreateMember() {
        String baseUri = "http://localhost:8091";
        String endpoint = "/members/member";

        // Create a new member
        MemberForm memberForm = new MemberForm();
        memberForm.setName("John Doe");
        memberForm.setCity("Seoul");
        memberForm.setStreet("123 Main St");
        memberForm.setZipcode("12345");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MemberForm> request = new HttpEntity<>(memberForm, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUri + endpoint, request, String.class);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Member created successfully"));

        // Verify the latest member
        ResponseEntity<Member> latestMemberResponse = restTemplate.getForEntity(baseUri + "/members/latest", Member.class);

        assertEquals(200, latestMemberResponse.getStatusCodeValue());

        Member latestMember = latestMemberResponse.getBody();
        assertNotNull(latestMember);
        assertEquals("John Doe", latestMember.getName());
        assertEquals("Seoul", latestMember.getAddress().getCity());
        assertEquals("123 Main St", latestMember.getAddress().getStreet());
        assertEquals("12345", latestMember.getAddress().getZipcode());
    }

    @Test
    public void testCreateAnotherMember() {
        String baseUri = "http://localhost:8092";
        String endpoint = "/members/member";

        // Create a new member
        MemberForm memberForm = new MemberForm();
        memberForm.setName("Jane Doe");
        memberForm.setCity("Busan");
        memberForm.setStreet("456 Market St");
        memberForm.setZipcode("67890");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MemberForm> request = new HttpEntity<>(memberForm, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUri + endpoint, request, String.class);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Member created successfully"));

        // Verify the latest member
        ResponseEntity<Member> latestMemberResponse = restTemplate.getForEntity(baseUri + "/members/latest", Member.class);

        assertEquals(200, latestMemberResponse.getStatusCodeValue());

        Member latestMember = latestMemberResponse.getBody();
        assertNotNull(latestMember);
        assertEquals("Jane Doe", latestMember.getName());
        assertEquals("Busan", latestMember.getAddress().getCity());
        assertEquals("456 Market St", latestMember.getAddress().getStreet());
        assertEquals("67890", latestMember.getAddress().getZipcode());
    }
}