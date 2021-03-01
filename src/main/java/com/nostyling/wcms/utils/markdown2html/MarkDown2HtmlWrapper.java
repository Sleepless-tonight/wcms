package com.nostyling.wcms.utils.markdown2html;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.google.common.base.Joiner;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: Sleepless-tonight.github.io
 * @author: shiliang
 * @create: 2019-02-18 14:43
 * @description:
 **/
public class MarkDown2HtmlWrapper {
    private static String MD_CSS = "../../Css/markdownCss/github-markdown.css";

    //private static String MD_CSS = null;
    static {
        try {
            if (null == MD_CSS) {
                MD_CSS = FileUtil.file("../../Css/markdownCss/github-markdown.css").getCanonicalPath();
            }
            MD_CSS = "<head>\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" charset=\"utf-8\">\n" +
                    "    <link rel=\"icon\" href=\"https://nostyling-1256016577.cos.ap-beijing.myqcloud.com/GitHub_cat.png\" type=\"image/x-icon\">\n" +
                    "    <link rel=\"stylesheet\" href=\"" +
                    MD_CSS +
                    "\">\n" +
                    "</head>\n" +
                    "<style>\n" +
                    "\t.markdown-body {\n" +
                    "\t\tbox-sizing: border-box;\n" +
                    "\t\tmin-width: 200px;\n" +
                    "\t\tmax-width: 980px;\n" +
                    "\t\tmargin: 0 auto;\n" +
                    "\t\tpadding: 45px;\n" +
                    "\t}\n" +
                    "\n" +
                    "\t@media (max-width: 767px) {\n" +
                    "\t\t.markdown-body {\n" +
                    "\t\t\tpadding: 15px;\n" +
                    "\t\t}\n" +
                    "\t}\n" +
                    "</style>";
        } catch (Exception e) {
            MD_CSS = "";
        }
    }

    /**
     * 将本地的markdown文件，转为html文档输出
     *
     * @param path 相对地址or绝对地址 ("/" 开头)
     * @return
     * @throws IOException
     */
    public static MarkdownEntity ofFile(String path) throws IOException {
        return ofStream(new FileReader(FileUtil.file(path)).getInputStream());
    }

    /**
     * 将本地的markdown文件，转为html文档输出
     *
     * @param file 相对地址or绝对地址 ("/" 开头)
     * @return
     * @throws IOException
     */
    public static MarkdownEntity ofFile(File file) throws IOException {
        return ofStream(new FileReader(file).getInputStream());
    }


    /**
     * 将流转为html文档输出
     *
     * @param stream
     * @return
     */
    public static MarkdownEntity ofStream(InputStream stream) {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(stream, Charset.forName("UTF-8")));
        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
        String content = Joiner.on("\n").join(lines);
        return ofContent(content);
    }


    /**
     * 直接将markdown语义的文本转为html格式输出
     *
     * @param content markdown语义文本
     * @return
     */
    public static MarkdownEntity ofContent(String content) {
        //String html = parse(content);
        String html = parse(content);
        MarkdownEntity entity = new MarkdownEntity();
        entity.setCss(MD_CSS);
        entity.setHtml(html);
        entity.addDivStyle("class", "markdown-body ");
        return entity;
    }

    public static String parse(String content) {
        MutableDataSet options = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(
                AutolinkExtension.create(),
                EmojiExtension.create(),
                StrikethroughExtension.create(),
                TaskListExtension.create(),
                TablesExtension.create()
        ))
                // set GitHub table parsing options 设置GitHub表解析选项
                .set(TablesExtension.WITH_CAPTION, false)//带标题
                .set(TablesExtension.COLUMN_SPANS, false)//列跨
                .set(TablesExtension.MIN_HEADER_ROWS, 1)//最大标题行
                .set(TablesExtension.MAX_HEADER_ROWS, 1)//最大标题行
                .set(TablesExtension.APPEND_MISSING_COLUMNS, true)//添加缺失列
                .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)//丢弃多余列
                .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)//标题分隔列匹配
                //.set(TablesExtension.TRIM_CELL_WHITESPACE, true)//修剪单元空白
                //.set(TablesExtension.MIN_SEPARATOR_DASHES, 3)//最小分隔灰
                //.set(TablesExtension.CLASS_NAME, "")//类名
                //.set(TablesExtension.MULTI_LINE_ROWS, false)//多线行

                // setup emoji shortcut options 设置表情符号快捷方式选项
                // uncomment and change to your image directory for emoji images if you have it setup取消注释并更改您的图像目录以获取表情符号图像（如果已设置）
                //.set(EmojiExtension.ROOT_IMAGE_PATH, emojiInstallDirectory())
                .set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB)
                .set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.IMAGE_ONLY)
                // other options
                ;

        // uncomment to convert soft-breaks to hard breaks like GitHub comments
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");取消注释将软中断转换为GitHub注释等硬中断

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances 您可以重复使用解析器和渲染器实例
        Node document = parser.parse(content);
        return renderer.render(document);
    }

}
