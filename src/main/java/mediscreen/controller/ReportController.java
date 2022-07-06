package mediscreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mediscreen.service.ReportService;

@RestController
public class ReportController {

	@Autowired
	private ReportService reportService;

	@CrossOrigin
	@GetMapping("/assess")
	public String generateReport(@RequestParam int patId) {
		return reportService.generateDiabetesReport(patId);
	}

}
