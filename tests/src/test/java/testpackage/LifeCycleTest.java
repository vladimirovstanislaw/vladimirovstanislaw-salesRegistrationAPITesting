package testpackage;

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
import ru.ikon.DTO.CreateServiceEntry.Response;
import ru.ikon.DTO.CreateServiceEntry.ServiceEntry;
import ru.ikon.DTO.CreateTyreSets.TyreSet;
import ru.ikon.DTO.QueryConsumers.Consumer;
import ru.ikon.entities.Environment;
import ru.ikon.entities.IntegrationPartner;
import ru.ikon.entities.User;
import ru.ikon.metaData.RequestMetaData;
import ru.ikon.metaData.RequestSetMetaData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class LifeCycleTest {
    /*
    Чтобы иметь возможность запустить данные тесты:
    1. Убедиться, что инициализированы переменные "environmentList" и "integrationPartnerList". Эти данные можно найти в Confluence.
    2. Убедиться, что для шины TYRE_ART_AUTOGRAPH_SNOW_C3 есть МК

     */
    private static final Logger log = LoggerFactory.getLogger(LifeCycleTest.class);

    private final String POSCODE_HEADER_NAME = "PosCode";
    private final String CONTENTTYPE_HEADER_NAME = "Content-type";
    private final String CONTENTTYPE_HEADER_VALUE = "application/xml; charset=UTF-8";

    private final String AUTHORIZATION_HEADER_NAME = "Authorization";

    public final String AUTH_SEPARATOR = ":";

    private final String AUTH_TYPE = "Basic ";
    private final List<User> userList = List.of(new User("79062716258"), new User("79218656914"));
    //salesRegistrationAPITesting
    private final String TYRE_ART_AUTOGRAPH_SNOW_C3 = "T729108";
    private final int MAX_TYRE_COUNT = 5;
    private final int TEST_ENVIRONMENT = 0;
    private final int TEST_ENVIRONMENT_PARTNER = 0;
    private final int TEST_ENVIRONMENT_USER = 0;
    //p.3.0.1
    private final String QUERY_CAMPAIGNS_URL = "/QueryCampaigns/v1/xml/?itemCode=";
    private final String QUERY_CAMPAIGNS = "QUERY_CAMPAIGNS";

    private final String TYPE_GUARANTEE = "&type=Guarantee";
    private final String QUERY_CONSUMERS_URL = "/QueryConsumers/v1/xml?phone=";
    private final String QUERY_CONSUMERS = "QUERY_CONSUMERS";
    private final String CREATE_CONSUMER_URL = "/CreateConsumer/v1/xml";
    private final String CREATE_CONSUMER = "CREATE_CONSUMER";
    private final String CREATE_TYRE_SETS_URL = "/CreateTyreSets/v1/xml";
    private final String CREATE_TYRE_SETS = "CREATE_TYRE_SETS";
    private final String CREATE_SERVICE_ENTRY_URL = "/CreateServiceEntry/v1/xml";
    private final String CREATE_SERVICE_ENTRY = "CREATE_SERVICE_ENTRY";

    private final int HTTP_200_CODE = 200;
    private final int HTTP_404_CODE = 404;
    private final String GUARANTEE = "Guarantee", RESERVED = "RESERVED";

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
    @DisplayName("3.0.1.(" + QUERY_CAMPAIGNS + ") Test for tyre exists in marketing campaigns")
    public void tyreExistsInMarketingCampaigns()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(environmentList.get(TEST_ENVIRONMENT).getApiSite() + QUERY_CAMPAIGNS_URL + TYRE_ART_AUTOGRAPH_SNOW_C3 + TYPE_GUARANTEE);
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
    @DisplayName("3.0.2.(" + QUERY_CONSUMERS + ") Test for user exists/or not within CRM")
    public void existsUser() throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(environmentList.get(TEST_ENVIRONMENT).getApiSite() + QUERY_CONSUMERS_URL + userList.get(TEST_ENVIRONMENT_USER).getPhone());
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
    @DisplayName("3.1.(" + CREATE_CONSUMER + ") Test for create user")
    public void createConsumer() throws IOException {

        // Given
        HttpPost request = new HttpPost(environmentList.get(TEST_ENVIRONMENT).getApiSite() + CREATE_CONSUMER_URL);
        request.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        request.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        request.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));
        User user = userList.get(TEST_ENVIRONMENT_USER);
        Request createConsumerRequest = new Request(user.getPhone(), user.getPhone());
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

    @Test
    @DisplayName("3.2.(" + CREATE_TYRE_SETS + ")(+3.1) HappyPath Test for create TyreSet")
    public void createTyreSet() throws IOException {

        // Достаем User-а
        HttpPost request = new HttpPost(environmentList.get(TEST_ENVIRONMENT).getApiSite() + CREATE_CONSUMER_URL);
        request.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        request.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        request.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));
        User user = userList.get(TEST_ENVIRONMENT_USER);
        Request createConsumerRequest = new Request(user.getPhone(), user.getPhone());
        String xml = mapper.writeValueAsString(createConsumerRequest);
        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);


        var statusCode = httpResponse.getStatusLine().getStatusCode();
        assertThat(statusCode).isEqualTo(HTTP_200_CODE);
        var streamToString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        log.info("Consumer: {}", streamToString);
        Consumer consumer = mapper.readValue(streamToString, Consumer.class);
        log.info("Consumer exist, his id= {}", consumer.getConsumerID());
        //Создаем TyreSet
        ru.ikon.DTO.CreateTyreSets.Request requestCreateTyreSet = new ru.ikon.DTO.CreateTyreSets.Request(consumer.getConsumerID(), TYRE_ART_AUTOGRAPH_SNOW_C3, MAX_TYRE_COUNT, makeTodayDate());
        log.info("requestCreateTyreSet: {}", requestCreateTyreSet);


        HttpPost createTyreSet = new HttpPost(environmentList.get(TEST_ENVIRONMENT).getApiSite() + CREATE_TYRE_SETS_URL);
        createTyreSet.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        createTyreSet.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        createTyreSet.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));

        String xmlCreateTyreSet = mapper.writeValueAsString(requestCreateTyreSet);
        HttpEntity entityCreateTyreSet = new ByteArrayEntity(xmlCreateTyreSet.getBytes("UTF-8"));
        createTyreSet.setEntity(entityCreateTyreSet);

        HttpResponse httpResponseCreateTyreSet = HttpClientBuilder.create().build().execute(createTyreSet);
        log.info("httpResponseCreateTyreSet: {}", httpResponseCreateTyreSet);

        var statusCodeCreateTyreSet = httpResponseCreateTyreSet.getStatusLine().getStatusCode();
        assertThat(statusCodeCreateTyreSet).isEqualTo(HTTP_200_CODE);
        var streamToStringCreateTyreSet = EntityUtils.toString(httpResponseCreateTyreSet.getEntity(), "UTF-8");
        log.info("TyreSet: {}", streamToStringCreateTyreSet);
        TyreSet tyreSet = mapper.readValue(streamToStringCreateTyreSet, TyreSet.class);
        log.info("TyreSet exist, his id= {}", tyreSet.getTyreSetID());
    }

    @Test
    @DisplayName("Case:3 | 3.3.+3.2.+3.1.+3.0.2.+3.0.1. HappyPath Test for create ServiceEntry")
    public void createServiceEntry() throws IOException {
        RequestSetMetaData requestSetMetaData = new RequestSetMetaData();
        // 3.0.1. Запрос на проверку участия шины в кампании
        RequestMetaData metaDataTyreWithinMC = new RequestMetaData();
        metaDataTyreWithinMC.setRequestType(QUERY_CAMPAIGNS);
        metaDataTyreWithinMC.setStartRequestLaunch(new Date().getTime());
        HttpUriRequest requestForTyreWithinMC = new HttpGet(environmentList.get(TEST_ENVIRONMENT).getApiSite() + QUERY_CAMPAIGNS_URL + TYRE_ART_AUTOGRAPH_SNOW_C3 + TYPE_GUARANTEE);
        requestForTyreWithinMC.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        requestForTyreWithinMC.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        requestForTyreWithinMC.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));

        HttpResponse httpResponseForTyreWithinMC = HttpClientBuilder.create().build().execute(requestForTyreWithinMC);

        var statusCodeForTyreWithinMC = httpResponseForTyreWithinMC.getStatusLine().getStatusCode();
        if (statusCodeForTyreWithinMC == HTTP_200_CODE) {
            var streamToString = EntityUtils.toString(httpResponseForTyreWithinMC.getEntity(), "UTF-8");
            log.info("MC exist: {}", streamToString);
        } else {
            assertThat(statusCodeForTyreWithinMC).isEqualTo(HTTP_404_CODE);
            log.info("MC not exist");
        }
        metaDataTyreWithinMC.setEndRequestLaunch(new Date().getTime());
        requestSetMetaData.getList().add(metaDataTyreWithinMC);
        //3.0.2. Запрос на проверку существования покупателя
        HttpUriRequest requestForUserExistsWithinCRM = new HttpGet(environmentList.get(TEST_ENVIRONMENT).getApiSite() + QUERY_CONSUMERS_URL + userList.get(TEST_ENVIRONMENT_USER).getPhone());
        requestForUserExistsWithinCRM.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        requestForUserExistsWithinCRM.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        requestForUserExistsWithinCRM.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));

        HttpResponse httpResponseForUserExistsWithinCRM = HttpClientBuilder.create().build().execute(requestForUserExistsWithinCRM);

        var statusCodeForUserExistsWithinCRM = httpResponseForUserExistsWithinCRM.getStatusLine().getStatusCode();
        if (statusCodeForUserExistsWithinCRM == HTTP_200_CODE) {
            var streamToString = EntityUtils.toString(httpResponseForUserExistsWithinCRM.getEntity(), "UTF-8");
            log.info("Consumer: {}", streamToString);
            Consumer value = mapper.readValue(streamToString, Consumer.class);
            log.info("Consumer exist, his id= {}", value.getConsumerID());
            assertThat(value.getConsumerID()).isNotZero();
        } else {
            assertThat(statusCodeForUserExistsWithinCRM).isEqualTo(HTTP_404_CODE);
            log.info("Consumer not exist");
        }

        // 3.1. Запрос на создание покупателя
        HttpPost request = new HttpPost(environmentList.get(TEST_ENVIRONMENT).getApiSite() + CREATE_CONSUMER_URL);
        request.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        request.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        request.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));
        User user = userList.get(TEST_ENVIRONMENT_USER);
        Request createConsumerRequest = new Request(user.getPhone(), user.getPhone());
        String xml = mapper.writeValueAsString(createConsumerRequest);
        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        var statusCode = httpResponse.getStatusLine().getStatusCode();
        assertThat(statusCode).isEqualTo(HTTP_200_CODE);
        var streamToString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        log.info("Consumer: {}", streamToString);
        Consumer consumer = mapper.readValue(streamToString, Consumer.class);
        log.info("Consumer exist, his id= {}", consumer.getConsumerID());

        //3.2. Запрос на создание набора шин
        ru.ikon.DTO.CreateTyreSets.Request requestCreateTyreSet = new ru.ikon.DTO.CreateTyreSets.Request(consumer.getConsumerID(), TYRE_ART_AUTOGRAPH_SNOW_C3, MAX_TYRE_COUNT, makeTodayDate());
        log.info("requestCreateTyreSet: {}", requestCreateTyreSet);


        HttpPost createTyreSet = new HttpPost(environmentList.get(TEST_ENVIRONMENT).getApiSite() + CREATE_TYRE_SETS_URL);
        createTyreSet.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        createTyreSet.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        createTyreSet.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));

        String xmlCreateTyreSet = mapper.writeValueAsString(requestCreateTyreSet);
        HttpEntity entityCreateTyreSet = new ByteArrayEntity(xmlCreateTyreSet.getBytes("UTF-8"));
        createTyreSet.setEntity(entityCreateTyreSet);

        HttpResponse httpResponseCreateTyreSet = HttpClientBuilder.create().build().execute(createTyreSet);
        log.info("httpResponseCreateTyreSet: {}", httpResponseCreateTyreSet);

        var statusCodeCreateTyreSet = httpResponseCreateTyreSet.getStatusLine().getStatusCode();
        assertThat(statusCodeCreateTyreSet).isEqualTo(HTTP_200_CODE);
        var streamToStringCreateTyreSet = EntityUtils.toString(httpResponseCreateTyreSet.getEntity(), "UTF-8");
        log.info("TyreSet: {}", streamToStringCreateTyreSet);
        TyreSet tyreSet = mapper.readValue(streamToStringCreateTyreSet, TyreSet.class);
        log.info("TyreSet created, his id= {}", tyreSet.getTyreSetID());

        //3.3. Запрос на создание электронного талона
        ru.ikon.DTO.CreateServiceEntry.Request requestCreateServiceEntry = new ru.ikon.DTO.CreateServiceEntry.Request(consumer.getConsumerID(), tyreSet.getTyreSetID(), GUARANTEE, RESERVED);
        log.info("requestCreateServiceEntry: {}", requestCreateServiceEntry);


        HttpPost createServiceEntry = new HttpPost(environmentList.get(TEST_ENVIRONMENT).getApiSite() + CREATE_SERVICE_ENTRY_URL);
        createServiceEntry.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        createServiceEntry.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        createServiceEntry.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));

        String xmlCreateServiceEntry = mapper.writeValueAsString(requestCreateServiceEntry);
        HttpEntity entityCreateServiceEntry = new ByteArrayEntity(xmlCreateServiceEntry.getBytes("UTF-8"));
        createServiceEntry.setEntity(entityCreateServiceEntry);

        HttpResponse httpResponseCreateServiceEntry = HttpClientBuilder.create().build().execute(createServiceEntry);
        log.info("entityCreateServiceEntry: {}", entityCreateServiceEntry);

        var statusCodeCreateServiceEntry = httpResponseCreateServiceEntry.getStatusLine().getStatusCode();
        assertThat(statusCodeCreateServiceEntry).isEqualTo(HTTP_200_CODE);
        var streamToStringCreateServiceEntry = EntityUtils.toString(httpResponseCreateServiceEntry.getEntity(), "UTF-8");
        log.info("Response: {}", streamToStringCreateServiceEntry);

        Response response = mapper.readValue(streamToStringCreateServiceEntry, Response.class);
        log.info("Response deserialized: {}", response);
        ServiceEntry serviceEntry = response.getServiceEntry();
        log.info("ServiceEntry deserialized: {}", serviceEntry);
    }

    @Test
    @DisplayName("3.3.(" + CREATE_SERVICE_ENTRY + ")(+3.2.)(+3.1) HappyPath Test for create ServiceEntry")
    public void case3() throws IOException {

        // Достаем User-а
        HttpPost request = new HttpPost(environmentList.get(TEST_ENVIRONMENT).getApiSite() + CREATE_CONSUMER_URL);
        request.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        request.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        request.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));
        User user = userList.get(TEST_ENVIRONMENT_USER);
        Request createConsumerRequest = new Request(user.getPhone(), user.getPhone());
        String xml = mapper.writeValueAsString(createConsumerRequest);
        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);


        var statusCode = httpResponse.getStatusLine().getStatusCode();
        assertThat(statusCode).isEqualTo(HTTP_200_CODE);
        var streamToString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        log.info("Consumer: {}", streamToString);
        Consumer consumer = mapper.readValue(streamToString, Consumer.class);
        log.info("Consumer exist, his id= {}", consumer.getConsumerID());
        //Создаем TyreSet
        ru.ikon.DTO.CreateTyreSets.Request requestCreateTyreSet = new ru.ikon.DTO.CreateTyreSets.Request(consumer.getConsumerID(), TYRE_ART_AUTOGRAPH_SNOW_C3, MAX_TYRE_COUNT, makeTodayDate());
        log.info("requestCreateTyreSet: {}", requestCreateTyreSet);


        HttpPost createTyreSet = new HttpPost(environmentList.get(TEST_ENVIRONMENT).getApiSite() + CREATE_TYRE_SETS_URL);
        createTyreSet.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        createTyreSet.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        createTyreSet.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));

        String xmlCreateTyreSet = mapper.writeValueAsString(requestCreateTyreSet);
        HttpEntity entityCreateTyreSet = new ByteArrayEntity(xmlCreateTyreSet.getBytes("UTF-8"));
        createTyreSet.setEntity(entityCreateTyreSet);

        HttpResponse httpResponseCreateTyreSet = HttpClientBuilder.create().build().execute(createTyreSet);
        log.info("httpResponseCreateTyreSet: {}", httpResponseCreateTyreSet);

        var statusCodeCreateTyreSet = httpResponseCreateTyreSet.getStatusLine().getStatusCode();
        assertThat(statusCodeCreateTyreSet).isEqualTo(HTTP_200_CODE);
        var streamToStringCreateTyreSet = EntityUtils.toString(httpResponseCreateTyreSet.getEntity(), "UTF-8");
        log.info("TyreSet: {}", streamToStringCreateTyreSet);
        TyreSet tyreSet = mapper.readValue(streamToStringCreateTyreSet, TyreSet.class);
        log.info("TyreSet created, his id= {}", tyreSet.getTyreSetID());
        //Создаем ServiceEntry

        ru.ikon.DTO.CreateServiceEntry.Request requestCreateServiceEntry = new ru.ikon.DTO.CreateServiceEntry.Request(consumer.getConsumerID(), tyreSet.getTyreSetID(), GUARANTEE, RESERVED);
        log.info("requestCreateServiceEntry: {}", requestCreateServiceEntry);


        HttpPost createServiceEntry = new HttpPost(environmentList.get(TEST_ENVIRONMENT).getApiSite() + CREATE_SERVICE_ENTRY_URL);
        createServiceEntry.setHeader(POSCODE_HEADER_NAME, integrationPartnerList.get(TEST_ENVIRONMENT).getPosCode());
        createServiceEntry.setHeader(CONTENTTYPE_HEADER_NAME, CONTENTTYPE_HEADER_VALUE);
        createServiceEntry.setHeader(AUTHORIZATION_HEADER_NAME, makeAuthHeader(TEST_ENVIRONMENT_PARTNER));

        String xmlCreateServiceEntry = mapper.writeValueAsString(requestCreateServiceEntry);
        HttpEntity entityCreateServiceEntry = new ByteArrayEntity(xmlCreateServiceEntry.getBytes("UTF-8"));
        createServiceEntry.setEntity(entityCreateServiceEntry);

        HttpResponse httpResponseCreateServiceEntry = HttpClientBuilder.create().build().execute(createServiceEntry);
        log.info("entityCreateServiceEntry: {}", entityCreateServiceEntry);

        var statusCodeCreateServiceEntry = httpResponseCreateServiceEntry.getStatusLine().getStatusCode();
        assertThat(statusCodeCreateServiceEntry).isEqualTo(HTTP_200_CODE);
        var streamToStringCreateServiceEntry = EntityUtils.toString(httpResponseCreateServiceEntry.getEntity(), "UTF-8");
        log.info("Response: {}", streamToStringCreateServiceEntry);

        Response response = mapper.readValue(streamToStringCreateServiceEntry, Response.class);
        log.info("Response deserialized: {}", response);
        ServiceEntry serviceEntry = response.getServiceEntry();
        log.info("ServiceEntry deserialized: {}", serviceEntry);
    }

    public String makeTodayDate() {
        Date today;
        String output;
        SimpleDateFormat formatter;
        String pattern = "yyyy-MM-dd";
        formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        today = new Date();
        output = formatter.format(today);
        System.out.println(pattern + " " + output);
        return output;
    }

    public String makeDatePlus2Days() {
        String output;
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate date = LocalDate.now().plusDays(2);
        output = date.format(formatter);
        return output;
    }

    public String makeDateMinus8Days() {
        String output;
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate date = LocalDate.now().minusDays(8);
        output = date.format(formatter);
        return output;
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
