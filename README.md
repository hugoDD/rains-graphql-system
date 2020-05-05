## 平台简介
![Java](https://img.shields.io/badge/JDK-1.8-green.svg) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.2.0.RELEASE-brightgreen.svg)](https://spring.io/projects/spring-boot)

看了很多优秀的开源项目。但是发现使用graphql开发的项目还是很少。特别是在解决权限和对于之前开发习惯，又方便开发的权限管理系统。所以，
就在前人的管理系统基础实现一个基于graphql特点的集成graphql+rsql+shiro+springboot+mybatis plus的权限系统。


参考后台模板[vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)


## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  通知公告：系统通知公告信息发布维护。
8.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
9. 登录日志：系统登录日志记录查询包含登录异常。
10. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
11. 系统接口：根据业务代码自动生成相关的graphql文档。
12. 在线构建器：拖动表单元素生成相应的HTML代码。

## 使用技术栈
    1.springboot
    2.shiro
    3.jetcache
    4.graphql-spring-boot
    5.vue
    6.element
    7.rsql
    8.redis
    9.mysql
    10.jjwt
    
## 安装步骤
    1.初始化db目录的sql脚本
```shell script
2.编译后端服务
git clone https://github.com/hugoDD/rains-graphql-system.git
cd rains-graphql-system
mvn clean pagckage -Dmaven.test.skip=true
```    

```shell script
3.编译与运行前端

```
## 演示图

 <table>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-36f163fee0ad272d26edcd68469c4f6d7f0.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-44259d63005199d4f20731b1c2a22d5c3ad.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-29d66c100d8a994dc3e772fcecaea3c9a1c.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-4fb0f7b27c7ad6ae95d73f2d0e32027bd80.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-9f3d2269e376904e2877f74a4c7c6828254.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-6e71a90d72dfec1be5cfd9b3badf54442c7.png"/></td>
    </tr>
</table>

####  特别感谢以下优秀的开源项目
- [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue.git)
- [Mybatis-Plus](https://gitee.com/baomidou/mybatis-plus)

