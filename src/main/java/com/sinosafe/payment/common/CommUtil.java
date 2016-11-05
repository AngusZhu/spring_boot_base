package com.sinosafe.payment.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommUtil {
    private final static String regexHKID = "[A-Z]{1,2}[0-9]{6}[A0-9]{1}";//Follow the HKID format
    private final static String regexVISA = "[A-Z0-9]*";
    private final static String regexEmail = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    private final static int academyNoLength = 7;
    private final static String regexPhoneNo = "[+]{0,1}[0-9]{8,}";
    private final static String CONTENT_TYPE_PLAIN = "text/plain;charset=utf-8";
    private final static String CONTENT_TYPE_HTML = "text/html;charset=utf-8";
    public final static String DEFAULT_CHARSET_CODE = "UTF-8";

    /**
     * Method used to validate the HKID's format and HKID's check digit
     *
     * @param passportNo
     * @return
     */
    public static boolean validateHKID(String passportNo) {
        return passportNo.matches(regexHKID) && HKIDAlgContentValidate(passportNo);
    }

    public static boolean validatePhoneNo(String phoneNo) {
        return phoneNo.matches(regexPhoneNo);
    }


    /**
     * Method used to validate the HKID's check digit
     *
     * @param hkid
     * @return
     */
    public static boolean HKIDAlgContentValidate(String hkid) {
        String prefix = null;
        String serial = null;
        String checkdigit = null;
        long value = 0;
        if (hkid.length() == 8) {
            prefix = hkid.substring(0, 1);
            serial = hkid.substring(1, 7);
            checkdigit = hkid.substring(7, 8);
        } else if (hkid.length() == 9) {
            prefix = hkid.substring(0, 2);
            serial = hkid.substring(2, 8);
            checkdigit = hkid.substring(8, 9);
        }
        String prefixU = prefix.toUpperCase();
        if (prefixU.length() == 2) {
            value += (prefixU.charAt(0) - 55) * 9 + (prefixU.charAt(1) - 55)
                    * 8;
        } else if (prefixU.length() == 1) {
            value += 36 * 9 + (prefixU.charAt(0) - 55) * 8;
        }
        for (int i = 0; i < 6; i++) {
            value += Integer.parseInt(serial.substring(i, i + 1)) * (7 - i);
        }
        long reminder = value % 11;
        long valid_checkdigit = 11 - reminder;
        if ("A".equalsIgnoreCase(checkdigit) && valid_checkdigit == 10) return true;
        if ("0".equalsIgnoreCase(checkdigit) && valid_checkdigit == 11) return true;
        try {
            if (valid_checkdigit == Integer.parseInt(checkdigit)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }


    public static boolean validateVISA(String passportNo) {
        return passportNo.matches(regexVISA);

    }

    public static boolean validateEmail(String email) {
        return email.matches(regexEmail);
    }

    /**
     * like oracle nvl() function, return p2 if p1 is null
     *
     * @param p1
     * @param p2
     * @return return p2 if p1 is null
     */
    public static String nvl(String p1, String p2) {
        return (p1 == null || nvl(p1).length() == 0) ? p2 : p1;
    }

    /**
     * return empty string if p1 is null
     *
     * @param p1
     * @return p1 or empty string
     */
    public static String nvl(String p1) {
        return (p1 == null) ? "" : p1;
    }


    /**
     * convert an image file to base64 format
     *
     * @param imgPath the full path of the image file
     * @return base64 string
     */
    public static String imgToBase64(String imgPath) {
        String imageString = null;
        try {
            BufferedImage img = ImageIO.read(new File(imgPath));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            String imgType = imgPath.substring(imgPath.lastIndexOf('.') + 1);
            ImageIO.write(img, imgType, bos);
            byte[] imageBytes = bos.toByteArray();
            // BASE64Encoder encoder = new BASE64Encoder();
            // imageString = encoder.encode(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    /**
     * resize image
     *
     * @param path   image path
     * @param width  new width, -1 to keep aspect proportion to height
     * @param height new height, -1 to keep aspect proportion to width
     */
    public static void resizeImage(String path, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(path));
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
/* BufferedImage resizedImage = new BufferedImage(width, height, type);
Graphics2D g = resizedImage.createGraphics();
g.drawImage(originalImage, 0, 0, width, height, null);
g.dispose();
ImageIO.write(resizedImage, path.substring(path.lastIndexOf('.')+1), new File(path)); */
            Image img = originalImage.getScaledInstance(width, height, type);
            BufferedImage resizedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            ImageIO.write(resizedImage, path.substring(path.lastIndexOf('.') + 1), new File(path));
        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static Serializable deeplyCopy(Serializable src) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(src);
            oos.close();
            baos.close();
            byte[] data = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Serializable copy = (Serializable) ois.readObject();
            ois.close();
            bais.close();
            return copy;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAbsoluteClassPath(Class<?> clazz) {
        String classPath = clazz.getClassLoader().getResource("/").getPath();
// For Windows
        if ("\\".equals(File.separator)) {
            classPath = classPath.substring(1).replace("/", "\\");
        }
// For Linux
        if ("/".equals(File.separator)) {
            classPath = classPath.replace("\\", "/");
        }
        return classPath;
    }

    public static String getAccessDevice(String userAgent) {
        String device = "";
        if (userAgent.toLowerCase().indexOf("windows") >= 0) {
            device = "Windows";
        } else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
            device = "Mac";
        } else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
            device = "Unix";
        } else if (userAgent.toLowerCase().indexOf("android") >= 0
                || userAgent.toLowerCase().indexOf("imgfornote") >= 0) {
            device = "Android";
        } else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
            device = "IPhone";
        } else if (userAgent.toLowerCase().indexOf("ipad") >= 0) {
            device = "iPad";
        } else {
            device = "UnKnown";
        }
        return device;
    }


    public static String getMD5Password(String loginId, String password) {
        return DigestUtils.md5DigestAsHex((password + loginId.toLowerCase()).getBytes());
    }

    public static boolean validatePassword(String password) {
        return (password.matches("[a-zA-Z0-9]{8,20}") && password.matches("[a-zA-Z]*[0-9]*[a-zA-Z]+[0-9]*[a-zA-Z]*"));
    }

    public static String generateRandomPassword() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

    public static boolean validateEmailAddress(String addressList) {
        if (nvl(addressList).length() == 0) return false;
        String[] addresses = addressList.split(";");
        boolean tag = true;
        for (String address : addresses) {
            tag = validateEmail(address);
            if (!tag) break;
        }
        return tag;
    }

    public static String[] regex(String regex, String from) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(from);
        List<String> results = new ArrayList<>();
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                results.add(matcher.group(i + 1));
            }
        }
        return results.toArray(new String[]{});
    }


    public static String getClientIp(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }





    private static final int NINE_DIGIT = 9;

    public static void main(String[] args) {
        System.out.println(isMobile("15361611147")?"alsdfjds":"false");
    }
}