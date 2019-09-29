<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="身份证">
              <a-input placeholder="请输入身份证" v-model="queryParam.idnumber"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="客户姓名">
              <a-input placeholder="请输入客户姓名" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="性别">
              <a-input placeholder="请输入性别" v-model="queryParam.gender"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="出生日期">
              <a-input placeholder="请输入出生日期" v-model="queryParam.birthday"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="民族">
              <a-input placeholder="请输入民族" v-model="queryParam.national"></a-input>
            </a-form-item>
          </a-col>
          </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('入住功能')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <checkIn-modal ref="modalForm" @ok="modalFormOk"></checkIn-modal>
  </a-card>
</template>

<script>
  import CheckInModal from './modules/CheckInModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "CheckInList",
    mixins:[JeecgListMixin],
    components: {
      CheckInModal
    },
    data () {
      return {
        description: '入住功能管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '身份证',
            align:"center",
            dataIndex: 'idnumber'
           },
		   {
            title: '客户姓名',
            align:"center",
            dataIndex: 'name'
           },
		   {
            title: '性别',
            align:"center",
            dataIndex: 'gender'
           },
		   {
            title: '出生日期',
            align:"center",
            dataIndex: 'birthday'
           },
		   {
            title: '民族',
            align:"center",
            dataIndex: 'national'
           },
		   {
            title: '身份证住址',
            align:"center",
            dataIndex: 'idaddress'
           },
		   {
            title: '手机号',
            align:"center",
            dataIndex: 'phonenum'
           },
		   {
            title: '房间号',
            align:"center",
            dataIndex: 'roomnum'
           },
		   {
            title: '真实入住时间',
            align:"center",
            dataIndex: 'truecreatetime'
           },
		   {
            title: '真实离店时间',
            align:"center",
            dataIndex: 'trueupdatetime'
           },
		   {
            title: '订单号',
            align:"center",
            dataIndex: 'orderid'
           },
		   {
            title: '预订天数',
            align:"center",
            dataIndex: 'preday'
           },
		   {
            title: '是否是同住人 0 是同住人 1 不是同住人',
            align:"center",
            dataIndex: 'isflag'
           },
		   {
            title: 'pdf-name',
            align:"center",
            dataIndex: 'pdfname'
           },
		   {
            title: '头像url',
            align:"center",
            dataIndex: 'imgurl'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/zzj/checkIn/list",
          delete: "/zzj/checkIn/delete",
          deleteBatch: "/zzj/checkIn/deleteBatch",
          exportXlsUrl: "zzj/checkIn/exportXls",
          importExcelUrl: "zzj/checkIn/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>