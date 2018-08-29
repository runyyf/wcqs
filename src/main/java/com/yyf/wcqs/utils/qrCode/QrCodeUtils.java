package com.yyf.wcqs.utils.qrCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * @Author: yuyf
 * @Description:
 * @Date: Created in 15:19 2018/8/29
 */
public class QrCodeUtils {
    public static void create(){
        String content="http://4yny3a.natappfree.cc/music/test.mp3";
        int width=100;
        int height=100;
        String format="png";
        // 定义二维码参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 字符集
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);// 纠错级别
        hints.put(EncodeHintType.MARGIN, 2);// 空白

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            Path file = new File("qrCode/img.png").toPath();

            MatrixToImageWriter.writeToPath(bitMatrix, format, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        create();
    }
}
