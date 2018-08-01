package com.yyf.wcqs.controller;

import com.yyf.wcqs.domain.Weather;
import com.yyf.wcqs.repository.WeatherRepository;
import com.yyf.wcqs.utils.EmptyUtils;
import com.yyf.wcqs.utils.weChat.MessageUtils;
import com.yyf.wcqs.utils.weChat.WeChatCheckoutUtil;
import com.yyf.wcqs.wap.weChat.WeChatMessageNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@Controller
public class WeChatAnswerController {

    @Autowired
    private WeatherRepository weatherRepository;

    /**
     *
     * 功能描述:  接受微信用户发送的消息
     *
     * @param: [req, resp]
     * @return: void
     * @auther: yyf
     * @date: 2018/7/31 23:21
     */
    @RequestMapping(value = "/hello",method = RequestMethod.POST)
    public void receive(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out =resp.getWriter();
        try {
            Map<String, String> map= MessageUtils.xmlToMap(req);
            String toUserName=map.get("ToUserName");
            String fromUserName=map.get("FromUserName");
            String msgType=map.get("MsgType");
            String content=map.get("Content");

            String message=null;
            if("text".equals(msgType)){
                //	System.out.println("text.equals(msgType)");
                WeChatMessageNotify text =new WeChatMessageNotify();
                text.setFromUserName(toUserName);
                text.setToUserName(fromUserName);
                text.setMsgType("text");

                //这里填写回复内容
                Weather weather = weatherRepository.getByCityName(content);
                if (EmptyUtils.isNotEmpty(weather)){
                    text.setContent(weather.toString());
                }else if (content.equals("卞蒙丹")){
                    text.setContent("你家峰峰哥哥很喜欢你");
                }else{
                    text.setContent("hello");
                }
                text.setCreateTime(new Date().getTime());
                message=MessageUtils.textMessageToXml(text);
            }
            //	System.out.println(message);
            out.print(message);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            out.close();
        }

    }



    /**
     * 微信消息接收和token验证
     *
     * @param model
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public void check(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException{
        PrintWriter print;
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (signature != null && WeChatCheckoutUtil.checkSignature(signature, timestamp, nonce)) {
            try {
                print = response.getWriter();
                print.write(echostr);
                print.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
