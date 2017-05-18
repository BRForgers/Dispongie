package me.d4rk.dispongie.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;


public class Hastebin {
    public static String paste(String toSend) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://hastebin.com/documents");
        try {
            post.setHeader("Charset","UTF-8");
            post.setHeaders(new Header[] { new BasicHeader("Charset","UTF-8"), new BasicHeader("Content-Type", "text/plain")});
            post.setEntity(new StringEntity(toSend));
            HttpResponse response = httpClient.execute(post);
            JSONObject o = new JSONObject(EntityUtils.toString(response.getEntity()));
            return "https://hastebin.com/" + o.getString("key");
        } catch (IOException e) {
            return "Hastebin Error";
        }

    }
}
