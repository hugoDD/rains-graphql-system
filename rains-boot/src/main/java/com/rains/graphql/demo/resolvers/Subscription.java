package com.rains.graphql.demo.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.rains.graphql.demo.publishers.StockTickerPublisher;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
class Subscription implements GraphQLSubscriptionResolver {

    private StockTickerPublisher stockTickerPublisher;

    Subscription(StockTickerPublisher stockTickerPublisher) {
        this.stockTickerPublisher = stockTickerPublisher;
    }

    Publisher<StockPriceUpdate> stockQuotes(List<String> stockCodes) {

        return stockTickerPublisher.getPublisher(stockCodes);
    }



}
