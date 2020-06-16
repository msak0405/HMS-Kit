package com.huawei.codelabkit.utilities;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

public class Global {

    public static String getToken() {
        String token = null, line;
        StringBuffer jsonString = new StringBuffer();
        try {
            URL url = new URL("https://connect-api.cloud.huawei.com/api/oauth2/v1/token");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            //conn.setRequestProperty("authorization", Authorization);

            JSONObject keyString = new JSONObject();
            keyString.put("client_id", "383204155885356096");
            keyString.put("client_secret", "6622F129C6AD152432517581DE366720C0470307954CF42143EB7C357CD5A27E");
            keyString.put("grant_type", "client_credentials");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(keyString.toString());
            writer.flush();
            writer.close();
            os.close();

            int statusCode = conn.getResponseCode();
            //System.out.println("statusCode: authorization " + url + ": "+ statusCode);
            InputStream is = null;
            if (statusCode >= 200 && statusCode < 400) {
                is = conn.getInputStream();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            conn.connect();

            System.out.println(jsonString.toString());
//        HttpPost post = new HttpPost(domain + "/oauth2/v1/token");
//            JSONObject keyString = new JSONObject();
//            keyString.put("client_id", "18893***83957248");
//            keyString.put("client_secret", "B15B497B44E080EBE2C4DE4E74930***52409516B2A1A5C8F0FCD2C579A8EB14");
//            keyString.put("grant_type", "client_credentials");
//            StringEntity entity = new StringEntity(keyString.toString(), Charset.forName("UTF-8"));
//            entity.setContentEncoding("UTF-8");
//            entity.setContentType("application/json");
//            post.setEntity(entity);
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpResponse response = httpClient.execute(post);
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == HttpStatus.SC_OK) {
//                BufferedReader br =
//                        new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Consts.UTF_8));
//                String result = br.readLine();
//                JSONObject object = JSON.parseObject(result);
//                token = object.getString("access_token");
//            }
//            post.releaseConnection();
//            httpClient.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
       return jsonString.toString();
    }
}
