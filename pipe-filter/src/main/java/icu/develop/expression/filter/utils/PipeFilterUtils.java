package icu.develop.expression.filter.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/27 22:51
 */
@Slf4j
public class PipeFilterUtils {

    private static final String SUB_VAR_PREFIX = "(";
    private static final String SUB_VAR_SUFFIX = ")";
    private static final String PIPELINE_FLAG = "|";
    private static final String MULTI_PIPE_FLAG = "\\|";
    private static final String FILTER_PARAM_FLAG = ":";
    private static final String PARAM_FLAG = ",";

    private static final String VAR_FLAG = "\\.";

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * Return {@code true} if the supplied Map is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param map the Map to check
     * @return whether the given Map is empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static String trim(String source) {
        return StringUtils.isBlank(source) ? "" : source.trim();
    }

    public static String[] getPipelines(String pipelines) {
        return pipelines.split(MULTI_PIPE_FLAG);
    }

    public static String[] getPipeFilter(String pipeline) {
        return pipeline.split(FILTER_PARAM_FLAG);
    }

    public static String[] getPipeFilter(String pipeline, int limit) {
        return pipeline.split(FILTER_PARAM_FLAG, limit);
    }

    public static String[] getPipeFilterParams(String pipeFilter) {
        try {
            return split(pipeFilter, PARAM_FLAG);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return pipeFilter.split(PARAM_FLAG);
    }

    /**
     * 是否是管道，是管道返回true，反之返回false
     *
     * @param variable 表达式
     * @return 是否管道
     */
    public static boolean isPipeline(String variable) {
        return Objects.nonNull(variable) && variable.contains(PIPELINE_FLAG);
    }

    /**
     * 获取变量字符串的管道前面的变量
     *
     * @param variable 包含管道的变量字符串
     * @return 管道前面的变量字符串
     */
    public static String getVariableName(String variable) {
        String[] varArray = PipeFilterUtils.getPipelines(variable);
        if (Objects.nonNull(varArray[0])) {
            return varArray[0].trim();
        }
        return null;
    }

    /**
     * 从map中获取值
     * @param dataMap map
     * @param key 例如: demo.test.name
     * @return 返回值
     */
    public static Object getValueOfMap(Map dataMap, String key) {

        if (StringUtils.isBlank(key)) {
            return null;
        }
        Map tempDataMap = dataMap;
        String[] keyArray = key.split(VAR_FLAG);
        for (int i = 0; i < keyArray.length; i++) {
            if (StringUtils.isBlank(keyArray[i])) {
                continue;
            }
            Object obj = tempDataMap.get(keyArray[i]);
            if (Objects.isNull(obj)) {
                continue;
            }
            if (i == keyArray.length - 1) {
                return obj;
            } else {
                if (obj instanceof BeanMap) {
                    tempDataMap = (BeanMap) obj;
                } else if (obj instanceof Map) {
                    tempDataMap = (Map)obj;
                } else {
                    tempDataMap = BeanMapUtils.create(obj);
                }
            }
        }
        return null;
    }

    public static String[] split(String source, String regex) {

        char ch = 0;
        if (((regex.toCharArray().length == 1 &&
                ".$|()[{^?*+\\".indexOf(ch = regex.charAt(0)) == -1) ||
                (regex.length() == 2 &&
                        regex.charAt(0) == '\\' &&
                        (((ch = regex.charAt(1))-'0')|('9'-ch)) < 0 &&
                        ((ch-'a')|('z'-ch)) < 0 &&
                        ((ch-'A')|('Z'-ch)) < 0)) &&
                (ch < Character.MIN_HIGH_SURROGATE ||
                        ch > Character.MAX_LOW_SURROGATE))
        {
            int off = 0;
            int next = 0;
            ArrayList<String> list = new ArrayList<>();
            while (true) {

                int next1 = source.indexOf(SUB_VAR_PREFIX, off);
                int next2 = source.indexOf(ch, off);
                if (next1 == -1 && next2 == -1) {
                    break;
                }
                if ((next1 != -1 && next2 != -1 && next1 < next2) || (next1 != -1 && next2 == -1)) {
                    next = next1;
                    off = next + 1;
                    next = source.indexOf(SUB_VAR_SUFFIX, off);
                    if (next != -1) {
                        list.add(source.substring(off, next));
                        off = next + 1;
                    } else {
                        throw new RuntimeException(String.format("子指令格式不正确，需要用()包裹, 源字符串:%s, 分隔符:%s", source, regex));
                    }
                } else {
                    next = next2;
                    if ((next == off) && next != 0) {
                        off = next + 1;
                        continue;
                    }
                    list.add(source.substring(off, next));
                    off = next + 1;
                }
            }
            // If no match was found, return this
            if (off == 0) {
                return new String[]{source};
            }

            // Add remaining segment
            list.add(source.substring(off, source.toCharArray().length));

            // Construct result
            int resultSize = list.size();
            while (resultSize > 0 && list.get(resultSize - 1).isEmpty()) {
                resultSize--;
            }
            String[] result = new String[resultSize];
            return list.subList(0, resultSize).toArray(result);
        }
        return Pattern.compile(regex).split(source);
    }

}
