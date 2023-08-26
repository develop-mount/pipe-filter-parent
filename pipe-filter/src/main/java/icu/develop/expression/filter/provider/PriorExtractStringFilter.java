package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/7/24 11:21
 */
public class PriorExtractStringFilter extends AbstractEchoFilter {

    private static final int PARAMS_SIZE = 2;

    @Override
    protected String filterName() {
        return "prior-extract";
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

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error(errorPrefix() + "传入参数不能为空");
        }

        if (!(value instanceof String || value instanceof Collection)) {
            return PipeDataWrapper.error(errorPrefix() + "指令输入数据不是字符串或集合");
        }

        if (value instanceof String) {

            Object result = null;
            for (String param: params()) {
                String[] priorParams = PipeFilterUtils.getPipeFilterParams(param);
                if (Objects.isNull(priorParams) || priorParams.length != PARAMS_SIZE) {
                    continue;
                }
                ExtractIndex extractIndex = indexHandle(priorParams);
                String serializable = extractString(value, extractIndex.begin(), extractIndex.end());
                if (StringUtils.isNotBlank(serializable)) {
                    result = serializable;
                    break;
                }
            }
            if (Objects.isNull(result)) {
                return PipeDataWrapper.error(errorPrefix() + String.format("没有匹配到[%s]的数据", String.join(",", params())), value);
            }
            return PipeDataWrapper.success(result);
        } else {

            List<Object> result = new ArrayList<>();
            //noinspection unchecked
            Collection<Object> collection = (Collection<Object>) value;
            for (Object item : collection) {
                for (String param: params()) {
                    String[] priorParams = PipeFilterUtils.getPipeFilterParams(param);
                    if (Objects.isNull(priorParams) || priorParams.length != PARAMS_SIZE) {
                        continue;
                    }
                    ExtractIndex extractIndex = indexHandle(priorParams);
                    String serializable = extractString(item, extractIndex.begin(), extractIndex.end());
                    if (StringUtils.isNotBlank(serializable)) {
                        result.add(serializable);
                        break;
                    }
                }
            }
            if (PipeFilterUtils.isEmpty(result)) {
                return PipeDataWrapper.error(errorPrefix() + String.format("没有匹配到[%s]的数据", String.join(",", params())), value);
            }
            return PipeDataWrapper.success(result);
        }

    }
}
