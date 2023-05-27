<template>
    <div style="text-align: center;margin: 0 20px">
        <div style="margin-top: 150px;font-size: 28px;font-weight: bold">登录</div>
        <div style="font-size: 17px;color: grey">在进入系统之前请先登录或者注册</div>

        <div style="margin-top: 50px">
            <el-input v-model="form.username" type="text" placeholder="用户名/邮箱">
                <template #prefix>
                    <el-icon><User /></el-icon>
                </template>
            </el-input>
            <el-input  v-model="form.password" type="text" placeholder="密码" style="margin-top: 10px">
                <template #prefix>
                    <el-icon><Lock /></el-icon>
                </template>
            </el-input>
        </div>

        <el-row >
            <el-col :span="10" style="text-align: left">
                <el-checkbox v-model="form.remember" label="记住我" size="large"/>
            </el-col>

            <el-col :span="14" style="text-align: right" >
                <el-link @click="router.push('/forget')" style="margin-top: 10px">忘记密码?</el-link>
            </el-col>

        </el-row>

        <div style="margin-top: 40px">
            <el-button @click="login()" style="width: 260px" type="success" plain>登录</el-button>
        </div>
        <el-divider style="font-size: 10px">没有账号?</el-divider>
        <div style="margin-top: 25px">
            <el-button @click="register()" style="width: 260px" type="warning" plain>注册</el-button>
        </div>
    </div>
</template>

<script setup>
import {Lock, User} from "@element-plus/icons-vue";
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import {get, post} from "@/net/axios";
import router from "@/router";
import {useCounterStore} from "@/stores/counter";

const form=reactive({
    username: '',
    password: '',
    remember: false
})

const store = useCounterStore()

const login = () =>{
    if(!form.username || !form.password){
        ElMessage.warning("请输入用户名和密码!")
    }else {
        post('/api/auth/login',{
            username: form.username,
            password: form.password,
            remember: form.remember
        },(message)=>{
            ElMessage.success(message)
            get("/api/member/me",(message)=>{
                store.auth.user = message
                router.push('/index')
            },()=>{
                store.auth.user = null
            })
            router.push('/index')
        },() => {
            store.auth.user = null
        })
    }
}

const register = () =>{
    router.push('/register')
}

</script>

<style scoped>

</style>