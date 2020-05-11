package com.rains.graphql.system.service.impl;

import com.rains.graphql.system.domain.UserPost;
import com.rains.graphql.system.mapper.UserPostMapper;
import com.rains.graphql.system.service.IUserPostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户岗位Service业务层处理
 *
 * @author hugo
 * @date 2020-03-25
 */
@Service
@Transactional
public class UserPostServiceImpl extends BaseService<UserPostMapper, UserPost> implements IUserPostService {

}
