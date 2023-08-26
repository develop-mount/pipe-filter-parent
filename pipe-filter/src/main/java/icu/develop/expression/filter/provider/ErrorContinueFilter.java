package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/7/20 14:39
 */
public class ErrorContinueFilter extends BasePipeFilter<Object, Object> {
    @Override
    protected String filterName() {
        return "error-continue";
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        // 设置忽略错误
        wrapper.setErrorContinue(true);
        if (wrapper.success()) {
            return PipeDataWrapper.success(wrapper.getData());
        }
        return PipeDataWrapper.error(wrapper.getMessage(), wrapper.getData());
    }
}

