package mediscreen.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mediscreen.model.Patient;
import mediscreen.proxy.MicroserviceNoteProxy;
import mediscreen.proxy.MicroservicePatientProxy;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private MicroservicePatientProxy proxy;

	@Autowired
	private MicroserviceNoteProxy noteProxy;

	@Override
	public String generateDiabetesReport(int patId) {
		int occurrences = noteProxy.getOccurrences(patId);

		Patient patient = proxy.getPatient(patId);
		int age = getPatientAge(patient);

		if (age >= 30) {
			if (occurrences >= 2 && occurrences < 6)
				return "Bordeline";
			if (occurrences >= 6 && occurrences < 8)
				return "In Danger";
			if (occurrences >= 8)
				return "Early onset";
		}

		if (age < 30) {
			if ("F".equals(patient.getSex())) {
				if (occurrences == 4) {
					return "In Danger";
				} else if (occurrences == 7) {
					return "Early onset";
				}
			} else if ("M".equals(patient.getSex())) {
				if (occurrences == 3) {
					return "In Danger";
				} else if (occurrences == 5) {
					return "Early onset";
				}
			}
		}
		return "None";
	}

	private int getPatientAge(Patient patient) {
		Date birthdate = patient.getDob();

		LocalDate localDate = LocalDate.now();
		LocalDate birthLocalDate = birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		long differenceBetween = ChronoUnit.YEARS.between(birthLocalDate, localDate);
		int age = Math.toIntExact(differenceBetween);
		return age;
	}
}
