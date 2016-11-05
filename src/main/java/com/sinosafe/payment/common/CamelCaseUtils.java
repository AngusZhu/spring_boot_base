package com.sinosafe.payment.common;

public class CamelCaseUtils {
 
    private static final char SEPARATOR = '_';
 
    public static String toUnderlineName(String s) {
        if (s == null) {
            return null;
        }
 
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
 
            boolean nextUpperCase = true;
 
            if (i < (s.length() - 1)) {
 nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
 
            if ((i >= 0) && Character.isUpperCase(c)) {
 if (!upperCase || !nextUpperCase) {
     if (i > 0) sb.append(SEPARATOR);
 }
 upperCase = true;
            } else {
 upperCase = false;
            }
 
            sb.append(Character.toLowerCase(c));
        }
 
        return sb.toString();
    }
 
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
 
        s = s.toLowerCase();
 
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
 
            if (c == SEPARATOR) {
 upperCase = true;
            } else if (upperCase) {
 sb.append(Character.toUpperCase(c));
 upperCase = false;
            } else {
 sb.append(c);
            }
        }
 
        return sb.toString();
    }
 
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
 
    public static void main(String[] args) {
        String s="     \"BANK_NO\": \"BQD\",  --银行简称\n" +
                "      \"BANKLIST_ID\": \"3E7E42F18810023EE0530A016C0AF48D\", --银行唯一标示\n" +
                "      \"MERCHANT_NO\": \"kq\",\n" +
                "      \"CARD_NAME\": \"储蓄卡\",  -- tab名称\n" +
                "      \"BANK_ID\": \"0000028\", ---自定义银行编码\n" +
                "      \"PAY_MODE\": \"00\",  ----------支付类型\n" +
                "      \"CARDTYPE_ID\": \"02\", ---------tab编号\n" +
                "      \"BANK_NAME\": \"青岛银行\"  ----银行名称";
        System.out.println(CamelCaseUtils.toCamelCase(s));

    }
}