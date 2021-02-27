package com.khumu.alimi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value = "/healthz", method = RequestMethod.GET)
    public String healthCheck() {
        logger.debug("health check");
        return "OK";
    }
}
