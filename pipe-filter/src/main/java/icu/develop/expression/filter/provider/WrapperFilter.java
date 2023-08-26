package icu.develop.expression.filter.provider;


import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.constant.EscapeEnums;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/18 9:43
 */
public class WrapperFilter extends AbstractEchoFilter {
    @Override
    protected String filterName() {
        return "wrapper";
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        if (Objects.isNull(wrapper)) {
            return PipeDataWrapper.error(errorPrefix() + "输入数据不能为空");
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.success("");
        }

        if (!(value instanceof String || value instanceof Collection)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不是字符串或集合");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.success("");
        }

        String left = params().get(0);
        if (StringUtils.isBlank(left)) {
            left = "";
        } else {
            Delimiter leftDelimiter = Delimiter.ofValue(left);
            if (Objects.nonNull(leftDelimiter)) {
                left = leftDelimiter.getDelimiter();
            } else {
                EscapeEnums leftEscapeEnums = EscapeEnums.match(left);
                if (Objects.nonNull(leftEscapeEnums)) {
                    left = leftEscapeEnums.getCh();
                }
            }
        }

        String right;
        if (params().size() > 1) {
            right = params().get(1);
            if (StringUtils.isNotBlank(right)) {
                Delimiter rightDelimiter = Delimiter.ofValue(right);
                if (Objects.nonNull(rightDelimiter)) {
                    right = rightDelimiter.getDelimiter();
                } else {
                    EscapeEnums rightEscapeEnums = EscapeEnums.match(right);
                    if (Objects.nonNull(rightEscapeEnums)) {
                        right = rightEscapeEnums.getCh();
                    }
                }
            } else {
                right = "";
            }
        } else {
            right = "";
        }

        if (value instanceof String) {

            String val = (String) value;
            if (StringUtils.isBlank(val)) {
                return PipeDataWrapper.success("");
            }
            return PipeDataWrapper.success(left + val + right);
        } else {
            //noinspection unchecked
            Collection<Object> collection = (Collection<Object>) value;
            if (PipeFilterUtils.isEmpty(collection)) {
                return PipeDataWrapper.success("");
            }
            String finalLeft = left;
            String finalRight = right;
            return PipeDataWrapper.success(collection.stream().map(val -> finalLeft + val + finalRight).collect(Collectors.toList()));
        }
    }
}
