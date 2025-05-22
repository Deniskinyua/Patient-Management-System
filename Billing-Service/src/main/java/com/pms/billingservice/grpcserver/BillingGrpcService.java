package com.pms.billingservice.grpcserver;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * gRPC service implementation for billing operations.
 *
 * <p>This class handles gRPC requests related to billing accounts. It extends
 * {@link BillingServiceImplBase}, which provides the base implementation for the
 * BillingService defined in the protobuf contract.</p>
 *
 * <p>Exposed via {@code @GrpcService}, this service is automatically discovered and
 * registered with the embedded gRPC server in the Spring Boot application.</p>
 *
 * <p>SLF4J logging is used for traceability and debugging purposes.</p>
 *
 * @author Denis Kinyua
 * @version 1.0
 * @since 2025-05-22
 */
@Slf4j
@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    /**
     * Handles the gRPC request to create a new billing account.
     *
     * <p>This method receives a {@link BillingRequest} containing the account creation
     * details and returns a {@link BillingResponse} with a fixed account ID and status.
     * In a real-world implementation, this would typically include validation, business logic,
     * and persistence to a database.</p>
     *
     * @param billingRequest      the request message containing billing account details
     * @param responseObserver    the stream observer used to send back the {@link BillingResponse}
     */
    @Override
    public void createBillingAccount(BillingRequest billingRequest,
                                     StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAccount request received: {}", billingRequest);

        // TODO: Implement business logic here (e.g., input validation, DB persistence, calculations)

        // Construct the response with static data for demonstration purposes
        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("ACTIVE")
                .build();

        // Send the response to the client
        responseObserver.onNext(billingResponse);
        responseObserver.onCompleted();
    }
}
