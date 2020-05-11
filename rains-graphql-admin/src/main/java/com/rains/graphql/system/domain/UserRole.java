package com.rains.graphql.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("sys_user_role")
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = -3166012934498268403L;
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;
    @TableId(value = "role_id", type = IdType.INPUT)
    private Long roleId;

}
