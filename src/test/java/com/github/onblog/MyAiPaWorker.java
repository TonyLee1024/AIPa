package com.github.onblog;

import com.github.onblog.executor.AiPaExecutor;
import com.github.onblog.util.AiPaUtil;
import com.github.onblog.worker.AiPaWorker;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Create by yster@foxmail.com 2019/3/13 0013 16:52
 */
public class MyAiPaWorker implements AiPaWorker {

    @Override
    public String run(Document doc, AiPaUtil util) throws ExecutionException, InterruptedException {
        //使用JSOUP进行HTML解析获取想要的div节点和属性
        //保存在数据库或本地文件中
        //新增aiPaUtil工具类可以再次请求网址
        AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyApiPaWorkerSon()).setCharset(Charset.forName("utf-8"));
        Element body = doc.body();
        Elements eleObj = body.getElementsByClass("results");
        //循环书柜
        for (Element element:eleObj) {
            //获取有多少本书
            Elements aObjs = element.getElementsByTag("a");
            for (int i = 0; i < aObjs.size(); i++) {
//
//                if(i<5 ){
//                    continue;
//                }
                Element aObj = aObjs.get(i);

                String bookName = "";//书名

                String hrefStr = aObj.attr("href");//书的链接

                //组合书名
                Elements spandsObj = aObj.getElementsByTag("span");
                for (int j = 0; j < spandsObj.size(); j++) {
                    String textStr = spandsObj.get(j).text();
                    bookName = bookName +textStr;
                }

                System.out.println(hrefStr+"-"+bookName);
                //准备网址集合
                List<String> linkList = new ArrayList<>();
                String ownUrl = "https://www.fx361.com/";
                linkList.add(ownUrl+hrefStr);


                //第二步：提交任务

                aiPaExecutor.submit(linkList , bookName,null);
                //第三步：读取返回值
                List<Future> futureList = aiPaExecutor.getFutureList();
                for (int y = 0; y < futureList.size(); y++) {
                    //get() 方法会阻塞当前线程直到获取返回值
                    System.out.println(y+"："+futureList.get(y).get());
                }


            }
        }
        //第四步：关闭线程池
        aiPaExecutor.shutdown();
        String bodyObj = doc.body().ownText();
        System.out.println("bodyObj is :"+bodyObj);
        return doc.title();
    }

    @Override
    public Boolean fail(String link) {
        //任务执行失败
        //可以记录失败网址
        //记录日志
        return false;
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ExecutionException, InterruptedException {
        //准备网址集合
        List<String> linkList = new ArrayList<>();


        linkList.add("https://www.fx361.com/bk/swyzhxby/history_2020.html");
        //第一步：新建AiPa实例
        AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyAiPaWorker()).setCharset(Charset.forName("utf-8"));
        //第二步：提交任务
//        for (int i = 0; i < 10; i++) {

//        }
        aiPaExecutor.submit(linkList,"",null);
        //第三步：读取返回值
        List<Future> futureList = aiPaExecutor.getFutureList();
        for (int i = 0; i < futureList.size(); i++) {
            //get() 方法会阻塞当前线程直到获取返回值
            System.out.println(i+"："+futureList.get(i).get());
        }
        //第四步：关闭线程池
        aiPaExecutor.shutdown();
    }
}