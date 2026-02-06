package com.vidal.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		RestTemplate restTemplate = new RestTemplate();


		String generateUrl =
				"https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "RAGHAVENDRA");
		requestBody.put("regNo", "130");
		requestBody.put("email", "raghavendruppar@gmail.com");

		ResponseEntity<Map> response =
				restTemplate.postForEntity(generateUrl, requestBody, Map.class);

		Map responseBody = response.getBody();

		String webhookUrl = (String) responseBody.get("webhook");
		String accessToken = (String) responseBody.get("accessToken");

		System.out.println("Webhook URL: " + webhookUrl);
		System.out.println("Access Token: " + accessToken);



		String finalQuery = "SELECT d.DEPARTMENT_NAME, AVG(TIMESTAMPDIFF(YEAR, e.DOB, CURDATE())) AS AVERAGE_AGE, SUBSTRING_INDEX(GROUP_CONCAT(CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) ORDER BY e.EMP_ID SEPARATOR ', '), ', ', 10) AS EMPLOYEE_LIST FROM DEPARTMENT d JOIN EMPLOYEE e ON d.DEPARTMENT_ID = e.DEPARTMENT JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID WHERE p.AMOUNT > 70000 GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME ORDER BY d.DEPARTMENT_ID DESC;";


		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> finalBody = new HashMap<>();
		finalBody.put("finalQuery", finalQuery);

		HttpEntity<Map<String, String>> entity =
				new HttpEntity<>(finalBody, headers);

		restTemplate.postForEntity(webhookUrl, entity, String.class);

		System.out.println("Final Query Submitted Successfully!");
	}
}
