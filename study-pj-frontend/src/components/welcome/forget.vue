<template>
    <div style="margin: 30px 20px">
        <el-steps :active="active" finish-status="success" align-center>
            <el-step title="验证邮箱" />
            <el-step title="重置密码" />
        </el-steps>
    </div>

    <div style="text-align: center;margin:0 20px" v-if="active === 0">
        <div style="margin-top: 100px;font-size: 28px;font-weight: bold">验证邮箱</div>
        <div style="font-size: 17px;color: grey">请输入需要重置密码的电子邮件地址</div>

        <div style="margin-top: 30px">
            <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
                <el-form-item prop="email">
                    <el-input v-model="form.email"  type="email" placeholder="电子邮箱地址" style="margin-top: 10px">
                        <template #prefix>
                            <el-icon><Message /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="code">
                    <div style="margin-top: 10px">
                        <el-row :gutter="10" style="width: 100%">
                            <el-col :span="14" style="width: 100%;text-align: right" >
                                <el-input v-model="form.code" type="text"  placeholder="请输入验证码">
                                    <template #prefix>
                                        <el-icon><Edit /></el-icon>
                                    </template>
                                </el-input>
                            </el-col>
                            <el-col :span="10" style="text-align: right">
                                <el-button  type="success" @click="verifyEmail()"
                                            :disabled="!isEmailValid || coldTime>0 ">
                                    {{coldTime>0 ? '等待'+coldTime+'秒后重试' : '获取验证码' }}
                                </el-button>
                            </el-col>
                        </el-row>
                    </div>
                </el-form-item>
            </el-form>
            <div style="margin-top: 50px">
                <el-button @click="DoNext()"  style="width: 260px;" type="danger">重置密码</el-button>
            </div>
        </div>
    </div>

    <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
        <div style="text-align: center;margin:0 20px" v-if="active === 1">
            <div style="margin-top: 100px;font-size: 28px;font-weight: bold">重置密码</div>
            <div style="font-size: 17px;color: grey">请填写您的新密码</div>

            <div style="margin-top: 30px">
                <el-form-item prop="password">
                    <el-input v-model="form.password"  type="password"  placeholder="密码" style="margin-top: 10px" show-password>
                        <template #prefix>
                            <el-icon><Lock /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password_repeat">
                    <el-input  v-model="form.password_repeat" type="password" placeholder="重复密码" style="margin-top: 10px" show-password>
                        <template #prefix>
                            <el-icon><Lock /></el-icon>
                        </template>
                        <template #append>
                            <el-icon><Lock /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <div style="margin-top: 50px">
                    <el-button @click="DoReSet()" style="width: 260px;" type="danger">立即重置密码</el-button>
                </div>
            </div>
        </div>
    </el-form>

</template>

<script setup>

import {Edit, Lock, Message} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {post} from "@/net/axios";
import {ElMessage} from "element-plus";
import router from "@/router";

const formRef = ref()
const isEmailValid = ref(false)
const coldTime=ref(0)

const onValidate = (prop,isValid) =>{
    if(prop==='email'){
        isEmailValid.value = isValid
    }
}

const DoNext= () =>{
    formRef.value.validate((isValid)=>{
        if(isValid){
            post("api/auth/reset-verify",
                {email: form.email,
                    code: form.code},
                (message)=>{
                    active.value=1
                })
        }else {
            ElMessage.warning("请填写完整的信息")
        }
    })
}

const verifyEmail = () =>{
    coldTime.value=60;
    post("/api/auth/reset-SendEmail",
        {email: form.email},
        (message)=>{
            ElMessage.success(message);
            setInterval(() => coldTime.value--,1000)
        },()=>{
            coldTime.value=1;
        })
}

const DoReSet= () =>{
    post("api/auth/reset",
        {password: form.password},
        (message)=>{
            ElMessage.success(message)
            router.push('/')
        })
}

const form = reactive({
    password:'',
    password_repeat:'',
    email:'',
    code:''
})

const active = ref(0)

const validatePass = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== form.password) {
        callback(new Error("两次密码不一致"))
    } else {
        callback()
    }
}

const rules ={
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 2, max: 16, message: '密码长度只能在2-16中', trigger: 'blur' },
    ],
    password_repeat: [
        { validator: validatePass, trigger:'blur' },
    ],
    email: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { type:"email", message: '请输入合法的电子邮箱地址', trigger: ['change','blur'] }
    ],
    code: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
    ]
}

</script>

<style scoped>

</style>