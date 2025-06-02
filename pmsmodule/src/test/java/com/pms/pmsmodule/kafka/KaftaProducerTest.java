package com.pms.pmsmodule.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pms.pmsmodule.model.Patient;
import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;
import patient.events.PatientEvent;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link KafkaProducer}.
 */
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEvent_shouldSendMessageSuccessfully() throws InvalidProtocolBufferException {
        // Arrange
        UUID patientId = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setName("John Doe");
        patient.setEmail("john.doe@example.com");

        // Act
        kafkaProducer.sendEvent(patient);

        // Assert
        ArgumentCaptor<byte[]> messageCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(kafkaTemplate, times(1)).send(eq("patient"), messageCaptor.capture());

        // Decode the message back to verify
        byte[] sentBytes = messageCaptor.getValue();
        PatientEvent sentEvent = PatientEvent.parseFrom(sentBytes);

        assertEquals("123", sentEvent.getPatientId());
        assertEquals("John Doe", sentEvent.getName());
        assertEquals("john.doe@example.com", sentEvent.getEmail());
        assertEquals("PATIENT_CREATED", sentEvent.getEventType());
    }

    @Test
    void sendEvent_shouldLogErrorIfKafkaFails() {
        // Arrange
        UUID patientId = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setName("Jane Doe");
        patient.setEmail("jane.doe@example.com");

        doThrow(new KafkaException("Kafka send failed"))
                .when(kafkaTemplate).send(anyString(), any(byte[].class));

        // Act & Assert
        assertDoesNotThrow(() -> kafkaProducer.sendEvent(patient));
        verify(kafkaTemplate, times(1)).send(eq("patient"), any(byte[].class));
    }
}
