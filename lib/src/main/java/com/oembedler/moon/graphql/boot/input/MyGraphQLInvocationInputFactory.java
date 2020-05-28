package com.oembedler.moon.graphql.boot.input;

import com.corundumstudio.socketio.SocketIOClient;
import com.oembedler.moon.graphql.boot.context.MyGraphQLContextBuilder;
import graphql.schema.GraphQLSchema;
import graphql.servlet.config.DefaultGraphQLSchemaProvider;
import graphql.servlet.config.GraphQLSchemaProvider;
import graphql.servlet.context.ContextSetting;
import graphql.servlet.core.DefaultGraphQLRootObjectBuilder;
import graphql.servlet.core.GraphQLRootObjectBuilder;
import graphql.servlet.core.internal.GraphQLRequest;
import graphql.servlet.input.GraphQLBatchedInvocationInput;
import graphql.servlet.input.GraphQLSingleInvocationInput;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.util.List;
import java.util.function.Supplier;

public class MyGraphQLInvocationInputFactory  {

    private final Supplier<GraphQLSchemaProvider> schemaProviderSupplier;
    private final Supplier<MyGraphQLContextBuilder> contextBuilderSupplier;
    private final Supplier<GraphQLRootObjectBuilder> rootObjectBuilderSupplier;

    protected MyGraphQLInvocationInputFactory(Supplier<GraphQLSchemaProvider> schemaProviderSupplier, Supplier<MyGraphQLContextBuilder> contextBuilderSupplier, Supplier<GraphQLRootObjectBuilder> rootObjectBuilderSupplier) {
        this.schemaProviderSupplier = schemaProviderSupplier;
        this.contextBuilderSupplier = contextBuilderSupplier;
        this.rootObjectBuilderSupplier = rootObjectBuilderSupplier;
    }

    public GraphQLSchemaProvider getSchemaProvider() {
        return schemaProviderSupplier.get();
    }

    public GraphQLSingleInvocationInput create(GraphQLRequest graphQLRequest, HttpServletRequest request, HttpServletResponse response) {
        return create(graphQLRequest, request, response, false);
    }

    public GraphQLBatchedInvocationInput create(ContextSetting contextSetting, List<GraphQLRequest> graphQLRequests, HttpServletRequest request,
                                                HttpServletResponse response) {
        return create(contextSetting, graphQLRequests, request, response, false);
    }


    public GraphQLSingleInvocationInput createReadOnly(GraphQLRequest graphQLRequest, HttpServletRequest request, HttpServletResponse response) {
        return create(graphQLRequest, request, response, true);
    }

    public GraphQLBatchedInvocationInput createReadOnly(ContextSetting contextSetting, List<GraphQLRequest> graphQLRequests, HttpServletRequest request, HttpServletResponse response) {
        return create(contextSetting, graphQLRequests, request, response, true);
    }

    public GraphQLSingleInvocationInput create(GraphQLRequest graphQLRequest) {
        return new GraphQLSingleInvocationInput(
                graphQLRequest,
                schemaProviderSupplier.get().getSchema(),
                contextBuilderSupplier.get().build(),
                rootObjectBuilderSupplier.get().build()
        );
    }

    private GraphQLSingleInvocationInput create(GraphQLRequest graphQLRequest, HttpServletRequest request, HttpServletResponse response,
                                                boolean readOnly) {
        return new GraphQLSingleInvocationInput(
                graphQLRequest,
                readOnly ? schemaProviderSupplier.get().getReadOnlySchema(request) : schemaProviderSupplier.get().getSchema(request),
                contextBuilderSupplier.get().build(request, response),
                rootObjectBuilderSupplier.get().build(request)
        );
    }

    private GraphQLBatchedInvocationInput create(ContextSetting contextSetting, List<GraphQLRequest> graphQLRequests, HttpServletRequest request,
                                                 HttpServletResponse response, boolean readOnly) {
        return contextSetting.getBatch(
                graphQLRequests,
                readOnly ? schemaProviderSupplier.get().getReadOnlySchema(request) : schemaProviderSupplier.get().getSchema(request),
                () -> contextBuilderSupplier.get().build(request, response),
                rootObjectBuilderSupplier.get().build(request)
        );
    }

