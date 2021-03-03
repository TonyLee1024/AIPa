package com.github.onblog;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.poi.word.Word07Writer;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Test {
    @org.junit.Test
    public void test() throws  Exception{
        String filePath = "D:\\a.doc";
        try {
            String allUrl ="https://mbd.baidu.com/newspage/data/landingshare?context=%7B%22nid%22%3A%22news_9881067036128581241%22%2C%22sourceFrom%22%3A%22bjh%22%7D";
            Document docAll = Jsoup.connect(allUrl).data("query", "Java")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();
            // 设置纸张大小

            com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);

            // 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
            // ByteArrayOutputStream baos = new ByteArrayOutputStream();

            File file = new File(filePath);
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

            /**
             标题
             */
            String webTitle = docAll.getElementsByTag("title").text();
            /**
             * 内容
             */
            Elements contexts = docAll.getElementsByTag("p");
            String contextString ="";
            for (Element context : contexts)
            {
                contextString+=context.text();
            }
            /**
             end
             */

            Paragraph title = new Paragraph(webTitle);
            //
            // 设置标题格式对齐方式

            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);

            // title.setFont(titleFont);

            document.add(title);

//            //  文本正文
//            Paragraph context = new Paragraph(contextString);
//
//            // 正文格式左对齐
//
//            context.setAlignment(com.lowagie.text.Element.ALIGN_LEFT);
//
//            // context.setFont(contextFont);
//
//            // 离上一段落（标题）空的行数
//
//            context.setSpacingBefore(5);
//
//            // 设置第一行空的列数
//
//            context.setFirstLineIndent(20);
//
//            document.add(context);
            //
            // // 利用类FontFactory结合Font和Color可以设置各种各样字体样式
            //
            // Paragraph underline = new Paragraph("下划线的实现", FontFactory.getFont(
            // FontFactory.HELVETICA_BOLDOBLIQUE, 18, Font.UNDERLINE,
            // new Color(0, 0, 255)));
            //
            // document.add(underline);
            //

            // // 添加图片 Image.getInstance即可以放路径又可以放二进制字节流
            //
            /**
             图片
             */
//            Elements imgs = docAll.getElementsByTag("img");
//            for (Element image : imgs)
//            {
//                //图片路径
//                String src = image.attr("src");
//                BufferedInputStream in = Jsoup.connect(src).ignoreContentType(true).execute().bodyStream();
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                byte[] buf = new byte[1024];
//                int length = 0;
//                while ((length = in.read(buf, 0, buf.length)) != -1) {
//                    out.write(buf, 0, length);
//                }
//                Image img = Image.getInstance(out.toByteArray());
//
//                img.setAbsolutePosition(0, 0);
//
//                img.setAlignment(Image.LEFT);// 设置图片显示位置
//
//
//                // img.scaleAbsolute(60, 60);// 直接设定显示尺寸
//                //
//                // // img.scalePercent(50);//表示显示的大小为原尺寸的50%
//                //
//                // // img.scalePercent(25, 12);//图像高宽的显示比例
//                //
//                // // img.setRotation(30);//图像旋转一定角度
//                //
//                document.add(img);
//                in.close();
//                out.close();
//            }




            document.close();

            // 得到输入流
            // wordFile = new ByteArrayInputStream(baos.toByteArray());
            // baos.close();

//            Connection referrer = Jsoup.connect(src).referrer(src);
//            referrer.ignoreContentType(true);
//            Connection.Response execute = referrer.execute();
//            BufferedInputStream in = execute.bodyStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
