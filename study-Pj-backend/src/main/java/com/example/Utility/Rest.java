package com.example.Utility;

import lombok.Data;

//返回的json信息格式
@Data
public class Rest {
    private int status;
    private boolean flag;
    private Object data;

    private Rest(int status, boolean flag, Object data) {
        this.status = status;
        this.flag = flag;
        this.data = data;
    }

    public static Rest RestMake(int status, boolean flag, Object data){
        return new Rest(status,flag,data);
    }

    public static Rest Success(Object data){
        return new Rest(200,true,data);
    }

    public static Rest Failure(Object data){
        return new Rest(402,false,data);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
