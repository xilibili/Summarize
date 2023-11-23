/*
 * Copyright (c) 2021, www.jd.com. All rights reserved.
 *
 * 警告：本计算机程序受著作权法和国际公约的保护，未经授权擅自复制或散布本程序的部分或全部、以及其他
 * 任何侵害著作权人权益的行为，将承受严厉的民事和刑事处罚，对已知的违反者将给予法律范围内的全面制裁。
 */
package com.github.summary.application.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author wangzhiqiang39
 * @version 1.0.0
 * @date 2021/10/13
 */
@Slf4j
@Configuration
public class ApplicationConfig {

    // /**
    //  * FastJson替换Jackson
    //  *
    //  * @return HttpMessageConverters
    //  */
    // @Bean
    // public HttpMessageConverters fastJsonConverters() {
    //     FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    //     FastJsonConfig config = new FastJsonConfig();
    //     config.setSerializerFeatures(SerializerFeature.PrettyFormat);
    //     converter.setFastJsonConfig(config);
    //     List<MediaType> mediaTypeList = new ArrayList<>();
    //     mediaTypeList.add(MediaType.APPLICATION_JSON);
    //     converter.setSupportedMediaTypes(mediaTypeList);
    //     return new HttpMessageConverters(converter);
    // }
}
