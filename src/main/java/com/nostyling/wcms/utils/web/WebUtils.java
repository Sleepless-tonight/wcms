package com.nostyling.wcms.utils.web;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author shiliang
 * @Classname WebUtils
 * @Date 2020/8/19 15:30
 * @Description WebUtils
 */
public class WebUtils {
    private static final TimeZone TZ_GMT8 = TimeZone.getTimeZone("GMT+8");

    /**
     * @param ctype request.getContentType()
     * @return
     */
    public static String getResponseCharset(String ctype) {
        String charset = "UTF-8";
        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            String[] var3 = params;
            int var4 = params.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String param = var3[var5];
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2 && !StringUtils.isEmpty(pair[1])) {
                        charset = pair[1].trim();
                    }
                    break;
                }
            }
        }

        return charset;
    }

    /**
     *
     * @param request
     * @param charset
     * @return
     * @throws IOException
     */
    public static Map<String, String> getHeaderMap(HttpServletRequest request, String charset) throws IOException {
        return getHeaderMap(request, charset, null);
    }

    /**
     *
     * @param request
     * @param charset
     * @param headerNames
     * @return
     * @throws IOException
     */
    public static Map<String, String> getHeaderMap(HttpServletRequest request, String charset, List<String> headerNames) throws IOException {
        Map<String, String> headerMap = new HashMap();
        //获得所有头信息的name
        Enumeration<String> names = request.getHeaderNames();
        if (null != names) {
            //输出所有的头信息的名字
            while (names.hasMoreElements()) {
                //获取request 的请求头名字name
                String key = names.nextElement();
                //根据头名字获取对应的值
                String value = request.getHeader(key);
                boolean contains = true;
                if (null != headerNames && headerNames.size() > 0) {
                    contains = headerNames.contains(key);
                }
                if (contains) {
                    if (StringUtils.isEmpty(value)) {
                        headerMap.put(key, "");
                    } else {
                        headerMap.put(key, URLDecoder.decode(value, charset));
                    }
                }

            }
        }
        return headerMap;
    }

    /**
     *
     * @param request
     * @param charset
     * @return
     * @throws IOException
     */
    public static Map<String, String> getQueryMap(HttpServletRequest request, String charset) throws IOException {
        Map<String, String> queryMap = new HashMap();
        String queryString = request.getQueryString();
        if (null != queryString) {
            String[] params = queryString.split("&");

            for (int i = 0; i < params.length; ++i) {
                String[] kv = params[i].split("=");
                String key;
                if (kv.length == 2) {
                    key = URLDecoder.decode(kv[0], charset);
                    String value = URLDecoder.decode(kv[1], charset);
                    queryMap.put(key, value);
                } else if (kv.length == 1) {
                    key = URLDecoder.decode(kv[0], charset);
                    queryMap.put(key, "");
                }
            }
        }

        return queryMap;
    }

    /**
     *
     * @param request
     * @param queryMap
     * @return
     * @throws IOException
     */
    public static Map<String, String> getFormMap(HttpServletRequest request, Map<String, String> queryMap) throws IOException {
        Map<String, String> formMap = new HashMap();
        Set<?> keys = request.getParameterMap().keySet();
        Iterator var4 = keys.iterator();

        while (var4.hasNext()) {
            Object tmp = var4.next();
            String key = String.valueOf(tmp);
            if (!queryMap.containsKey(key)) {
                String value = request.getParameter(key);
                if (StringUtils.isEmpty(value)) {
                    formMap.put(key, "");
                } else {
                    formMap.put(key, value);
                }
            }
        }

        return formMap;
    }

    /**
     *
     * @param stream
     * @param charset
     * @return
     * @throws IOException
     */
    public static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);
            StringBuilder response = new StringBuilder();
            char[] buff = new char[1024];
            boolean var5 = false;

            int read;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }

            String var6 = response.toString();
            return var6;
        } finally {
            if (stream != null) {
                stream.close();
            }

        }
    }

    /**
     *
     * @param response
     * @param obj
     * @throws IOException
     */
    public static void responseJson(HttpServletResponse response, Object obj) throws IOException {
        responseJson(response, JSON.toJSONString(obj));
    }

    /**
     *
     * @param response
     * @param obj
     * @throws IOException
     */
    public static void responseJson(HttpServletResponse response, String obj) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(obj);
        writer.close();
        response.flushBuffer();
    }

    /**
     *
     * @param str
     * @return
     */
    public static Date parseDateTime(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TZ_GMT8);

        try {
            return format.parse(str);
        } catch (ParseException var3) {
            throw new RuntimeException(var3);
        }
    }
}

