package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.TrafficUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/27 12:32
 */
@Slf4j
public class MinSizeFilter extends AbstractSizeFilter {

    @Override
    protected String filterName() {
        return "min-size";
    }

    @Override
    protected PipeDataWrapper<Object> matchSize(Double value, Double input, TrafficUnit trafficUnit) {
        if (value < input) {
            return PipeDataWrapper.error(errorPrefix() + String.format("传入数字:[%s]没有达到最小[%s]大小", value, params().get(0)));
        }
        return PipeDataWrapper.success(getResult(value, trafficUnit));
    }
}
