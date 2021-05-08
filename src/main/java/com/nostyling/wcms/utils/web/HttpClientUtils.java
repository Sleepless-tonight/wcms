package com.nostyling.wcms.utils.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @program: oms_api_v2
 * @author: shiliang
 * @create: 2019-08-26 14:46
 * @description:
 **/


public class HttpClientUtils {
    private final static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);


    /**
     * 默认超时时间
     */
    private final static int DEFAULT_TIMEOUT = 20000;
    private final static int MAX_TOTAL = 50;
    private final static int DEFAULT_MAX_PER_ROUTE = 10;
    /**
     * ThreadSafeClientConnManager 是一个 HttpClient 个线程连接管理器。
     */
    private static PoolingHttpClientConnectionManager connectionManager;

    private static RequestConfig defaultRequestConfig;


    private int timeout = 0;

    /**
     * 代理对象
     */
    private CredentialsProvider credsProvider;
    private HttpHost proxy;

    private Map <String, String> headerMap;
    private Map <String, ?> fromMap;
    private HttpEntity entity;
    private RequestBuilder requestBuilder;


    static {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_TOTAL);
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);//例如默认每路由最高10并发，具体依据业务来定
        defaultRequestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(DEFAULT_TIMEOUT)
                .setSocketTimeout(DEFAULT_TIMEOUT)
                .setConnectTimeout(DEFAULT_TIMEOUT).build();

    }


    public HttpClientUtils() {

    }

    /**
     * 以指定类型解析 response ，默认 String。
     *
     * @return
     */
    public String execute() throws IOException {
        return execute(null, null);
    }

    public <T> T execute(Class <T> clazz) throws IOException {
        return execute(clazz, null);
    }

    public <T> T execute(TypeReference <T> type) throws IOException {

        return execute(null, type);
    }

    private <T> T execute(Class <T> clazz, TypeReference <T> type) throws IOException {
        T resultEntity = null;
        RequestConfig.Builder requestConfig = null;

        if (0 != timeout || null != proxy) {
            requestConfig = RequestConfig.copy(defaultRequestConfig);
            if (0 != timeout) {
                requestConfig.setSocketTimeout(timeout)
                        .setConnectTimeout(timeout)
                        .setConnectionRequestTimeout(timeout);
            }
            if (null != proxy) {
                requestConfig.setProxy(proxy);
            }

        }

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(connectionManager);

        if (null != credsProvider) {
            httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
        }
        CloseableHttpClient httpClient = httpClientBuilder.build();

        if (null != fromMap && !fromMap.isEmpty()) {
            for (Map.Entry <String, ?> entry : fromMap.entrySet()) {
                requestBuilder.addParameter(entry.getKey(),entry.getValue().toString());
            }
        }

        if (null != this.entity) {
            requestBuilder.setEntity(this.entity);
        }

        if (null != this.headerMap) {
            for (Map.Entry <String, String> entry : headerMap.entrySet()) {
                requestBuilder.setHeader(entry.getKey(), entry.getValue());
            }
        }
        if (null != requestConfig) {
            requestBuilder.setConfig(requestConfig.build());
        } else {
            requestBuilder.setConfig(defaultRequestConfig);
        }

        HttpUriRequest httpUriRequest = requestBuilder.build();
        logger.debug("HttpUriRequest={}",httpUriRequest);
        CloseableHttpResponse execute = httpClient.execute(httpUriRequest);
        logger.debug("CloseableHttpResponse={}",execute);

        try {
            StatusLine statusLine = execute.getStatusLine();
            //获取响应实体 通常来说响应实体以流的形式是是需要关闭流的， EntityUtils.toString  在内部会关闭流
            HttpEntity responseEntity = execute.getEntity();

            if (statusLine.getStatusCode() >= 300) {
                throw new HttpResponseException(
                        statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }
            if (responseEntity == null) {
                throw new ClientProtocolException("Response is null");
            }
            // 请求发送成功，并得到响应
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                String strResult = null;

                try {
                    // 读取服务器返回过来的json字符串数据
                    strResult = EntityUtils.toString(responseEntity, Consts.UTF_8);
                    // 把json字符串转换成json对象
                    if (null != clazz) {
                        resultEntity = JSONObject.parseObject(strResult, clazz);
                    } else if (null != type) {
                        resultEntity = JSONObject.parseObject(strResult, type);
                    } else {
                        resultEntity = (T) strResult;
                    }

                } catch (JSONException e) {
                    logger.error("读取服务器返回过来的json字符串序列化失败,responseEntity={}。", strResult, e);
                    throw e;
                }
            } else {
                throw new HttpResponseException(
                        statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }
        } finally {
            execute.close();
            //httpClient.close();
        }

        return resultEntity;
    }

    /**
     * 发送post请求
     *
     * @param uri
     * @return
     */
    public static HttpClientUtils post(String uri) {
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        httpClientUtils.requestBuilder = RequestBuilder.post(uri);
        return httpClientUtils;
    }

    /**
     * 发送get请求
     *
     * @param uri
     * @return
     */
    public static HttpClientUtils get(String uri) {
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        httpClientUtils.requestBuilder = RequestBuilder.get(uri);
        return httpClientUtils;
    }

    /**
     * 设置超时时间
     *
     * @param timeout
     * @return
     */
    public HttpClientUtils setTimeout(int timeout) {
        if (timeout != 0) {
            this.timeout = timeout;
        }
        return this;
    }

    /**
     * 设置代理
     *
     * @param proxyHost
     * @param proxyPort
     * @param userName
     * @param password
     * @return
     */
    public HttpClientUtils setProxy(final String proxyHost, final int proxyPort, final String userName, final String password) {

        this.credsProvider = new BasicCredentialsProvider();
        this.credsProvider.setCredentials(
                new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(userName, password));
        // 设置代理HttpHost
        this.proxy = new HttpHost(proxyHost, proxyPort);
        return this;
    }

    /**
     * 设置请求头
     *
     * @param headerMap
     * @return
     */
    public HttpClientUtils setHeader(Map <String, String> headerMap) {
        if (null != headerMap && !headerMap.isEmpty()) {
            this.headerMap = headerMap;
        }
        return this;
    }

    /**
     * 设置表单请求参数
     *
     * @param formParams
     * @return
     */
    public HttpClientUtils setForm(Map <String, ?> formParams) {
        if (null != formParams && !formParams.isEmpty()) {
            List <NameValuePair> formMaps = new ArrayList <NameValuePair>(formParams.size());
            for (Map.Entry <String, ?> entry : formParams.entrySet()) {
                formMaps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            this.entity = new UrlEncodedFormEntity(formMaps, Consts.UTF_8);

        }
        return this;
    }

    /**
     * 设置表单请求参数
     *
     * @param formParams
     * @return
     */
    public HttpClientUtils setForms(Map <String, ?> formParams) {
        if (null != formParams && !formParams.isEmpty()) {

            this.fromMap = formParams;

        }
        return this;
    }

    /**
     * 设置 body 请求参数
     * <pre>
     * 1. 标准参数，例如 a=1&amp;b=2 这种格式（先不实现这个）
     * 2. Rest模式，此时 body 需要传入一个JSON或者XML字符串（默认Json）
     * @return
     */
    public HttpClientUtils setBody(Map <String, ?> bodyMap) {
        if (null != bodyMap && !bodyMap.isEmpty()) {
            setBody(JSON.toJSONString(bodyMap), "application/json");
        }
        return this;
    }

    /**
     * 设置 body 请求参数
     * <pre>
     * 1. 标准参数，例如 a=1&amp;b=2 这种格式（先不实现这个）
     * 2. Rest模式，此时 body 需要传入一个JSON或者XML字符串（默认Json）
     * @return
     */
    public HttpClientUtils setBody(String bodyString) {
        if (null != bodyString && !bodyString.isEmpty()) {
            setBody(bodyString, "application/json");
        }
        return this;
    }

    /**
     * 设置 body 请求参数
     * <pre>
     * 1. 标准参数，例如 a=1&amp;b=2 这种格式（先不实现这个）
     * 2. Rest模式，此时 body 需要传入一个JSON或者XML字符串（默认Json）
     * @return
     */
    public HttpClientUtils setBody(String bodyString, String contentType) {
        if (null != bodyString && !bodyString.isEmpty()) {
            StringEntity entity = new StringEntity(bodyString, Consts.UTF_8);
            entity.setContentEncoding(Consts.UTF_8.toString());
            entity.setContentType(contentType);
            entity.setChunked(true);
            this.entity = entity;
        }
        return this;
    }


    public static void main(String[] args) throws IOException {
        //HttpUriRequest httpUriRequest = RequestBuilder.post()
        //        .setUri(new URI("http://gw.api.taobao.com/router/rest"))
        //        .setEntity(entity)
        //        .setConfig(requestConfig)
        //        .build();
        String httpPost = "http://localhost:8080/router/restJson";//local
        String proxyHost = "59.110.70.158";
        int proxyPort = 80;
        String userName = "taobao-oms-proxy";
        String password = "Dayi@7568";
        HashMap <String, Object> jsonParam = new HashMap <>();
        jsonParam.put("No", 888);

        int count = 10;
        //与countDownLatch.await();实现运行完所有线程之后才执行后面的操作
        final CountDownLatch cdl = new CountDownLatch(count);
        //final CyclicBarrier barrier = new CyclicBarrier(count);  //与barrier.await() 实现并发;
        int j = 0;
        for (int i = 0; i < count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    cdl.countDown();
                    try {
                        //这一步是为了将全部线程任务执行完以后，开始执行后面的任务（计算时间，数量）
                        cdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        String execute = HttpClientUtils.post(httpPost).setBody(Thread.currentThread().toString()).execute();
                        System.out.println(execute + Thread.currentThread().toString() + "、");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        System.out.println("************************");

    }
}


