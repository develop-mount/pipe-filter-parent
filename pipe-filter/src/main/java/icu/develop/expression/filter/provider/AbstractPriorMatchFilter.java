package icu.develop.expression.filter.provider;


import icu.develop.expression.filter.PipeDataWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/15 9:27
 */
public abstract class AbstractPriorMatchFilter extends AbstractMatchFilter {

    /**
     * 多参数处理,优先级
     *
     * @param value 待处理数据
     * @return 处理后的数据
     */
    @Override
    protected PipeDataWrapper<Object> moreParamsHandle(Object value) {
        List<String> error = new ArrayList<>();
        for (String center : params()) {
            if (Objects.isNull(center)) {
                continue;
            }
            PipeDataWrapper<Object> dataWrapper = instructHandle(value, center);
            if (dataWrapper.success()) {
                return dataWrapper;
            } else {
                error.add(dataWrapper.getMessage());
            }
        }
        return PipeDataWrapper.error(errorPrefix() + String.format("没有匹配到[%s]结果", String.join(",", error)));
    }

}
