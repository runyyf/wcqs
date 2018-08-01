package com.yyf.wcqs.cache;


import com.google.gson.Gson;
import com.yyf.wcqs.utils.CloseableUtil;
import com.yyf.wcqs.utils.EmptyUtils;
import com.yyf.wcqs.utils.IOUtils;
import com.yyf.wcqs.wap.weChat.WeChatBaseApiResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.InputStream;
import java.util.List;

/**
 * Created by hcx on 2017/4/1.
 * 微信公众号缓存
 */
public class WeChatCache {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static volatile WeChatCache weChatCache;//单例对象
    private static final String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String jsApiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    private static final String getCallBackIpUrl = "https://api.weixin.qq.com/cgi-bin/getcallbackip";

    private String appId;//公众号ID
    private String mchID;//微信支付分配的商户号ID
    private String secret;//公众号安全码
    private String accessToken;//凭证
    private String jsApiTicket;//票据
    private List<String> ipList;//ip列表

    private WeChatCache() {
//        this.appId = soSetting.getValue(SettingConst.UNIQUE_KEY_WE_APP_ID);
//        this.mchID = soSetting.getValue(SettingConst.UNIQUE_KEY_WE_MCH_ID);
//        this.secret = soSetting.getValue(SettingConst.UNIQUE_KEY_WE_SECRET);

        //个人微信号
        this.appId  = "wx89a915bc3f41c8bf";
        this.secret = "9a9f671b1c24f43809f69c5ba1134650";

    }

    public static WeChatCache getInstance() {
        if (weChatCache == null) {
            synchronized (WeChatCache.class) {
                if (weChatCache == null) {
                    weChatCache = new WeChatCache();
                }
            }
        }
        return weChatCache;
    }

    /**
     * 刷新accessToken
     */
    private void flushAccessToken() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        try {
            String url = accessTokenUrl + "?grant_type=client_credential&appid=" + appId + "&secret=" + secret;
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(2000).build();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            inputStream = response.getEntity().getContent();
            byte[] bytes = IOUtils.readFully(inputStream);
            String result = new String(bytes);
            WeChatBaseApiResponse weChatBaseApiResponse = new Gson().fromJson(result, WeChatBaseApiResponse.class);
            if (weChatBaseApiResponse != null && weChatBaseApiResponse.getErrcode() == null) {
                accessToken = weChatBaseApiResponse.getAccess_token();
                logger.info("刷新access_token[" + accessToken + "]");
            }
        } catch (Exception e) {
            logger.error("获取access_token出错");
        } finally {
            CloseableUtil.close(inputStream, response, httpClient);
        }
    }

    /**
     * 刷新jsApiTicket
     */
    private void flushJsApiTicket() {
        if (accessToken == null) {
            logger.error("获取jsApiTicket时缺少accessToken参数");
            return;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        try {
            String url = jsApiTicketUrl + "?access_token=" + accessToken + "&type=jsapi";
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(2000).build();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            inputStream = response.getEntity().getContent();
            byte[] bytes = IOUtils.readFully(inputStream);
            String result = new String(bytes);
            WeChatBaseApiResponse weChatBaseApiResponse = new Gson().fromJson(result, WeChatBaseApiResponse.class);
            if (weChatBaseApiResponse != null && weChatBaseApiResponse.getErrcode() == 0) {
                jsApiTicket = weChatBaseApiResponse.getTicket();
                logger.info("刷新jsApiTicket[" + jsApiTicket + "]");
            }
        } catch (Exception e) {
            logger.error("获取jsApiTicket出错");
        } finally {
            CloseableUtil.close(inputStream, response, httpClient);
        }
    }

    /**
     * 刷新ipList
     */
    private void flushIpList() {
        if (accessToken == null) {
            logger.error("获取ipList时缺少accessToken参数");
            return;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        try {
            String url = getCallBackIpUrl + "?access_token=" + accessToken;
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(2000).build();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            inputStream = response.getEntity().getContent();
            byte[] bytes = IOUtils.readFully(inputStream);
            String result = new String(bytes);
            WeChatBaseApiResponse weChatBaseApiResponse = new Gson().fromJson(result, WeChatBaseApiResponse.class);
            if (weChatBaseApiResponse != null && weChatBaseApiResponse.getErrcode() == null) {
                ipList = weChatBaseApiResponse.getIp_list();
                logger.info("刷新ipList:" + ipList);
            }
        } catch (Exception e) {
            logger.error("获取ipList出错");
        } finally {
            CloseableUtil.close(inputStream, response, httpClient);
        }
    }

    /**
     * 刷新所有
     */
    public void flushAll() {
        flushAccessToken();
        flushJsApiTicket();
        flushIpList();
    }

    public String getAppId() {
        return appId;
    }

    public String getMchID() {
        return mchID;
    }

    public String getSecret() {
        return secret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getJsApiTicket() {
        return jsApiTicket;
    }

    public List<String> getIpList() {
        return ipList;
    }

    /**
     * ip地址是否在IP列表中
     *
     * @param ip
     * @return
     */
    public boolean isInIpList(String ip) {
        if (EmptyUtils.isEmpty(ipList)) {
            return false;
        }
        for (String ipPattern : ipList) {
            if (isInRange(ip, ipPattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个IP地址是否在一个IP规则中
     *
     * @param ip
     * @param ipPattern
     * @return
     */
    private boolean isInRange(String ip, String ipPattern) {
        if (!ipPattern.contains("/")) {
            return ip.equals(ipPattern);
        }
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
                | (Integer.parseInt(ips[1]) << 16)
                | (Integer.parseInt(ips[2]) << 8)
                | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(ipPattern.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String patternIp = ipPattern.replaceAll("/.*", "");
        String[] patternIps = patternIp.split("\\.");
        int patternIpAddr = (Integer.parseInt(patternIps[0]) << 24)
                | (Integer.parseInt(patternIps[1]) << 16)
                | (Integer.parseInt(patternIps[2]) << 8)
                | Integer.parseInt(patternIps[3]);
        return (ipAddr & mask) == (patternIpAddr & mask);
    }
}
