package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.BasePipeFilter;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * trim pipe filter
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/26 8:34
 */
public class TrimFilter extends BasePipeFilter<Object, Object> {

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

        if (value instanceof String) {

            return PipeDataWrapper.success(value.toString().trim());
        } else if (value instanceof Collection) {
            //noinspection unchecked
            Collection<Object> valList = (Collection<Object>) value;
            if (PipeFilterUtils.isEmpty(valList)) {
                return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
            }
            return PipeDataWrapper.success(valList.stream().filter(Objects::nonNull).map(str -> str.toString().trim()).collect(Collectors.toList()));
        }

        return PipeDataWrapper.error(errorPrefix() + "指令输入数据不是字符串或集合");
    }

    @Override
    protected String filterName() {
        return "trim";
    }
}
