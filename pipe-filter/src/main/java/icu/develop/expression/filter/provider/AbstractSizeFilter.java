package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.NumberUtils;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;
import icu.develop.expression.filter.utils.TrafficUnit;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/8/10 15:55
 */
@Slf4j
public abstract class AbstractSizeFilter extends BasePipeFilter<Object, Object> {
    private static final String NUMBER_REG = "^[0-9]+(.[0-9]+)?$";

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

        Double paramInt;
        Double valueInt;
        TrafficUnit trafficUnit = TrafficUnit.KB;
        try {
            paramInt = getParamInt(param);
            if (value instanceof String) {
                String val = ((String) value).trim();
                if (!StringUtils.isNumeric(val)) {
                    if (val.length() > 2) {
                        String number = val.substring(0, val.length()-2);
                        String unit = val.substring(val.length()-2);
                        trafficUnit = TrafficUnit.ofName(unit);
                        if (Objects.isNull(trafficUnit)) {
                            unit = val.substring(val.length()-1);
                            number = val.substring(0, val.length()-1);
                            trafficUnit = TrafficUnit.ofName(unit);
                        }

                        if (Objects.isNull(trafficUnit)) {
                            throw new RuntimeException(errorPrefix() + "参数格式错误，例如:[kb/mb/gb]");
                        }

                        if (!number.matches(NUMBER_REG)) {
                            throw new RuntimeException(errorPrefix() + "参数格式错误，例如:[kb/mb/gb]");
                        }

                        valueInt = trafficUnit.toB(NumberUtils.parseDouble(number, NumberUtils.FormatProperty.EMPTY));

                    } else {
                        valueInt = TrafficUnit.KB.toB(NumberUtils.parseDouble(param, NumberUtils.FormatProperty.EMPTY));
                    }
                } else {
                    valueInt = TrafficUnit.KB.toB(NumberUtils.parseDouble(param, NumberUtils.FormatProperty.EMPTY));
                }
            } else if (value instanceof Number) {
                valueInt = TrafficUnit.KB.toB(((Number) value).doubleValue());
            } else {
                throw new RuntimeException(errorPrefix() + "传入数据类型应该是数字");
            }
        } catch (NumberFormatException e) {
            log.warn(e.getMessage(), e);
            return PipeDataWrapper.error(errorPrefix() + "传入数据类型应该是数字");
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return PipeDataWrapper.error(e.getMessage());
        }

        return matchSize(valueInt, paramInt, trafficUnit);
    }

    private Double getParamInt(String param) throws ParseException {
        TrafficUnit trafficUnit;
        double paramInt;

        if (!StringUtils.isNumeric(param)) {
            if (param.length() > 2) {
                String number = param.substring(0, param.length()-2);
                String unit = param.substring(param.length()-2);
                trafficUnit = TrafficUnit.ofName(unit);
                if (Objects.isNull(trafficUnit)) {
                    unit = param.substring(param.length()-1);
                    number = param.substring(0, param.length()-1);
                    trafficUnit = TrafficUnit.ofName(unit);
                }

                if (Objects.isNull(trafficUnit)) {
                    throw new RuntimeException(errorPrefix() + "参数格式错误，例如:[kb/mb/gb]");
                }

                if (!number.matches(NUMBER_REG)) {
                    throw new RuntimeException(errorPrefix() + "参数格式错误，例如:[kb/mb/gb]");
                }

                paramInt = trafficUnit.toB(NumberUtils.parseDouble(number, NumberUtils.FormatProperty.EMPTY));

            } else {
                paramInt = TrafficUnit.KB.toB(NumberUtils.parseDouble(param, NumberUtils.FormatProperty.EMPTY));
            }
        } else {
            paramInt = TrafficUnit.KB.toB(NumberUtils.parseDouble(param, NumberUtils.FormatProperty.EMPTY));
        }

        return paramInt;
    }

    /**
     *
     * @param valueInt 值
     * @param trafficUnit 单位
     * @return 转换后的字符串
     */
    protected static String getResult(double valueInt, TrafficUnit trafficUnit) {

        if (TrafficUnit.isB(trafficUnit)) {
            return TrafficUnit.B.toB(valueInt) + trafficUnit.name();
        } else if (TrafficUnit.isKB(trafficUnit)) {
            return TrafficUnit.B.toKB(valueInt) + trafficUnit.name();
        } else if (TrafficUnit.isMB(trafficUnit)) {
            return TrafficUnit.B.toMB(valueInt) + trafficUnit.name();
        } else if (TrafficUnit.isGB(trafficUnit)) {
            return TrafficUnit.B.toGB(valueInt) + trafficUnit.name();
        }
        return TrafficUnit.B.toKB(valueInt) + trafficUnit.name();
    }

    /**
     * 匹配大小及输出
     * @param value 待比较大小
     * @param input 限制大小
     * @param trafficUnit 单位
     * @return 数据包裹
     */
    protected abstract PipeDataWrapper<Object> matchSize(Double value, Double input, TrafficUnit trafficUnit);
}
