package com.rains.graphql.common.graphql.context;

import com.corundumstudio.socketio.SocketIOClient;
import graphql.servlet.context.DefaultGraphQLContext;
import graphql.servlet.context.GraphQLContext;
import org.dataloader.DataLoaderRegistry;

import javax.security.auth.Subject;


public class DefaultGraphQLSocketIoContext extends DefaultGraphQLContext implements GraphQLContext {

    private final SocketIOClient session;


    private DefaultGraphQLSocketIoContext(DataLoaderRegistry dataLoaderRegistry, Subject subject,
                                           SocketIOClient session) {
        super(dataLoaderRegistry, subject);
        this.session = session;

    }

    public SocketIOClient getSession() {
        return session;
    }
//
//    @Override
//    public Optional<Object> getConnectResult() {
//        return Optional.of(session).map(session -> session.getUserProperties().get(ApolloSubscriptionConnectionListener.CONNECT_RESULT_KEY));
//    }
//
//    @Override
//    public HandshakeRequest getHandshakeRequest() {
//        return handshakeRequest;
//    }

    public static Builder createSocketIoContext(DataLoaderRegistry registry, Subject subject) {
        return new Builder(registry, subject);
    }

    public static Builder createSocketIoContext() {
        return new Builder(new DataLoaderRegistry(), null);
    }

    public static class Builder {
        private SocketIOClient session;
        private DataLoaderRegistry dataLoaderRegistry;
        private Subject subject;

        private Builder(DataLoaderRegistry dataLoaderRegistry, Subject subject) {
            this.dataLoaderRegistry = dataLoaderRegistry;
            this.subject = subject;
        }

        public DefaultGraphQLSocketIoContext build() {
            return new DefaultGraphQLSocketIoContext(dataLoaderRegistry, subject, session);
        }

        public Builder with(SocketIOClient session) {
            this.session = session;
            return this;
        }



        public Builder with(DataLoaderRegistry dataLoaderRegistry) {
            this.dataLoaderRegistry = dataLoaderRegistry;
            return this;
        }

        public Builder with(Subject subject) {
            this.subject = subject;
            return this;
        }

    }
}
