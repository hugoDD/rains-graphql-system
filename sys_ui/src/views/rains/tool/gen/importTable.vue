<template>
  <!-- 导入表 -->
  <el-dialog title="导入表" :visible.sync="visible" width="800px" top="5vh">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="表名称" prop="tableName">
        <el-input
          v-model="queryParams.re_tableName"
          placeholder="请输入表名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input
          v-model="queryParams.re_tableComment"
          placeholder="请输入表描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-table :data="dbTableList" @selection-change="handleSelectionChange" height="260px">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="tableName" label="表名称"></el-table-column>
        <el-table-column prop="tableComment" label="表描述"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column prop="updateTime" label="更新时间"></el-table-column>
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="fetch"
      />
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleImportTable">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { objToRsql, requestOpt, requestParam, requestQuery } from '../../../../utils/index'
import * as genOpt from "../../../../operation/genCode";

export default {
  data() {
    return {
      // 遮罩层
      visible: false,
      // 选中数组值
      tables: [],
      // 总条数
      total: 0,
      // 表数据
      dbTableList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tableName: undefined,
        tableComment: undefined
      }
    };
  },
  methods: {
    // 显示弹框
    show() {
      // this.getList();
      this.fetch()
      this.visible = true;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      // this.tables = selection.map(item => item.tableName);
      this.tables = selection
    },
    // 查询表数据
    fetch(condition = {}, params = {}) {
      requestParam.opt = 'dbTable'
      requestParam.pageNum = this.queryParams.pageNum
      requestParam.pageSize = this.queryParams.pageSize
      requestParam.filter = objToRsql(this.queryParams)
      requestQuery.request = requestParam
      console.log(requestQuery)
      this.$graphql(genOpt.pageQuery, {page: {...requestParam}}).then((r) => {
        const pageData = r.data.data.genTablePage
        this.total = pageData.total
        this.dbTableList = pageData.data
      })
    },
    getList() {
      listDbTable(this.queryParams).then(res => {
        if (res.code === 200) {
          this.dbTableList = res.rows;
          this.total = res.total;
        }
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.fetch();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导入按钮操作 */
    handleImportTable() {
      requestParam.opt = "importTable"
      requestParam.datas = this.tables
      this.$graphql(genOpt.baseMutation, {request:{...requestParam}}).then(() => {
        this.$message({
          message: this.$t('tips.createSuccess'),
          type: 'success'
        })
        this.visible = false
        this.$emit("ok")
      })
      // importTable({ tables: this.tables.join(",") }).then(res => {
      //   this.msgSuccess(res.msg);
      //   if (res.code === 200) {
      //     this.visible = false;
      //     this.$emit("ok");
      //   }
      // });
    }
  }
};
</script>
