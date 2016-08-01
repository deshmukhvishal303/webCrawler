package com.web.crawler.dto;

/**
 * Created by vishal.deshmukh on 28/07/16.
 */
public class CrawlerInputDto {
    private String siteUrl;
    private Integer timeout;

    public CrawlerInputDto(String siteUrl, Integer timeout){
        this.siteUrl = siteUrl;
        this.timeout = timeout;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public Integer getTimeout() {
        return timeout;
    }
}
