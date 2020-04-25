package com.rains.graphql.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("t_role")
@Excel("角色信息表")
public class Role implements Serializable {

    private static final long serialVersionUID = -1714476694755654924L;

    /**
     * 角色ID
     */
    @TableId(value = "ROLE_ID", type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名称
     */
    @ExcelField(value = "角色名称")
    @TableField("ROLE_NAME")
    private String roleName;
    /**
     * 角色权限字符串
     */
    @ExcelField(value = "角色权限")
    @TableField("ROLE_KEY")
    private String roleKey;
    /**
     * 显示顺序
     */
    @ExcelField(value = "显示顺序")
    @TableField("ROLE_SORT")
    private Long roleSort;
    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    @ExcelField(value = "数据范围", writeConverterExp = "1=全部数据权限,2=自定数据权限,3=本部门数据权限,4=本部门及以下数据权限")
    @TableField("DATA_SCOPE")
    private String dataScope;
    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelField(value = "角色状态", writeConverterExp = "0=正常,1=停用")
    @TableField("STATUS")
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ExcelField(value = "删除标志", writeConverterExp = "0=存在,2=删除")
    @TableField("DEL_FLAG")
    private String delFlag;
    /**
     * 创建者
     */
    @TableField("CREATE_BY")
    private String createBy;
    /**
     * 创建时间
     */
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    @TableField("CREATE_TIME")
    private Date createTime;
    /**
     * 更新者
     */
    @TableField("MODIFY_BY")
    private String modifyBy;
    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private Date modifyTime;

    private String remark;


    private transient List<String> menuIds;

    private transient String createTimeFrom;
    private transient String createTimeTo;
    private transient String menuId;

}