    public GraphQLSingleInvocationInput create(GraphQLRequest graphQLRequest, Session session, HandshakeRequest request) {
        return new GraphQLSingleInvocationInput(
                graphQLRequest,
                schemaProviderSupplier.get().getSchema(request),
                contextBuilderSupplier.get().build(session, request),
                rootObjectBuilderSupplier.get().build(request)
        );
    }
    public GraphQLSingleInvocationInput create(GraphQLRequest graphQLRequest, SocketIOClient session) {
        return new GraphQLSingleInvocationInput(
                graphQLRequest,
                schemaProviderSupplier.get().getSchema(),
                contextBuilderSupplier.get().build(session),
                rootObjectBuilderSupplier.get().build()
        );
    }

    public GraphQLBatchedInvocationInput create(ContextSetting contextSetting, List<GraphQLRequest> graphQLRequest, Session session, HandshakeRequest request) {
        return contextSetting.getBatch(
                graphQLRequest,
                schemaProviderSupplier.get().getSchema(request),
                () -> contextBuilderSupplier.get().build(session, request),
                rootObjectBuilderSupplier.get().build(request)
        );
    }

    public static MyGraphQLInvocationInputFactory.Builder newBuilder(GraphQLSchema schema) {
        return new MyGraphQLInvocationInputFactory.Builder(new DefaultGraphQLSchemaProvider(schema));
    }

    public static MyGraphQLInvocationInputFactory.Builder newBuilder(GraphQLSchemaProvider schemaProvider) {
        return new MyGraphQLInvocationInputFactory.Builder(schemaProvider);
    }

    public static MyGraphQLInvocationInputFactory.Builder newBuilder(Supplier<GraphQLSchemaProvider> schemaProviderSupplier) {
        return new MyGraphQLInvocationInputFactory.Builder(schemaProviderSupplier);
    }

    public static class Builder {
        private final Supplier<GraphQLSchemaProvider> schemaProviderSupplier;
        private Supplier<MyGraphQLContextBuilder> contextBuilderSupplier = MyGraphQLContextBuilder::new;
        private Supplier<GraphQLRootObjectBuilder> rootObjectBuilderSupplier = DefaultGraphQLRootObjectBuilder::new;

        public Builder(GraphQLSchemaProvider schemaProvider) {
            this(() -> schemaProvider);
        }

        public Builder(Supplier<GraphQLSchemaProvider> schemaProviderSupplier) {
            this.schemaProviderSupplier = schemaProviderSupplier;
        }

        public MyGraphQLInvocationInputFactory.Builder withGraphQLContextBuilder(MyGraphQLContextBuilder contextBuilder) {
            return withGraphQLContextBuilder(() -> contextBuilder);
        }

        public MyGraphQLInvocationInputFactory.Builder withGraphQLContextBuilder(Supplier<MyGraphQLContextBuilder> contextBuilderSupplier) {
            this.contextBuilderSupplier = contextBuilderSupplier;
            return this;
        }

        public MyGraphQLInvocationInputFactory.Builder withGraphQLRootObjectBuilder(GraphQLRootObjectBuilder rootObjectBuilder) {
            return withGraphQLRootObjectBuilder(() -> rootObjectBuilder);
        }

        public MyGraphQLInvocationInputFactory.Builder withGraphQLRootObjectBuilder(Supplier<GraphQLRootObjectBuilder> rootObjectBuilderSupplier) {
            this.rootObjectBuilderSupplier = rootObjectBuilderSupplier;
            return this;
        }

        public MyGraphQLInvocationInputFactory build() {
            return new MyGraphQLInvocationInputFactory(schemaProviderSupplier, contextBuilderSupplier, rootObjectBuilderSupplier);
        }
    }
}
