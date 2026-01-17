package com.cebp.doclocbackend;

import com.cebp.doclocbackend.ai.AISummarizationHttpClient;
import com.cebp.doclocbackend.ai.AiClientProperties;
import com.cebp.doclocbackend.ai.SummaryResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AISummarizationHttpClientTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer server;
    private AiClientProperties props;

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        server = MockRestServiceServer.createServer(restTemplate);
        props = new AiClientProperties();
        props.setBaseUrl("http://localhost:3000");
        props.setAuthToken("token");
    }

    @Test
    void summarize_callsServiceAndParsesResult() {
        server.expect(once(), requestTo("http://localhost:3000/api/summarize"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer token"))
                .andRespond(withSuccess("{\"summary\":\"hello\",\"aiService\":\"openrouter\",\"model\":\"m1\"}", MediaType.APPLICATION_JSON));

        AISummarizationHttpClient client = new AISummarizationHttpClient(restTemplate, props);
        SummaryResult result = client.summarize("bytes".getBytes(), "file.pdf", "openrouter", "m1");

        server.verify();
        assertThat(result.summaryText()).isEqualTo("hello");
        assertThat(result.provider()).isEqualTo("openrouter");
        assertThat(result.model()).isEqualTo("m1");
    }
}
