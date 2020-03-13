# tableComment 查询参数
type ${className} {
<#if columns??>
    <#list columns as column>
    # ${column.remark}
        <#if column.isKey = true>
    ${column.field?uncap_first}: ID
        <#elseif (column.type = 'varchar' || column.type = 'text' || column.type = 'uniqueidentifier'
        || column.type = 'varchar2' || column.type = 'nvarchar' || column.type = 'VARCHAR2'
        || column.type = 'VARCHAR'|| column.type = 'CLOB' || column.type = 'char' || column.type = 'longvarchar')>
    ${column.field?uncap_first}: String
        <#elseif  column.type = 'timestamp' || column.type = 'date' || column.type = 'datetime'||column.type = 'TIMESTAMP' || column.type = 'DATE' || column.type = 'DATETIME'>
    ${column.field?uncap_first}: Date
        <#elseif column.type = 'int' || column.type = 'smallint' || column.type = 'bigint' || column.type = 'tinyint' || column.type = 'integer'>
    ${column.field?uncap_first}: Int
        <#elseif column.type = 'double' || column.type = 'float' || column.type = 'real' || column.type = 'numeric' || column.type = 'decimal' >
    ${column.field?uncap_first}: Float
        <#elseif column.type = 'bit'>
    ${column.field?uncap_first}: Boolean
        <#else >
    ${column.field?uncap_first}: Object
        </#if>
    </#list>
</#if>
}

# tableComment 分页返回
type ${className}s {
    data: [${className}!]!
    total: Int
}

# tableComment 编辑实体
input ${className}Condition {
<#if columns??>
    <#list columns as column>
    # ${column.remark}
        <#if column.isKey = true>
    ${column.field?uncap_first}: ID
        <#elseif (column.type = 'varchar' || column.type = 'text' || column.type = 'uniqueidentifier'
        || column.type = 'varchar2' || column.type = 'nvarchar' || column.type = 'VARCHAR2'
        || column.type = 'VARCHAR'|| column.type = 'CLOB' || column.type = 'char' || column.type = 'longvarchar')>
    ${column.field?uncap_first}: String
        <#elseif  column.type = 'timestamp' || column.type = 'date' || column.type = 'datetime'||column.type = 'TIMESTAMP' || column.type = 'DATE' || column.type = 'DATETIME'>
    ${column.field?uncap_first}: Date
        <#elseif column.type = 'int' || column.type = 'smallint' || column.type = 'bigint' || column.type = 'tinyint' || column.type = 'integer'>
    ${column.field?uncap_first}: Int
        <#elseif column.type = 'double' || column.type = 'float' || column.type = 'real' || column.type = 'numeric' || column.type = 'decimal' >
    ${column.field?uncap_first}: Float
        <#elseif column.type = 'bit'>
    ${column.field?uncap_first}: Boolean
        <#else >
    ${column.field?uncap_first}: Object
        </#if>
    </#list>
</#if>
}

extend type Query {
    # tableComment 分页
    ${className?uncap_first}Page(page: QueryRequest): ${className}s
}
extend type Mutation {
    # tableComment 编辑 包括新增、修改、删除
    # opt:insert表示新增
    # opt:update表示修改
    # opt:delete表示删除
    ${className?uncap_first}BaseMutation(request: QueryRequest,entity: ${className}Condition): Boolean
}