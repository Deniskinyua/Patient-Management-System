package com.pms.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

/**
 * KafkaConsumer is a Spring service responsible for consuming patient events from a Kafka topic.
 * <p>
 * This consumer listens to the {@code patient} topic and processes incoming messages serialized as Protocol Buffers.
 * Upon successful deserialization, it logs key patient information and can be extended to perform analytics logic.
 * </p>
 *
 * <p><b>Springdoc OpenAPI:</b> This class is a backend service and does not expose REST endpoints,
 * so it is not directly documented in the OpenAPI specification. However, its purpose and usage are documented here for maintainability.</p>
 *
 * <p><b>Best Practices:</b></p>
 * <ul>
 *   <li>Uses SLF4J for logging.</li>
 *   <li>Handles deserialization errors gracefully.</li>
 *   <li>Designed for extension with analytics logic.</li>
 * </ul>
 *
 * @author Denis Kinyua
 * @since 1.0
 */
@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    /**
     * Consumes patient events from the Kafka {@code patient} topic.
     * <p>
     * The method expects messages as byte arrays, deserializes them into {@link PatientEvent} objects,
     * and logs relevant patient information. If deserialization fails, an error is logged.
     * </p>
     *
     * @param event the serialized {@link PatientEvent} message received from Kafka
     */
    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumePatientEvent(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            log.info("Received Patient Event: [PatientId={}, PatientName={}, PatientEmail={}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail());
            // Perform analytics logic here...

        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event {}", e.getMessage());
        }
    }
}