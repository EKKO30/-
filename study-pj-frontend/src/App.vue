<script setup>
import {get} from "@/net/axios";
import {ElMessage} from "element-plus";
import {useCounterStore} from "@/stores/counter";
import router from "@/router";

const store = useCounterStore()

if(store.auth.user == null){
    get("/api/member/me",(message)=>{
        console.log(message)
        store.auth.user = message
        router.push('/index')
        console.log(store.auth.user)
    },()=>{
        ElMessage.error('未登录')
        console.log(store.auth.user)
        store.auth.user = null
    })
}


</script>

<template>
  <router-view/>
</template>

<style scoped>

</style>
