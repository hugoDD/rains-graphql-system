package com.rains.graphql.system.query;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.domain.UserPost;
import com.rains.graphql.system.service.IUserPostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户岗位 QueryResolver
 *
 * @author hugo
 * @date 2020-03-25
 */
@Slf4j
@Component
public class UserPostQuery implements GraphQLQueryResolver {

    @Autowired
    private IUserPostService userPostService;

    @RequiresPermissions("userPost:view")
    public PageData<UserPost> userPostPage(QueryRequest request) {
        return userPostService.query(request);
    }


}
