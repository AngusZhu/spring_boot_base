package com.sinosafe.payment.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮件发送公用类
 */
public class MailUtil {
    public static Logger logger = LoggerFactory.getLogger(MailUtil.class);


    public static void sendExceptionMail(JavaMailSender javaMailSender, Exception e) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("zhuhuanmin@sinosafe.com.cn");
        helper.setTo("zhuhuanmin@sinosafe.com.cn,chenshengfen@sinosafe.com.cn,ex_xubin@sinosafe.com.cn,ex_liqiang@sinosafe.com.cn".split(","));
        helper.setSubject("[TEST]重要：支付异常");
        Map<String, Object> model = new ConcurrentHashMap<String, Object>();
        model.put("exception", getStackTrace(e));
        String text = getExceptionEmailContent(model);
        helper.setText(text, true);
        javaMailSender.send(mimeMessage);
    }

    public static String getExceptionEmailContent(Object object) throws TemplateException {
        String templatePath = "/templates/email/";        // 报文模板路径
        String templateFileName = "exception_mail.ftl";  // 模板文件名
        String requestXml = "";

        // 构建请求报文，并转换成报文字符串
        Configuration config = new Configuration(new Version("2.3.25"));
        try {

            config.setClassForTemplateLoading(Configuration.class.getClass(), "/");
            Template template = config.getTemplate(templatePath + templateFileName, "UTF-8");// 报文模板
            // 设置模板参数
            requestXml = FreeMarkerTemplateUtils.processTemplateIntoString(template, object);
            logger.info("getQuickPayRequest is:  " + requestXml);
            if (requestXml == null || "".equals(requestXml)) {
                logger.error("填充模板出错，模板路径：" + templatePath + templateFileName);
                return null;
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        // 返回报文字符串
        return requestXml;
    }

    private static String getStackTrace(Exception t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}