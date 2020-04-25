package com.rains.graphql.system.query;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.system.domain.Notice;
import com.rains.graphql.system.domain.PageData;
import com.rains.graphql.system.service.INoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 公告信息 QueryResolver
 *
 * @author hugo
 * @date 2020-03-25
 */
@Slf4j
@Component
public class NoticeQuery implements GraphQLQueryResolver {

    @Autowired
    private INoticeService noticeService;

    @RequiresPermissions("notice:view")
    public PageData<Notice> noticePage(QueryRequest request) {
        return noticeService.query(request);
    }


}
