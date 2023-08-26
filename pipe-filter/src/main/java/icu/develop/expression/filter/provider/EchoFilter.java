package icu.develop.expression.filter.provider;


import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.constant.EscapeEnums;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;

import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/18 9:43
 */
public class EchoFilter extends AbstractEchoFilter {
    @Override
    protected String filterName() {
        return "echo";
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        if (Objects.isNull(wrapper)) {
            return PipeDataWrapper.error(errorPrefix() + "输入数据不能为空");
        }

        if (!wrapper.success()) {
            return PipeDataWrapper.error(wrapper.getMessage(), "");
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.success("");
        }

        if (!(value instanceof String)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不是字符串");
        }

        String val = (String) value;
        if (StringUtils.isBlank(val)) {
            return PipeDataWrapper.success("");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.success("");
        }

        String delimiter = params().get(0);

        Delimiter delimiterEnum = Delimiter.ofValue(delimiter);
        if (Objects.nonNull(delimiterEnum)) {
            return PipeDataWrapper.success(val + delimiterEnum.getDelimiter());
        }

        String delimiterEscape = delimiter;
        EscapeEnums delimiterEscapeEnums = EscapeEnums.match(delimiter);
        if (Objects.nonNull(delimiterEscapeEnums)) {
            delimiterEscape = delimiterEscapeEnums.getCh();
        }

        return PipeDataWrapper.success(val + delimiterEscape);
    }
}
