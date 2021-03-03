package com.github.onblog;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileAppender;
import com.github.onblog.util.AiPaUtil;
import com.github.onblog.worker.AiPaWorker;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.rtf.RtfWriter2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MyAiPaWorkerSonSon implements AiPaWorker {

    @Override
    public Object run(Document doc, AiPaUtil util) throws ExecutionException, InterruptedException, FileNotFoundException, DocumentException {

        String fileName = util.getFileName();
        List<String> pObjList = new ArrayList<>();
        List<Image> imgList = new ArrayList<>();
        Map<String,List> resultMap =  new HashMap<>();
        File file = new File("D:/" + fileName +".doc");
        if(!file.exists()){
            FileUtil.touch(file);
        }

        FileAppender appender = new FileAppender(file, 16, true);

        Element body = doc.body();
        Elements eleObjs = body.getElementsByClass("detail_body");

        for (int i = 0; i < eleObjs.size(); i++) {
            Element eleObj = eleObjs.get(i);

            Elements pObjs = eleObj.getElementsByTag("p");

            for (int j = 0; j < pObjs.size(); j++) {

                String pObjText = pObjs.get(j).text();

                pObjList.add(pObjText);

            }

        }
        resultMap.put("pObjList",pObjList);
        resultMap.put("imgList",imgList);

        return resultMap;
    }

    @Override
    public Object fail(String link) {
        return null;
    }
}
