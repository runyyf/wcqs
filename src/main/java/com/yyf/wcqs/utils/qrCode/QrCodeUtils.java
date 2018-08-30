package com.yyf.wcqs.utils.qrCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yyf.wcqs.configure.SystemConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * @Author: yuyf
 * @Description:
 * @Date: Created in 15:19 2018/8/29
 */
public class QrCodeUtils {

    public static void create(String content,String name){
        int width=150;
        int height=150;
        String format="png";
        // 定义二维码参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 字符集
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);// 纠错级别
        hints.put(EncodeHintType.MARGIN, 2);// 空白

        String qrCodePath = SystemConfig.getProperty("path.file.qrCode");

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            String filePath = qrCodePath+"/"+name+".png";
            Path file = new File(filePath).toPath();

            MatrixToImageWriter.writeToPath(bitMatrix, format, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        create("http://www.baidu.com","123");
    }
}
