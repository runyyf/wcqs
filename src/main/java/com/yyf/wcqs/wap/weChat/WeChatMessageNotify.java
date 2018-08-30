package com.yyf.wcqs.wap.weChat;

/*
 * 文本消息
<xml>
 <ToUserName><![CDATA[toUser]]></ToUserName>
 <FromUserName><![CDATA[fromUser]]></FromUserName>
 <CreateTime>1348831860</CreateTime>
 <MsgType><![CDATA[text]]></MsgType>
 <Content><![CDATA[this is a test]]></Content>
 <MsgId>1234567890123456</MsgId>
 </xml>
 *
 */
/**
 *		 参数				描述
 *	ToUserName		开发者微信号
 *	FromUserName	发送方帐号（一个OpenID）
 * 	CreateTime		消息创建时间 （整型）
 *	MsgType			text
 *	Content			文本消息内容
 *	MsgId			消息id，64位整型
 *
 */

public class WeChatMessageNotify {
    private String ToUserName;
    private String FromUserName;
    private long CreateTime;
    private String MsgType;
    private String Content;
    private String MsgId;
    private String MediaId;
    public String getToUserName() {
        return ToUserName;
    }
    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }
    public String getFromUserName() {
        return FromUserName;
    }
    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }
    public long getCreateTime() {
        return CreateTime;
    }
    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }
    public String getMsgType() {
        return MsgType;
    }
    public void setMsgType(String msgType) {
        MsgType = msgType;
    }
    public String getContent() {
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }
    public String getMsgId() {
        return MsgId;
    }
    public void setMsgId(String msgId) {
        MsgId = msgId;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}
