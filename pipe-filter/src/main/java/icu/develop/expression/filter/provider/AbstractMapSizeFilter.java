package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.*;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.*;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/8/10 16:10
 */
@Slf4j
public abstract class AbstractMapSizeFilter extends AbstractMatchListFilter {
    private static final String NUMBER_REG = "^[0-9]+(.[0-9]+)?$";

    @Override
    protected boolean matchProcess(String source, String match) {
        return source.equalsIgnoreCase(match);
    }

    /**
     * 匹配大小错误消息前缀
     * @return 错误消息
     */
    protected abstract String matchSizeTip();

    /**
     * get object from map or object
     *
     * @param value 值
     * @param center 匹配值
     * @return 包裹对象
     */
    protected PipeDataWrapper<Object> getPipeDataOfMap(Object value, String center) {
        Map<Object, Object> colMap;
        if (value instanceof Map) {
            //noinspection unchecked
            colMap = (Map<Object, Object>) value;
        } else {
            //noinspection unchecked
            colMap = BeanMapUtils.create(value);
        }

        PipeDataWrapper<Object> result = null;
        for (Map.Entry<Object, Object> entry : colMap.entrySet()) {
            if (Objects.isNull(entry.getKey())) {
                continue;
            }
            if (matchProcess(entry.getKey().toString(), center)) {
                if (Objects.nonNull(entry.getValue()) && matchMaxSize(entry.getValue(), params().get(1))) {
                    result = PipeDataWrapper.success(colMap);
                } else {
                    result = PipeDataWrapper.error(errorPrefix() + String.format(matchSizeTip() +":%s", params().get(1)), colMap);
                }
                break;
            }
        }
        return result;
    }

    @Override
    protected PipeDataWrapper<Object> instructHandle(Object value, String center) {

        if (value instanceof Collection) {
            List<Object> resultList = new ArrayList<>();
            //noinspection unchecked
            Collection<Object> collection = (Collection<Object>) value;
            for (Object col : collection) {

                PipeDataWrapper<Object> result;
                if (Objects.isNull(col)) {
                    continue;
                }
                if (col instanceof String || col instanceof Collection) {
                    log.warn(errorPrefix() + "传入数据不支持集合套集合的情况");
                    continue;
                } else {
                    result = getPipeDataOfMap(col, center);
                }
                if (Objects.nonNull(result) && result.success()) {
                    resultList.add(result.getData());
                }
            }
            if (PipeFilterUtils.isEmpty(resultList)) {
                return PipeDataWrapper.error(errorPrefix() + String.format("没有包含[%s]的数据或数据"+matchSizeTip()+"[%s]", center, params().get(1)));
            } else {
                return PipeDataWrapper.success(resultList);
            }
        } else if (value instanceof String) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不能为字符串");
        } else {
            PipeDataWrapper<Object> result = getPipeDataOfMap(value, center);
            if (Objects.nonNull(result) && result.success()) {
                return PipeDataWrapper.success(result.getData());
            } else {
                return PipeDataWrapper.error(errorPrefix() + String.format("没有包含[%s]的数据或数据"+matchSizeTip()+"[%s]", center, params().get(1)), result.getData());
            }
        }
    }

    @Override
    protected PipeDataWrapper<Object> singleParamsHandle(Object value) {
        String center = params().get(0);
        return instructHandle(value, center);
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error(errorPrefix() + "指令缺失参数");
        }

        if (params().size() != 2) {
            return PipeDataWrapper.error(errorPrefix() + "指令参数个数只能是2个");
        }

        if (StringUtils.isBlank(params().get(0))) {
            return PipeDataWrapper.error(errorPrefix() + "指令参数不能为空");
        }

        if (StringUtils.isBlank(params().get(1))) {
            return PipeDataWrapper.error(errorPrefix() + "指令参数不能为空");
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
        }

        Object dataObj;
        if (value instanceof Collection || value instanceof Map) {
            dataObj = value;
        } else if (value instanceof String) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不能为字符串");
        } else {
            dataObj = BeanMapUtils.create(value);
        }

        return singleParamsHandle(dataObj);
    }

    private boolean matchMaxSize(Object value, String param) {

        Double paramInt;
        Double valueInt;
        TrafficUnit trafficUnit;
        try {
            paramInt = getParamInt(param);
            if (value instanceof String) {
                String val = ((String) value).trim();
                if (!StringUtils.isNumeric(val)) {
                    if (val.length() > 2) {
                        String number = val.substring(0, val.length() - 2);
                        String unit = val.substring(val.length() - 2);
                        trafficUnit = TrafficUnit.ofName(unit);
                        if (Objects.isNull(trafficUnit)) {
                            unit = val.substring(val.length() - 1);
                            number = val.substring(0, val.length() - 1);
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
            throw new RuntimeException(errorPrefix() + "传入数据类型应该是数字");
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        return matchSize(valueInt, paramInt);
    }

    /**
     * 匹配大小
     *
     * @param value 输入值
     * @param match 匹配值
     * @return 是否匹配成功
     */
    protected abstract boolean matchSize(Double value, Double match);

    private Double getParamInt(String param) throws ParseException {
        TrafficUnit trafficUnit;
        double paramInt;

        if (!StringUtils.isNumeric(param)) {
            if (param.length() > 2) {
                String number = param.substring(0, param.length() - 2);
                String unit = param.substring(param.length() - 2);
                trafficUnit = TrafficUnit.ofName(unit);
                if (Objects.isNull(trafficUnit)) {
                    unit = param.substring(param.length() - 1);
                    number = param.substring(0, param.length() - 1);
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
}
