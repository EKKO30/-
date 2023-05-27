<template>
    <div style="text-align: center;margin: 0 20px">
        <div style="margin-top: 150px;font-size: 28px;font-weight: bold">注册</div>
        <div style="font-size: 17px;color: grey">没有账号请先注册新用户</div>

        <div style="margin-top: 40px">
            <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
                <el-form-item prop="username">
                    <el-input v-model="form.username" type="text" placeholder="用户名">
                        <template #prefix>
                            <el-icon><User /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
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
                <el-form-item prop="email">
                    <el-input v-model="form.email"  type="email" placeholder="电子邮箱地址" style="margin-top: 10px">
                        <template #prefix>
                            <el-icon><Message /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="code">
                    <div style="margin-top: 10px">
                        <el-row :gutter="20" style="width: 100%">
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
        </div>

        <div>
            <el-button @click="register()" style="margin-top: 50px;width: 260px" type="warning" plain>立即注册</el-button>
        </div>

        <div style="font-size: 20px">
            <el-link type="primary" @click="router.push('/')">已有账号?立即登录</el-link>
        </div>
    </div>

</template>

<script setup>

import {Edit, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net/axios";


const form = reactive({
    username:'',
    password:'',
    password_repeat:'',
    email:'',
    code:''
})

const validateUsername = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入用户名'))
    } else if(! /^[a-zA-Z\u4e00-\u9fa5]+$/.test(value)){
        callback(new Error('用户名只能是中文/英文,不能包含特殊字符'))
    }else {
        callback();
    }
}

const validatePass2 = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== form.password) {
        callback(new Error("两次密码不一致"))
    } else {
        callback()
    }
}

const formRef = ref()
const isEmailValid = ref(false)
const coldTime=ref(0)

const onValidate = (prop,isValid) =>{
    if(prop==='email'){
        isEmailValid.value = isValid
    }
}

const register= () =>{
    formRef.value.validate((isValid)=>{
        if(isValid){
            post("api/auth/register",
                {username: form.username,
                      password: form.password,
                      email: form.email,
                      code: form.code},
                (message)=>{
                    ElMessage.success(message)
                    router.push('/')
                })
        }else {
            ElMessage.warning("请填写完整的注册信息")
        }
    })
}

const verifyEmail = () =>{
    coldTime.value=60;
    post("/api/auth/verifyEmail",
        {email: form.email},
        (message)=>{
        ElMessage.success(message);
        setInterval(() => coldTime.value--,1000)
    },(message)=>{
            ElMessage.error(message);
            coldTime.value=1;
        })
}

const rules ={
    username: [
        { validator: validateUsername, trigger: ['change','blur'] },
        { min: 2, max: 9, message: '用户名长度只能在2-9中', trigger: 'blur' },
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 2, max: 16, message: '密码长度只能在2-16中', trigger: 'blur' },
    ],
    password_repeat: [
        { validator: validatePass2, trigger: 'blur' },
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














