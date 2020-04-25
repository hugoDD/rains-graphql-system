package com.rains.graphql.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 用户岗位对象 sys_user_post
 *
 * @author hugo
 * @date 2020-03-25
 */
@Data
@TableName("sys_user_post")
public class UserPost {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;
    @TableId(value = "post_id", type = IdType.INPUT)
    private Long postId;


}
