package com.example.demo.config;

import com.example.demo.Service.MailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import java.util.Properties;

//該類別會讀取環境參數
@Configuration
//使用setter自動將屬性配置properties檔的資源, prefix可以設定properties的key值前綴
@ConfigurationProperties(prefix = "mail")
//properties預設使用系統的application.properties, 欲將配置檔單獨配置, 該標註可設定配置檔的來源
//多個配置檔, 使用陣列方式匯入 ex: ({"a", "b"})
@PropertySource("classpath:mail.properties")
public class MailConfig {

    public static final String GMAIL_SERVICE = "gmailService";
    public static final String YAHOO_MAIL_SERVICE = "yahooMailService";

//    把value 用setter方法, 就能取得配置檔的設定值
    private String gmailHost;
    private int    gmailPort;
    private String gmailUserAddress;
    private String gmailUserPwd;

    private String yahooHost;
    private int    yahooPort;
    private String yahooUserAddress;
    private String yahooUserPwd;

    private boolean authEnable;
    private boolean starttlsEnabled;
    private String userDisplayName;

    private String platform;


    public String getGmailHost() {
        return gmailHost;
    }

    public void setGmailHost(String gmailHost) {
        this.gmailHost = gmailHost;
    }

    public int getGmailPort() {
        return gmailPort;
    }

    public void setGmailPort(int gmailPort) {
        this.gmailPort = gmailPort;
    }

    public String getGmailUserAddress() {
        return gmailUserAddress;
    }

    public void setGmailUserAddress(String gmailUserAddress) {
        this.gmailUserAddress = gmailUserAddress;
    }

    public String getGmailUserPwd() {
        return gmailUserPwd;
    }

    public void setGmailUserPwd(String gmailUserPwd) {
        this.gmailUserPwd = gmailUserPwd;
    }

    public String getYahooHost() {
        return yahooHost;
    }

    public void setYahooHost(String yahooHost) {
        this.yahooHost = yahooHost;
    }

    public int getYahooPort() {
        return yahooPort;
    }

    public void setYahooPort(int yahooPort) {
        this.yahooPort = yahooPort;
    }

    public String getYahooUserAddress() {
        return yahooUserAddress;
    }

    public void setYahooUserAddress(String yahooUserAddress) {
        this.yahooUserAddress = yahooUserAddress;
    }

    public String getYahooUserPwd() {
        return yahooUserPwd;
    }

    public void setYahooUserPwd(String yahooUserPwd) {
        this.yahooUserPwd = yahooUserPwd;
    }

    public boolean isAuthEnable() {
        return authEnable;
    }

    public void setAuthEnable(boolean authEnable) {
        this.authEnable = authEnable;
    }

    public boolean isStarttlsEnabled() {
        return starttlsEnabled;
    }

    public void setStarttlsEnabled(boolean starttlsEnabled) {
        this.starttlsEnabled = starttlsEnabled;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }


//    若元件的建立有多種方法, 可以用名稱(name="")來識別, 在注入時標示即可
//    @Bean(name = GMAIL_SERVICE)
    public MailService gmailService() throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", gmailHost);
        props.put("mail.smtp.port", gmailPort);
        props.put("mail.smtp.auth", String.valueOf(authEnable));
        props.put("mail.smtp.starttls.enable", String.valueOf(starttlsEnabled));

        InternetAddress fromAddress = new InternetAddress(gmailUserAddress, userDisplayName);
        PasswordAuthentication pwdAuth = new PasswordAuthentication(gmailUserAddress, gmailUserPwd);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return pwdAuth;
            }
        };

        return new MailService(props, fromAddress, authenticator);
    }

//    @Bean(name = YAHOO_MAIL_SERVICE)
    public MailService yahooMailService() throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", yahooHost);
        props.put("mail.smtp.port", yahooPort);
        props.put("mail.smtp.auth", String.valueOf(authEnable));
        props.put("mail.smtp.starttls.enable", String.valueOf(starttlsEnabled));

        InternetAddress fromAddress = new InternetAddress(yahooUserAddress, userDisplayName);
        PasswordAuthentication pwdAuth = new PasswordAuthentication(yahooUserAddress, yahooUserPwd);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return pwdAuth;
            }
        };

        return new MailService(props, fromAddress, authenticator);
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Bean
    public MailService mailService() throws Exception {
//        該方法會判斷環境參數, 並生成指定的元件
        return "yahoo".equals(platform)
                ? yahooMailService()
                : gmailService();
    }
}
