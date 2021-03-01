package com.nostyling.wcms.utils.markdown2html;


import java.io.IOException;
import cn.hutool.core.io.file.FileWriter;
/**
 * @program: Sleepless-tonight.github.io
 * @author: shiliang
 * @create: 2019-02-18 14:42
 * @description: markdown to html
 **/
public class Markdown2Html {
    public static void main(String[] args) {
        //String file = "D:\\GitHub\\Sleepless-tonight.github.io\\html\\templates\\Beginning C ,Fifth Edition.mk";
        //String file2 = "C:\\Users\\shiliang\\Desktop\\Beginning C ,Fifth Edition.html";

        // String file = "D:\\GitHub\\Sleepless-tonight.github.io\\html\\templates\\Thinking-In-Java.mk";
        // String file2 = "C:\\Users\\shiliang\\Desktop\\Thinking-In-Java.html";

        // String file = "D:\\GitHub\\Sleepless-tonight.github.io\\html\\NTFS\\分区表.mk";
        // String file2 = "C:\\Users\\shiliang\\Desktop\\分区表.html";
        String file = "D:\\GitHub\\Sleepless-tonight.github.io\\html\\templates\\JavaFx.mk";
        String file2 = "C:\\Users\\shiliang\\Desktop\\JavaFx.html";
        MarkdownEntity html = null;
        try {
            html = MarkDown2HtmlWrapper.ofFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(html.toString());
        new FileWriter(file2).append(html.toString());
    }
}
