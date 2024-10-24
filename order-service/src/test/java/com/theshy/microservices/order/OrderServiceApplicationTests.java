package com.theshy.microservices.order;

import com.theshy.microservices.order.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {
	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer(DockerImageName.parse("mysql:8.3.0"));

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
	}

	static {
		mySQLContainer.start();
	}

	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
			{
				"skuCode": "iphone_15",
				"price": 1111,
				"quantity": 1
			}
		""";
		InventoryClientStub.stubInventoryCall("iphone_15", 1);

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();
		MatcherAssert.assertThat(responseBodyString, Matchers.is("Order Placed successfully"));
	}

}
