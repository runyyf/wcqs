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

    private  SynthesizeToUriListener mySynListener = new SynthesizeToUriListener() {
        @Override
        public void onBufferProgress(int i) {

        }

        @Override
        public void onSynthesizeCompleted(String s, SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, int i3, Object o, Object o1) {

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
        mTts.synthesizeToUri("语音合成测试","./tts_test.pcm",mySynListener);
    }

    public void pcmToWav(){

    }

    public void wavToMp3(){

    }

    public static void main(String args[]){
        new TTSUtils().createPcm();
    }
}
