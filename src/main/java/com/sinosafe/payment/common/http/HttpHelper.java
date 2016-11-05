package com.sinosafe.payment.common.http;

import com.sinosafe.payment.common.GalaxyX509TrustManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
public class HttpHelper {
    
    public enum Method {  
        GET, POST  
    }  
      
    public static HttpHelper connect(String url) {  
        HttpHelper http = new HttpHelper();  
        http.url(url);  
        return http;  
    }  
  
    public static HttpHelper connect(URL url) {  
        HttpHelper http = new HttpHelper();  
        http.url(url);  
        return http;  
    }  
  
    private Request req;
    private Response res;
  
    private HttpHelper() {  
        req = new Request();  
        res = new Response();  
    }  
  
    public HttpHelper url(URL url) {  
        req.url(url);  
        return this;  
    }  
  
    public HttpHelper url(String url) {  
        try {  
            req.url(new URL(url));  
        } catch (MalformedURLException e) {  
            throw new IllegalArgumentException("Malformed URL: " + url, e);  
        }  
        return this;  
    }  
      
    public HttpHelper userAgent(String userAgent) {  
        req.header("User-Agent", userAgent);  
        return this;  
    }  
  
    public HttpHelper timeout(int millis) {  
        req.timeout(millis);  
        return this;  
    }  
  
    public HttpHelper maxBodySize(int bytes) {  
        req.maxBodySize(bytes);  
        return this;  
    }  
  
    public HttpHelper followRedirects(boolean followRedirects) {  
        req.followRedirects(followRedirects);  
        return this;  
    }  
  
    public HttpHelper referrer(String referrer) {   
        req.header("Referer", referrer);  
        return this;  
    }  
  
    public HttpHelper method(Method method) {  
        req.method(method);  
        return this;  
    }  
  
    public HttpHelper ignoreHttpErrors(boolean ignoreHttpErrors) {  
        req.ignoreHttpErrors(ignoreHttpErrors);  
        return this;  
    }  
  
    public HttpHelper data(String key, String value) {  
        req.data(KeyVal.create(key, value));  
        return this;  
    }  
  
    public HttpHelper data(Map<String, String> data) {  
        for (Map.Entry<String, String> entry : data.entrySet()) {  
            req.data(KeyVal.create(entry.getKey(), entry.getValue()));  
        }  
        return this;  
    }  
  
  
    public HttpHelper header(String name, String value) {  
        req.header(name, value);  
        return this;  
    }  
  
    public HttpHelper cookie(String name, String value) {  
        req.cookie(name, value);  
        return this;  
    }  
  
    public HttpHelper cookies(Map<String, String> cookies) {  
        for (Map.Entry<String, String> entry : cookies.entrySet()) {  
            req.cookie(entry.getKey(), entry.getValue());  
        }  
        return this;  
    }  
  
    public Response get() throws Exception {
        req.method(Method.GET);  
        res = Response.execute(req);  
        return res;  
    }  
  
    public Response post() throws Exception {
        req.method(Method.POST);  
        res = Response.execute(req);  
        return res;  
    }  
    public Response post(String data) throws Exception {
        req.method(Method.POST);  
        res = Response.execute(req,data);  
        return res;  
    } 
    public Request request() {
        return req;  
    }  
  
    public HttpHelper request(Request request) {
        req = request;  
        return this;  
    }  
      
