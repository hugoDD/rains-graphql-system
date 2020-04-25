package com.rains.graphql.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rains.graphql.system.domain.GeneratorConfig;

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
