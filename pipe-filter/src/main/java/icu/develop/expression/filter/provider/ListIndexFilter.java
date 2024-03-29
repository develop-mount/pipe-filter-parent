package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;
import icu.develop.expression.filter.BasePipeFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 16:52
 */
@Slf4j
public class ListIndexFilter extends BasePipeFilter<Object, Object> {

    /**
     * 参数格式  list-index:n n表示下标，从1开始
     *
     * @param wrapper the function argument
     * @return 包裹对象
     */
    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        // 验证
        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error(errorPrefix() + "指令缺失参数");
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
        }

        if (!(value instanceof Collection)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不是集合");
        }

        @SuppressWarnings("unchecked")
        List<Object> collection = (List<Object>) value;

        if (PipeFilterUtils.isEmpty(collection)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
        }

        if (PipeFilterUtils.isEmpty(params()) || params().size() > 1) {
            return PipeDataWrapper.error(errorPrefix() + "传入参数下标为空或是超过一个");
        }

        String index = params().get(0);
        if (StringUtils.isBlank(index)) {
            return PipeDataWrapper.error(errorPrefix() + "传入参数下标为空");
        }

        try {
            int ind = Integer.parseInt(index);
            if (ind < 0) {
                ind = 0;
            }
            if (ind >= 1) {
                ind -= 1;
            }
            if (ind > collection.size() - 1) {
                return PipeDataWrapper.error(errorPrefix() + "下标超出数据最大范围");
            }
            return PipeDataWrapper.success(collection.get(ind));
        } catch (NumberFormatException e) {

            log.warn(e.getMessage(), e);
            return PipeDataWrapper.error(errorPrefix() + "下标转换错误");
        }
    }

    @Override
    protected String filterName() {
        return "list-index";
    }
}
