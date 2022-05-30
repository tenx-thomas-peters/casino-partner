package com.casino.common.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.casino.modules.partner.common.entity.Member;

public class HttpUtils {

    public static ResponseEntity<String> userSubBalance(String url, String username, Float amount, String apiKey) {
        System.out.println("HttpUtils==userSubBalance==");
        System.out.println("*************** param info ***************");
        System.out.println("*** url : "+url);
        System.out.println("*** username : "+username);
        System.out.println("*** amount : "+amount);
        System.out.println("*** apiKey : "+apiKey);
        System.out.println("******************************************");

        try {
            HttpHeaders headers = new HttpHeaders();

            /*
             * set header content
             * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ <
             */
            headers.add(HttpHeaders.AUTHORIZATION, apiKey);
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ >

            RestTemplate restTemplate = new RestTemplate();

            Member member = new Member();
            member.setUsername(username);
            member.setAmount(amount);
            HttpEntity<Member> request = new HttpEntity<>(member, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            System.out.println("HttpUtils==userSubBalance========== api success");
            System.out.println(response);
            return response;
        } catch (Exception e) {
            System.out.println("HttpUtils==userSubBalance==========Exception :");
            e.printStackTrace();
            return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
