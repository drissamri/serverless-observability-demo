package com.drissamri;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.drissamri.model.Order;
import io.lumigo.handlers.LumigoConfiguration;
import io.lumigo.handlers.LumigoRequestExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static com.drissamri.config.AppConfig.*;

public class AddOrderLambda implements RequestHandler<SQSEvent, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(AddOrderLambda.class);
    private DynamoDbAsyncTable<Order> orderTable;

    static{
        LumigoConfiguration.builder().token("t_b100cf2f17aa4f74878f7").build().init();
    }

    public AddOrderLambda() {
        this(orderTable());
    }

    public AddOrderLambda(DynamoDbAsyncTable<Order> orderTable) {
        this.orderTable = orderTable;
    }

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        Supplier<Void> supplier = () -> handle(event);
        return LumigoRequestExecutor.execute(event, context, supplier);
    }

    public Void handle(SQSEvent input) {
        LOG.info("Message received: {}", input);
        input.getRecords().forEach(msg -> {
            Order savedOrder = null;
            try {
                savedOrder = add(msg.getBody());
                LOG.info("Order created: {}", savedOrder);
            } catch (Exception e) {
                LOG.warn("Order failed: {}", savedOrder);
            }
        });


        return null;
    }

    private Order add(String orderName) throws ExecutionException, InterruptedException {
        Order newOrder = new Order();
        newOrder.setId(UUID.randomUUID().toString());
        newOrder.setName(orderName);

        orderTable.putItem(newOrder).get();
        return newOrder;
    }
}