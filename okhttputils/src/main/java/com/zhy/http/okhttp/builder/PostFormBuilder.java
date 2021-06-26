package com.zhy.http.okhttp.builder;

import com.socks.library.KLog;
import com.zhy.http.okhttp.request.PostFormRequest;
import com.zhy.http.okhttp.request.RequestCall;
import com.zhy.http.okhttp.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhy on 15/12/14.
 */
public class PostFormBuilder extends OkHttpRequestBuilder<PostFormBuilder> implements HasParamsable {
    private List<FileInput> files = new ArrayList<>();

    @Override
    public RequestCall build() {
        return new PostFormRequest(url, tag, params, headers, files, id).build();
    }

    public PostFormBuilder files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    public PostFormBuilder addFile(String name, String filename, File file) {
        files.add(new FileInput(name, filename, file));
        return this;
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }


    @Override
    public PostFormBuilder params(Map<String, String> params) {
        params = setParams(params);
        this.params = params;
        params(this.url, this.params);
        return this;
    }

    @Override
    public PostFormBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    public static Map<String, String> setParams(Map<String, String> params) {
        Map<String, String> map = new HashMap();
        Iterator var2 = params.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var2.next();
            if (!strEmpty((String)entry.getValue())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }

        String time = System.currentTimeMillis() / 1000L + "";
        String jsonText = Util.gson.toJson(map);
        map.put("data", jsonText);
        map.put("time", time);
        map.put("checksum", Util.getSha1("nyyc" + Util.toMD5(jsonText) + time));
        return map;
    }

    public static boolean strEmpty(String value) {
        return null == value || "".equals(value) || "null".equals(value);
    }

    public static void params(String url, Map<String, String> map) {
        String params = "";

        Map.Entry entry;
        for(Iterator var3 = map.entrySet().iterator(); var3.hasNext(); params = params + (String)entry.getKey() + "=" + (String)entry.getValue() + "&") {
            entry = (Map.Entry)var3.next();
        }

        KLog.e("请求参数 ------ " + url + "?" + params);
    }

}
