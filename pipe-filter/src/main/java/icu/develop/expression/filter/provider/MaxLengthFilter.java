package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/7/11 10:39
 */
@Slf4j
public class MaxLengthFilter extends BasePipeFilter<Object, Object> {
    @Override
    protected String filterName() {
        return "max-length";
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {
        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }
        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.success("");
        }

        if (!(value instanceof String)) {
            return PipeDataWrapper.error(errorPrefix() + "输出数据只能是字符串");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error(errorPrefix() + "参数为空");
        }
        if (params().size() != 1) {
            return PipeDataWrapper.error(errorPrefix() + "仅支持1个参数");
        }
        String param = params().get(0);
        if (StringUtils.isBlank(param)) {
            return PipeDataWrapper.error(errorPrefix() + "参数为空");
        }
        param = param.trim();

        try {
            String val = value.toString();
            int maxInt = Integer.parseInt(param);
            int length = val.length();
            if (length <= maxInt) {
                return PipeDataWrapper.success(value);
            } else {
                return PipeDataWrapper.error(errorPrefix() + "传入字符串长度大于:" + maxInt, value);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return PipeDataWrapper.error(errorPrefix() + "参数转换数字错误", value);
    }
}
