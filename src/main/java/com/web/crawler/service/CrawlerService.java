package com.web.crawler.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by vishal.deshmukh on 28/07/16.
 */
public class CrawlerService implements Callable<Object> {

    private static ConcurrentLinkedQueue<String> crawlerQueue = new ConcurrentLinkedQueue<String>();
    private static volatile Set<String> urlVisited = new HashSet<String>();

    public CrawlerService(){}

    public Object call() throws Exception {
        crawl();

        return null;
    }

    private void crawl() throws IOException {
        while(!Thread.currentThread().isInterrupted()) {
                String url = crawlerQueue.remove();
                System.out.println("\nVisiting URL: " + url);

                System.out.println(Thread.currentThread().getName()+" "+new Date() + " Crawler Queue: " + crawlerQueue);
                System.out.println(Thread.currentThread().getName()+" "+new Date() + " URL Visited: " + urlVisited);

                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("a[href]");

                for (Element e : elements) {
                    String urlOnPage = e.attr("href");
                    if (!urlOnPage.startsWith("http") && !urlOnPage.startsWith("https"))
                        continue;
                    if (!urlVisited.contains(urlOnPage))
                        crawlerQueue.add(urlOnPage);
                }

            synchronized (crawlerQueue) {
                urlVisited.add(url);
            }
        }
    }

    public static boolean isCrawlerQueueEmpty(){
        return crawlerQueue.isEmpty();
    }

    public static void enqueueURL(String url){
        crawlerQueue.add(url);
    }

    public static void emptyQueue(){
        synchronized(crawlerQueue) {
            crawlerQueue.clear();
            crawlerQueue.notifyAll();
        }
    }
}
