package com.rains.graphql.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("sys_role_dept")
@Data
public class RoleDept implements Serializable {

    private static final long serialVersionUID = -7573904024872252113L;

    private Long roleId;

    private Long deptId;
}
