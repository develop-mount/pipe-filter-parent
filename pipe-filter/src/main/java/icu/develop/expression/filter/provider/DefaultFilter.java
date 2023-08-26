package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * Description:
 * trim pipe filter
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/26 8:34
 */
public class DefaultFilter extends BasePipeFilter<Object, Object> {

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error(errorPrefix() + "指令参数不能为空");
        }

        String param = params().get(0);
        if (StringUtils.isBlank(param)) {
            return PipeDataWrapper.error(errorPrefix() + "指令参数不能为空");
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.success(param);
        }

        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return PipeDataWrapper.success(param);
            }
        } else if (value instanceof Collection) {
            //noinspection unchecked
            Collection<Object> valList = (Collection<Object>) value;
            if (PipeFilterUtils.isEmpty(valList)) {
                return PipeDataWrapper.success(param);
            }
        }

        return PipeDataWrapper.success(value);
    }

    @Override
    protected String filterName() {
        return "trim";
    }
}
