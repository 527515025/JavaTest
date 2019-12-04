package com.us.basics.http.logisticsMode;

import java.util.List;

/**
 * @author yyb
 * @time 2019/12/4
 */
public class Result {
    private String number;
    private String type;
    private String typename;
    private String logo;
    private List<ResultPoint> list;
    private String deliverystatus;
    private String issign;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<ResultPoint> getList() {
        return list;
    }

    public void setList(List<ResultPoint> list) {
        this.list = list;
    }

    public String getDeliverystatus() {
        return deliverystatus;
    }

    public void setDeliverystatus(String deliverystatus) {
        this.deliverystatus = deliverystatus;
    }

    public String getIssign() {
        return issign;
    }

    public void setIssign(String issign) {
        this.issign = issign;
    }
}
