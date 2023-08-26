package icu.develop.expression.filter;

import icu.develop.expression.filter.provider.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/7/21 18:33
 */
public enum PipeFilterPool {
    /**
     *
     */
    INSTANCE;

    private static final Map<String, Supplier<BasePipeFilter<Object, Object>>> PIPE_FILTER_MAP = new HashMap<>();

    static {
        // 初始化内置管道过滤器
        PIPE_FILTER_MAP.put("trim", TrimFilter::new);
        PIPE_FILTER_MAP.put("equals", EqualsFilter::new);
        PIPE_FILTER_MAP.put("prior-equals", PriorEqualsFilter::new);
        PIPE_FILTER_MAP.put("ends-with", EndsWithFilter::new);
        PIPE_FILTER_MAP.put("prior-ends-with", PriorEndsWithFilter::new);
        PIPE_FILTER_MAP.put("starts-with", StartsWithFilter::new);
        PIPE_FILTER_MAP.put("prior-starts-with", PriorStartsWithFilter::new);
        PIPE_FILTER_MAP.put("pattern", PatternFilter::new);
        PIPE_FILTER_MAP.put("pattern-list", PatternListFilter::new);
        PIPE_FILTER_MAP.put("prior-pattern", PriorPatternFilter::new);
        PIPE_FILTER_MAP.put("prior-pattern-list", PriorPatternListFilter::new);
        PIPE_FILTER_MAP.put("date-format", DateFormatFilter::new);
        PIPE_FILTER_MAP.put("contains", ContainsFilter::new);
        PIPE_FILTER_MAP.put("contains-list", ContainsListFilter::new);
        PIPE_FILTER_MAP.put("prior-contains", PriorContainsFilter::new);
        PIPE_FILTER_MAP.put("prior-contains-list", PriorContainsListFilter::new);
        PIPE_FILTER_MAP.put("list-index", ListIndexFilter::new);
        PIPE_FILTER_MAP.put("list-echo", ListEchoFilter::new);
        PIPE_FILTER_MAP.put("list-range", ListRangeFilter::new);
        PIPE_FILTER_MAP.put("echo", EchoFilter::new);
        PIPE_FILTER_MAP.put("condition-echo", ConditionEchoFilter::new);
        PIPE_FILTER_MAP.put("cal-add", AdditionFilter::new);
        PIPE_FILTER_MAP.put("cal-sub", SubtractionFilter::new);
        PIPE_FILTER_MAP.put("cal-mul", MultiplicationFilter::new);
        PIPE_FILTER_MAP.put("cal-div", DivisionFilter::new);
        PIPE_FILTER_MAP.put("substring", SubstringFilter::new);
        PIPE_FILTER_MAP.put("replace", ReplaceFilter::new);
        PIPE_FILTER_MAP.put("replace-regex", ReplaceRegexFilter::new);
        PIPE_FILTER_MAP.put("max-size", MaxSizeFilter::new);
        PIPE_FILTER_MAP.put("min-size", MinSizeFilter::new);
        PIPE_FILTER_MAP.put("max-length", MaxLengthFilter::new);
        PIPE_FILTER_MAP.put("must", MustFilter::new);
        PIPE_FILTER_MAP.put("wrapper", WrapperFilter::new);
        PIPE_FILTER_MAP.put("map-get", MapGetFilter::new);
        PIPE_FILTER_MAP.put("ends-with-list", EndsWithListFilter::new);
        PIPE_FILTER_MAP.put("starts-with-list", StartsWithListFilter::new);
        PIPE_FILTER_MAP.put("prior-ends-with-list", PriorEndsWithListFilter::new);
        PIPE_FILTER_MAP.put("prior-starts-with-list", PriorStartsWithListFilter::new);
        PIPE_FILTER_MAP.put("max-size-filter", MapMaxSizeFilter::new);
        PIPE_FILTER_MAP.put("min-size-filter", MapMinSizeFilter::new);
        PIPE_FILTER_MAP.put("error-continue", ErrorContinueFilter::new);
        PIPE_FILTER_MAP.put("list-echo-condition", ListEchoConditionFilter::new);
        PIPE_FILTER_MAP.put("default", DefaultFilter::new);
        PIPE_FILTER_MAP.put("extract", ExtractStringFilter::new);
        PIPE_FILTER_MAP.put("prior-extract", PriorExtractStringFilter::new);
        PIPE_FILTER_MAP.put("replace-exclude-number", ReplaceExcludeNumberFilter::new);
        PIPE_FILTER_MAP.put("random-string", RandomStringFilter::new);
    }

    public void addPipeFilter(String name, Supplier<BasePipeFilter<Object, Object>> pipeFilter) {
        PIPE_FILTER_MAP.put(name, pipeFilter);
    }

    public void addPipeFilter(Map<String, Supplier<BasePipeFilter<Object, Object>>> pipeFilterMap) {
        PIPE_FILTER_MAP.putAll(pipeFilterMap);
    }

    public Supplier<BasePipeFilter<Object, Object>> getPipeFilter(String filterName) {
        return PIPE_FILTER_MAP.get(filterName);
    }

}
