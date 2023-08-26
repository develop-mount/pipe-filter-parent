package icu.develop.expression.filter.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * ��String��صĹ��߷�����
 * @author Jimmy
 *
 */
public class StringUtil {

    public static String getMatcher(String regex, String source) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public List<String> getString(String s) {
        List<String> strs = new ArrayList<String>();
        Pattern p = Pattern.compile("GraphType\\s*=\\s*\".+\"\\s*");
        Matcher m = p.matcher(s);
        while(m.find()) {
            strs.add(m.group());
        }
        return strs;
    }

    public static void test () {
        String data = "�ҵ�1<if test='case == 1'>1</if>�ҵ�2<if test='case == 2'>2</if>";
        String regex = "<if test='([\\s\\S]*?)'>([\\s\\S]*?)</if>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            String group = matcher.group();
            System.out.println(group);
        }
    }

    public static void main(String[] args) {
//        String url = "http://172.12.1.123/test.txt";
//        String regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
////        String regex = "(\\d{1,3}\\.){1,3}(\\d{1,3})";
//        System.out.println(getMatcher(regex,url));
//        test();

        String url1 = "��ULTRASONS ET DEGAZAGE�� - M��thode de traitement par vibration ultrasonique, forte puissance de p��n��tration, t��te de vibration de haute qualit�� industrielle, nettoyage de haute fr��quence. La fonction de d��gazage prot��ge le produit de l'oxydation ou d'autres r��actions chimiques avec l'air pendant le processus de nettoyage, augmentant la brillance et am��liorant l'efficacit�� du nettoyage.";
        String regex1 = "��(.*)��.*";
        System.out.println(getMatcher(regex1,url1));

        String url2 = "Friendly Note :The product may be sent separately. Pls, do not worry, you will receive ALL the parcels in recent days after the order.";
        String regex2 = "(.*):.*";
        System.out.println(getMatcher(regex2,url2));

    }

}
