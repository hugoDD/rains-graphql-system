package com.rains.system.domain;

import com.yahoo.elide.annotation.Include;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_user")
@Include(rootLevel = true)
@Table(appliesTo = "t_sys_user",comment = "账号")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User  extends BaseEntity {
    @Column
    private String avatar;
    @Column
    private String account;
    @Column
    private String password;
    @Column
    private String salt;
    @Column
    private String name;
    @Column
    private Date birthday;
    @Column
    private Integer sex;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private List<Role> roles;
    @Column
    private List<Dept> depts;

    private Set<String> permissions;
    @Column
    private Integer status;
    @Column
    private Integer version;
}
