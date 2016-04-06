package com.springapp.mvc.Domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JowayWong on 2015/11/15.
 */
public class Files implements Serializable {
    private static final long serialVersionUID = 74458L;

    private List<MultipartFile> files;

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
