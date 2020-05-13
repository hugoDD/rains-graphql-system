package com.rains.graphql.arthas.service.impl;

import com.rains.graphql.arthas.entity.ArthasMachineinfoPrivilege;
import com.rains.graphql.arthas.mapper.ArthasMachineinfoPrivilegeMapper;
import com.rains.graphql.arthas.service.ArthasMachineinfoPrivilegeService;
import com.rains.graphql.system.service.impl.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (ArthasMachineinfoPrivilege)表服务实现类
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Service
@Transactional
public class ArthasMachineinfoPrivilegeServiceImpl extends BaseService<ArthasMachineinfoPrivilegeMapper, ArthasMachineinfoPrivilege> implements ArthasMachineinfoPrivilegeService {

}
