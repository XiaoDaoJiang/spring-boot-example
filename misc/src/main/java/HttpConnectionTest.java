import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateExpiredException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpConnectionTest {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        // 开启 SSL 调试模式
        //System.setProperty("javax.net.debug", "ssl");

        //TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
        //testHttps();

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
            try {
                System.out.println("thread name: " + Thread.currentThread().getName() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                testHttps();
            }
            catch (CertificateExpiredException e) {
                System.out.println("证书过期");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }, 5,5, TimeUnit.SECONDS);
    }

    private static void testHttps() throws IOException {
        URL url = new URL("https://zwms.gdbs.gov.cn/ebus/zwdsj-ppl/V1/PPL/PLI2/HQMYA");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET"); // 可以使用 "POST" 等其他方法
        con.setRequestProperty("User-Agent", "Mozilla/5.0"); // 设置请求头
        con.setDoOutput(true);
        String data = "param1=value1&param2=value2";

        OutputStream out = con.getOutputStream();
        out.write(data.getBytes());
        out.flush();
        out.close();

        // 打印 SSL 握手日志
        //System.out.println("Cipher suite: " + con.getCipherSuite());
        //System.out.println("Local certificates: " + Arrays.toString(con.getLocalCertificates()));
        //System.out.println("Peer certificates: " + Arrays.toString((con.getServerCertificates())));
        int status = con.getResponseCode(); // 获取响应状态码
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        System.out.println(content); // 打印响应内容

        con.disconnect();
    }

    public static void https() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        // 开启 SSL 调试模式
        System.setProperty("javax.net.debug", "ssl");

// 创建 SSLContext 对象
        SSLContext sslContext = SSLContext.getInstance("TLS");

// 初始化 SSLContext 对象
        sslContext.init(null, null, null);

// 创建 SSLSocketFactory 对象
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

// 创建 URL 对象
        URL url = new URL("https://example.com");

// 创建 HttpsURLConnection 对象
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(sslSocketFactory);

// 打印 SSL 握手日志
        System.out.println("Cipher suite: " + conn.getCipherSuite());
        System.out.println("Local certificates: " + Arrays.asList(conn.getLocalCertificates()));
        System.out.println("Peer certificates: " + Arrays.asList((conn.getServerCertificates())));

    }
}
