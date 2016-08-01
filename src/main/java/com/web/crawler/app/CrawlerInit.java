package com.web.crawler.app;

import com.web.crawler.constant.CrawlerConstants;
import com.web.crawler.dto.CrawlerInputDto;
import com.web.crawler.exception.CrawlerException;
import com.web.crawler.service.CrawlerService;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vishal.deshmukh on 28/07/16.
 */
public class CrawlerInit {

    private static Logger logger = Logger.getLogger(CrawlerInit.class.getName());

    public static void main(String args[]) throws CrawlerException{
        CrawlerInputDto crawlerInputDto = getWebSiteSeed();
        logger.log(Level.INFO, "Starting Crawling");
        startCrawling(crawlerInputDto);
    }

    private static CrawlerInputDto getWebSiteSeed() throws CrawlerException{
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter website to be crawled (http://www.example.com): ");
            String siteUrl = scanner.next();

            System.out.print("Enter timeout for crawler in seconds: ");
            Integer timeout = scanner.nextInt();

            logger.log(Level.INFO, "WebSite to be crawled: "+siteUrl + " for "+timeout+" minutes");
            CrawlerInputDto crawlerInputDto = new CrawlerInputDto(siteUrl, timeout);
            return crawlerInputDto;
        } catch (Exception e) {
            String errorMessage = "Exception occurred while reading input from console";
            logger.log(Level.INFO, errorMessage);
            throw new CrawlerException(CrawlerConstants.DEFAULT_ERROR_CODE, errorMessage);
        }
    }

    private static void startCrawling(CrawlerInputDto crawlerInputDto) throws CrawlerException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CrawlerService.enqueueURL(crawlerInputDto.getSiteUrl());
        Date dstart = new Date();
        Future<Object> future = executorService.submit(new CrawlerService());
        try {
            future.get(crawlerInputDto.getTimeout(), TimeUnit.SECONDS);
            executorService.awaitTermination(crawlerInputDto.getTimeout(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            future.cancel(true);
            CrawlerService.emptyQueue();
        }

        Date dend = new Date();

        System.out.println("Crawler Start Time: "+dstart);
        System.out.println("Crawler End Time: "+dend);
        future.cancel(true);
        executorService.shutdownNow();
    }
}
