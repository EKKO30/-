<template>

  <!--设置一个单元,此单元大小为全屏，超出部分不显示，布局方式为flex-->
    <div style="width :100vw;height: 100vh;overflow: hidden;display: flex">
        <!--设置一个子单元放置背景图,布局方式为flex:1即为自动填充-->
        <div style="flex: 1" >
            <el-image style="object-fit: cover;width: 100%;height: 100%"
                      src="https://ts1.cn.mm.bing.net/th/id/R-C.3b9e28c56a16ceca8989bd5f0ad0d078?rik=MyozM1fwLD6h2A&riu=http%3a%2f%2fa3.att.hudong.com%2f05%2f22%2f19300534866937136480227539173.jpg&ehk=9zTwAYd%2fElyEMojd3bXEPmSyf1175HU2apusTReMc0Q%3d&risl=&pid=ImgRaw&r=0"/>
        </div>
        <div class="welcome-title">
            <div style="font-weight: bold;font-size: 20px">欢迎加入我们的学习论坛</div>
            <div style="margin-top: 10px;font-style: italic">在这里你可以与五湖四海的人交流学习心得,互帮互助</div>
        </div>
        <!--设置一个子单元用于登录或者注册,布局方式为300px宽度-->
        <div style="width: 400px;background-color: white;z-index: 1">
            <router-view v-slot="{ Component }">
                <transition name="el-fade-in-linear" mode="out-in">
                    <component :is="Component" />
                </transition>
            </router-view>
        </div>
    </div>
</template>

<script setup>


</script>

<style scoped>
.welcome-title{
    position: absolute;
    bottom: 30px;
    left: 30px;
    color: white;
    text-shadow: 0 0 10px black;
}
</style>