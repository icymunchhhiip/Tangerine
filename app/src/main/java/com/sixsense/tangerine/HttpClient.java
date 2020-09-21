package com.sixsense.tangerine;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private static final String TAG = "Http connect";

    private static String serverUrl = "http://ec2-34-203-38-62.compute-1.amazonaws.com/";
    private HttpURLConnection conn = null;

    private final String twoHyphens = "--";
    private final String boundary = "*****";
    private final String lineEnd = "\r\n";

    public String InsertDataRequest(String urlPage, String paramNames[], String params[], String imgPath, String imgName){
        try{
            URL url = new URL(this.serverUrl +urlPage);

            conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection","Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
            conn.setRequestProperty("Charset","UTF-8");
            if(imgPath!=null) conn.setRequestProperty("uploaded_file",imgPath);

            //파라미터 전송
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            if(paramNames != null){
                for(int i =0; i<paramNames.length;i++){
                    dos.writeBytes(twoHyphens + boundary + lineEnd); //필드 구분자 시작
                    dos.writeBytes("Content-Disposition: form-data; name=\""+paramNames[i]+"\""+ lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.write(params[i].getBytes("UTF-8"));
                    dos.writeBytes(lineEnd);
                }
            }

            //이미지 전송
            if(imgPath != null){
                File sourceFile = new File(imgPath);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+imgName+"\""+lineEnd);
                dos.writeBytes("Content-Type: image/jpeg"+lineEnd+lineEnd);

                FileInputStream fis = new FileInputStream(sourceFile);
                int bytesAvailable = fis.available();
                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable,maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                int bytesRead = fis.read(buffer,0,bufferSize);

                Log.d(TAG,"image byte ="+ bytesRead);

                while(bytesRead > 0 ){
                    dos.write(buffer,0,bufferSize);
                    bytesAvailable = fis.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fis.read(buffer,0,bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                fis.close();
            }

            dos.flush();
            dos.close();

            //응답확인
            int responseStatusCode = conn.getResponseCode();
            Log.d(TAG,"POST response code -" + responseStatusCode);

            InputStream is;
            if(responseStatusCode == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();
            }
            else{
                is = conn.getErrorStream();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;

            while((line=br.readLine()) != null){
                sb.append(line);
            }
            br.close();
            return sb.toString().trim();
        } catch(Exception e){
            Log.e(TAG,"InsertData : Error "+e.getMessage());
        }
        return null;
    }

    public String GetDataRequest(String urlPage, String paramNames[], String params[]) {
        try{
            URL url = new URL(this.serverUrl +urlPage);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream outputStream = conn.getOutputStream();

            String postParameters = null;

            if(params != null){
                postParameters = paramNames[0] + "=" + params[0];
                for(int i =1; i<paramNames.length;i++){
                    postParameters += "&"+ paramNames[i] + "=" + params[i];
                }
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();

            }
            outputStream.close();

            //응답확인
            int responseStatusCode = conn.getResponseCode();
            Log.d(TAG,"POST response code -" + responseStatusCode);

            InputStream is;
            if(responseStatusCode == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();
            }
            else{
                is = conn.getErrorStream();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;

            while((line=br.readLine()) != null){
                sb.append(line);
            }

            br.close();
            return sb.toString().trim(); //result: JsonString

        } catch(Exception e){
            Log.e(TAG,"GetData : Error"+e.getMessage());
            return null;
        }
    }
}