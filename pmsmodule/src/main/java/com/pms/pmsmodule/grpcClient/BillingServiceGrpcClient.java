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

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    //lcalhost:9001/BillingService/CreatePatientAccount --> on load
    //aws.grpc:12345/BillingService/CreatePatientAccount
    public BillingServiceGrpcClient(@Value("${billing.service.address:localhost}") String serverAdress,
                                    @Value("${billing.service.grpc.port:9001}") int serverPort) {
        log.info("Connecting to Billing gRPC Service at {}:{}", serverAdress, serverPort);

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(serverAdress,serverPort)
                .usePlaintext().build();

        blockingStub = BillingServiceGrpc.newBlockingStub(managedChannel);
    }

    //
    public BillingResponse createBillingAccount(String patientId, String name, String email){
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
