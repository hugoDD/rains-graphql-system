package com.rains.graphql.arthas.service.impl;

import com.rains.graphql.arthas.entity.ArthasCmdlog;
import com.rains.graphql.arthas.mapper.ArthasCmdlogMapper;
import com.rains.graphql.arthas.service.ArthasCmdlogService;
import com.rains.graphql.system.service.impl.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (ArthasCmdlog)表服务实现类
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Service
@Transactional
public class ArthasCmdlogServiceImpl extends BaseService<ArthasCmdlogMapper, ArthasCmdlog> implements ArthasCmdlogService {

}
