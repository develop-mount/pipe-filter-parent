package icu.develop.expression.filter.utils;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import icu.develop.expression.filter.utils.TemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/30 15:18
 */
@Slf4j
class PipeTemplateUtilsTest {

    @Test
    void testMaxSizeFitlerOfObjList() {

        DemoData body1 = new DemoData("100MB", "http://www.baidu.om/100");
        DemoData body2 = new DemoData("20MB", "http://www.baidu.om/20");
        List<DemoData> list = new ArrayList<>();
        list.add(body1);
        list.add(body2);

        Map<String, Object> params = new HashMap<>();
        params.put("test", list);
        Object render = TemplateUtils.parse("{test | error-continue  | max-size-filter:fileSize,20MB | prior-equals:image}").render(params);
        System.out.println(render);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testMaxSizeFitlerOfList() {
        Map<String, Object> body = new HashMap<>();
        body.put("file-size", "100MB");
        body.put("image", "http://www.baidu.om/100");

        Map<String, Object> body1 = new HashMap<>();
        body1.put("file-size", "20MB");
        body1.put("image", "http://www.baidu.om/20");

        List<Map> list = new ArrayList<>();
        list.add(body);
        list.add(body1);

        Map<String, Object> params = new HashMap<>();
        params.put("test", list);
        Object render = TemplateUtils.parse("{test | max-size-filter:file-size,20MB}").render(params);
        System.out.println(render);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testMaxSizeFitlerOfObj1() {

        DemoData body2 = new DemoData("20MB", "http://www.baidu.om/20");

        Map<String, Object> params = new HashMap<>();
        params.put("test", body2);
        Object render = TemplateUtils.parse("{test | max-size-filter:fileSize,30MB}").render(params);
        System.out.println(render);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testMaxSizeFitlerOfObj2() {

        DemoData body2 = new DemoData("100MB", "http://www.baidu.om/100");

        Map<String, Object> params = new HashMap<>();
        params.put("test", body2);
        Object render = TemplateUtils.parse("{test | max-size-filter:fileSize,30MB}").render(params);
        System.out.println(render);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testMaxSizeFitlerOfMap1() {
        Map<String, Object> body = new HashMap<>();
        body.put("file-size", "100MB");
        body.put("image", "http://www.baidu.om");


        Map<String, Object> params = new HashMap<>();
        params.put("test", body);
        Object render = TemplateUtils.parse("{test | max-size-filter:file-size,20MB}").render(params);
        System.out.println(render);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testMaxSizeFitlerOfMap2() {
        Map<String, Object> body = new HashMap<>();
        body.put("file-size", "20MB");
        body.put("image", "http://www.baidu.om/20");


        Map<String, Object> params = new HashMap<>();
        params.put("test", body);
        Object render = TemplateUtils.parse("{test | max-size-filter:file-size,20MB}").render(params);
        System.out.println(render);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testEcho() {
        Map<String, Object> params = new HashMap<>();
        params.put("test", "hello");
        Object render = TemplateUtils.parse("{test | echo:&#58}").render(params);
        System.out.println(render);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testWrapper() {
        Map<String, Object> params = new HashMap<>();
        params.put("test", "test");
        Object render = TemplateUtils.parse("{test | wrapper:,&#58}").render(params);
        System.out.println(render);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testReplace() {
        Map<String, Object> params = new HashMap<>();
        params.put("test", "?,test..uus");
        Object render = TemplateUtils.parse("{test | replace:&brvbar,test,0}").render(params);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testReplace2() {
        Map<String, Object> params = new HashMap<>();
        params.put("test", "?,test.,uus");
        Object render = TemplateUtils.parse("{test | replace:&#44,oo,1}").render(params);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testReplace3() {
        Map<String, Object> params = new HashMap<>();
        params.put("test", "?,test.,uus");
        Object render = TemplateUtils.parse("{test | replace:s,&#44,1}").render(params);
        Assert.notNull(render, "渲染错误");
    }

    @org.junit.jupiter.api.Test
    void prepareData() {

        List<String> word = new ArrayList<>();
        word.add("123");

        Map<String, Object> params = new HashMap<>();
        params.put("test", "hello");
        params.put("hello", word);

        Object expressionItems = TemplateUtils.parse("{test | replace:(o),(&empty),1}").render(params);
        System.out.println(expressionItems);

    }

    @Test
    void testMaxSize() {
        Map<String, Object> params = new HashMap<>();
        params.put("test", "1000kb");
        Object render = TemplateUtils.parse("{test | max-size:1mb}").render(params);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testMaxLength() {
        Map<String, Object> params = new HashMap<>();
        params.put("test", "售后单（支持独立站用户从前端自主发起售后流程，涵盖退款/重发/补发单）搭建售后工流程，规范售后各环节流转机制：对外增加用户提单入口，快速收集用户述求，且实现售后处理关键节点信息外化方便用户查看，培养用户侧售后提单及信息查看的心智，让用户对售后服务进程有更加清晰的感知；对内提升客服确认用户信息及述求的效率，降低邮件往返次数，以售后工单作为媒介完整串联售后处理环节中各个系统单据（客服tickets、OFC重发、配件单等）以实现售后工单在公司内部各个售后环节责任部门间的");
        Object render = TemplateUtils.parse("{test | max-length:10}").render(params);
        Assert.notNull(render, "渲染错误");
    }

    @Test
    void testImage() {

        Map<String, String> detail = new HashMap<>();
        detail.put("image", "http://www.nadiui.com/m100-2.jpg");

        Map<String, Object> item = new HashMap<>();
        item.put("http://www.nadiui.com/m100-2.jpg", detail);

        List<Map> images = new ArrayList<>();
        images.add(item);

        Map<String, Object> params = new HashMap<>();

        params.put("test", images);

        Object expressionItems = TemplateUtils.parse("{test | prior-ends-with:m100-2.jpg | prior-equals:image}").render(params);
        System.out.println(expressionItems);

    }

    @Test
    void testJsonArray() {

        JSONObject jsonObject = JSONUtil.parseObj(json);

        try {
            Object expressionItems = TemplateUtils.parse("{lll | ends-with-list:f2.jpg,f1000.jpg,m100-2.jpg | error-continue | map-get:scaleAttUrl | list-index:1}").render(jsonObject);
            System.out.println(expressionItems);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    void testJsonArray2() {

        JSONObject jsonObject = JSONUtil.parseObj(json);

        Object expressionItems = TemplateUtils.parse("{lll | ends-with:f1.jpg | equals:scaleAttUrl}").render(jsonObject);
        System.out.println(expressionItems);
    }

    @Test
    void testJsonArray3() {

        JSONObject jsonObject = JSONUtil.parseObj(json);

        Object expressionItems = TemplateUtils.parse("{lll | prior-ends-with:f1.jpg | equals:scaleAttUrl | echo:comma}").render(jsonObject);
        System.out.println(expressionItems);
    }

    @Test
    void testJsonArray4() {

        JSONObject jsonObject = JSONUtil.parseObj(json);

        try {
            Object expressionItems = TemplateUtils.parse("{lll | map-get:f1.jpg,f2.jpg,m100-2.jpg | map-get:scaleAttUrl | list-index:1}").render(jsonObject);
            System.out.println(expressionItems);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    void testJsonArray5() {

        JSONObject jsonObject = JSONUtil.parseObj(json);

        try {
            Object expressionItems = TemplateUtils.parse("{lll | map-get:f1.jpg,f2.jpg,m100-2.jpg | map-get:scaleAttUrl | list-index:1}").render(jsonObject);
            System.out.println(expressionItems);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    void testJson2() {
        JSONObject jsonObject = JSONUtil.parseObj(jsonhh);
        Object byPath = jsonObject.getByPath("relationships[0].relationships[0].parentAsins[0]");
        System.out.println(byPath);
    }

    private static String jsonhh = "{\"asin\":\"B08PB44ZR9\",\"attributes\":{},\"dimensions\":[{\"item\":{\"length\":{\"unit\":\"inches\",\"value\":20.9},\"width\":{\"unit\":\"inches\",\"value\":20.5},\"weight\":{\"unit\":\"pounds\",\"value\":45.9222891746},\"height\":{\"unit\":\"inches\",\"value\":12.2}},\"marketplaceId\":\"A1C3SOZRARQ6R3\"}],\"identifiers\":[{\"identifiers\":[],\"marketplaceId\":\"A1C3SOZRARQ6R3\"}],\"images\":[{\"images\":[{\"height\":1600,\"link\":\"https://m.media-amazon.com/images/I/71+Gq1Ox5RL.jpg\",\"variant\":\"MAIN\",\"width\":1600},{\"height\":500,\"link\":\"https://m.media-amazon.com/images/I/41JN-dzRcrL.jpg\",\"variant\":\"MAIN\",\"width\":500},{\"height\":75,\"link\":\"https://m.media-amazon.com/images/I/41JN-dzRcrL._SL75_.jpg\",\"variant\":\"MAIN\",\"width\":75},{\"height\":1600,\"link\":\"https://m.media-amazon.com/images/I/71k0r4gV7FL.jpg\",\"variant\":\"PT01\",\"width\":1600},{\"height\":500,\"link\":\"https://m.media-amazon.com/images/I/518H0g3lGvL.jpg\",\"variant\":\"PT01\",\"width\":500},{\"height\":75,\"link\":\"https://m.media-amazon.com/images/I/518H0g3lGvL._SL75_.jpg\",\"variant\":\"PT01\",\"width\":75},{\"height\":1600,\"link\":\"https://m.media-amazon.com/images/I/61do4WrjhEL.jpg\",\"variant\":\"PT02\",\"width\":1600},{\"height\":500,\"link\":\"https://m.media-amazon.com/images/I/41jp+2BwVNL.jpg\",\"variant\":\"PT02\",\"width\":500},{\"height\":75,\"link\":\"https://m.media-amazon.com/images/I/41jp+2BwVNL._SL75_.jpg\",\"variant\":\"PT02\",\"width\":75},{\"height\":1600,\"link\":\"https://m.media-amazon.com/images/I/612l7lDQT+L.jpg\",\"variant\":\"PT03\",\"width\":1600},{\"height\":500,\"link\":\"https://m.media-amazon.com/images/I/41kxD3W7lfL.jpg\",\"variant\":\"PT03\",\"width\":500},{\"height\":75,\"link\":\"https://m.media-amazon.com/images/I/41kxD3W7lfL._SL75_.jpg\",\"variant\":\"PT03\",\"width\":75},{\"height\":1600,\"link\":\"https://m.media-amazon.com/images/I/61cxNgDBmZL.jpg\",\"variant\":\"PT04\",\"width\":1600},{\"height\":500,\"link\":\"https://m.media-amazon.com/images/I/416r7iy+JqL.jpg\",\"variant\":\"PT04\",\"width\":500},{\"height\":75,\"link\":\"https://m.media-amazon.com/images/I/416r7iy+JqL._SL75_.jpg\",\"variant\":\"PT04\",\"width\":75},{\"height\":1600,\"link\":\"https://m.media-amazon.com/images/I/61mFoFsSUqL.jpg\",\"variant\":\"PT05\",\"width\":1600},{\"height\":500,\"link\":\"https://m.media-amazon.com/images/I/41yVYUuTMtL.jpg\",\"variant\":\"PT05\",\"width\":500},{\"height\":75,\"link\":\"https://m.media-amazon.com/images/I/41yVYUuTMtL._SL75_.jpg\",\"variant\":\"PT05\",\"width\":75},{\"height\":1600,\"link\":\"https://m.media-amazon.com/images/I/81OvtizJ5VL.jpg\",\"variant\":\"PT06\",\"width\":1600},{\"height\":500,\"link\":\"https://m.media-amazon.com/images/I/51lCLrdUYIL.jpg\",\"variant\":\"PT06\",\"width\":500},{\"height\":75,\"link\":\"https://m.media-amazon.com/images/I/51lCLrdUYIL._SL75_.jpg\",\"variant\":\"PT06\",\"width\":75},{\"height\":500,\"link\":\"https://m.media-amazon.com/images/I/41q2I0JOZYL.jpg\",\"variant\":\"PT07\",\"width\":500},{\"height\":75,\"link\":\"https://m.media-amazon.com/images/I/41q2I0JOZYL._SL75_.jpg\",\"variant\":\"PT07\",\"width\":75},{\"height\":1600,\"link\":\"https://m.media-amazon.com/images/I/71pLspAqM3L.jpg\",\"variant\":\"PT08\",\"width\":1600},{\"height\":500,\"link\":\"https://m.media-amazon.com/images/I/417Ludzj5jL.jpg\",\"variant\":\"PT08\",\"width\":500},{\"height\":75,\"link\":\"https://m.media-amazon.com/images/I/417Ludzj5jL._SL75_.jpg\",\"variant\":\"PT08\",\"width\":75}],\"marketplaceId\":\"A1C3SOZRARQ6R3\"}],\"productTypes\":[{\"marketplaceId\":\"A1C3SOZRARQ6R3\",\"productType\":\"PORTABLE_ELECTRONIC_DEVICE_MOUNT\"}],\"relationships\":[{\"marketplaceId\":\"A1C3SOZRARQ6R3\"}],\"salesRanks\":[{\"marketplaceId\":\"A1C3SOZRARQ6R3\"}],\"summaries\":[{\"brand\":\"VEVOR\",\"browseClassification\":{\"classificationId\":\"26955567031\",\"displayName\":\"Kategorie(20657433031)|Kina domowe, telewizory i odtwarzacze(20788265031)|Akcesoria(20788366031)|Akcesoria do projektorów(20788529031)|Mocowania(26955567031)\"},\"itemClassification\":\"BASE_PRODUCT\",\"itemName\":\"VEVOR Sufitowy Wspornik Projektora Podnośnik 1 m Uniwersalna Podnośnik Projektora 24w Podnośnik Projektora Zmotoryzowany Wspornik 30 Kg z Pilotem do Spotkań Sale Kinowe 500x500x350 Mm\",\"manufacturer\":\"VEVOR\",\"marketplaceId\":\"A1C3SOZRARQ6R3\",\"packageQuantity\":1,\"partNumber\":\"3l24e2\",\"websiteDisplayGroup\":\"wireless_display_on_website\"}]}";

    private static String json = "{\"lll\":[{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f1.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f1.jpg\",\"fileSize\":\"1003.25KB\",\"ruleName\":\"kw-f1\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f1.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-fb1.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-fb1.jpg\",\"fileSize\":\"540.52KB\",\"ruleName\":\"kw-fb1\",\"fileParameter\":\"1080x1080\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-fb1.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-x1.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-x1.jpg\",\"fileSize\":\"399.78KB\",\"ruleName\":\"kw-x1\",\"fileParameter\":\"750x1000\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-x1.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f2.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f2.jpg\",\"fileSize\":\"833.35KB\",\"ruleName\":\"kw-f2\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f2.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f3.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f3.jpg\",\"fileSize\":\"782.89KB\",\"ruleName\":\"kw-f3\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f3.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f4.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f4.jpg\",\"fileSize\":\"783.18KB\",\"ruleName\":\"kw-f4\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f4.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f5.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f5.jpg\",\"fileSize\":\"566.04KB\",\"ruleName\":\"kw-f5\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f5.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f6.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f6.jpg\",\"fileSize\":\"780.97KB\",\"ruleName\":\"kw-f6\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f6.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.jpg\",\"fileSize\":\"424.15KB\",\"ruleName\":\"kw-a100-1\",\"fileParameter\":\"970x300\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-dlz1.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-dlz1.jpg\",\"fileSize\":\"203.24KB\",\"ruleName\":\"kw-a100-dlz1\",\"fileParameter\":\"970x300\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-dlz1.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-2.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-2.jpg\",\"fileSize\":\"523.29KB\",\"ruleName\":\"kw-m100-2\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-2.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-2.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-2.jpg\",\"fileSize\":\"28.78KB\",\"ruleName\":\"kw-a100-2\",\"fileParameter\":\"300x400\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-2.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-3.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-3.jpg\",\"fileSize\":\"407.83KB\",\"ruleName\":\"kw-m100-3\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-3.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-3.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-3.jpg\",\"fileSize\":\"9.19KB\",\"ruleName\":\"kw-a100-3\",\"fileParameter\":\"350x175\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-3.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-4.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-4.jpg\",\"fileSize\":\"798.19KB\",\"ruleName\":\"kw-m100-4\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-4.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-5.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-5.jpg\",\"fileSize\":\"865.72KB\",\"ruleName\":\"kw-m100-5\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-5.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-6.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-6.jpg\",\"fileSize\":\"326.79KB\",\"ruleName\":\"kw-m100-6\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-6.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-7.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-7.jpg\",\"fileSize\":\"850.78KB\",\"ruleName\":\"kw-m100-7\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-7.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-8.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-8.jpg\",\"fileSize\":\"283.16KB\",\"ruleName\":\"kw-m100-8\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-8.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-9.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-9.jpg\",\"fileSize\":\"345.93KB\",\"ruleName\":\"kw-m100-9\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-9.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-10.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-10.jpg\",\"fileSize\":\"288.97KB\",\"ruleName\":\"kw-m100-10\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-10.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.1.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-1.1.jpg\",\"fileSize\":\"932.08KB\",\"ruleName\":\"kw-m100-1.1\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.1.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-11.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-11.jpg\",\"fileSize\":\"318.76KB\",\"ruleName\":\"kw-m100-11\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-11.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.2.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-1.2.jpg\",\"fileSize\":\"286.27KB\",\"ruleName\":\"kw-m100-1.2\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.2.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-12.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-12.jpg\",\"fileSize\":\"286.05KB\",\"ruleName\":\"kw-m100-12\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-12.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.3.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-1.3.jpg\",\"fileSize\":\"1.08MB\",\"ruleName\":\"kw-m100-1.3\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.3.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/m100-13.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/m100-13.jpg\",\"fileSize\":\"140.55KB\",\"ruleName\":\"kw-m100-13\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/m100-13.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.4.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.4.jpg\",\"fileSize\":\"378.11KB\",\"ruleName\":\"kw-a100-1.4\",\"fileParameter\":\"970x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.4.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.6.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.6.jpg\",\"fileSize\":\"416.83KB\",\"ruleName\":\"kw-a100-1.6\",\"fileParameter\":\"1200x628\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.6.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.9.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.9.jpg\",\"fileSize\":\"795.05KB\",\"ruleName\":\"kw-a100-1.9\",\"fileParameter\":\"1920x1080\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.9.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.10.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.10.jpg\",\"fileSize\":\"790.05KB\",\"ruleName\":\"kw-a100-1.10\",\"fileParameter\":\"1920x1080\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.10.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.11.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.11.jpg\",\"fileSize\":\"790.05KB\",\"ruleName\":\"kw-a100-1.11\",\"fileParameter\":\"1920x1080\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.11.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.12.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-1.12.jpg\",\"fileSize\":\"548.01KB\",\"ruleName\":\"kw-m100-1.12\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.12.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.12.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.12.jpg\",\"fileSize\":\"535.63KB\",\"ruleName\":\"kw-a100-1.12\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.12.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.13.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.13.jpg\",\"fileSize\":\"670.43KB\",\"ruleName\":\"kw-a100-1.13\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.13.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.14.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.14.jpg\",\"fileSize\":\"197.05KB\",\"ruleName\":\"kw-a100-1.14\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.14.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/mats-a100-1.15.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/mats-a100-1.15.jpg\",\"fileSize\":\"562.91KB\",\"ruleName\":\"kw-a100-1.15\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/mats-a100-1.15.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.16.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.16.jpg\",\"fileSize\":\"184.72KB\",\"ruleName\":\"kw-a100-1.16\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.16.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.17.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.17.jpg\",\"fileSize\":\"194.26KB\",\"ruleName\":\"kw-a100-1.17\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.17.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.18.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.18.jpg\",\"fileSize\":\"194.26KB\",\"ruleName\":\"kw-a100-1.18\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.18.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.19.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.19.jpg\",\"fileSize\":\"539.48KB\",\"ruleName\":\"kw-a100-1.19\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.19.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.20.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.20.jpg\",\"fileSize\":\"539.48KB\",\"ruleName\":\"kw-a100-1.20\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.20.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.21.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.21.jpg\",\"fileSize\":\"773.25KB\",\"ruleName\":\"kw-a100-1.21\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.21.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.22.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.22.jpg\",\"fileSize\":\"354.05KB\",\"ruleName\":\"kw-a100-1.22\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.22.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.23.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.23.jpg\",\"fileSize\":\"617.72KB\",\"ruleName\":\"kw-a100-1.23\",\"fileParameter\":\"1464x600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.23.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/chain-binder-m100-1.30.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/chain-binder-m100-1.30.jpg\",\"fileSize\":\"1.70MB\",\"ruleName\":\"kw-m100-1.30\",\"fileParameter\":\"1600x1600\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/chain-binder-m100-1.30.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.92.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.92.jpg\",\"fileSize\":\"790.05KB\",\"ruleName\":\"kw-a100-1.92\",\"fileParameter\":\"1920x1080\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.92.jpg\",\"fileType\":\"jpg\"}},{\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.93.jpg\":{\"scaleAttUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.93.jpg\",\"fileSize\":\"174.62KB\",\"ruleName\":\"kw-a100-1.93\",\"fileParameter\":\"640x360\",\"mainUrl\":\"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.93.jpg\",\"fileType\":\"jpg\"}}]}";
}
