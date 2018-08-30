package com.yyf.wcqs.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author: yuyf
 * @Description:
 * @Date: Created in 14:50 2018/8/30
 */
@Configuration
public class WebResourceConfig extends WebMvcConfigurerAdapter {

    @Value("${path.file.mp3}")
    private String mp3FilePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/myMp3/**").addResourceLocations("file:"+mp3FilePath);
        super.addResourceHandlers(registry);
    }
}
