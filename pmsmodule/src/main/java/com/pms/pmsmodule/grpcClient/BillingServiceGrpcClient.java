package com.pms.pmsmodule.grpcClient;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * {@code BillingServiceGrpcClient} is a Spring-managed service that acts as a gRPC client
 * for communicating with the remote Billing microservice.
 *
 * <p>
 * <b>Usage:</b>
 * <ul>
 *   <li>Establishes a gRPC channel to the Billing service using configurable host and port.</li>
 *   <li>Provides a method to create billing accounts by invoking the remote gRPC endpoint.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Configuration:</b>
 * <ul>
 *   <li>{@code billing.service.address} - The hostname or IP address of the Billing gRPC server (default: localhost).</li>
 *   <li>{@code billing.service.grpc.port} - The port number of the Billing gRPC server (default: 9001).</li>
 * </ul>
 * These can be set via <code>application.properties</code> or environment variables for containerized deployments.
 * </p>
 *
 * <p>
 * <b>Best Practices:</b>
 * <ul>
 *   <li>Use dependency injection for configuration to support flexible deployments (local, cloud, Docker Compose).</li>
 *   <li>Log all remote calls for traceability and debugging.</li>
 *   <li>Encapsulate gRPC logic in a dedicated service for maintainability and testability.</li>
 * </ul>
 * </p>
 *
 * @author Denis Kinyua
 * @since 1.0
 */
@Service
public class BillingServiceGrpcClient {

    /** Logger for structured logging of gRPC client operations. */
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);

    /** Blocking stub for synchronous communication with the Billing gRPC service. */
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    /**
     * Constructs a {@code BillingServiceGrpcClient} and establishes a gRPC channel to the Billing service.
     *
     * @param serverAdress the hostname or IP address of the Billing gRPC server (injected from configuration)
     * @param serverPort   the port number of the Billing gRPC server (injected from configuration)
     */
    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAdress,
            @Value("${billing.service.grpc.port:9001}") int serverPort) {
        log.info("Connecting to Billing gRPC Service at {}:{}", serverAdress, serverPort);

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(serverAdress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = BillingServiceGrpc.newBlockingStub(managedChannel);
    }

    /**
     * Calls the remote Billing gRPC service to create a billing account for a patient.
     *
     * @param patientId the unique identifier of the patient
     * @param name      the patient's full name
     * @param email     the patient's email address
     * @return {@link BillingResponse} containing the account ID and status from the Billing service
     *
     * <p><b>OpenAPI/SpringDoc Note:</b> This method is invoked internally by the application and is not exposed as a REST endpoint.
     * For REST API documentation, see the corresponding controller classes.</p>
     */
    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        BillingRequest billingRequest = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();

        BillingResponse billingResponse = blockingStub.createBillingAccount(billingRequest);
        log.info("Received response from Billing Service via gRPC: {}", billingResponse);

        return billingResponse;
    }
}