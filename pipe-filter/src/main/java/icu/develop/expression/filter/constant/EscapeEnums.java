package icu.develop.expression.filter.constant;

/**
 * 特殊字符转义
 *
 * @author KK
 */
public enum EscapeEnums {
    /**
     * 特殊字符
     */
    ES1("\\\"", "&quot"),
    ES2("<", "&lt"),
    ES3(">", "&gt"),
    ES4(" ", "&nbsp"),
    ES5("¥", "&yen"),
    ES6("|", "&brvbar"),
    ES7("§", "&sect"),
    ES8("«", "&laquo"),
    ES9("®", "&reg"),
    ES10("#", "&#35"),
    ES11("$", "&#36"),
    ES12("%", "&#37"),
    ES13("'", "&#39"),
    ES14("(", "&#40"),
    ES15(")", "&#41"),
    ES16("*", "&#42"),
    ES17("+", "&#43"),
    ES18(",", "&#44"),
    ES19("-", "&#45"),
    ES20(".", "&#46"),
    ES21("/", "&#47"),
    ES22(":", "&#58"),
    ES23(";", "&#59"),
    ES24("=", "&#61"),
    ES25("[", "&#91"),
    ES27("]", "&#93"),
    ES28("_", "&#95"),
    ES99("&", "&amp"),
    ES98("", "&empty");
    private final String ch;
    private final String escape;

    EscapeEnums(String ch, String escape) {
        this.ch = ch;
        this.escape = escape;
    }

    public static EscapeEnums match(String escape) {
        for (EscapeEnums value : EscapeEnums.values()) {
            if (escape.equals(value.escape)) {
                return value;
            }
        }
        return null;
    }

    public String getCh() {
        return ch;
    }

    public String getEscape() {
        return escape;
    }
}
