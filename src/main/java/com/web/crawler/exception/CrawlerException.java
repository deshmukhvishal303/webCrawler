package com.web.crawler.exception;

import com.web.crawler.constant.CrawlerConstants;

/**
 * Created by vishal.deshmukh on 28/07/16.
 */
public class CrawlerException extends Throwable {
    private int errorCode;
    private String errorMessage;

    public CrawlerException(){
        super();
        this.errorCode       = CrawlerConstants.DEFAULT_ERROR_CODE;
        this.errorMessage    = CrawlerConstants.DEFAULT_ERROR_MESSAGE;
    }

    public CrawlerException(Integer errorCode, String errorMessage){
        this.errorCode      = errorCode;
        this.errorMessage   = errorMessage;
    }

}
