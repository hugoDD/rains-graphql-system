package com.rains.graphql.common.graphql;

import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GraphQLHttpUtil {

    public static HttpServletResponse getResponse(DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        HttpServletResponse response = context.getHttpServletResponse();
        return response;
    }

    public static HttpServletRequest getRequest(DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        HttpServletRequest request = context.getHttpServletRequest();
        return request;
    }
}
