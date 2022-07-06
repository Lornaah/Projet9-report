package mediscreen.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservice-notes", url = "${NOTE_HOST:localhost}:8081")
public interface MicroserviceNoteProxy {

	@GetMapping(value = "/getOccurrencesByPatId")
	int getOccurrences(@RequestParam int patId);
}
