//封装axios
import axios from "axios";
import {ElMessage} from "element-plus";

axios.defaults.withCredentials = true

const defaultError = () => {
    ElMessage.error("发生了一些错误,请联系管理员")
    console.log(onerror)
}
const defaultFailure = (message) =>ElMessage.warning(message)

function post(url,data,success,failure=defaultFailure,error=defaultError){
    axios.post(url,data,{
        headers:{
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        },
        //携带cookie
        withCredentials:true
    }).then((res) => {
        if (res.data.flag){
            success(res.data.data, res.data.status)
        } else{
            failure(res.data.data, res.data.status)
        }
    }).catch(error)
}

function get(url,success,failure=defaultFailure,error=defaultError){
    axios.get(url,{
        //携带cookie
        withCredentials:true
    }).then((res) => {
        if (res.data.flag){
            success(res.data.data, res.data.status)
        } else{
            failure(res.data.data, res.data.status)
        }
    }).catch(error)
}

//将这两个方法导出，以便让其他方法使用
export {get,post}








