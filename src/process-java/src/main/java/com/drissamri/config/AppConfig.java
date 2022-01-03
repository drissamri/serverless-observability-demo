package com.drissamri.config;

import com.drissamri.model.Order;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.http.crt.AwsCrtAsyncHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

public class AppConfig {
    public static DynamoDbAsyncTable orderTable() {
        DynamoDbAsyncClient dynamoDbClient = DynamoDbAsyncClient.builder()
                .httpClient(AwsCrtAsyncHttpClient.builder().build())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        DynamoDbEnhancedAsyncClient client = DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        return client.table(
                System.getenv("TABLE"),
                TableSchema.fromBean(Order.class));
    }
}
