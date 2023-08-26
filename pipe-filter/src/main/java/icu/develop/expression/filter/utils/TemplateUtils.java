package icu.develop.expression.filter.utils;

import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.PipeFilterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/29 16:36
 */
@Slf4j
public class TemplateUtils {

    private static final String ESCAPE_FILL_PREFIX = "\\\\\\{";
    private static final String ESCAPE_FILL_SUFFIX = "\\\\\\}";
    private static final String FILL_PREFIX = "{";
    private static final String FILL_SUFFIX = "}";
    private static final char IGNORE_CHAR = '\\';
    private static final String COLLECTION_PREFIX = ".";

    /**
     * 解析模板字符串
     *
     * @param value 模板字符串
     * @return 模板builder
     */
    public static TemplateBuilder parse(String value) {

        if (StringUtils.isEmpty(value)) {
            return new TemplateBuilder(Boolean.TRUE, Collections.emptyList(), Collections.emptyList());
        }

        List<String> variableList = new ArrayList<>();
        List<String> prepareDataList = new ArrayList<>();

        boolean onlyOneVariable = Boolean.TRUE;
        int startIndex = 0;
        int length = value.length();
        int lastPrepareDataIndex = 0;
        out:
        while (startIndex < length) {
            int prefixIndex = value.indexOf(FILL_PREFIX, startIndex);
            if (prefixIndex < 0) {
                break;
            }
            if (prefixIndex != 0) {
                char prefixPrefixChar = value.charAt(prefixIndex - 1);
                if (prefixPrefixChar == IGNORE_CHAR) {
                    startIndex = prefixIndex + 1;
                    continue;
                }
            }
            int suffixIndex = -1;
            while (suffixIndex == -1 && startIndex < length) {
                suffixIndex = value.indexOf(FILL_SUFFIX, startIndex + 1);
                if (suffixIndex < 0) {
                    break out;
                }
                startIndex = suffixIndex + 1;
                char prefixSuffixChar = value.charAt(suffixIndex - 1);
                if (prefixSuffixChar == IGNORE_CHAR) {
                    suffixIndex = -1;
                }
            }
            String variable = value.substring(prefixIndex + 1, suffixIndex);
            if (StringUtils.isEmpty(variable)) {
                continue;
            }
            int collectPrefixIndex = variable.indexOf(COLLECTION_PREFIX);
            if (collectPrefixIndex == 0) {
                variable = variable.substring(collectPrefixIndex + 1);
                if (StringUtils.isEmpty(variable)) {
                    continue;
                }
            }
            variableList.add(variable);
            if (lastPrepareDataIndex == prefixIndex) {
                prepareDataList.add(StringUtils.EMPTY);
                if (lastPrepareDataIndex != 0) {
                    onlyOneVariable = Boolean.FALSE;
                }
            } else {
                String data = convertPrepareData(value.substring(lastPrepareDataIndex, prefixIndex));
                prepareDataList.add(data);
                onlyOneVariable = Boolean.FALSE;
            }
            lastPrepareDataIndex = suffixIndex + 1;
        }

        if (lastPrepareDataIndex == length) {
            prepareDataList.add(StringUtils.EMPTY);
        } else {
            prepareDataList.add(convertPrepareData(value.substring(lastPrepareDataIndex)));
            onlyOneVariable = Boolean.FALSE;
        }

        return new TemplateBuilder(onlyOneVariable, variableList, prepareDataList);

    }

    /**
     * 替换 {}
     * @param prepareData 原始字符串
     * @return 替换后的字符串
     */
    private static String convertPrepareData(String prepareData) {
        prepareData = prepareData.replaceAll(ESCAPE_FILL_PREFIX, FILL_PREFIX);
        prepareData = prepareData.replaceAll(ESCAPE_FILL_SUFFIX, FILL_SUFFIX);
        return prepareData;
    }

    public static class TemplateBuilder {

        /**
         *
         */
        private final PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        /**
         * 仅一个变量
         */
        private boolean onlyOneVariable = Boolean.TRUE;
        /**
         * 内容
         */
        private final List<String> variable;
        /**
         * 预处理数据
         */
        private final List<String> prepareData;

        public TemplateBuilder(boolean onlyOneVariable, List<String> variable, List<String> prepareData) {
            this.onlyOneVariable = onlyOneVariable;
            this.variable = variable;
            this.prepareData = prepareData;
        }

        /**
         * 注册filter
         *
         * @param name       name
         * @param pipeFilter pipe filter
         * @return this
         */
        public TemplateBuilder registerPipeFilter(String name, Supplier<BasePipeFilter<Object, Object>> pipeFilter) {
            pipeFilterFactory.registerPipeFilter(name, pipeFilter);
            return this;
        }

        /**
         * 渲染字符串
         *
         * @param params 参数
         * @return 渲染后的字符串
         */
        public Object render(Map<String, Object> params) {

            PipeDataWrapper<Object> apply;
            List<String> errorList = new ArrayList<>();
            List<Object> variableValueList = new ArrayList<>();
            if (!PipeFilterUtils.isEmpty(variable)) {

                for (String var : variable) {

                    Object valueOfMap = PipeFilterUtils.getValueOfMap(params, PipeFilterUtils.getVariableName(var));
                    if (PipeFilterUtils.isPipeline(var)) {
                        apply = pipeFilterFactory.addParams(var).apply(PipeDataWrapper.success(valueOfMap));
                        // 若是错误继续或是成功
                        if (apply.isErrorContinue() || apply.success()) {
                            if (!apply.success()) {
                                errorList.add(apply.getMessage());
                            }
                            variableValueList.add(apply.getData());
                        } else {
                            errorList.add(apply.getMessage());
                            variableValueList.add(StringUtils.EMPTY);
                        }
                    } else {
                        variableValueList.add(valueOfMap);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(errorList)) {
                log.warn(String.join(",", errorList));
            }

            if (onlyOneVariable) {
                return variableValueList.get(0);
            }

            StringBuilder renderContent = new StringBuilder();
            if (!PipeFilterUtils.isEmpty(prepareData)) {
                for (int i = 0; i < prepareData.size(); i++) {
                    renderContent.append(prepareData.get(i));
                    if (i <= variableValueList.size() - 1) {
                        renderContent.append(variableValueList.get(i));
                    }
                }
            }
            return renderContent.toString();
        }

    }

}
