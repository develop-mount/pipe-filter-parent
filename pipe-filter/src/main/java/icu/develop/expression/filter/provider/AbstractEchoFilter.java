package icu.develop.expression.filter.provider;


import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.constant.EscapeEnums;
import icu.develop.expression.filter.utils.StringUtils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/18 9:44
 */
public abstract class AbstractEchoFilter extends BasePipeFilter<Object, Object> {

    public static final String INDEX_ZERO = "0";

    protected enum Delimiter {
        /**
         * 空格
         */
        BLANK("blank", " "),
        /**
         * 回车
         */
        WRAP("wrap", "\n"),
        /**
         * HTML 换行
         */
        BR("br", "<br>"),
        /**
         * 逗号
         */
        COMMA("comma", ",");

        private final String value;
        private final String delimiter;

        Delimiter(String value, String delimiter) {
            this.value = value;
            this.delimiter = delimiter;
        }

        public static Delimiter ofValue(String value) {
            for (Delimiter delim : Delimiter.values()) {
                if (delim.value.equalsIgnoreCase(value)) {
                    return delim;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }

        public String getDelimiter() {
            return delimiter;
        }
    }

    protected String extractString(Object value, String finalBegin, String finalEnd) {

        AtomicInteger beginIndex = new AtomicInteger();
        AtomicInteger endIndex = new AtomicInteger();
        String source = value.toString();
        if (StringUtils.isNumeric(finalBegin)) {
            beginIndex.set(Integer.parseInt(finalBegin));
        } else {
            int ind = source.indexOf(finalBegin);
            if (ind != -1) {
                beginIndex.set(ind + 1);
            } else {
                beginIndex.set(0);
            }
        }

        if (StringUtils.isNumeric(finalEnd)) {
            endIndex.set(Integer.parseInt(finalEnd));
        } else {
            int ind = source.indexOf(finalEnd);
            if (ind != -1) {
                endIndex.set(ind);
            } else {
                endIndex.set(0);
            }
        }

        if (beginIndex.get() == endIndex.get() && beginIndex.get() == 0) {
            return null;
        }

        int length = source.length();
        if (endIndex.get() > length) {
            endIndex.set(length);
        }
        int subLen = endIndex.get() - beginIndex.get();
        if (subLen < 0) {
            return null;
        }
        return source.substring(beginIndex.get(), endIndex.get());
    }

    protected ExtractIndex indexHandle(String[] priorParams) {

        String begin = priorParams[0];
        if (StringUtils.isBlank(begin)) {
            begin = INDEX_ZERO;
        } else {
            Delimiter beginDelimiter = Delimiter.ofValue(begin);
            if (Objects.nonNull(beginDelimiter)) {
                begin = beginDelimiter.getDelimiter();
            } else {
                EscapeEnums beginEscapeEnums = EscapeEnums.match(begin);
                if (Objects.nonNull(beginEscapeEnums)) {
                    begin = beginEscapeEnums.getCh();
                }
            }
        }

        String end = priorParams[1];
        if (StringUtils.isBlank(end)) {
            end = INDEX_ZERO;
        } else {
            Delimiter endDelimiter = Delimiter.ofValue(end);
            if (Objects.nonNull(endDelimiter)) {
                end = endDelimiter.getDelimiter();
            } else {
                EscapeEnums endEscapeEnums = EscapeEnums.match(end);
                if (Objects.nonNull(endEscapeEnums)) {
                    end = endEscapeEnums.getCh();
                }
            }
        }

        String finalBegin = begin;
        String finalEnd = end;
        return new ExtractIndex() {
            @Override
            public String begin() {
                return finalBegin;
            }

            @Override
            public String end() {
                return finalEnd;
            }
        };
    }

    public interface ExtractIndex {
        String begin();
        String end();
    }

}
