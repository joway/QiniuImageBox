package com.springapp.mvc.Qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

import java.io.*;
import java.util.Arrays;

/**
 * Created by JowayWong on 2015/10/31.
 */
public class QiniuClient {

    private String bucketName;
    private String[] buckets;

    private BucketManager bucketManager;
    private Auth auth;
    UploadManager uploadManager;
    private String bucketUrl;

    public QiniuClient(String access_key,String secret_key) throws QiniuException {

        auth = Auth.create(access_key, secret_key);
        bucketManager = new BucketManager(auth);
        uploadManager = new UploadManager();
        buckets=getBuckets();

        if(buckets.length>0)
            bucketName=buckets[0];
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String[] getBuckets() throws QiniuException {
        buckets = bucketManager.buckets();
        return buckets;
    }

    public FileInfo[] getFileListInfo(String prefix){

        FileInfo[] fileInfos=new FileInfo[0];
        BucketManager.FileListIterator it = bucketManager.createFileListIterator(bucketName, prefix);
        while (it.hasNext()) {
            FileInfo[] items = it.next();
            fileInfos = Arrays.copyOf(fileInfos, fileInfos.length + items.length);
            System.arraycopy(items,0,fileInfos,fileInfos.length-items.length,items.length);
        }
        return fileInfos;
    }

    public FileInfo getFileInfo(String key) throws QiniuException {
        return bucketManager.stat(this.bucketName, key);
    }

    //����ָ��key,��Ĭ���ļ�������
    public void copyFile(String key,String targetBucket) throws QiniuException {
        copyFile(key, targetBucket, key);
    }

    public void copyFile(String key,String targetBucket,String targetKey) throws QiniuException {
        bucketManager.copy(bucketName, key, targetBucket, targetKey);
    }

    public void rename(String oldKey,String newKey) throws QiniuException {
        bucketManager.rename(bucketName, oldKey, newKey);
    }

    public void move(String key,String targetBucket,String targetKey) throws QiniuException {
        bucketManager.move(bucketName, key, targetBucket, targetKey);
    }

    public void delete(String key) throws QiniuException {
        bucketManager.delete(bucketName, key);
    }



    //��ָ�� key ʱ���ļ��� hash ֵΪ key
    public DefaultPutRet fetch(String url) throws QiniuException {
        return fetch(url, null);
    }

    public DefaultPutRet fetch(String url,String key) throws QiniuException {
        return bucketManager.fetch(url, bucketName, key);
    }

    public void prefetch(String key) throws QiniuException {
        //��keyƴ�ӵ�����Դ��ַ��Ȼ����ȡ��Դ�����ڿռ�
        bucketManager.prefetch(bucketName, key);
    }


    public int upload(byte[] Data,String key) throws QiniuException {
        Response res = uploadManager.put(Data, key, this.auth.uploadToken(bucketName));
        return res.statusCode;
    }

    public int upload(String fileName,String key) throws IOException {
        return upload(fileToByte(fileName), key);
    }

    /**
     * the traditional io way
     * @param filename
     * @return
     * @throws IOException
     */
    private byte[] fileToByte(String filename) throws IOException {

        File f = new File(filename);
        if(!f.exists()){
            throw new FileNotFoundException(filename);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());
        BufferedInputStream in = null;
        try{
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while(-1 != (len = in.read(buffer,0,buf_size))){
                bos.write(buffer,0,len);
            }
            return bos.toByteArray();
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if (in != null) {
                    in.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    public static void main(String[] args){

    }


    public String getBucketUrl() {
        return bucketUrl;
    }

    public void setBucketUrl(String bucketUrl) {
        this.bucketUrl = bucketUrl;
    }
}
