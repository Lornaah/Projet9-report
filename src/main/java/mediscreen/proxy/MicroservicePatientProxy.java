package mediscreen.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mediscreen.model.Patient;

@FeignClient(name = "microservice-patients", url = "${PATIENT_HOST:localhost}:8080")
public interface MicroservicePatientProxy {

	@GetMapping(value = "/patient/get")
	Patient getPatient(@RequestParam int id);
}
