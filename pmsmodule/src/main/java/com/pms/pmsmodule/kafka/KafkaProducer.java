package com.pms.pmsmodule.kafka;

import com.pms.pmsmodule.model.Patient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

/**
 * KafkaProducer is a Spring-managed service responsible for publishing patient-related events
 * to the Kafka messaging system.
 *
 * <p>This class produces messages of type {@link PatientEvent} to the Kafka topic <strong>"patient"</strong>.
 * The messages are serialized to byte arrays using the protocol buffer serialization format.</p>
 *
 * <p>Follows the event-driven architecture pattern to decouple services and ensure scalability and resilience.</p>
 *
 * <p><strong>Best Practices Implemented:</strong></p>
 * <ul>
 *   <li>Uses SLF4J for logging</li>
 *   <li>Handles exceptions gracefully with meaningful logs</li>
 *   <li>Leverages KafkaTemplate for Kafka interactions</li>
 *   <li>Uses Lombok's @AllArgsConstructor to reduce boilerplate</li>
 *   <li>Encapsulates Kafka logic in a dedicated service layer</li>
 * </ul>
 *
 * @author DenisKinyua
 * @since 1.0
 */
@Service
@AllArgsConstructor
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
            log.debug("Failed event payload: {}", patientEvent);
        }
    }
}
