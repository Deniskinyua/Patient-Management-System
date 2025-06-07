package com.pms.pmsmodule.kafka;

import com.pms.pmsmodule.model.Patient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;


@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    /**
     * KafkaTemplate for sending messages with a String key and a byte array value.
     */
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    /**
     * Publishes a {@link PatientEvent} to the Kafka topic <strong>"patient"</strong> upon patient creation.
     *
     * <p>The event includes the patient's ID, name, email, and a hardcoded event type "PATIENT_CREATED".</p>
     *
     * @param patient the {@link Patient} entity containing information used to build the event
     */
    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendEvent(Patient patient) {
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try {
            kafkaTemplate.send("patient", patientEvent.toByteArray());
            log.info("Event PATIENT_CREATED sent successfully for patientId={}", patient.getId());
        } catch (Exception exception) {
            log.error("Error sending PATIENT_CREATED event for patientId={}", patient.getId(), exception);
        }
    }
}
