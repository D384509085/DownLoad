package com.simple_httpurlconnection;


import javax.print.DocFlavor;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/7/10 0010.
 */
public class SimpleDownload {
    public static boolean downloadFile(String downloadUrl, String downloadPath) {
        try {
            URL url = new URL(downloadUrl);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.connect();

            int fileLength = httpURLConnection.getContentLength();

            String filePathUrl = httpURLConnection.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);
            URLConnection urlConnection1 = url.openConnection();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            String path = downloadPath + File.separatorChar + fileFullName;
            File file = new File(path);
            OutputStream out = new FileOutputStream(file);

            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bufferedInputStream.read(buf)) !=-1) {
                len += size;
                out.write(buf, 0, size);
                // System.out.println("下载了-------> " + len * 100 / fileLength +
            }
            bufferedInputStream.close();
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static void main(String[] args) {
        downloadFile("http://localhost:8080/1.png", "E:");
    }
}
