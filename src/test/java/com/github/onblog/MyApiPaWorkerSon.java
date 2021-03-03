package com.github.onblog;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.io.file.FileWriter;
import com.github.onblog.executor.AiPaExecutor;
import com.github.onblog.util.AiPaUtil;
import com.github.onblog.worker.AiPaWorker;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MyApiPaWorkerSon implements AiPaWorker {

    @Override
    public Object run(Document doc, AiPaUtil util) throws ExecutionException, InterruptedException, IOException, DocumentException {
        com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);

        String fileName = util.getFileName();
        File file = new File("D:/" + fileName +".doc");
        if(!file.exists()){
            FileUtil.touch(file);
        }
        RtfWriter2.getInstance(document, new FileOutputStream(file));

        FileAppender appender = new FileAppender(file, 16, true);

        Element body = doc.body();
        document.open();
        Elements eleObjs = body.getElementsByClass("dirItem02");
        for (int i = 0; i < eleObjs.size(); i++) {

            Element eleObj = eleObjs.get(i);

            Elements h5Objs = eleObj.getElementsByTag("h5");

            for (int j = 0; j < h5Objs.size(); j++) {

                String h5ObjText = h5Objs.get(j).text();
                Paragraph title = new Paragraph(h5ObjText);
                //
                // 设置标题格式对齐方式

                title.setAlignment(com.lowagie.text.Element.ALIGN_LEFT);

                // title.setFont(titleFont);

                document.add(title);

            }
            Elements aObjs = eleObj.getElementsByTag("a");

            for (int k = 0; k < aObjs.size(); k++) {
                String hrefStr = aObjs.get(k).attr("href");
                String titleStr = aObjs.get(k).attr("title");
                Paragraph titletitleStr = new Paragraph(titleStr);
                titletitleStr.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                document.add(titletitleStr);
                //打开子链接打开并获取 文件 写入|| hrefStr.contains("/page/2017/0325/1278828.shtml")|| hrefStr.contains("/page/2019/0306/4923372.shtml")
                if(hrefStr.contains("/page/2018/1130/4552097.shtml") || hrefStr.contains("/page/2017/0325/1278828.shtml")){
                    continue;
                }
                //第二步：提交任务
                List<String> linkList = new ArrayList<>();
                String ownUrl = "https://www.fx361.com/";
                linkList.add(ownUrl+hrefStr);
                AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyAiPaWorkerSonSon()).setCharset(Charset.forName("utf-8"));
                //第三步：读取返回值
                aiPaExecutor.submit(linkList,fileName,document);
                List<Future> futureList = aiPaExecutor.getFutureList();
                for (int y = 0; y < futureList.size(); y++) {
                    //get() 方法会阻塞当前线程直到获取返回值
                    Map<String,List> resultMap =  new HashMap<>();
                    List<String> pObjList = new ArrayList<>();
                    List<Image> imgList = new ArrayList<>();
                    resultMap = (Map<String, List>) futureList.get(y).get();
                    pObjList = resultMap.get("pObjList");
                    if(pObjList!=null){
                        for (int j = 0; j < pObjList.size(); j++) {
                            String pObj  = "    " + pObjList.get(j);
                            Paragraph par = new Paragraph(pObj);
                            par.setSpacingBefore(5);
                            document.add(par);
                        }
                    }
                    if(imgList!=null){
                        for (int j = 0; j < imgList.size(); j++) {

                        }
                    }

                    System.out.println(y+"："+futureList.get(y).get());
                }
                linkList = null;
                aiPaExecutor.shutdown();

            }

            appender.flush();
        }
        document.close();

        return doc.title();
    }

    private void writeDocByContext(String fileName,String contextStr,Elements imgs, Integer typeNum) throws DocumentException, IOException {
        com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
        File file = new File(fileName);
        if(!file.exists()){
            FileUtil.touch(file);
        }
        RtfWriter2.getInstance(document, new FileOutputStream(file));

        document.open();
        // 设置中文字体

        BaseFont bfChinese = BaseFont.createFont(BaseFont.HELVETICA,
                BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

        // 标题字体风格

        Font titleFont = new Font(bfChinese, 12, Font.BOLD);

        // // 正文字体风格
        //
        Font contextFont = new Font(bfChinese, 10, Font.NORMAL);

        Paragraph title = new Paragraph(contextStr);
        //
        // 设置标题格式对齐方式

        title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);

        title.setFont(titleFont);

        document.add(title);

        if(typeNum == 1){


            //  文本正文
            Paragraph context = new Paragraph(contextStr);

            // 正文格式左对齐

            context.setAlignment(com.lowagie.text.Element.ALIGN_LEFT);

            // context.setFont(contextFont);

            // 离上一段落（标题）空的行数

            context.setSpacingBefore(5);

            // 设置第一行空的列数

            context.setFirstLineIndent(20);

            document.add(context);

        }else if(typeNum == 2) {

            for (Element image : imgs) {
                //图片路径
                String src = image.attr("src");
                BufferedInputStream in = Jsoup.connect(src).ignoreContentType(true).execute().bodyStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int length = 0;
                while ((length = in.read(buf, 0, buf.length)) != -1) {
                    out.write(buf, 0, length);
                }
                Image img = Image.getInstance(out.toByteArray());

                img.setAbsolutePosition(0, 0);

                img.setAlignment(Image.LEFT);// 设置图片显示位置


                // img.scaleAbsolute(60, 60);// 直接设定显示尺寸
                //
                // // img.scalePercent(50);//表示显示的大小为原尺寸的50%
                //
                // // img.scalePercent(25, 12);//图像高宽的显示比例
                //
                // // img.setRotation(30);//图像旋转一定角度
                //
                document.add(img);
                in.close();
                out.close();
            }
        }else if(typeNum == 3){

        }
        document.close();

    }

    @Override
    public Object fail(String link) {
        return null;
    }
}
