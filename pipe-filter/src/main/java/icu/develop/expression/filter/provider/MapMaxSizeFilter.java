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
public class MapMaxSizeFilter extends AbstractMapSizeFilter {

    @Override
    protected String filterName() {
        return "max-size-filter";
    }

    @Override
    protected String matchSizeTip() {
        return "超出限制";
    }

    @Override
    protected boolean matchSize(Double value, Double match) {
        return value < match;
    }
}
