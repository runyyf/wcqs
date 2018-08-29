package com.yyf.wcqs.utils.ffmpeg;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class FfmpegUtils {

    /**
     * 将amr文件输入转为mp3格式
     * @param
     * @return
     */
    public static InputStream amrToMP3(String amrPath,String name) {
        String ffmpegPath = getLinuxOrWindowsFfmpegPath();
        Runtime runtime = Runtime.getRuntime();
        try {

            URL url = Thread.currentThread().getContextClassLoader().getResource("static/music/");
            String mp3FilePath = url.getFile()+name + ".mp3";

            //执行ffmpeg文件，将amr格式转为mp3
            //filePath ----> amr文件在临时文件夹中的地址
            //mp3FilePath  ----> 转换后的mp3文件地址
            Process p = runtime.exec(ffmpegPath + "ffmpeg -i " + amrPath + " " + mp3FilePath);//执行ffmpeg.exe,前面是ffmpeg.exe的地址，中间是需要转换的文件地址，后面是转换后的文件地址。-i是转换方式，意思是可编码解码，mp3编码方式采用的是libmp3lame

            //释放进程
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.waitFor();

            File mp3File = new File(mp3FilePath);
            InputStream fileInputStream = new FileInputStream(mp3File);

            //应该在调用该方法的地方关闭该input流（使用完后），并且要删除掉临时文件夹下的相应文件
            /*File amrFile = new File(filePath);
            File mp3File = new File(mp3FilePath);
            if (amrFile.exists()) {
                boolean delete = amrFile.delete();
                System.out.println("删除源文件："+delete);
            }
            if (mp3File.exists()) {
                boolean delete = mp3File.delete();
                System.out.println("删除mp3文件："+delete);
            }*/

            return fileInputStream;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            runtime.freeMemory();
        }
        return null;
    }






    /**
     * 判断系统是Windows还是linux并且拼接ffmpegPath
     * @return
     */
    private static String getLinuxOrWindowsFfmpegPath() {
        String ffmpegPath = "";
        String osName = System.getProperties().getProperty("os.name");
        if (osName.toLowerCase().indexOf("linux") >= 0) {
            ffmpegPath = "";
        } else {
            URL url = Thread.currentThread().getContextClassLoader().getResource("ffmpeg/windows/");
            if (url != null) {
                ffmpegPath = url.getFile();
            }
        }
        return ffmpegPath;
    }
}
