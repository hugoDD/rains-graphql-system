package com.rains.graphql.system.service.impl;

import com.rains.graphql.system.domain.Notice;
import com.rains.graphql.system.mapper.NoticeMapper;
import com.rains.graphql.system.service.INoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公告信息Service业务层处理
 *
 * @author hugo
 * @date 2020-03-25
 */
@Service
@Transactional
public class NoticeServiceImpl extends BaseService<NoticeMapper, Notice> implements INoticeService {

}
