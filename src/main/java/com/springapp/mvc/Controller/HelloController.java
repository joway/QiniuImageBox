package com.springapp.mvc.Controller;

import com.qiniu.common.QiniuException;
import com.springapp.mvc.Config;
import com.springapp.mvc.Qiniu.QiniuClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

@Controller
@RequestMapping("/")
public class HelloController {


    private QiniuClient initQiniu() throws QiniuException {

        String ACCESS_KEY = Config.ACCESS_KEY;
        String SECRET_KEY = Config.SECRET_KEY;
        String bucketName = Config.bucketName;
        String bucketUrl = Config.bucketUrl;

        QiniuClient qiniuClient = new QiniuClient(ACCESS_KEY, SECRET_KEY);
        qiniuClient.setBucketName(bucketName);
        qiniuClient.setBucketUrl(bucketUrl);
        return qiniuClient;
    }

    @RequestMapping("/")
    public String fileUpload(ModelMap modelMap, HttpServletRequest request) throws IOException {

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator iter = multiRequest.getFileNames();

            QiniuClient qiniuClient = initQiniu();

            ArrayList<String> urls = new ArrayList<String>();

            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    String filename = (new Date()).getTime() + "_" + file.getOriginalFilename();
                    qiniuClient.upload(file.getBytes(), filename);
                    String full_url = qiniuClient.getBucketUrl() + filename;
                    urls.add(full_url);
                }
            }

            modelMap.addAttribute("urls", urls);

        }

        return "upload";
    }
}