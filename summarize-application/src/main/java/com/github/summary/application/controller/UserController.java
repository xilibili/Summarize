/*
 * Copyright (c) 2021, www.jd.com. All rights reserved.
 *
 * 警告：本计算机程序受著作权法和国际公约的保护，未经授权擅自复制或散布本程序的部分或全部、以及其他
 * 任何侵害著作权人权益的行为，将承受严厉的民事和刑事处罚，对已知的违反者将给予法律范围内的全面制裁。
 */
package com.github.summary.application.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author wangzhiqiang39
 * @version 1.0.0
 * @date 2021/10/13
 */
@Api(tags = "接口文档-用户")
@ApiSupport(author = "wangzhiqiang6@jd.com", order = 1)
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/{id}")
    @ApiOperation(value = "用户查询")
    public String getUser(@PathVariable String id) {
        return id;
    }
}
