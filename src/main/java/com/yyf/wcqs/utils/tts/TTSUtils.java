package com.yyf.wcqs.utils.tts;


import com.iflytek.cloud.speech.*;

/**
 * @Author: yuyf
 * @Description:
 * @Date: Created in 16:37 2018/8/29
 */
public class TTSUtils {
    public TTSUtils() {
        SpeechUtility.createUtility(SpeechConstant.APPID+"=5b8654b6");
    }

    private SpeechSynthesizer mySynListener = new SpeechSynthesizer() {
        @Override
        public void startSpeaking(String s, SynthesizerListener synthesizerListener) {

        }

        @Override
        public void synthesizeToUri(String s, String s1, SynthesizeToUriListener synthesizeToUriListener) {

        }

        @Override
        public void pauseSpeaking() {

        }

        @Override
        public void resumeSpeaking() {

        }

        @Override
        public void stopSpeaking() {

        }

        @Override
        public boolean isSpeaking() {
            return false;
        }

        @Override
        public boolean destroy() {
            return false;
        }
    };

    /**
     * @Author: yuyf
     * @Description: 生成语音文件
     * @Date: Created in 16:51 2018/8/29
    */
    public void createPcm(){
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer();
        mTts.setParameter(SpeechConstant.VOICE_NAME,"xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED,"50");
        mTts.setParameter(SpeechConstant.VOLUME,"80");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,"./tts_test.pcm");
//        mTts.startSpeaking("语音合成测试",mySynListener);
    }


}
