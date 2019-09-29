<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="身份证">
          <a-input placeholder="请输入身份证" v-decorator="['idnumber', validatorRules.idnumber ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="客户姓名">
          <a-input placeholder="请输入客户姓名" v-decorator="['name', validatorRules.name ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="性别">
          <a-input placeholder="请输入性别" v-decorator="['gender', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="出生日期">
          <a-input placeholder="请输入出生日期" v-decorator="['birthday', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="民族">
          <a-input placeholder="请输入民族" v-decorator="['national', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="身份证住址">
          <a-input placeholder="请输入身份证住址" v-decorator="['idaddress', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="手机号">
          <a-input placeholder="请输入手机号" v-decorator="['phonenum', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="房间号">
          <a-input placeholder="请输入房间号" v-decorator="['roomnum', validatorRules.roomnum ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="真实入住时间">
          <a-input placeholder="请输入真实入住时间" v-decorator="['truecreatetime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="真实离店时间">
          <a-input placeholder="请输入真实离店时间" v-decorator="['trueupdatetime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单号">
          <a-input placeholder="请输入订单号" v-decorator="['orderid', validatorRules.orderid ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="预订天数">
          <a-input placeholder="请输入预订天数" v-decorator="['preday', validatorRules.preday ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="默认0 未入住 1 已入住 2离店">
          <a-input placeholder="请输入默认0 未入住 1 已入住 2离店" v-decorator="['state', validatorRules.state ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="是否是同住人 0 是同住人 1 不是同住人">
          <a-input placeholder="请输入是否是同住人 0 是同住人 1 不是同住人" v-decorator="['isflag', validatorRules.isflag ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="pdf-name">
          <a-input placeholder="请输入pdf-name" v-decorator="['pdfname', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="头像url">
          <a-input placeholder="请输入头像url" v-decorator="['imgurl', {}]" />
        </a-form-item>
		
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "CheckoutModal",
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
        idnumber:{rules: [{ required: true, message: '请输入身份证!' }]},
        name:{rules: [{ required: true, message: '请输入客户姓名!' }]},
        roomnum:{rules: [{ required: true, message: '请输入房间号!' }]},
        orderid:{rules: [{ required: true, message: '请输入订单号!' }]},
        preday:{rules: [{ required: true, message: '请输入预订天数!' }]},
        state:{rules: [{ required: true, message: '请输入默认0 未入住 1 已入住 2离店!' }]},
        isflag:{rules: [{ required: true, message: '请输入是否是同住人 0 是同住人 1 不是同住人!' }]},
        },
        url: {
          add: "/zzj/checkout/add",
          edit: "/zzj/checkout/edit",
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
          this.form.setFieldsValue(pick(this.model,'idnumber','name','gender','birthday','national','idaddress','phonenum','roomnum','truecreatetime','trueupdatetime','orderid','preday','state','isflag','pdfname','imgurl'))
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

</style>