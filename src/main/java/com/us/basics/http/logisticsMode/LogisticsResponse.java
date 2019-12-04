package com.us.basics.http.logisticsMode;

/**
 * @author yyb
 * @time 2019/12/4
 */
public class LogisticsResponse {
    private Integer status;
    private String msg;
    private Result result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
