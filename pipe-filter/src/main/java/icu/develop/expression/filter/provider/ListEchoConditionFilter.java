package icu.develop.expression.filter.provider;


import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.PipeFilterPool;
import icu.develop.expression.filter.constant.EscapeEnums;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Description:
 * list-out
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 16:52
 */
public class ListEchoConditionFilter extends AbstractEchoFilter {

    @Override
    protected String filterName() {
        return "list-echo-condition";
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.success("");
        }

        if (!(value instanceof Collection)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不是集合");
        }

        @SuppressWarnings("unchecked")
        List<Object> collection = (List<Object>) value;

        if (PipeFilterUtils.isEmpty(collection)) {
            return PipeDataWrapper.success("");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.success(collection.stream().map(String::valueOf).collect(Collectors.joining(Delimiter.WRAP.getDelimiter())));
        }

        String delimiter = params().get(0);

        String delimiterEscape = delimiter;

        Delimiter delimiterEnum = Delimiter.ofValue(delimiter);
        if (Objects.isNull(delimiterEnum)) {
            EscapeEnums oldCharEscapeEnums = EscapeEnums.match(delimiter);
            if (Objects.nonNull(oldCharEscapeEnums)) {
                delimiterEscape = oldCharEscapeEnums.getCh();
            }
        } else {
            delimiterEscape = delimiterEnum.getDelimiter();
        }

        String subFilter = params().get(1);
        if (StringUtils.isBlank(subFilter)) {
            return PipeDataWrapper.success(collection.stream().map(String::valueOf).collect(Collectors.joining(delimiterEscape)));
        }

        String[] expressArray = PipeFilterUtils.getPipeFilter(subFilter, 2);
        if (StringUtils.isBlank(expressArray[0])) {
            return PipeDataWrapper.success(collection.stream().map(String::valueOf).collect(Collectors.joining(delimiterEscape)));
        }

        String filterName = PipeFilterUtils.trim(expressArray[0]).toLowerCase();
        if (StringUtils.isBlank(filterName)) {
            return PipeDataWrapper.success(collection.stream().map(String::valueOf).collect(Collectors.joining(delimiterEscape)));
        }

        Supplier<BasePipeFilter<Object, Object>> pipeFilterSupplier = PipeFilterPool.INSTANCE.getPipeFilter(filterName);
        if (Objects.isNull(pipeFilterSupplier)) {
            return PipeDataWrapper.success(collection.stream().map(String::valueOf).collect(Collectors.joining(delimiterEscape)));
        }

        BasePipeFilter<Object, Object> pipeFilter = pipeFilterSupplier.get();
        if (expressArray.length > 1 && StringUtils.isNotBlank(expressArray[1])) {
            String[] paramArray = PipeFilterUtils.getPipeFilterParams(PipeFilterUtils.trim(expressArray[1]));
            pipeFilter.addParams(paramArray);
        }

        StringBuilder concat = new StringBuilder();
        for (Object coll : collection) {
            PipeDataWrapper<Object> apply = pipeFilter.apply(PipeDataWrapper.success(concat + coll.toString()));
            if (apply.success()) {
                concat.append(coll).append(delimiterEscape);
            } else {
                break;
            }
        }
        return PipeDataWrapper.success(concat.substring(0, concat.lastIndexOf(delimiterEscape)));

    }
}
