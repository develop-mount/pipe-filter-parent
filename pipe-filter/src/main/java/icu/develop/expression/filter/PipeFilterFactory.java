package icu.develop.expression.filter;


import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/27 22:19
 */
public class PipeFilterFactory extends BasePipeFilter<Object, Object> {

    /**
     * 创建管道过滤器
     *
     * @return 管道过滤器
     */
    public static PipeFilterFactory createPipeFilter() {
        return new PipeFilterFactory();
    }

    /**
     * 注册管道过滤器
     *
     * @param name       filter name
     * @param pipeFilter pipe filer
     * @return factory
     */
    public PipeFilterFactory registerPipeFilter(String name, Supplier<BasePipeFilter<Object, Object>> pipeFilter) {
        PipeFilterPool.INSTANCE.addPipeFilter(name, pipeFilter);
        return this;
    }

    /**
     * 处理后
     * @param selfWrapper wrapper
     * @return wrapper
     */
    protected PipeDataWrapper<Object> applyAfter(PipeDataWrapper<Object> selfWrapper){
        return selfWrapper;
    }

    /**
     * 设置错误信息前缀
     * @param pipeFilter pipe filter
     * @param variableName 变量名称
     */
    protected void noticePrefix(BasePipeFilter<Object, Object> pipeFilter, String variableName) {
        pipeFilter.setNoticePrefix(String.format("[%s]变量", variableName));
    }

    /**
     * 默认附加信息方法
     * @param pipeFilter pipe filter
     */
    protected void additionalInfo(BasePipeFilter<Object, Object> pipeFilter) {

    }

    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> value) {

        if (PipeFilterUtils.isEmpty(params())) {
            throw new RuntimeException("管道字符串格式不正确");
        }

        String variable = params().get(0);
        if (StringUtils.isBlank(variable)) {
            throw new RuntimeException("管道字符串格式不正确");
        }

        String[] pipeArray = PipeFilterUtils.getPipelines(variable);
        Objects.requireNonNull(pipeArray, "管道字符串格式不正确");
        if (pipeArray.length <= 1) {
            throw new RuntimeException("管道字符串格式不正确");
        }

        String variableName = pipeArray[0].trim();

        List<BasePipeFilter<Object, Object>> pipeFilterList = new ArrayList<>();
        for (int i = 1; i < pipeArray.length; i++) {
            if (StringUtils.isBlank(pipeArray[i])) {
                continue;
            }

            String[] expressArray = PipeFilterUtils.getPipeFilter(pipeArray[i], 2);
            if (StringUtils.isBlank(expressArray[0])) {
                continue;
            }

            String filterName = PipeFilterUtils.trim(expressArray[0]).toLowerCase();
            if (StringUtils.isBlank(filterName)) {
                continue;
            }

            Supplier<BasePipeFilter<Object, Object>> supplier = PipeFilterPool.INSTANCE.getPipeFilter(filterName);
            if (Objects.isNull(supplier)) {
                throw new RuntimeException(String.format("没有[%s]的管道过滤器", filterName));
            }
            BasePipeFilter<Object, Object> pipeFilter = supplier.get();
            if (Objects.nonNull(pipeFilter)) {
                pipeFilterList.add(pipeFilter);
            }

            if (expressArray.length > 1 && StringUtils.isNotBlank(expressArray[1])) {
                String[] paramArray = PipeFilterUtils.getPipeFilterParams(PipeFilterUtils.trim(expressArray[1]));
                pipeFilter.addParams(paramArray);
            }

            noticePrefix(pipeFilter, variableName);
            additionalInfo(pipeFilter);
        }

        if (PipeFilterUtils.isEmpty(pipeFilterList)) {
            return value;
        }
        // 构建pipeline
        Function<PipeDataWrapper<Object>, PipeDataWrapper<Object>> currFilter = pipeFilterList.get(0);
        for (int i = 1; i < pipeFilterList.size(); i++) {
            currFilter = currFilter.andThen(pipeFilterList.get(i));
        }
        return applyAfter(currFilter.apply(value));
    }


    @Override
    protected String filterName() {
        return "factory";
    }
}
