package com.nostyling.wcms.entity;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import com.nostyling.wcms.utils.markdown2html.MarkDown2HtmlWrapper;
import com.nostyling.wcms.utils.markdown2html.MarkdownEntity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shiliang
 * @Classname FileEntity
 * @Date 2021/2/25 11:00
 * @Description 文件实体
 */
@Data
public class FileEntity {

    /**
     * 是否是文件夹
     */
    private boolean isDir;
    /**
     * 文件夹路径
     */
    private String rootPath;

    /**
     * 文件夹路径
     */
    private String path;

    /**
     * 相对文件夹路径
     */
    private String relativePath;


    /**
     * 文件夹名称
     */
    private String name;

    /**
     * 文件
     */
    private File file;

    /**
     * 文件
     */
    private MarkdownEntity html;

    /**
     *
     */
    private List<FileEntity> fileEntityList;

    /**
     * 创建
     *
     * @param rootPath
     */
    public FileEntity(String rootPath) {
        this(rootPath, rootPath);
    }

    /**
     * 创建
     *
     * @param rootPath
     * @param path
     */
    public FileEntity(String rootPath, String path) {
        this.rootPath = rootPath;
        this.path = path;
        this.relativePath = StringUtils.removeStart(this.path, this.rootPath);
    }


    /**
     * 加载文件
     */
    public void loadingFileEntity() {
        String rootPath = this.rootPath;
        File fileByPath = FileUtil.file(this.path);
        this.isDir = FileUtil.isDirectory(fileByPath);
        this.path = fileByPath.getPath();
        this.name = fileByPath.getName();
        if (this.isDir) {
            File[] files = fileByPath.listFiles();
            if (files != null && files.length != 0) {
                List<FileEntity> list = new ArrayList<>(files.length);
                for (File file : files) {
                    FileEntity fileEntity = new FileEntity(rootPath, file.getPath());
                    fileEntity.loadingFileEntity();
                    list.add(fileEntity);
                }
                this.fileEntityList = list;
            }
        } else {
            this.file = fileByPath;
        }
    }

    /**
     * 翻译文件
     */
    public void markdown2Html() {
        if (null != this.file) {
            String mimeType = FileUtil.getName(this.file);
            if (StringUtils.endsWith(mimeType, ".mk")) {
                try {
                    html = MarkDown2HtmlWrapper.ofFile(this.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
        if (null != this.fileEntityList) {
            for (FileEntity fileEntity : this.fileEntityList) {
                fileEntity.markdown2Html();
            }
        }
    }

    /**
     * 输出
     *
     * @param outPath
     */
    public void htmlFileWriter(String outPath) {
        if (this.isDir) {
            FileUtil.mkdir(outPath + relativePath);
        }
        if (null != this.html) {
            String s = ".html";
            String s1 = StringUtils.removeEnd(relativePath, ".mk") + s;
            new FileWriter(outPath + s1).append(html.toString());
        }
        if (null != this.fileEntityList) {
            for (FileEntity fileEntity : this.fileEntityList) {
                fileEntity.htmlFileWriter(outPath);
            }
        }
    }

}
