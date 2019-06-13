package com.xjtu.dlc.autoqa.weixin.handler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class MenuHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        String msg = String.format("type:%s, event:%s, key:%s",
                wxMessage.getMsgType(), wxMessage.getEvent(),
                wxMessage.getEventKey());
        //type是view的是链接，直接打开就行。如果想执行什么也行
        if (WxConsts.BUTTON_VIEW.equals(wxMessage.getEvent())) {
            return null;
        }
        if (wxMessage.getEventKey().equals("V1001_KF")) {
            return WxMpXmlOutMessage.TEXT().content("将多源、片面、无序的碎片化知识聚合成符合人类认知学习特点的“知识森林”，缓解碎片化知识引发的学习迷航问题！\n <a href='http://yotta.xjtushilei.com:888/Yotta_frontend/index.html'>点击查看</a> /:8-) ")
                    .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                    .build();
        }
        if (wxMessage.getEventKey().equals("V1001_GOOD")) {
            return WxMpXmlOutMessage.TEXT().content("谢谢你的点赞！ /调皮")
                    .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                    .build();
        }
        return null;
    }

}
