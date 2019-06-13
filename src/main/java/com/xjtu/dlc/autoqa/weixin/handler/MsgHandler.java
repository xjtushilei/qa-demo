package com.xjtu.dlc.autoqa.weixin.handler;


import com.xjtu.dlc.autoqa.qa.answer.CosineSimilarAlgorithmHandler;
import com.xjtu.dlc.autoqa.weixin.builder.TextBuilder;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.xjtu.dlc.autoqa.config.DataConfig.getExcelDataMap;


@Component
public class MsgHandler extends AbstractHandler {


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT)) {
            //TODO 可以选择将消息保存到本地
        }
        String input = null;
        if (wxMessage.getMsgType().equals(WxConsts.XML_MSG_VOICE)) {
            input = wxMessage.getRecognition();
        } else if (wxMessage.getMsgType().equals(WxConsts.XML_MSG_TEXT)) {
            input = wxMessage.getContent();
        }
        // 用户id
        String userID = wxMessage.getFromUser();

        //TODO 组装回复消息
//        String content = "收到了:" + input ;
        String content = CosineSimilarAlgorithmHandler.answer(input, getExcelDataMap());

        return new TextBuilder().build(content, wxMessage, weixinService);

    }

}
