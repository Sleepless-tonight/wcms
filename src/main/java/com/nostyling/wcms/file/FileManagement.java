package com.nostyling.wcms.file;

import com.nostyling.wcms.entity.FileEntity;

/**
 * @author shiliang
 * @Classname FileManagement
 * @Date 2021/2/25 11:09
 * @Description Exception
 */
public class FileManagement {
    public static void main(String[] args) {
        FileEntity rootDir = new FileEntity("D:\\GitHub\\Sleepless-tonight.github.io\\templates");
        rootDir.loadingFileEntity();
        rootDir.markdown2Html();
        rootDir.htmlFileWriter("C:\\Users\\shiliang\\Desktop\\html");

        System.out.println(rootDir);
    }
}
