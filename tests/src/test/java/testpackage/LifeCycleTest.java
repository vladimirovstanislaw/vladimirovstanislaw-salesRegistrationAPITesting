package testpackage;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ikon.DTO.CreateConsumer.Request;
import ru.ikon.DTO.QueryConsumers.Consumer;
import ru.ikon.entities.Environment;
import ru.ikon.entities.IntegrationPartner;
import ru.ikon.entities.User;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LifeCycleTest {
    //G:/NotPass.txt
    private static final Logger log = LoggerFactory.getLogger(LifeCycleTest.class);

    private final String POSCODE_HEADER_NAME = "PosCode";
    private final String CONTENTTYPE_HEADER_NAME = "Content-type";
    private final String CONTENTTYPE_HEADER_VALUE = "application/xml; charset=UTF-8";

    private final String AUTHORIZATION_HEADER_NAME = "Authorization";

    public final String AUTH_SEPARATOR = ":";

    private final String AUTH_TYPE = "Basic ";
    private final List<User> userList = List.of(new User("79062716258"));
    //salesRegistrationAPITesting
    private final String TYRE_ART_AUTOGRAPH_SNOW_C3 = "T729108";
    private final int TEST_ENVIRONMENT = 0;
    private final int TEST_ENVIRONMENT_PARTNER = 0;
    private final int TEST_ENVIRONMENT_USER = 0;
    //p.3.0.1
    private final String QUERY_CAMPAIGNS = "/QueryCampaigns/v1/xml/?itemCode=";

    private final String TYPE_GUARANTEE = "&type=Guarantee";
    private final String QUERY_CONSUMERS = "/QueryConsumers/v1/xml?phone=";
    private final String CREATE_CONSUMER = "/CreateConsumer/v1/xml";

    private final int HTTP_200_CODE = 200;
    private final int HTTP_404_CODE = 404;

    private static XmlMapper mapper;

    @BeforeAll
    public static void globalSetUp() {
        mapper = new XmlMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }


    @BeforeEach
    public void setUp() {
        System.out.print("\n@BeforeEach. ");

    }

    @Test
    @DisplayName("Test from example")
    public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()
            throws ClientProtocolException, IOException {

        // Given
        String name = "12312312";
        HttpUriRequest request = new HttpGet("https://api.github.com/users/" + name);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("3.0.1.(QUERY_CAMPAIGNS) Test for tyre exists in marketing campaigns")
    public void tyreExistsInMarketingCampaigns()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(environmentList.get(TEST_ENVIRONMENT).getApiSite() + QUERY_CAMPAIGNS + TYRE_ART_AUTOGRAPH_SNOW_C3 + TYPE_GUARANTEE);
        request.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        request.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        request.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        var statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == HTTP_200_CODE) {
            var streamToString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            log.info("MC exist: {}", streamToString);
        } else {
            assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(HTTP_404_CODE);
            log.info("MC not exist");
        }
    }

    @Test
    @DisplayName("3.0.2.(QUERY_CONSUMERS) Test for user exists/or not within CRM")
    public void existsUser() throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(environmentList.get(TEST_ENVIRONMENT).getApiSite() + QUERY_CONSUMERS + userList.get(TEST_ENVIRONMENT_USER).getPhone());
        request.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        request.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        request.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        var statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == HTTP_200_CODE) {
            var streamToString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            log.info("Consumer: {}", streamToString);
            Consumer value = mapper.readValue(streamToString, Consumer.class);
            log.info("Consumer exist, his id= {}", value.getConsumerID());
            assertThat(value.getConsumerID()).isNotZero();
        } else {
            assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(HTTP_404_CODE);
            log.info("Consumer not exist");
        }
    }

    @Test
    @DisplayName("3.1.(CREATE_CONSUMER) Test for create user")
    public void createConsumer() throws IOException {

        // Given
        HttpPost request = new HttpPost(environmentList.get(TEST_ENVIRONMENT).getApiSite() + CREATE_CONSUMER);
        request.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        request.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        request.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));
        Request createConsumerRequest = new Request(userList.get(TEST_ENVIRONMENT_USER).getPhone(), userList.get(TEST_ENVIRONMENT_USER).getPhone());
        String xml = mapper.writeValueAsString(createConsumerRequest);
        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
        request.setEntity(entity);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        log.info("httpResponse: {}", httpResponse);
        // Then
        var statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == HTTP_200_CODE) {
            var streamToString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            log.info("Consumer: {}", streamToString);
            Consumer value = mapper.readValue(streamToString, Consumer.class);
            log.info("Consumer exist, his id= {}", value.getConsumerID());
        } else {
            assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(HTTP_404_CODE);
            log.info("Consumer not exist");
        }
    }

    private String makeAuthHeader(int partnerId) {
        IntegrationPartner partner = integrationPartnerList.get(partnerId);
        String loginPlusPassword = partner.getLogin() + AUTH_SEPARATOR + partner.getPassword();
        String AUTHORIZATION_HEADER_VALUE = AUTH_TYPE + Base64.getEncoder().encodeToString(loginPlusPassword.getBytes());
        return AUTHORIZATION_HEADER_VALUE;
    }


    @Test
    void anyTest1() {
        System.out.print("@Test: anyTest1. ");

    }


    @Test
    void anyTest2() {
        System.out.print("@Test: anyTest2. ");

    }


    @AfterEach
    public void tearDown() {
        System.out.print("@AfterEach. ");

    }


    @AfterAll
    public static void globalTearDown() {
        System.out.println("\n@AfterAll");
    }
}
