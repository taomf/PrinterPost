package cn.speedpay.s.xedj.frame.http;

import java.io.File;

import okhttp3.MediaType;

/**
 * 说明：包装文件
 */
public class FileWrapper {

    private File file;
    private String fileName;
    private MediaType mediaType;
    private long fileSize;

    public FileWrapper(File file,MediaType mediaType) {
        this.file = file;
        this.fileName = file.getName();
        this.mediaType = mediaType;
        this.fileSize = file.length();
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public File getFile() {
        return file;
    }

    public String getFileName(){
        return fileName;
    }

}
