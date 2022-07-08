package mediscreen.report;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

import mediscreen.model.Patient;
import mediscreen.proxy.MicroservicePatientProxy;
import mediscreen.service.ReportService;

@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties
public class ControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@RegisterExtension
	static WireMockExtension noteServer = WireMockExtension.newInstance().options(wireMockConfig().port(8081)).build();
	@RegisterExtension
	static WireMockExtension patientServer = WireMockExtension.newInstance().options(wireMockConfig().port(8080))
			.build();

	@Autowired
	MicroservicePatientProxy proxy;

	@Autowired
	ReportService service;

	@BeforeEach
	private void stub() throws JsonProcessingException {
		noteServer.stubFor(WireMock.get(WireMock.urlMatching("/getOccurrencesByPatId\\?patId=([0-9]+)"))
				.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).withBody(String.valueOf(0))));

		Patient patient = new Patient(0, "family", "given", new Date(), "F", "address", "phone");

		patientServer.stubFor(WireMock.get(WireMock.urlMatching("/patient/get\\?id=([0-9]+)"))
				.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody(objectMapper.writeValueAsString(patient))));
	}

	@Test
	public void generateReportTest_WillReturnNone() throws Exception {
		MvcResult result = mockMvc.perform(get("/assess").param("patId", "0")).andExpect(status().isOk()).andReturn();

		String report = result.getResponse().getContentAsString();

		assertTrue("None".equals(report));
	}

	@Test
	public void generateReportTest_WillReturnInDanger() throws Exception {
		editNoteStub(6);
		editPatientStub(31, "F");
		MvcResult result = mockMvc.perform(get("/assess").param("patId", "0")).andExpect(status().isOk()).andReturn();

		String report = result.getResponse().getContentAsString();

		assertTrue("In Danger".equals(report));
	}

	@Test
	public void generateReportTest_WillReturnBorderline() throws Exception {
		editNoteStub(3);
		editPatientStub(32, "M");
		MvcResult result = mockMvc.perform(get("/assess").param("patId", "0")).andExpect(status().isOk()).andReturn();

		String report = result.getResponse().getContentAsString();

		assertTrue("Borderline".equals(report));
	}

	@Test
	public void generateReportTest_WillReturnEarlyOnset() throws Exception {
		editNoteStub(8);
		editPatientStub(32, "M");
		MvcResult result = mockMvc.perform(get("/assess").param("patId", "0")).andExpect(status().isOk()).andReturn();

		String report = result.getResponse().getContentAsString();

		assertTrue("Early onset".equals(report));
	}

	private void editNoteStub(int occurences) {
		noteServer.stubFor(WireMock.get(WireMock.urlMatching("/getOccurrencesByPatId\\?patId=([0-9]+)"))
				.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody(String.valueOf(occurences))));
	}

	private void editPatientStub(int age, String gender) throws JsonProcessingException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -age);
		Date patientAge = cal.getTime();

		Patient patient = new Patient(0, "familiy", "given", patientAge, gender, "address", "phone");

		patientServer.stubFor(WireMock.get(WireMock.urlMatching("/patient/get\\?id=([0-9]+)"))
				.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody(objectMapper.writeValueAsString(patient))));
	}

}
