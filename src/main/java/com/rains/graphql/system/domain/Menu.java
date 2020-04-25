package com.rains.graphql.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rains.graphql.common.converter.TimeConverter;
import com.rains.graphql.common.domain.ITree;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@TableName("t_menu")
@Excel("菜单信息表")
public class Menu implements ITree<Long>, Serializable {

    public static final String TYPE_MENU = "0";
    public static final String TYPE_BUTTON = "1";
    private static final long serialVersionUID = 7187628714679791771L;
    @TableId(value = "MENU_ID", type = IdType.AUTO)
    private Long menuId;

    private Long parentId;

    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "名称")
    private String menuName;

    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "地址")
    private String path;

    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "对应Vue组件")
    private String component;

    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "权限")
    private String perms;

    @ExcelField(value = "图标")
    private String icon;
    /**
     * 类型（M目录 C菜单 F按钮）
     */
    @NotBlank(message = "{required}")
    @ExcelField(value = "类型", writeConverterExp = "M=目录,C=菜单,F=按钮")
    private String type;

    private Double orderNum;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;

    /**
     * 是否为外链（0是 1否）
     */
    private String isFrame;
    /**
     * 菜单状态（0显示 1隐藏）
     */
    @ExcelField(value = "是否可见", writeConverterExp = "0=显示,1=隐藏")
    private String visible;

    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();


    @Override
    public Long getId() {
        return menuId;
    }


    @Override
    public String getLabel() {
        return menuName;
    }
}
