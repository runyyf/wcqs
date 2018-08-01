package com.yyf.wcqs.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather {
    private Integer id;
    //城市名称
    private String cityName;
    //最高温度
    private String highTemperature;
    //最低温度
    private String lowTemperature;
    //风向
    private String fx;
    //
    private String typeDesc;
    //风力
    private String fl;
    //注意事项
    private String notice;
    //感冒指数
    private String ganmao;
    //温度
    private String temperature;
    //湿度
    private String humidity;

    private Date dateCreate;
    private Date dateUpdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(String highTemperature) {
        this.highTemperature = highTemperature;
    }

    public String getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(String lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return cityName+"  今日温度:"+ temperature +"℃\n"+highTemperature+","+lowTemperature+
                ",湿度: "+humidity+"\n"+typeDesc+","+fx+fl+","+notice+"\n健康提醒:"+ganmao+"\n更新时间："+
                formatter.format(dateUpdate);
    }
}
