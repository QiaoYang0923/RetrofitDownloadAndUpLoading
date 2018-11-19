package com.qiaoyang.www.retrofitdownloadanduploading;

/**
 * 创建接收服务器返回数据的类
 *
 * @author Joe
 */
public class BaseBean {

    /**
     * Code : 1
     * Msg : 操作成功
     * Data : null
     */

    /**
     * 状态码
     */
    private int Code;
    /**
     * 提示信息
     */
    private String Msg;
    /**
     * 组合信息
     */
    private String Data;


    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "Code=" + Code +
                ", Msg='" + Msg + '\'' +
                ", Data='" + Data + '\'' +
                '}';
    }
}
