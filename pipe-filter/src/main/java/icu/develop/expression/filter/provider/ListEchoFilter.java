package icu.develop.expression.filter.provider;


import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.constant.EscapeEnums;
import icu.develop.expression.filter.utils.PipeFilterUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * list-out
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 16:52
 */
public class ListEchoFilter extends AbstractEchoFilter {

    @Override
    protected String filterName() {
        return "list-echo";
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

        Delimiter delimiterEnum = Delimiter.ofValue(delimiter);
        if (Objects.nonNull(delimiterEnum)) {
            return PipeDataWrapper.success(collection.stream().map(String::valueOf).collect(Collectors.joining(delimiterEnum.getDelimiter())));
        }

        String delimiterEscape = delimiter;
        EscapeEnums oldCharEscapeEnums = EscapeEnums.match(delimiter);
        if (Objects.nonNull(oldCharEscapeEnums)) {
            delimiterEscape = oldCharEscapeEnums.getCh();
        }

        return PipeDataWrapper.success(collection.stream().map(String::valueOf).collect(Collectors.joining(delimiterEscape)));

    }
}
