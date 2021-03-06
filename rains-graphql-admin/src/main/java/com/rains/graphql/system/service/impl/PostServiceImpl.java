package com.rains.graphql.system.service.impl;

import com.rains.graphql.system.domain.Post;
import com.rains.graphql.system.mapper.PostMapper;
import com.rains.graphql.system.service.IPostService;
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
public class PostServiceImpl extends BaseService<PostMapper, Post> implements IPostService {

}
