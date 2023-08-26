package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.BeanMapUtils;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/15 8:51
 */
@Slf4j
public abstract class AbstractMatchFilter extends BasePipeFilter<Object, Object> {

    /**
     * 字符串匹配
     *
     * @param source 源字符串
     * @param match  匹配字符串
     * @return 是否匹配
     */
    protected abstract boolean matchProcess(String source, String match);

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

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

        Object dataObj;
        if (value instanceof String || value instanceof Collection || value instanceof Map) {
            dataObj = value;
        } else {
            dataObj = BeanMapUtils.create(value);
        }

        if (params().size() == 1) {

            return singleParamsHandle(dataObj);
        } else {

            return moreParamsHandle(dataObj);
        }
    }

    /**
     * 单参数处理
     *
     * @param value 待处理数据
     * @return 处理后的数据
     */
    protected PipeDataWrapper<Object> singleParamsHandle(Object value) {
        String center = params().get(0);
        if (StringUtils.isBlank(center)) {
            return PipeDataWrapper.error(errorPrefix() + "指令参数不能为空");
        }

        PipeDataWrapper<Object> objectPipeDataWrapper = instructHandle(value, center);
        if (!objectPipeDataWrapper.success()) {
            String message = objectPipeDataWrapper.getMessage();
            objectPipeDataWrapper.setMessage(errorPrefix() + String.format("没有包含[%s]的数据", message));
        }
        return objectPipeDataWrapper;
    }

    /**
     * 多参数处理
     *
     * @param value 待处理数据
     * @return 处理后的数据
     */
    protected PipeDataWrapper<Object> moreParamsHandle(Object value) {
        List<Object> result = new ArrayList<>();
        List<String> error = new ArrayList<>();
        for (String center : params()) {
            if (Objects.isNull(center)) {
                continue;
            }
            PipeDataWrapper<Object> itemDataWrapper = instructHandle(value, center);
            Object data = itemDataWrapper.getData();
            if (itemDataWrapper.success()) {
                if (data instanceof Collection) {
                    //noinspection unchecked
                    Collection<Object> collection = (Collection<Object>) data;
                    result.addAll(collection);
                } else {
                    result.add(data);
                }
            } else {
                error.add(itemDataWrapper.getMessage());
            }
        }
        if (PipeFilterUtils.isEmpty(error)) {
            return PipeDataWrapper.success(result);
        } else {
            return PipeDataWrapper.error(errorPrefix() + String.format("没有包含[%s]的数据", String.join(",", error)), result);
        }
    }

    /**
     * 指令处理
     *
     * @param value  待处理的值
     * @param center 待匹配的字符串
     * @return 数据包裹
     */
    protected PipeDataWrapper<Object> instructHandle(Object value, String center) {

        if (value instanceof Collection) {
            Object result = null;
            //noinspection unchecked
            Collection<Object> collection = (Collection<Object>) value;
            for (Object col : collection) {
                if (Objects.isNull(col)) {
                    continue;
                }
                if (col instanceof String) {
                    String cel = (String) col;
                    if (StringUtils.isNotBlank(cel) && matchProcess(cel, center)) {
                        result = cel;
                        break;
                    }
                } else if (col instanceof Collection) {
                    log.warn(errorPrefix() + "传入数据不支持集合套集合的情况");
                    break;
                } else {
                    result = getObjectOfMap(col, center);
                    if (Objects.nonNull(result)) {
                        break;
                    }
                }
            }
            if (Objects.isNull(result)) {
                return PipeDataWrapper.error(center);
            } else {
                return PipeDataWrapper.success(result);
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

    /**
     * get object from map or object
     *
     * @param value 值
     * @param center 匹配对象
     * @return 最终值
     */
    protected Object getObjectOfMap(Object value, String center) {
        Map<Object, Object> colMap;
        if (value instanceof Map) {
            //noinspection unchecked
            colMap = (Map<Object, Object>) value;
        } else {
            //noinspection unchecked
            colMap = BeanMapUtils.create(value);
        }

        Object result = null;
        for (Map.Entry<Object, Object> entry : colMap.entrySet()) {
            if (Objects.isNull(entry.getKey())) {
                continue;
            }
            if (matchProcess(entry.getKey().toString(), center)) {
                if (Objects.nonNull(entry.getValue())) {
                    result = entry.getValue();
                }
                break;
            }
        }
        return result;
    }


}
