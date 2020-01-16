package com.rains.graphql.system.service;

import com.rains.graphql.system.domain.GeneratorConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author hugoDD
 */
public interface GeneratorConfigService extends IService<GeneratorConfig> {

    /**
     * 查询
     *
     * @return GeneratorConfig
     */
    GeneratorConfig findGeneratorConfig();

    /**
     * 修改
     *
     * @param generatorConfig generatorConfig
     */
    void updateGeneratorConfig(GeneratorConfig generatorConfig);

}
