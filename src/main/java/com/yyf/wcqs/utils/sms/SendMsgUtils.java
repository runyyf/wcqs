package com.yyf.wcqs.utils.sms;



import com.google.gson.JsonSyntaxException;
import com.yyf.wcqs.utils.CloseableUtil;
import com.yyf.wcqs.utils.EmptyUtils;
import com.yyf.wcqs.utils.IOUtils;
import com.yyf.wcqs.utils.MD5;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wu_bin
 * Date: 2017-03-27 15:29
 * Des: 短信发送工具类
 */
public class SendMsgUtils {
    private static final Logger logger = LoggerFactory.getLogger(SendMsgUtils.class);
    private static final String NAME = "短信发送工具类：";

    /**
     * 短信发送
     *
     * @param smsMob  手机号码
     * @param smsText 短信内容
     */
    public static boolean sendMsg(String smsMob, String smsText) {
        String url = "http://utf8.sms.webchinese.cn/";
        String uid = "38844996@qq.com";
        String key = "d41d8cd98f00b204e980";
        if (EmptyUtils.isEmpty(url) || EmptyUtils.isEmpty(uid) || EmptyUtils.isEmpty(key)) {
            return false;
        }
        //通信
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            //添加参数
            List<NameValuePair> params = new ArrayList<>();
            String md5Key = MD5.md5(key);
            params.add(new BasicNameValuePair("Uid", uid));
            params.add(new BasicNameValuePair("KeyMD5", md5Key.toUpperCase()));
            params.add(new BasicNameValuePair("smsMob", smsMob));
            params.add(new BasicNameValuePair("smsText", smsText));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            //增加请求配置
            httpPost.setConfig(buildRequestConfig());
            //执行Wap请求
            response = httpClient.execute(httpPost);
            //读取数据
            inputStream = response.getEntity().getContent();
            byte[] bytes = IOUtils.readFully(inputStream);
            //解码并转为json对象
            String result = new String(bytes, "UTF-8");
            if (EmptyUtils.isEmpty(result)) {
                return false;
            }
            if (Integer.parseInt(result) <= 0) {
                return false;
            }
            return true;
        } catch (JsonSyntaxException jse) {
            logger.error(NAME + "Wap通信接收到的json字符串转为WapResponse对象异常" + jse);
        } catch (SocketTimeoutException | ConnectTimeoutException | ConnectException connectExp) {
            logger.error(NAME + "Wap通信过程中超时异常");
        } catch (IOException ioe) {
            logger.error(NAME + "Wap通信过程中异常");
        } catch (RuntimeException re) {
            logger.error(NAME + "Wap通信发生运行时异常" + re.getMessage());
        } catch (Exception e) {
            logger.error(NAME + "Wap通信时发生普通异常:" + e.getMessage());
        } finally {
            CloseableUtil.close(inputStream, response, httpClient);
        }
        return false;
    }

    /**
     * 读取配置文件中的对Http通信的配置
     *
     * @return RequestConfig配置对象
     */
    private static synchronized RequestConfig buildRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(2000).build();
        return requestConfig;
    }
}
