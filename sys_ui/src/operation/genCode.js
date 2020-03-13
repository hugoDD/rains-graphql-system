export const pageQuery = `query pageQuery($page: QueryRequest) {
  genTablePage(page:$page){
    data{
    tableId
    tableName
    tableComment
    className
    tplCategory
    packageName
    moduleName
    businessName
    functionName
    functionAuthor
    options
    createBy
    createTime
    updateBy
    updateTime
    remark
  }
  total
  }
}`

export const previewCodeQuery = `query previewQuery($page: QueryRequest) {
  genTablePage(page:$page){
    data{
    tableId
    tableName
    preview
    }
  }
}`

export const genInfo = `query pageQuery($page: QueryRequest) {
  info: genTablePage(page: $page) {
    data {
      tableId
      tableName
      tableComment
      className
      tplCategory
      packageName
      moduleName
      businessName
      functionName
      functionAuthor
      options
      createBy
      createTime
      updateBy
      updateTime
      remark
    }
  }
  rows: genTableColumnPage(page: $page) {
    data {
      columnId
      tableId
      columnName
      columnComment
      columnType
      javaType
      graphqlType
      javaField
      isPk
      isIncrement
      isRequired
      isInsert
      isEdit
      isList
      isQuery
      queryType
      htmlType
      dictType
      sort
      createBy
      createTime
      updateBy
      updateTime
    }
  }
  dictTypes: dictTypePage(page: { pageNum: 1, pageSize: 200 }) {
    data {
      createBy
      createTime
      dictId
      dictName
      dictType
      remark
      status
      updateBy
      updateTime
    }
  }
}`

export const baseMutation = `mutation baseMutation($request:QueryRequest){
  genTableBaseMutation(request:$request)
}`
