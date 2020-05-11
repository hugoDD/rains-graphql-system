package com.rains.graphql.system.service.impl;

import com.rains.graphql.system.domain.RoleDept;
import com.rains.graphql.system.mapper.RoleDeptMapper;
import com.rains.graphql.system.service.IRoleDeptService;
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
public class RoleDeptServiceImpl extends BaseService<RoleDeptMapper, RoleDept> implements IRoleDeptService {

}
