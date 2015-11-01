package com.springapp.mvc.Controller;

import com.qiniu.common.QiniuException;
import com.springapp.mvc.Qiniu.QiniuClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;

@Controller
@RequestMapping("/")
public class HelloController {


	private QiniuClient initQiniu() throws QiniuException {

		String ACCESS_KEY="hxAZX2n5VRbB1Nj-4VXq-bllzak5ClgytGYCx7dt";
		String SECRET_KEY="vIB-JjlqQn6M2MYqX8r0wG9FXO13ExeK3Y21fj4f";
		String bucketName="images";
		String bucketUrl="https://dn-joway.qbox.me/";

		QiniuClient qiniuClient = new QiniuClient(ACCESS_KEY,SECRET_KEY);
		qiniuClient.setBucketName(bucketName);
		qiniuClient.setBucketUrl(bucketUrl);
		return qiniuClient;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "upload";
	}

	@RequestMapping("fileUpload")
	public String fileUpload(ModelMap modelMap,HttpServletRequest request) throws IOException {
		//将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//检查form中是否有enctype="multipart/form-data"
		if(multipartResolver.isMultipart(request))
		{
			//将request变成多部分request
			MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
			//获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();

			//根据自己的信息，配置七牛
			QiniuClient qiniuClient = initQiniu();;

			while(iter.hasNext())
			{
				//一次遍历所有文件
				MultipartFile file=multiRequest.getFile(iter.next().toString());
				if(file!=null)
				{
					qiniuClient.upload(file.getBytes(),file.getOriginalFilename());
					String full_url=qiniuClient.getBucketUrl()+file.getOriginalFilename();
					modelMap.addAttribute("url",full_url);
				}
			}
		}

		// 重定向
		return "upload";
	}
}