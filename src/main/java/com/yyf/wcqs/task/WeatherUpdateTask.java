package com.yyf.wcqs.task;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yyf.wcqs.domain.Weather;
import com.yyf.wcqs.repository.WeatherRepository;
import com.yyf.wcqs.utils.CloseableUtil;
import com.yyf.wcqs.utils.IOUtils;
import com.yyf.wcqs.utils.SpringToolUtils;
import com.yyf.wcqs.wap.weChat.WeChatBaseApiResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 天气请求任务，12小时更新一次
 * */
public class WeatherUpdateTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    //服务类
    private WeatherRepository weatherRepository;

    public WeatherUpdateTask() {
        weatherRepository = (WeatherRepository)SpringToolUtils.getBean("weatherRepository");
    }

    public void task(){
        logger.info("开始更新天气信息");
        List<Weather> weatherList = weatherRepository.getAll();
        try {
            for (Weather weather : weatherList){
                request(weather);
                //10s
                Thread.sleep(10000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void request(Weather weather){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        try {
            String url = "https://www.sojson.com/open/api/weather/json.shtml?city="+weather.getCityName();
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(2000).build();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            inputStream = response.getEntity().getContent();
            byte[] bytes = IOUtils.readFully(inputStream);
            String result = new String(bytes);
            logger.info(result);
            JSONObject jsonObject  = JSONObject.parseObject(result);
            JSONObject jsonObjectData = (JSONObject) jsonObject.get("data");

            String temperature = (String) jsonObjectData.get("wendu");
            String humidity = (String) jsonObjectData.get("shidu");
            String ganmao = (String) jsonObjectData.get("ganmao");
            JSONArray forecast = (JSONArray)jsonObjectData.get("forecast");
            JSONObject today = (JSONObject)forecast.get(0);

            weather.setTemperature(temperature);
            weather.setHumidity(humidity);
            weather.setGanmao(ganmao);
            weather.setHighTemperature((String) today.get("high"));
            weather.setLowTemperature((String) today.get("low"));
            weather.setFx((String) today.get("fx"));
            weather.setFl((String) today.get("fl"));
            weather.setTypeDesc((String) today.get("type"));
            weather.setNotice((String) today.get("notice"));
            weather.setDateUpdate(new Date());
            weatherRepository.update(weather);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("请求天气异常"+e.getMessage());
        } finally {
            CloseableUtil.close(inputStream, response, httpClient);
        }

    }



}
