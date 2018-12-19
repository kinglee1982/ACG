package com.kcx.acg.base;

/**
 * 回调信息统一封装类
 * Created by jb.
 */
public class BaseResultEntity<T> {
    //  判断标示
    private int ErrorCode;
    //    提示信息
    private String ErrorMsg;
    //显示数据（用户需要关心的数据）
    private T ReturnData;

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public T getReturnData() {
        return ReturnData;
    }

    public void setReturnData(T returnData) {
        ReturnData = returnData;
    }
}
