package com.yyf.wcqs.cache;


import com.google.gson.Gson;
import com.yyf.wcqs.configure.SystemConfig;
import com.yyf.wcqs.utils.CloseableUtil;
import com.yyf.wcqs.utils.EmptyUtils;
import com.yyf.wcqs.utils.IOUtils;
import com.yyf.wcqs.utils.ffmpeg.FfmpegUtils;
import com.yyf.wcqs.wap.weChat.WeChatBaseApiResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private static final String getMediaUrl = "https://api.weixin.qq.com/cgi-bin/media/get";
    private static final String uploadFileUrl = "https://api.weixin.qq.com/cgi-bin/media/upload";

    private String appId;//公众号ID
    private String mchID;//微信支付分配的商户号ID
    private String secret;//公众号安全码
    private String accessToken;//凭证
    private String jsApiTicket;//票据
    private List<String> ipList;//ip列表

    private String domainUrl;//域名
    private String nginxPort;//文件访问端口
    private String voicePath;//voice存放路径
    private String mp3Path;//mp3存放路径
    private String qrCodePath;//qrCode存放路径

    private WeChatCache() {
//        this.appId = soSetting.getValue(SettingConst.UNIQUE_KEY_WE_APP_ID);
//        this.mchID = soSetting.getValue(SettingConst.UNIQUE_KEY_WE_MCH_ID);
//        this.secret = soSetting.getValue(SettingConst.UNIQUE_KEY_WE_SECRET);

        //个人微信号
        this.appId  = "wx89a915bc3f41c8bf";
        this.secret = "9a9f671b1c24f43809f69c5ba1134650";

        //读取配置文件
        domainUrl = SystemConfig.getProperty("path.domain.url");
        voicePath = SystemConfig.getProperty("path.file.voice");
        qrCodePath = SystemConfig.getProperty("path.file.qrCode");
        mp3Path = SystemConfig.getProperty("path.file.mp3");
        nginxPort = SystemConfig.getProperty("path.nginx.port");

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
     *
     * 功能描述:  下载媒体资源
     *
     * @param:
     * @return:
     * @auther: yyf
     * @date: 2018/8/29 22:04
     */
    public String getMediaResourceUrl(String mediaId,String name){
        if (accessToken == null) {
            logger.error("获取MediaResource时缺少accessToken参数");
            return null;
        }
        if (mediaId == null) {
            logger.error("获取MediaResource时缺少mediaId参数");
            return null;
        }
        System.out.println(accessToken);
        System.out.println(mediaId);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        try {
            String url = getMediaUrl + "?access_token=" + accessToken+"&media_id=" + mediaId;
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(2000).build();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            inputStream = response.getEntity().getContent();
            byte[] bytes = IOUtils.readFully(inputStream);

            //保存原始amr 文件
            String amrFileName = voicePath+"//"+name+".amr";
            File mediaFile = new File(amrFileName);
            FileOutputStream outStream = new FileOutputStream(mediaFile);
            outStream.write(bytes);
            outStream.close();

            //amr文件转化为mp3文件
            String mp3FileName = mp3Path+"/"+name+".mp3";
            FfmpegUtils.amrToMP3(amrFileName,mp3FileName);
            return domainUrl+":"+nginxPort+"/mp3/"+name+".mp3";

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取MediaResource出错");
        } finally {
            CloseableUtil.close(inputStream, response, httpClient);
        }
        return null;
    }


    public String uploadFile(File file){
        StringBuffer buffer = new StringBuffer();

        try{
            //1.建立连接
            URL url = new URL(uploadFileUrl+"?access_token=" + accessToken+"&type=image");
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  //打开链接

            //1.1输入输出设置
            httpUrlConn.setDoInput(true);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setUseCaches(false); // post方式不能使用缓存
            //1.2设置请求头信息
            httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("Charset", "UTF-8");
            //1.3设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            httpUrlConn.setRequestProperty("Content-Type","multipart/form-data; boundary="+ BOUNDARY);

            // 请求正文信息
            // 第一部分：
            //2.将文件头输出到微信服务器
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length()
                    + "\";filename=\""+ file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            OutputStream outputStream = new DataOutputStream(httpUrlConn.getOutputStream());
            // 将表头写入输出流中：输出表头
            outputStream.write(head);

            //3.将文件正文部分输出到微信服务器
            // 把文件以流文件的方式 写入到微信服务器中
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                outputStream.write(bufferOut, 0, bytes);
            }
            in.close();
            //4.将结尾部分输出到微信服务器
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            outputStream.write(foot);
            outputStream.flush();
            outputStream.close();

            //5.将微信服务器返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();


        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            return null;
        }

        WeChatBaseApiResponse weChatBaseApiResponse = new Gson().fromJson(buffer.toString(), WeChatBaseApiResponse.class);
        if (weChatBaseApiResponse != null && weChatBaseApiResponse.getErrcode() == null) {
            return weChatBaseApiResponse.getMedia_id();
        }

        return null;
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
