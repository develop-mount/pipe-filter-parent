package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/7/24 11:21
 */
public class ExtractStringFilter extends AbstractEchoFilter {

    private static final int PARAMS_SIZE = 2;

    @Override
    protected String filterName() {
        return "extract";
    }

    @Override
    protected PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {
        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
        }

        if (PipeFilterUtils.isEmpty(params()) || params().size() != PARAMS_SIZE) {
            return PipeDataWrapper.error(errorPrefix() + "传入参数下标为空或是超过两个");
        }

        ExtractIndex extractIndex = indexHandle(params().toArray(new String[0]));

        if (value instanceof String) {

            String serializable = extractString(value, extractIndex.begin(), extractIndex.end());
            if (StringUtils.isBlank(serializable)) {
                return PipeDataWrapper.error(errorPrefix() + "没有匹配到数据", value);
            }
            return PipeDataWrapper.success(serializable);
        } else if (value instanceof Collection) {

            //noinspection unchecked
            Collection<Object> collection = (Collection<Object>) value;
            List<String> collect = collection.stream().map(item -> extractString(value, extractIndex.begin(), extractIndex.end())).collect(Collectors.toList());
            if (PipeFilterUtils.isEmpty(collect)) {
                return PipeDataWrapper.error(errorPrefix() + "没有匹配到数据", value);
            }
            return PipeDataWrapper.success(collect);

        } else {
            return PipeDataWrapper.error(errorPrefix() + "指令输入数据不是字符串或字符串集合");
        }
    }
}
