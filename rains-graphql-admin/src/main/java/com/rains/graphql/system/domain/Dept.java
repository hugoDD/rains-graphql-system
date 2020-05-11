package com.rains.graphql.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.domain.ITree;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_dept")
@Excel("部门信息表")
public class Dept implements ITree<Long>, Serializable {

    private static final long serialVersionUID = -7790334862410409053L;

    @TableId(value = "DEPT_ID", type = IdType.AUTO)
    private Long deptId;

    private Long parentId;

    /**
     * 祖级列表
     */
    @ExcelField(value = "祖级列表")
    private String ancestors;

    @NotBlank(message = "{required}")
    @Size(max = 20, message = "{noMoreThan}")
    @ExcelField(value = "部门名称")
    private String deptName;

    private Double orderNum;

    /**
     * 负责人
     */
    private String leader;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 部门状态（0正常 1停用）
     */
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String modifyBy;
    /**
     * 修改时间
     */
    private Date modifyTime;


    @Override
    public Long getId() {
        return this.deptId;
    }


    @Override
    public String getLabel() {
        return this.deptName;
    }

    @Override
    public String getKey() {
        return this.getDeptId() + "";
    }

    @Override
    public String getValue() {
        return this.getDeptId() + "";
    }
}
