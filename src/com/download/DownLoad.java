package com.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/7/13 0013.
 */
public class DownLoad {

    private Executor threadPool = Executors.newFixedThreadPool(3);

    static class DownloadRunnable implements Runnable {

        private String url;
        private String fileName;
        private long start;
        private long end;

        public DownloadRunnable(String url, String fileName, long start, long end) {
            this.url =url;
            this.fileName = fileName;
            //this.fileName = "E:/1.png";
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            try {
                URL httpUrl = new URL(url);
                HttpURLConnection con = (HttpURLConnection)httpUrl.openConnection();
                con.setRequestMethod("GET");
                con.setReadTimeout(5000);
                con.setRequestProperty("Range","bytes="+start+"-"+end);
                RandomAccessFile access = new RandomAccessFile(new File(fileName), "rwd");
                access.seek(start);
                InputStream in = con.getInputStream();
                byte[] b = new byte[1024*4];
                int len = 0;
                while ((len =in.read(b))!=-1) {
                    access.write(b,0,len);
                }
                System.out.println("下载成功");
                if (access!=null) {
                    access.close();
                }
                if (in!=null) {
                    in.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void downLoadFile(String url, String downLoadFileName) {
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection)httpUrl.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(5000);
            int count = con.getContentLength();
            int block = count/3;

            String pathName = con.getURL().getFile();
            String path = downLoadFileName+File.separatorChar+getFileName(pathName);
            File downLoad = new File(path);

            for (int i=0;i<3;i++) {
                long start = i*block;
                long end = (i+1)*block-1;
                if (i==2) {
                    end = count;
                }
                DownloadRunnable runnable = new DownloadRunnable(url, downLoad.getAbsolutePath(),start,end);
                threadPool.execute(runnable);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName(String url) {
        int index = url.lastIndexOf(File.separatorChar)+1;
        String fullName = url.substring(index);
        return url.substring(url.lastIndexOf(File.separatorChar)+1);
    }

    public static void main(String[] args) {
        DownLoad downLoad = new DownLoad();
        downLoad.downLoadFile("http://localhost:8080/1.png","E:");
    }
}
