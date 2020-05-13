package com.rains.graphql.arthas.service.impl;

import com.rains.graphql.arthas.entity.ArthasRsa;
import com.rains.graphql.arthas.mapper.ArthasRsaMapper;
import com.rains.graphql.arthas.service.ArthasRsaService;
import com.rains.graphql.system.service.impl.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (ArthasRsa)表服务实现类
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Service
@Transactional
public class ArthasRsaServiceImpl extends BaseService<ArthasRsaMapper, ArthasRsa> implements ArthasRsaService {

}
