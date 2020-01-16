package com.rains.graphql.common.graphql;

import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLHttpServlet;
import graphql.servlet.config.GraphQLConfiguration;
import graphql.servlet.core.GraphQLObjectMapper;
import graphql.servlet.core.GraphQLQueryInvoker;
import graphql.servlet.core.GraphQLServletListener;
import graphql.servlet.input.GraphQLInvocationInputFactory;
import graphql.servlet.input.GraphQLSingleInvocationInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CustomGraphQLHttpServlet extends GraphQLHttpServlet {
    private static final Logger log = LoggerFactory.getLogger(CustomGraphQLHttpServlet.class);
    private GraphQLConfiguration configuration;
    private static final int STATUS_BAD_REQUEST = 400;
    private static final String APPLICATION_DOWNLOAD = "application/download";

    private HttpRequestHandler dowloadHandler;

    public static GraphQLHttpServlet with(GraphQLSchema schema) {
        return new CustomGraphQLHttpServlet(GraphQLConfiguration.with(schema).build());
    }

    public static GraphQLHttpServlet with(GraphQLConfiguration configuration) {
        return new CustomGraphQLHttpServlet(configuration);
    }

    public CustomGraphQLHttpServlet(GraphQLConfiguration configuration) {
        this.configuration = Objects.requireNonNull(configuration, "configuration is required");
    }

    @Override
    protected GraphQLConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void init() {
        super.init();
        this.dowloadHandler = (request, response) -> {
            GraphQLInvocationInputFactory invocationInputFactory = configuration.getInvocationInputFactory();
            GraphQLObjectMapper graphQLObjectMapper = configuration.getObjectMapper();
            GraphQLQueryInvoker queryInvoker = configuration.getQueryInvoker();

            try {
                GraphQLSingleInvocationInput invocationInput = invocationInputFactory.create(graphQLObjectMapper.readGraphQLRequest(request.getInputStream()), request, response);
                 queryInvoker.query(invocationInput);
            } catch (Exception e) {
                log.info("Bad POST request: parsing failed", e);
                response.setStatus(STATUS_BAD_REQUEST);
            }
        };
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String respType=resp.getContentType();
        String reqType = req.getContentType();
        if(APPLICATION_DOWNLOAD.equals(reqType)){
            init();
            doRequest(req,resp,dowloadHandler,null);
        }else{
            super.doPost(req,resp);
        }


    }

    private void doRequest(HttpServletRequest request, HttpServletResponse response, HttpRequestHandler handler, AsyncContext asyncContext) {

        List<GraphQLServletListener.RequestCallback> requestCallbacks = runListeners(l -> l.onRequest(request, response));

        try {
            handler.handle(request, response);
            runCallbacks(requestCallbacks, c -> c.onSuccess(request, response));
        } catch (Throwable t) {
            response.setStatus(500);
            log.error("Error executing GraphQL request!", t);
            runCallbacks(requestCallbacks, c -> c.onError(request, response, t));
        } finally {
            runCallbacks(requestCallbacks, c -> c.onFinally(request, response));
            if (asyncContext != null) {
                asyncContext.complete();
            }
        }
    }

    private <T> void runCallbacks(List<T> callbacks, Consumer<T> action) {
        callbacks.forEach(callback -> {
            try {
                action.accept(callback);
            } catch (Throwable t) {
                log.error("Error running callback: {}", callback, t);
            }
        });
    }
    private <R> List<R> runListeners(Function<? super GraphQLServletListener, R> action) {
        return configuration.getListeners().stream()
                .map(listener -> {
                    try {
                        return action.apply(listener);
                    } catch (Throwable t) {
                        log.error("Error running listener: {}", listener, t);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
