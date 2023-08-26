package icu.develop.expression.filter.provider;


import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/15 9:27
 */
@Slf4j
public abstract class AbstractPriorMatchListFilter extends AbstractPriorMatchFilter {

    /**
     * 指令处理
     *
     * @param value  待处理的值
     * @param center 待匹配的字符串
     * @return 数据包裹
     */
    @Override
    protected PipeDataWrapper<Object> instructHandle(Object value, String center) {

        if (value instanceof Collection) {
            List<Object> resultList = new ArrayList<>();
            //noinspection unchecked
            Collection<Object> collection = (Collection<Object>) value;
            for (Object col : collection) {

                Object result = null;
                if (Objects.isNull(col)) {
                    continue;
                }
                if (col instanceof String) {
                    String cel = (String) col;
                    if (StringUtils.isNotBlank(cel) && matchProcess(cel, center)) {
                        result = cel;
                    }
                } else if (col instanceof Collection) {
                    log.warn(errorPrefix() + "传入数据不支持集合套集合的情况");
                    continue;
                } else {
                    result = getObjectOfMap(col, center);
                }
                if (Objects.nonNull(result)) {
                    resultList.add(result);
                }
            }
            if (PipeFilterUtils.isEmpty(resultList)) {
                return PipeDataWrapper.error(center);
            } else {
                return PipeDataWrapper.success(resultList);
            }
        } else if (value instanceof String) {
            String col = (String) value;
            if (StringUtils.isNotBlank(col) && matchProcess(col, center)) {
                return PipeDataWrapper.success(col);
            } else {
                return PipeDataWrapper.error(center);
            }
        } else {
            Object result = getObjectOfMap(value, center);
            if (Objects.isNull(result)) {
                return PipeDataWrapper.error(center);
            } else {
                return PipeDataWrapper.success(result);
            }
        }
    }

}
