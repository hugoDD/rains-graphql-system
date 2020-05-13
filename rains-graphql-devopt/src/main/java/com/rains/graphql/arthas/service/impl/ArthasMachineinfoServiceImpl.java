package com.rains.graphql.arthas.service.impl;

import com.rains.graphql.arthas.entity.ArthasMachineinfo;
import com.rains.graphql.arthas.mapper.ArthasMachineinfoMapper;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.system.service.impl.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (ArthasMachineinfo)表服务实现类
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Service
@Transactional
public class ArthasMachineinfoServiceImpl extends BaseService<ArthasMachineinfoMapper, ArthasMachineinfo> implements ArthasMachineinfoService {

}
