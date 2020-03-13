<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm">
      <el-form-item label="表名称" prop="tableName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入表名称"
          size="small"
          v-model="queryParams.tableName"
        />
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入表描述"
          size="small"
          v-model="queryParams.tableComment"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          end-placeholder="结束日期"
          range-separator="-"
          size="small"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          v-model="dateRange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" icon="el-icon-search" size="mini" type="primary">搜索</el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh" size="mini">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          @click="handleGenTable"
          icon="el-icon-download"
          size="mini"
          type="primary"
          v-has-permission="['tool:gen:code']"
        >生成
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="openImportTable"
          icon="el-icon-upload"
          size="mini"
          type="info"
          v-has-permission="['tool:gen:import']"
        >导入
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :disabled="single"
          @click="handleEditTable"
          icon="el-icon-edit"
          size="mini"
          type="success"
          v-has-permission="['tool:gen:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :disabled="multiple"
          @click="handleDelete"
          icon="el-icon-delete"
          size="mini"
          type="danger"
          v-has-permission="['tool:gen:remove']"
        >删除
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="tableList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="序号" prop="tableId" width="50px"/>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="表名称"
        prop="tableName"
        width="130"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="表描述"
        prop="tableComment"
        width="130"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="实体"
        prop="className"
        width="130"
      />
      <el-table-column align="center" label="创建时间" prop="createTime" width="160"/>
      <el-table-column align="center" label="更新时间" prop="updateTime" width="160"/>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handlePreview(scope.row)"
            icon="el-icon-view"
            size="small"
            type="text"
            v-has-permission="['tool:gen:preview']"
          >预览
          </el-button>
          <el-button
            @click="handleEditTable(scope.row)"
            icon="el-icon-edit"
            size="small"
            type="text"
            v-has-permission="['tool:gen:edit']"
          >编辑
          </el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="small"
            type="text"
            v-has-permission="['tool:gen:remove']"
          >删除
          </el-button>
          <el-button
            @click="handleGenTable(scope.row)"
            icon="el-icon-download"
            size="small"
            type="text"
            v-has-permission="['tool:gen:code']"
          >生成代码
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="fetch"
      v-show="total>0"
    />
    <!-- 预览界面 -->
    <el-dialog :title="preview.title" :visible.sync="preview.open" top="5vh" width="80%">
      <el-tabs v-model="preview.activeName">
        <el-tab-pane
          :key="key"
          :label="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          :lazy="true"
          :name="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          v-for="(value, key) in preview.data"
        >
          <java-editor :height="preview.height" :value="value"></java-editor>
          <!-- <json-editor :value="value" ></json-editor>-->

        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <import-table @ok="handleQuery" ref="import"/>
  </div>
</template>

<script>
  import importTable from "./importTable";
  import * as genOpt from "../../../../operation/genCode";
  import {objToRsql, requestOpt, requestParam} from "../../../../utils";
  import JavaEditor from "../../../../components/JavaEdit/index";


  export default {
    name: "Gen",
    components: {JavaEditor, importTable},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 唯一标识符
        uniqueId: "",
        // 选中数组
        ids: [],
        // 选中表数组
        tableNames: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 表数据
        tableList: [],
        // 日期范围
        dateRange: "",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          tableName: undefined,
          tableComment: undefined
        },
        // 预览参数
        preview: {
          open: false,
          height: '',
          title: "代码预览",
          data: {},
          activeName: "domain.java"
        }
      };
    },
    created() {
      this.preview.height = document.documentElement.clientHeight - 210 + 'px';
      this.fetch();
    },
    activated() {
      const time = this.$route.query.t;
      if (time != null && time != this.uniqueId) {
        this.uniqueId = time;
        this.resetQuery();
      }
    },
    methods: {
      /** 查询表集合 */
      fetch() {
        // params.pageSize = this.pagination.size
        // params.pageNum = this.pagination.num
        // if (this.queryParams.timeRange) {
        //   condition.createTimeFrom = this.queryParams.timeRange[0]
        //   condition.createTimeTo = this.queryParams.timeRange[1]
        // }
        const reqParam = Object.create(requestParam);
        reqParam.pageNum = this.queryParams.pageNum;
        reqParam.pageSize = this.queryParams.pageSize;
        reqParam.filter = objToRsql(this.queryParams);
        this.loading = true;
        this.$graphql(genOpt.pageQuery, {page: {...reqParam}}).then((r) => {
          const pageData = r.data.data.genTablePage;
          this.total = pageData.total;
          this.tableList = pageData.data;
          this.loading = false
        })
      },
      delete(ids) {
        const reqParam = Object.create(requestParam);
        reqParam.opt = requestOpt.delete;
        reqParam.ids = ids;
        const childParam = Object.create(requestParam);
        childParam.opt = requestOpt.delete
        childParam.filter = "tableId=in=(" + ids + ")";
        childParam.ids = null;
        reqParam.child = {genTableColumn: childParam};
        this.$graphql(genOpt.baseMutation, {request: {...reqParam}}).then(() => {
          this.$message({
            message: this.$t('tips.deleteSuccess'),
            type: 'success'
          });
          this.fetch()
        })
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.fetch();
      },
      /** 生成代码操作 */
      handleGenTable(row) {
        const reqParam = Object.create(requestParam);
        reqParam.opt = 'genCode';
        reqParam.ids = this.ids;
        if (this.ids == "") {
          this.msgError("请选择要生成的数据");
          return;
        }
        this.$graphqlDownload(genOpt.baseMutation, {request: {...reqParam}}, `rains_code.zip`)
        //downLoadZip("/tool/gen/batchGenCode?tables=" + tableNames, "ruoyi");
      },
      /** 打开导入表弹窗 */
      openImportTable() {
        this.$refs.import.show();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 预览按钮 */
      handlePreview(row) {
        // 获取表详细信息
        requestParam.filter = "tableId==" + row.tableId;
        console.log(requestParam);
        this.$graphql(genOpt.previewCodeQuery, {page: {...requestParam}}).then((r) => {
          const pageData = r.data.data.genTablePage;
          this.preview.data = pageData.data[0].preview;
          this.preview.open = true;
        })
        // previewTable(row.tableId).then(response => {
        //   this.preview.data = response.data;
        //   this.preview.open = true;
        // });
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.tableId);
        this.tableNames = selection.map(item => item.tableName);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },

      /** 修改按钮操作 */
      handleEditTable(row) {
        const tableId = row.tableId || this.ids[0];
        this.$router.push({path: "/gen/edit", query: {tableId: tableId}});
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const tableIds = row.tableId || this.ids;
        this.$confirm('是否确认删除表编号为"' + tableIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          return this.delete(tableIds);
        }).catch(function () {
        });
      }
    }
  }
</script>
