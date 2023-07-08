package com.shagtv.contentcalendar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(value = "cc")
public record ContentCalendarProperties(
        Integer title,
        List<String> about
) {
}