    public HttpHelper charset(String charset) {  
        req.charset(charset);  
        return this;  
    }  
  
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    private static abstract class Base<T extends Base> {  
        private URL url;  
        private Method method;  
        private Map<String, String> headers;  
        private Map<String, String> cookies;  
  
        private Base() {  
            headers = new LinkedHashMap<String, String>();  
            cookies = new LinkedHashMap<String, String>();  
        }  
  
        public URL url() {  
            return url;  
        }  
  
        public T url(URL url) {  
            this.url = url;  
            return (T) this;  
        }  
  
        public Method method() {  
            return method;  
        }  
  
        public T method(Method method) {  
            this.method = method;  
            return (T) this;  
        }  
  
        public String header(String name) {  
            return getHeaderCaseInsensitive(name);  
        }  
  
        public T header(String name, String value) {  
            removeHeader(name); 
            headers.put(name, value);  
            return (T) this;  
        }  
  
        public boolean hasHeader(String name) {  
            return getHeaderCaseInsensitive(name) != null;  
        }  
  
        public T removeHeader(String name) {  
            Map.Entry<String, String> entry = scanHeaders(name); 
            if (entry != null) {  
                headers.remove(entry.getKey()); 
            }  
            return (T) this;  
        }  
  
        public Map<String, String> headers() {  
            return headers;  
        }  
  
        private String getHeaderCaseInsensitive(String name) {  
            
            String value = headers.get(name);  
            if (value == null) {  
                value = headers.get(name.toLowerCase());  
            }  
            if (value == null) {  
                Map.Entry<String, String> entry = scanHeaders(name);  
                if (entry != null) {  
                    value = entry.getValue();  
                }  
            }  
            return value;  
        }  
  
        private Map.Entry<String, String> scanHeaders(String name) {  
            String lc = name.toLowerCase();  
            for (Map.Entry<String, String> entry : headers.entrySet()) {  
                if (entry.getKey().toLowerCase().equals(lc)) {  
                    return entry;  
                }  
            }  
            return null;  
        }  
  
        public String cookie(String name) {  
            return cookies.get(name);  
        }  
  
        public T cookie(String name, String value) {  
            cookies.put(name, value);  
            return (T) this;  
        }  
  
        public boolean hasCookie(String name) {  
            return cookies.containsKey(name);  
        }  
  
        public T removeCookie(String name) {  
            cookies.remove(name);  
            return (T) this;  
        }  
  
        public Map<String, String> cookies() {  
            return cookies;  
        }  
    }  
  
    public static class Request extends Base<Request> {  
          
        private int timeoutMilliseconds;  
        private int maxBodySizeBytes;  
        private boolean followRedirects;  
        private Collection<KeyVal> data;
        private boolean ignoreHttpErrors = false;  
        private String charset;  
  
        private Request() {  
            timeoutMilliseconds = 3000;  
            maxBodySizeBytes = 3 * 1024 * 1024; 
            followRedirects = true;  
            data = new ArrayList<KeyVal>();
            super.method = Method.GET;  
            super.headers.put("Accept-Encoding", "gzip");  
        }  
  
        public int timeout() {  
            return timeoutMilliseconds;  
        }  
  
        public Request timeout(int millis) {  
            timeoutMilliseconds = millis;  
            return this;  
        }  
  
        public int maxBodySize() {  
            return maxBodySizeBytes;  
        }  
  
        public Request maxBodySize(int bytes) {
            maxBodySizeBytes = bytes;  
            return this;  
        }  
  
        public boolean followRedirects() {  
            return followRedirects;  
        }  
  
        public Request followRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;  
            return this;  
        }  
  
        public boolean ignoreHttpErrors() {  
            return ignoreHttpErrors;  
        }  
  
        public Request ignoreHttpErrors(boolean ignoreHttpErrors) {
            this.ignoreHttpErrors = ignoreHttpErrors;  
            return this;  
        }  
  
        public Request data(KeyVal keyval) {
            data.add(keyval);  
            return this;  
        }  
  
        public Collection<KeyVal> data() {
            return data;  
        }  
          
        public String charset() {  
            return charset;  
        }  
          
