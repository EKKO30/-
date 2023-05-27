import { createApp } from 'vue'
import { createPinia } from 'pinia'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from "axios";

import App from './App.vue'
import router from './router'

import 'element-plus/dist/index.css'

const app = createApp(App)

axios.defaults.baseURL= 'http://localhost:8080'

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(createPinia())
app.use(router)

app.mount('#app')
