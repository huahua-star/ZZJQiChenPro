<template>
  <a-drawer
      :title="title"
      :width="800"
      placement="right"
      :closable="false"
      @close="close"
      :visible="visible"
  >

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单号">
          <a-input placeholder="请输入订单号" v-decorator="['orderid', validatorRules.orderid ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="支付方式 0银行卡 1支付宝 2微信">
          <a-input placeholder="请输入支付方式 0银行卡 1支付宝 2微信" v-decorator="['paymethod', validatorRules.paymethod ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="支付类型 0 普通支付 1预授权支付">
          <a-input placeholder="请输入支付类型 0 普通支付 1预授权支付" v-decorator="['paytype', validatorRules.paytype ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="消费金额">
          <a-input-number v-decorator="[ 'amount', validatorRules.amount ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="预授权金额">
          <a-input-number v-decorator="[ 'preamount', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="状态 0 撤销 1 未支付 2 已支付">
          <a-input placeholder="请输入状态 0 撤销 1 未支付 2 已支付" v-decorator="['state', validatorRules.state ]" />
        </a-form-item>
		
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "TblTxnpModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
        orderid:{rules: [{ required: true, message: '请输入订单号!' }]},
        paymethod:{rules: [{ required: true, message: '请输入支付方式 0银行卡 1支付宝 2微信!' }]},
        paytype:{rules: [{ required: true, message: '请输入支付类型 0 普通支付 1预授权支付!' }]},
        amount:{rules: [{ required: true, message: '请输入消费金额!' }]},
        state:{rules: [{ required: true, message: '请输入状态 0 撤销 1 未支付 2 已支付!' }]},
        },
        url: {
          add: "/zzj/tblTxnp/add",
          edit: "/zzj/tblTxnp/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'orderid','paymethod','paytype','amount','preamount','state'))
		  //时间格式化
        });

      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化
            
            console.log(formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })



          }
        })
      },
      handleCancel () {
        this.close()
      },


    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>