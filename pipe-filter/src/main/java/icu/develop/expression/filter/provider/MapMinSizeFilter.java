package icu.develop.expression.filter.provider;

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/27 12:32
 */
@Slf4j
public class MapMinSizeFilter extends AbstractMapSizeFilter {

    @Override
    protected String filterName() {
        return "min-size-filter";
    }

    @Override
    protected String matchSizeTip() {
        return "未达到最小限制";
    }

    @Override
    protected boolean matchSize(Double value, Double match) {
        return value > match;
    }
}
