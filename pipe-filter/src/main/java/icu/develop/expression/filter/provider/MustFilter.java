package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * Description:
 * must pipe filter
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/26 8:34
 */
public class MustFilter extends BasePipeFilter<Object, Object> {

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
        }

        if (value instanceof Collection) {
            @SuppressWarnings("rawtypes")
            Collection collection = (Collection) value;
            if (PipeFilterUtils.isEmpty(collection)) {
                return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
            }
        }
        if (value instanceof String) {
            String val = (String) value;
            if (StringUtils.isBlank(val)) {
                return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
            }
        }

        return PipeDataWrapper.success(value);
    }

    @Override
    protected String filterName() {
        return "must";
    }
}
