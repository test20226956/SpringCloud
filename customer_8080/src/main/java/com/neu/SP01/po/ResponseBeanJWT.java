package com.neu.SP01.po;

public class ResponseBeanJWT extends ResponseBean{
    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public ResponseBeanJWT() {
    }

    public ResponseBeanJWT(Integer status, String msg, Object data, String jwt) {
        super(status, msg, data);
        this.jwt = jwt;
    }
}
