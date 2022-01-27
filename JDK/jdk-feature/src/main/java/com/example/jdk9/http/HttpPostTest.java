package com.example.jdk9.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class HttpPostTest {

    static int count = 0;

    static int chatCnt = 0;

    public static void main(String[] args) throws Exception {
        //原始测试集地址
        File files = new File("/Users/zxj/Desktop/test");

        for (File file : files.listFiles()) {

            if (file.getAbsolutePath().endsWith(".DS_Store"))
                continue;

            System.out.println(file.getAbsolutePath());
//
            List<String> list = getListByFile(file.getAbsolutePath());

            System.out.println(chatCnt / (count * 1.0));

//            //生成的测试集地址
            String out = "/Users/zxj/Desktop/uncertainIntent_out.txt";
//          System.out.println(list);

            for (String line : list) {
                // 语料 + tab键 + 模型名称 + tab键 + 标签名称
//             line = line.replaceAll(" ","")+"\t"+ "pingan_medical_law_2.0"+ "\t" + "PA_MEDICAL";
                if (!sendGet(line)) {
                    writeFileAdd(new File(out), line);
                }
            }
        }
    }

    private static boolean sendGet(String text) {
        try {
            HttpClient client = HttpClient.newHttpClient();

//          // GET
//          HttpResponse<String> response = client.send(
//                  HttpRequest
//                          .newBuilder(new URI("http://pabst-core-stg.pingan.com.cn/service/iss?appkey=lkp5ya565i6xiuljzmctywqem7v7a3k73jlj32yj&text="+text+"&city=&ver=3.2&scenario=&udid=dafds&appver=&isdialog=True&time=2019-01-09-12:39:17&debug=true&voiceid=&gps=&method=iss.getTalk&dpi=&history=&appsig=E5D986C1AFFA37601E02ADE153F6A8F70BE3C18E"))
//                          .GET()
//                          .build(),
//                  HttpResponse.BodyHandlers.ofString()
//          );

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("word", text);

            // POST
            HttpResponse<String> response = client.send(
                    HttpRequest
                            .newBuilder(new URI("http://wwww.baidu.com"))
                            .header("Content-Type", "application/json")
                            .header("token", "88080a24149f4fea839ad64dcdc5b7e3")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString())).build(), HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            JSONObject json = JSON.parseObject(body);

            String str = json.getString("code");

            boolean status = json.getJSONObject("data").getBoolean("status");

            if (status && "5200".equals(str)) {
                System.out.println("true");
            } else {
                return false;
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     * 读入TXT文件 返回测试集case集合
     *
     * @param pathname
     * @return List<List < String>>
     */
    public static List<String> getListByFile(String pathname) {
        String line = null;
        List<String> list = new ArrayList<String>();
        try (BufferedReader bf = new BufferedReader(new FileReader(pathname))) {
            int num = 0;

            while ((line = bf.readLine()) != null) {
                if (line.length() > 0) {
                    String[] lines = line.split("\\t");
//                    if (lines.length == 2) {
                    list.add(lines[0]);
//                    }
                    num++;
                    count++;
                    if (num > 2500) break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 追加写入TXT文件
     * 在文件末尾添加
     */
    public static void writeFileAdd(File f, String result) {

        try (FileWriter fw = new FileWriter(f, true)) {
            fw.write(result);
            fw.write("\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
