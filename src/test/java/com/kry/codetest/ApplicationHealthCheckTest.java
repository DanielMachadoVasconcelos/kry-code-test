package com.kry.codetest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationHealthCheckTest {

    @LocalManagementPort
    private int randomManagementPort;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testThatHealthEndpointsDontNeedAuth() throws Exception {
        var healthUri = new URI(String.format("http://localhost:%s/health", randomManagementPort));
        var response = template.getForEntity(healthUri, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