        public void charset(String charset) {  
            this.charset = charset;  
        }  
          
    }  
  
    public static class Response extends Base<Response> {  
          
        private static final String defaultCharset = "UTF-8"; 
          
        private static final int MAX_REDIRECTS = 20;  
        private int statusCode;  
        private String statusMessage;  
        private ByteBuffer byteData;  
        private String charset;  
        private String contentType;  
        private boolean executed = false;  
        private int numRedirects = 0;  
        @SuppressWarnings("unused")  
        private Request req;
  
        Response() {  
            super();  
        }  
  
        private Response(Response previousResponse) throws IOException {  
            super();  
            if (previousResponse != null) {  
                numRedirects = previousResponse.numRedirects + 1;  
                if (numRedirects >= MAX_REDIRECTS) {  
                    throw new IOException(String.format("Too many redirects occurred trying to load URL %s", previousResponse.url()));  
                }  
            }  
        }  
        private static Response execute(Request req,String data) throws Exception {
            return execute(req, null,data);  
        }  
        private static Response execute(Request req) throws Exception {
            return execute(req, null,null);  
        }  
  
        private static Response execute(Request req, Response previousResponse,String data) throws Exception {
              
            String protocol = req.url().getProtocol(); 
            if (!protocol.equals("http") && !protocol.equals("https")) {  
                throw new MalformedURLException("Only http & https protocols supported");  
            }  
            
            if (req.method() == Method.GET && req.data().size() > 0) {
                serialiseRequestUrl(req); 
            }  
            HttpURLConnection conn = createConnection(req);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setRequestProperty("Content-type", "application/json");
            Response res;  
            try {  
                conn.connect();  
                if (req.method() == Method.POST) {  
                	writePost(req.data(),data, conn.getOutputStream());  
                }  
                int status = conn.getResponseCode();  
                boolean needsRedirect = false;  
                if (status != HttpURLConnection.HTTP_OK) {  
                    if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER) {  
                        needsRedirect = true;  
                    } else if (!req.ignoreHttpErrors()) {  
                        throw new HttpStatusException("HTTP error fetching URL", status, req.url().toString());  
                    }  
                }  
                res = new Response(previousResponse);  
                res.setupFromConnection(conn, previousResponse);  
                if (needsRedirect && req.followRedirects()) {  
                    req.method(Method.GET); 
                    req.data().clear();  
                    req.url(new URL(req.url(), res.header("Location")));  
                    for (Map.Entry<String, String> cookie : res.cookies().entrySet()) { 
                        req.cookie(cookie.getKey(), cookie.getValue());  
                    }  
                    return execute(req, res,null);  
                }  
                res.req = req;  
  
                InputStream dataStream = null;  
                InputStream bodyStream = null;  
                try {  
                    dataStream = conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream();  
                    bodyStream = res.hasHeader("Content-Encoding") && res.header("Content-Encoding").equalsIgnoreCase("gzip") ?  
                            new BufferedInputStream(new GZIPInputStream(dataStream)) :  
                            new BufferedInputStream(dataStream);  
  
                    res.byteData = readToByteBuffer(bodyStream, req.maxBodySize());  
                    if(req.charset() == null) {  
                        res.charset = getCharsetFromContentType(res.contentType); 
                    } else {  
                        res.charset = req.charset;  
                    }  
                } finally {  
                    if (bodyStream != null) {   
                        bodyStream.close();  
                    }  
                    if (dataStream != null) {  
                        dataStream.close();  
                    }  
                }  
            } finally {  
                conn.disconnect();  
            }  
  
            res.executed = true;  
            return res;  
        }  
  
        public int statusCode() {  
            return statusCode;  
        }  
  
        public String statusMessage() {  
            return statusMessage;  
        }  
  
        public String charset() {  
            return charset;  
        }  
          
        public String contentType() {  
            return contentType;  
        }  
  
        public String html() throws Exception {  
              
            String content = null;  
              
            if(charset == null) {  
                  
                content = Charset.forName(defaultCharset).decode(byteData).toString();  
                  
                Pattern pattern = Pattern.compile("<meta[^>]*content=\"(.+?)\"[^>]*>");  
                  
                Matcher matcher = pattern.matcher(content);  
                  
                String foundCharset = null;  
                  
                while (matcher.find()) {  
                    foundCharset = getCharsetFromContentType(matcher.group(1));  
                    if(foundCharset != null) {  
                        break;  
                    }  
                }  
                  
                if(foundCharset == null) {  
                    pattern = Pattern.compile("<meta[^>]*charset=\"(.+?)\"[^>]*>");  
                    matcher = pattern.matcher(content);  
                    while (matcher.find()) {  
                        foundCharset = matcher.group(1);  
                        if(foundCharset != null) {  
                            break;  
                        }  
                    }  
                }  
                  
                if(foundCharset != null) {  
                    charset = foundCharset;  
                    content = Charset.forName(foundCharset).decode(byteData).toString();  
                }  
            } else {  
                content = Charset.forName(charset).decode(byteData).toString();  
            }  
              
            if (content.length() > 0 && content.charAt(0) == 65279) {  
                content = content.substring(1);  
            }  
            byteData.rewind();  
              
            return content;  
        }  
          
        public void toFile(String to) {  
              
            makeDirs(to);  
              
            generateFile(to);  
              
        }  
          
        private static void makeDirs(String path) {  
            try {  
                int i = path.lastIndexOf("/");  
                if (i < 1) {  
                    i = path.lastIndexOf("\\");  
                }  
                path = path.substring(0, i);  
                File file = new File(path);  
                if (!file.exists()) {  
                    file.mkdirs();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
          
        private void generateFile(String to) {  
            OutputStream bos = null;  
            try {  
                bos = new BufferedOutputStream(new FileOutputStream(to));  
                if(byteData != null) {  
                    bos.write(byteData.array(), 0, byteData.array().length);  
                }  
                bos.flush();  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            } finally {  
                if (bos != null) {  
                    try {  
                        bos.close();  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
  
        
        private static HttpURLConnection createConnection(Request req) throws Exception {
        	 
        	
            HttpURLConnection conn = (HttpURLConnection) req.url().openConnection();  
            conn.setRequestMethod(req.method().name());  
            conn.setInstanceFollowRedirects(false); 
            conn.setConnectTimeout(req.timeout());  
            conn.setReadTimeout(req.timeout());  
            if (req.method() == Method.POST) {  
                conn.setDoOutput(true);  
            }  
            if (req.cookies().size() > 0) {  
                conn.addRequestProperty("Cookie", getRequestCookieString(req));  
            }  
            for (Map.Entry<String, String> header : req.headers().entrySet()) {  
                conn.addRequestProperty(header.getKey(), header.getValue());  
            }  
            if(req.url().getProtocol().equals("https")){
            	TrustManager[] tm = { new GalaxyX509TrustManager() };
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
                sslContext.init(null, tm, new java.security.SecureRandom());  
                
                SSLSocketFactory ssf = sslContext.getSocketFactory();  
                HttpsURLConnection conn2=(HttpsURLConnection)conn;
                conn2.setSSLSocketFactory(ssf);  
            }
            return conn;  
        }  
  
        
        private void setupFromConnection(HttpURLConnection conn, Response previousResponse) throws IOException {
            super.method = Method.valueOf(conn.getRequestMethod());
            super.url = conn.getURL();  
            statusCode = conn.getResponseCode();  
            statusMessage = conn.getResponseMessage();  
            contentType = conn.getContentType();  
  
            Map<String, List<String>> resHeaders = conn.getHeaderFields();  
            processResponseHeaders(resHeaders);  
  
            
            if (previousResponse != null) {  
                for (Map.Entry<String, String> prevCookie : previousResponse.cookies().entrySet()) {  
                    if (!hasCookie(prevCookie.getKey())) {  
                        cookie(prevCookie.getKey(), prevCookie.getValue());  
                    }  
                }  
            }  
        }  
  
        private void processResponseHeaders(Map<String, List<String>> resHeaders) {  
            for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {  
                String name = entry.getKey();  
                if (name == null) {  
                    continue; 
                }  
  
                List<String> values = entry.getValue();  
                if (name.equalsIgnoreCase("Set-Cookie")) {  
                    for (String value : values) {  
                        if (value == null){  
                            continue;  
                        }  
                        TokenQueue cd = new TokenQueue(value);  
                        String cookieName = cd.chompTo("=").trim();  
                        String cookieVal = cd.consumeTo(";").trim();  
                        if (cookieVal == null) {  
                            cookieVal = "";  
                        }  
                        
                        
                        if (cookieName != null && cookieName.length() > 0) {  
                            cookie(cookieName, cookieVal);  
                        }  
                    }  
                } else { 
                    if (!values.isEmpty()) {  
                        header(name, values.get(0));  
                    }  
                }  
            }  
        }  
  
        private static void writePost(Collection<KeyVal> data,String strData, OutputStream outputStream) throws IOException {
        	
            OutputStreamWriter w = new OutputStreamWriter(outputStream, defaultCharset);  
            
            if(strData!=null&&!strData.trim().equals("")){
            	 w.write(strData); 
            }
            boolean first = true; 
            if(data!=null&&data.size()>0){
	            for (KeyVal keyVal : data) {
	                if (!first) {  
	                    w.append('&');  
	                } else {  
	                    first = false;  
	                }  
	                  
	                w.write(URLEncoder.encode(keyVal.key(), defaultCharset));  
	                w.write('=');  
	                w.write(URLEncoder.encode(keyVal.value(), defaultCharset));  
	            }
            }
            w.close();  
        }  





        private static String getRequestCookieString(Request req) {
            StringBuilder sb = new StringBuilder();  
            boolean first = true;  
            for (Map.Entry<String, String> cookie : req.cookies().entrySet()) {  
                if (!first) {  
                    sb.append("; ");  
                } else {  
                    first = false;  
                }  
                sb.append(cookie.getKey()).append('=').append(cookie.getValue());  
            }  
            return sb.toString();  
        }  
  
        
        private static void serialiseRequestUrl(Request req) throws IOException {
            URL in = req.url();  
            StringBuilder url = new StringBuilder();  
            boolean first = true;  
            
            url.append(in.getProtocol())  
                .append("://")  
                .append(in.getAuthority()) 
                .append(in.getPath())  
                .append("?");  
            if (in.getQuery() != null) {  
                url.append(in.getQuery());  
                first = false;  
            }  
            for (KeyVal keyVal : req.data()) {
                if (!first) {  
                    url.append('&');  
                } else {  
                    first = false;  
                }  
                url.append(URLEncoder.encode(keyVal.key(), defaultCharset))  
                    .append('=')  
                    .append(URLEncoder.encode(keyVal.value(), defaultCharset));  
            }  
            req.url(new URL(url.toString()));  
            req.data().clear(); 
        }  
    }  
      
    private static class TokenQueue {  
        private String queue;  
        private int pos = 0;  
          
        public TokenQueue(String data) {  
            queue = data;  
        }  
  
        public boolean isEmpty() {  
            return remainingLength() == 0;  
        }  
          
        private int remainingLength() {  
            return queue.length() - pos;  
        }  
  
        public boolean matches(String seq) {  
            return queue.regionMatches(true, pos, seq, 0, seq.length());  
        }  
  
        public boolean matchChomp(String seq) {  
            if (matches(seq)) {  
                pos += seq.length();  
                return true;  
            } else {  
                return false;  
            }  
        }  
  
        public char consume() {  
            return queue.charAt(pos++);  
        }  
  
        public String consumeTo(String seq) {  
            int offset = queue.indexOf(seq, pos);  
            if (offset != -1) {  
                String consumed = queue.substring(pos, offset);  
                pos += consumed.length();  
                return consumed;  
            } else {  
                return remainder();  
            }  
        }  
          
        public String chompTo(String seq) {  
            String data = consumeTo(seq);  
            matchChomp(seq);  
            return data;  
        }  
  
        public String remainder() {  
            StringBuilder accum = new StringBuilder();  
            while (!isEmpty()) {  
                accum.append(consume());  
            }  
            return accum.toString();  
        }  
          
        public String toString() {  
            return queue.substring(pos);  
        }  
    }  
  
    public static class KeyVal {  
          
        private String key;  
        private String value;  
  
        public static KeyVal create(String key, String value) {  
            return new KeyVal(key, value);  
        }  
  
        private KeyVal(String key, String value) {  
            this.key = key;  
            this.value = value;  
        }  
  
        public KeyVal key(String key) {  
            this.key = key;  
            return this;  
        }  
  
        public String key() {  
            return key;  
        }  
  
        public KeyVal value(String value) {  
            this.value = value;  
            return this;  
        }  
  
        public String value() {  
            return value;  
        }  
  
    }  
      
    @SuppressWarnings("unused")  
    private static class HttpStatusException extends IOException {  
  
        private static final long serialVersionUID = -2926428810498166324L;  
        private int statusCode;  
        private String url;  
  
        public HttpStatusException(String message, int statusCode, String url) {  
            super(message);  
            this.statusCode = statusCode;  
            this.url = url;  
        }  
  
        public int getStatusCode() {  
            return statusCode;  
        }  
  
        public String getUrl() {  
            return url;  
        }  
  
        public String toString() {  
            return super.toString() + ". Status=" + statusCode + ", URL=" + url;  
        }  
    }  
      
    @SuppressWarnings("unused")  
    private static class UnsupportedMimeTypeException extends IOException {  
          
        private static final long serialVersionUID = 2535952512520299658L;  
        private String mimeType;  
        private String url;  
  
        public UnsupportedMimeTypeException(String message, String mimeType, String url) {  
            super(message);  
            this.mimeType = mimeType;  
            this.url = url;  
        }  
  
        public String getMimeType() {  
            return mimeType;  
        }  
  
        public String getUrl() {  
            return url;  
        }  
  
        public String toString() {  
            return super.toString() + ". Mimetype=" + mimeType + ", URL="+url;  
        }  
    }  
      
    private static ByteBuffer readToByteBuffer(InputStream inStream, int maxSize) throws IOException { 
        final boolean capped = maxSize > 0;  
        byte[] buffer = new byte[0x20000];  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(0x20000);  
        int read;  
        int remaining = maxSize;  
  
        while (true) {  
            read = inStream.read(buffer);  
            if (read == -1) break;  
            if (capped) {  
                if (read > remaining) {  
                    outStream.write(buffer, 0, remaining);  
                    break;  
                }  
                remaining -= read;  
            }  
            outStream.write(buffer, 0, read);  
        }  
        ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());  
        return byteData;  
    }  
      
    private static String getCharsetFromContentType(String contentType) {  
        if (contentType == null) {  
            return null;  
        }  
        Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");  
        Matcher m = charsetPattern.matcher(contentType);  
        if (m.find()) {  
            String charset = m.group(1).trim();  
            if (Charset.isSupported(charset)) {  
                return charset;  
            }  
            charset = charset.toUpperCase(Locale.ENGLISH);  
            if (Charset.isSupported(charset)) {  
                return charset;  
            }  
        }  
        return null;  
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            if(param != null)
            	out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    
  	public static final byte[] readBytes(HttpServletRequest request) throws IOException {
  		  int contentLen = request.getContentLength();
          InputStream is = request.getInputStream();
		  if (contentLen > 0) {
		  	int readLen = 0;
		  	int readLengthThisTime = 0;
		  	byte[] message = new byte[contentLen];
		      try {
		      	while (readLen != contentLen) {
		      		readLengthThisTime = is.read(message, readLen, contentLen- readLen);
		              if (readLengthThisTime == -1) {
		              	break;
		              }
		              readLen += readLengthThisTime;
		      	}
		          return message;
		      } catch (IOException e) {
		      	e.printStackTrace();
		      	return new byte[] {};
		      }
		  }
		  return new byte[] {};
  	}
  	public static String getPayChannel(HttpServletRequest request) {
  		String PayChannel = "02";//获取支付终端类型参数
		String userAgent = request.getHeader("user-agent");
		if (userAgent != null) {
			if (userAgent.matches(".*Android.*|.*BlackBerry.*|.*iPhone.*|.*iPad.*|.*iPod.*|.*Opera Mini.*|.*IEMobile.*|.*Mobile.*")) {
				PayChannel = "01"; // 02:PC 01:移动
			} else {
				PayChannel = "02";
			}
			// 此处用于添加微信客户端03
		} else {
			PayChannel = "02";
		}
		return PayChannel;
  	}
}