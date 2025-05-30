package com.pms.pmsmodule.kafka;

import com.pms.pmsmodule.model.Patient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;




@Service
@AllArgsConstructor
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(Patient patient){
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try{
            kafkaTemplate.send("patient", patientEvent.toByteArray());
            log.info("Event PATIENT_CREATED sent successfully");
        } catch (Exception exception){
            log.error("",exception);
            log.error("Error sending PATIENT_CREATED event: {}", patientEvent);
        }

    }
}
