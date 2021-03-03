package com.github.onblog.Callable;

import com.github.onblog.util.AiPaUtil;
import com.github.onblog.worker.AiPaWorker;
import com.lowagie.text.DocumentException;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Create by yster@foxmail.com 2018/9/26/026 23:10
 */
public class AiPaCallable implements Callable<Object> {
    private AiPaWorker aiPaWorker;// 用户实现的方法
    private int maxFailCount;// 最多失败次数
    private String link;    // 要爬取的网址
    private AiPaUtil aiPaUtil;// 爬虫工具类

    public AiPaCallable() {
    }

    public AiPaCallable init(AiPaWorker aiPaWorker, int maxFailCount, String link, AiPaUtil aiPaUtil) {
        this.aiPaWorker = aiPaWorker;
        this.maxFailCount = maxFailCount;
        this.link = link;
        this.aiPaUtil = aiPaUtil;
        return this;
    }

    public AiPaCallable init(AiPaWorker aiPaWorker, int maxFailCount, String link, Charset charset, Map<String, String> header, Connection.Method method, int timeout, String userAgent,Map<String, String> cookies,String fileName,com.lowagie.text.Document document) {
        this.aiPaWorker = aiPaWorker;
        this.maxFailCount = maxFailCount;
        this.link = link;
        this.aiPaUtil = new AiPaUtil(charset,header,method,timeout,userAgent,cookies,fileName,document);
        return this;
    }

    @Override
    public Object call() {
        int c = 0;
        while (c < maxFailCount) {
            try {
                // 开始爬虫
                Document body = aiPaUtil.getHtmlDocument(link);
                // 执行任务
                return aiPaWorker.run(body,aiPaUtil);
            } catch (IOException | ExecutionException | InterruptedException | DocumentException e) {
                c++;
//                if (c == maxFailCount) {
//                    e.printStackTrace();
//                }
                System.out.println(e);
            }
        }
        // 爬取失败执行的方法
        return aiPaWorker.fail(link);
    }

}
