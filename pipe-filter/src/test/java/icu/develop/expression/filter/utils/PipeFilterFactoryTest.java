package icu.develop.expression.filter.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.PipeFilterFactory;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/17 13:02
 */
class PipeFilterFactoryTest {

    @Test
    void testRandomNA() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("PublishCenter.Bullet Point.bulletPoints | random-string:3,513");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(""));
        System.out.println(apply.getData());
        Assert.isTrue(!apply.success(), "�ɹ�");
    }

    @Test
    void testRandomA() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("PublishCenter.Bullet Point.bulletPoints | random-string:2,12");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(""));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testRandomNumber() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("PublishCenter.Bullet Point.bulletPoints | random-string:1,6");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(""));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testPriorExtract() {

        String json = "[\n" +
                "    \"4.3 inch Pipe Capacity: The snap-on jaw of this cast steel pipe wrench is adjustable, providing the largest grip surface. Maximum opening: 4.3 inch/110 mm. It features full floating forged hook jaws for superior gripping. The precision machine teeth grip pipes tightly.\",\n" +
                "    \"High Strength Steel: This 48\\\" pipe wrench is made of premium cast steel, through heat treatment. It delivers the exceptional durability and features good rigidity, high hardness, and wear-resistance.\",\n" +
                "    \"Adjustable Jaw Design: The heavy-duty pipe wrench has a non-stick adjustment nut for easily adjust the jaw opening to fit different pipe diameter. The tool with compact size is suitable for usage in both narrow areas and normal areas.\",\n" +
                "    \"Ergonomic Design: The handle of the wrench meets the humanized design and fits nicely into your hand. It makes you grasp the twist more comfortably, not easy to slip. Long and wide I-beam handle design provides better weight distribution and saves labor.\",\n" +
                "    \"Wide Application: Our wrench is suitable for all pipe-related home improvement repairs that need a tight grip, such as gas tank repair, household plumbing, vehicle maintenance. This pipe wrench kit is versatile enough for professional plumb.\"\n" +
                "]";

        JSONArray jsonArray = JSONUtil.parseArray(json);

        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("PublishCenter.Bullet Point.bulletPoints | prior-extract:(,&#58),(��,��) | wrapper:blank,blank | list-echo:&brvbar");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(jsonArray));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testPriorExtract1() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | prior-extract:(,&#58),(��,��) | list-echo:?");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("User-Friendly Designs: Using the dolly is a breeze. Lift the car tire with a jack, position the wheel dolly underneath"
                , "��ULTRASONS ET DEGAZAGE�� - Using the dolly is a breeze. Lift the car tire with a jack, position "
                , "��ULTRASONS ET DEGAZAGE�� - Using the dolly is a breeze. Lift the car tire with a jack, position "
                , "User-Friendly Designs: Using the dolly is a breeze. Lift the car ")));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testExtract() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | extract : ,(:)");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("User-Friendly Designs: Using the dolly is a breeze. Lift the car tire with a jack, position the wheel dolly underneath, and lock the wheel in place. Repeat the simple process for each tire, and within approximately 10 minutes, you'll be ready to go. No assistance from others is required. The handle design makes it convenient to hang in your garage or carry in your trunk, providing easy storage and portability."));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testExtract2() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | extract : ��,��");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("��ULTRASONS ET DEGAZAGE�� - Using the dolly is a breeze. Lift the car tire with a jack, position the wheel dolly underneath, and lock the wheel in place. Repeat the simple process for each tire, and within approximately 10 minutes, you'll be ready to go. No assistance from others is required. The handle design makes it convenient to hang in your garage or carry in your trunk, providing easy storage and portability."));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testExtract3() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | extract : ��,��");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("��ULTRASONS ET DEGAZAGE: - Using the dolly is a breeze. Lift the car tire with a jack, position the wheel dolly underneath, and lock the wheel in place. Repeat the simple process for each tire, and within approximately 10 minutes, you'll be ready to go. No assistance from others is required. The handle design makes it convenient to hang in your garage or carry in your trunk, providing easy storage and portability."));
        System.out.println(apply.getData());
        Assert.isTrue(!apply.success(), "�ɹ�");
    }

    @Test
    void testSplit3() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | split : (:),0");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("User-Friendly Designs: Using the dolly is a breeze. Lift the car tire with a jack, position the wheel dolly underneath, and lock the wheel in place. Repeat the simple process for each tire, and within approximately 10 minutes, you'll be ready to go. No assistance from others is required. The handle design makes it convenient to hang in your garage or carry in your trunk, providing easy storage and portability."));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testSplit2() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | split : images,");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
                , "http://www.baidu.com/images/m100-1.2.jpg"
                , "http://www.baidu.com/images/m100-1.3.jpg"
                , "http://www.baidu.com/images/m100-1.4.jpg"
                , "http://www.baidu.com/images/m100-1.5.jpg"
                , "http://www.baidu.com/images/K100-1.2.jpg")));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testSplit1() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | split : images,v");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
                , "http://www.baidu.com/images/m100-1.2.jpg"
                , "http://www.baidu.com/images/m100-1.3.jpg"
                , "http://www.baidu.com/images/m100-1.4.jpg"
                , "http://www.baidu.com/images/m100-1.5.jpg"
                , "http://www.baidu.com/images/K100-1.2.jpg")));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testSplit0() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | split : images,0");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
                , "http://www.baidu.com/images/m100-1.2.jpg"
                , "http://www.baidu.com/images/m100-1.3.jpg"
                , "http://www.baidu.com/images/m100-1.4.jpg"
                , "http://www.baidu.com/images/m100-1.5.jpg"
                , "http://www.baidu.com/images/K100-1.2.jpg")));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testListEchoCondition() {

        String json = "[\n" +
                "    \"Handle Material: Steel\",\n" +
                "    \"Max Length: 48\\\"\",\n" +
                "    \"Jaw Capacity: 4.3\\\"/110 mm\",\n" +
                "    \"Jaw Type: Full-Floating Hook Jaw\",\n" +
                "    \"Non-Stick Adjustment Nut: Yes\",\n" +
                "    \"Package size: 50x5x2 inch\"\n" +
                "]";

        JSONArray jsonArray = JSONUtil.parseArray(json);

        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | list-echo-condition:&brvbar,(max-length:420)");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(jsonArray));
        System.out.println(apply.getData());
        Assert.isTrue(apply.success(), "�ɹ�");
    }



    @Test
    void testEndsWithError1() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("PhotoStore.allPic | prior-ends-with:m100-1.2.jpg,m100-1.2ae.jpg | map-get:mainUrl");


        String sg = "[\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f1.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f1.jpg\",\n" +
                "            \"fileSize\": \"1003.25KB\",\n" +
                "            \"ruleName\": \"kw-f1\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f1.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-fb1.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-fb1.jpg\",\n" +
                "            \"fileSize\": \"540.52KB\",\n" +
                "            \"ruleName\": \"kw-fb1\",\n" +
                "            \"fileParameter\": \"1080x1080\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-fb1.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-x1.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-x1.jpg\",\n" +
                "            \"fileSize\": \"399.78KB\",\n" +
                "            \"ruleName\": \"kw-x1\",\n" +
                "            \"fileParameter\": \"750x1000\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-x1.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f2.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f2.jpg\",\n" +
                "            \"fileSize\": \"833.35KB\",\n" +
                "            \"ruleName\": \"kw-f2\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f2.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f3.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f3.jpg\",\n" +
                "            \"fileSize\": \"782.89KB\",\n" +
                "            \"ruleName\": \"kw-f3\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f3.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f4.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f4.jpg\",\n" +
                "            \"fileSize\": \"783.18KB\",\n" +
                "            \"ruleName\": \"kw-f4\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f4.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f5.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f5.jpg\",\n" +
                "            \"fileSize\": \"566.04KB\",\n" +
                "            \"ruleName\": \"kw-f5\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f5.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f6.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-f6.jpg\",\n" +
                "            \"fileSize\": \"780.97KB\",\n" +
                "            \"ruleName\": \"kw-f6\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-f6.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.jpg\",\n" +
                "            \"fileSize\": \"424.15KB\",\n" +
                "            \"ruleName\": \"kw-a100-1\",\n" +
                "            \"fileParameter\": \"970x300\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-dlz1.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-dlz1.jpg\",\n" +
                "            \"fileSize\": \"203.24KB\",\n" +
                "            \"ruleName\": \"kw-a100-dlz1\",\n" +
                "            \"fileParameter\": \"970x300\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-dlz1.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-2.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-2.jpg\",\n" +
                "            \"fileSize\": \"523.29KB\",\n" +
                "            \"ruleName\": \"kw-m100-2\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-2.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-2.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-2.jpg\",\n" +
                "            \"fileSize\": \"28.78KB\",\n" +
                "            \"ruleName\": \"kw-a100-2\",\n" +
                "            \"fileParameter\": \"300x400\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-2.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-3.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-3.jpg\",\n" +
                "            \"fileSize\": \"407.83KB\",\n" +
                "            \"ruleName\": \"kw-m100-3\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-3.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-3.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-3.jpg\",\n" +
                "            \"fileSize\": \"9.19KB\",\n" +
                "            \"ruleName\": \"kw-a100-3\",\n" +
                "            \"fileParameter\": \"350x175\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-3.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-4.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-4.jpg\",\n" +
                "            \"fileSize\": \"798.19KB\",\n" +
                "            \"ruleName\": \"kw-m100-4\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-4.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-5.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-5.jpg\",\n" +
                "            \"fileSize\": \"865.72KB\",\n" +
                "            \"ruleName\": \"kw-m100-5\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-5.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-6.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-6.jpg\",\n" +
                "            \"fileSize\": \"326.79KB\",\n" +
                "            \"ruleName\": \"kw-m100-6\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-6.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-7.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-7.jpg\",\n" +
                "            \"fileSize\": \"850.78KB\",\n" +
                "            \"ruleName\": \"kw-m100-7\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-7.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-8.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-8.jpg\",\n" +
                "            \"fileSize\": \"283.16KB\",\n" +
                "            \"ruleName\": \"kw-m100-8\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-8.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-9.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-9.jpg\",\n" +
                "            \"fileSize\": \"345.93KB\",\n" +
                "            \"ruleName\": \"kw-m100-9\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-9.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-10.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-10.jpg\",\n" +
                "            \"fileSize\": \"288.97KB\",\n" +
                "            \"ruleName\": \"kw-m100-10\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-10.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.1.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-1.1.jpg\",\n" +
                "            \"fileSize\": \"932.08KB\",\n" +
                "            \"ruleName\": \"kw-m100-1.1\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.1.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-11.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-11.jpg\",\n" +
                "            \"fileSize\": \"318.76KB\",\n" +
                "            \"ruleName\": \"kw-m100-11\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-11.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.2.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-1.2.jpg\",\n" +
                "            \"fileSize\": \"286.27KB\",\n" +
                "            \"ruleName\": \"kw-m100-1.2\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.2.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-12.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-12.jpg\",\n" +
                "            \"fileSize\": \"286.05KB\",\n" +
                "            \"ruleName\": \"kw-m100-12\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-12.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.3.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-1.3.jpg\",\n" +
                "            \"fileSize\": \"1.08MB\",\n" +
                "            \"ruleName\": \"kw-m100-1.3\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.3.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/m100-13.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/m100-13.jpg\",\n" +
                "            \"fileSize\": \"140.55KB\",\n" +
                "            \"ruleName\": \"kw-m100-13\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/m100-13.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/chain-binder-a100-1.4-m.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/chain-binder-a100-1.4-m.jpg\",\n" +
                "            \"fileSize\": \"802.38KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.4-m\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/chain-binder-a100-1.4-m.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/chain-binder-a100-1.4.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/chain-binder-a100-1.4.jpg\",\n" +
                "            \"fileSize\": \"802.38KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.4\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/chain-binder-a100-1.4.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.6.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.6.jpg\",\n" +
                "            \"fileSize\": \"416.83KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.6\",\n" +
                "            \"fileParameter\": \"1200x628\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.6.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.9.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.9.jpg\",\n" +
                "            \"fileSize\": \"795.05KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.9\",\n" +
                "            \"fileParameter\": \"1920x1080\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.9.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/meat-air-compressor-a100-2.2.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/meat-air-compressor-a100-2.2.jpg\",\n" +
                "            \"fileSize\": \"106.43KB\",\n" +
                "            \"ruleName\": \"kw-a100-2.2\",\n" +
                "            \"fileParameter\": \"970x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/meat-air-compressor-a100-2.2.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.2-m.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-2.2-m.jpg\",\n" +
                "            \"fileSize\": \"273.23KB\",\n" +
                "            \"ruleName\": \"kw-a100-2.2-m\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.2-m.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.3-m.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-2.3-m.jpg\",\n" +
                "            \"fileSize\": \"275.73KB\",\n" +
                "            \"ruleName\": \"kw-a100-2.3-m\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.3-m.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.3.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-2.3.jpg\",\n" +
                "            \"fileSize\": \"275.73KB\",\n" +
                "            \"ruleName\": \"kw-a100-2.3\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.3.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.4.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-2.4.jpg\",\n" +
                "            \"fileSize\": \"389.68KB\",\n" +
                "            \"ruleName\": \"kw-a100-2.4\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.4.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.4-m.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-2.4-m.jpg\",\n" +
                "            \"fileSize\": \"389.68KB\",\n" +
                "            \"ruleName\": \"kw-a100-2.4-m\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.4-m.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.5-m.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-2.5-m.jpg\",\n" +
                "            \"fileSize\": \"196.65KB\",\n" +
                "            \"ruleName\": \"kw-a100-2.5-m\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.5-m.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.5.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-2.5.jpg\",\n" +
                "            \"fileSize\": \"196.65KB\",\n" +
                "            \"ruleName\": \"kw-a100-2.5\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-2.5.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.10.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.10.jpg\",\n" +
                "            \"fileSize\": \"790.05KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.10\",\n" +
                "            \"fileParameter\": \"1920x1080\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.10.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.11.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.11.jpg\",\n" +
                "            \"fileSize\": \"790.05KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.11\",\n" +
                "            \"fileParameter\": \"1920x1080\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.11.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.12.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-m100-1.12.jpg\",\n" +
                "            \"fileSize\": \"548.01KB\",\n" +
                "            \"ruleName\": \"kw-m100-1.12\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-m100-1.12.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.12.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.12.jpg\",\n" +
                "            \"fileSize\": \"535.63KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.12\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.12.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.13.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.13.jpg\",\n" +
                "            \"fileSize\": \"670.43KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.13\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.13.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.14.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.14.jpg\",\n" +
                "            \"fileSize\": \"197.05KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.14\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.14.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.15.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.15.jpg\",\n" +
                "            \"fileSize\": \"376.87KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.15\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.15.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.16.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.16.jpg\",\n" +
                "            \"fileSize\": \"184.72KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.16\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.16.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.17.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.17.jpg\",\n" +
                "            \"fileSize\": \"194.26KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.17\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.17.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.18.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.18.jpg\",\n" +
                "            \"fileSize\": \"194.26KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.18\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.18.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.19.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.19.jpg\",\n" +
                "            \"fileSize\": \"539.48KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.19\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.19.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.20.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.20.jpg\",\n" +
                "            \"fileSize\": \"539.48KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.20\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.20.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.21.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.21.jpg\",\n" +
                "            \"fileSize\": \"773.25KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.21\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.21.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.22.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.22.jpg\",\n" +
                "            \"fileSize\": \"354.05KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.22\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.22.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.23.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/air-compressor-a100-1.23.jpg\",\n" +
                "            \"fileSize\": \"617.72KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.23\",\n" +
                "            \"fileParameter\": \"1464x600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/air-compressor-a100-1.23.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/chain-binder-m100-1.30.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/chain-binder-m100-1.30.jpg\",\n" +
                "            \"fileSize\": \"1.70MB\",\n" +
                "            \"ruleName\": \"kw-m100-1.30\",\n" +
                "            \"fileParameter\": \"1600x1600\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/chain-binder-m100-1.30.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.92.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.92.jpg\",\n" +
                "            \"fileSize\": \"790.05KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.92\",\n" +
                "            \"fileParameter\": \"1920x1080\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.92.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.93.jpg\": {\n" +
                "            \"scaleAttUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/small/pipe-wrench-a100-1.93.jpg\",\n" +
                "            \"fileSize\": \"174.62KB\",\n" +
                "            \"ruleName\": \"kw-a100-1.93\",\n" +
                "            \"fileParameter\": \"640x360\",\n" +
                "            \"mainUrl\": \"https://d3uelg7zvfr5gj.cloudfront.net/product/48YCGZQ0000000001/pipe-wrench-a100-1.93.jpg\",\n" +
                "            \"fileType\": \"jpg\"\n" +
                "        }\n" +
                "    }\n" +
                "]";


        List<Map> maps = JSONObject.parseArray(sg, Map.class);

        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(maps));
        System.out.println(apply.getMessage());
        Assert.isTrue(apply.success(), "�ɹ�");
    }

    @Test
    void testEndsWithError() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | ends-with :  ");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.4.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        System.out.println(apply.getMessage());
        Assert.isTrue(!apply.success(), "�ɹ�");
    }

    @Test
    void testEndsWith() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | ends-with : m100-1.3.jpg,m100-1.5.jpg,m100-1.4.jpg ");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.4.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success(), "ʧ��");
    }

    @Test
    void testProEndsWith() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | prior-ends-with : m100-1.3.jpg,m100-1.5.jpg,m100-1.4.jpg ");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.4.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success(), "ʧ��");
    }

    @Test
    void testlistecho() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | list-echo: > ");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.4.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success(), "ʧ��");
    }

    @Test
    void testSubstr() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | substring : 0,10 ");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("���ʾ�����򴴽������� double ���͵ı��� a �� b���ֱ�ֵΪ 10 �� 5��Ȼ����� Calculator ���е��ĸ���������������������У�divide �����ڳ���Ϊ0ʱ���׳��쳣������ʹ���� try-catch ��䲶���쳣�������������Ϣ��"));
        Assert.isTrue(apply.success(), "ʧ��");
    }

    @Test
    void testEquals() {
//
        Map<String, Object> titleMap = new HashMap<>(2);
        titleMap.put("eBayTitle", "eBayTitle���ʾ�����򴴽������� double ���͵ı��� a �� b���ֱ�ֵΪ 10 �� 5��Ȼ����� Calculator ���е��ĸ����������������");
        titleMap.put("amazonTitle", "");
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("PublishCenter.titleMap | prior-equals:amazonTitle,eBayTitle");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(titleMap));
        Assert.isTrue(apply.success() && apply.getData().toString().startsWith("amazonTitle"), "ʧ��");
    }

    @Test
    void testNumber() {
//        ebayManno.price | cal-mul:1.4,int | cal-add:0.99,number_2
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("ebayManno.price | cal-mul:1.4,int | cal-add:0.99,number_2");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("34"));
        Assert.isTrue(apply.success() && apply.getData().toString().equals("47.99"), "ʧ��");
    }

    @Test
    void testEcho() {
//        PhotoStore.attach | prior-ends-with:m100-8.jpg | echo:wrap
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("ebayManno.price | prior-ends-with:m100-1.4.jpg | echo:wrap");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.4.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success() && apply.getData().toString().endsWith("\n"), "ʧ��");
    }

    @Test
    void testEchoNone() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("ebayManno.price | prior-ends-with:m100-1.4.jpg | echo:wrap");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success() && apply.getData().toString().endsWith("\n"), "ʧ��");
    }

    @Test
    void testListEcho() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("ebayManno.price | list-echo:wrap");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success(), "ʧ��");
    }

    @Test
    void testReplace() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("ebayManno.price | replace:��,e,1 | replace:��,e,1 | replace:��,e,1 | replace:��,a,1");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("sd��s,��u,����"));
        Assert.isTrue(apply.success(), "ʧ��");
    }

    @Test
    void testConfition() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("ebayManno.price | condition-echo:��,Yes,No");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("��"));
        Assert.isTrue(apply.success(), "ʧ��");
    }

    @Test
    void testProEndsWithMap() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("test | ends-with : m100-1.1.jpg | prior-equals:size | max-size:10000");

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("url", "http://www.baidu.com/images/m100-1.1.jpg");
        dataMap.put("size", 123123);
        Map<String, Object> dMap = new HashMap<>();
        dMap.put("http://www.baidu.com/images/m100-1.1.jpg", dataMap);

        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList(dMap)));
        Assert.isTrue(apply.success(), "ʧ��");
    }

    @Test
    void testMust() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("ebayManno.price | must");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(""));
        Assert.isTrue(apply.success(), "ʧ��");
    }



    @Test
    void testMaxSiz() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("PublishCenter.Bullet Point.bulletPoints | max-size:500kb");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("31.3kb"));
        Assert.isTrue(apply.success(), "ʧ��");
    }

    @Test
    void testEndsWithList() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter();
        pipeFilterFactory.addParams("PhotoStore.allPic |  ends-with-list:f1.jpg |error-continue | map-get:mainUrl,scaleAttUrl");


        List<Map> maps = JSONObject.parseArray(data, Map.class);

        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(maps));
        System.out.println(apply.getMessage()+":"+apply.getData());
        Assert.isTrue(!apply.success(), "�ɹ�");
    }

    private static String data1 = "[\n" +
        "    \"Heavy-Duty Frame and Bags: Simplify your laundry routine with the remarkable laundry sorter cart. Its heavy-duty 600D Oxford cloth bags are waterproof and easy to clean, while the thickened metal frame and 6 horizontal bars provide enhanced stability.\",\n" +
        "    \"Ample Storage Capacity: Each heavy-duty bag holds up to 25 lbs of clothes, providing ample space for clothes, towels, bedding, and other laundry items, keeping them organized and easily accessible. The basket serves as a practical storage solution when not used for carrying laundry.\",\n" +
        "    \"3 Sections for Easy Sorting: This laundry basket is divided into 3 sections of different colors, allowing you to easily sort your laundry by whites, darks, and grey, making it convenient to keep your clothes organized.\",\n" +
        "    \"Easy to Move Around: Equipped with 4 smooth-rolling casters, our laundry hamper with wheels ensures effortless movement, making it easy to pick up laundry from every room. Two locking casters provide stability when parked, and the foamed handle on the laundry cart offers a comfortable grip, ensuring ease of maneuverability.\",\n" +
        "    \"Ultra-practical Laundry Cart: The VEVOR laundry sorter cart offers the perfect solution for all your laundry needs, ensuring everything is organized and easily accessible. Whether it's for your laundry room, bedroom, entryway, living room, small apartment, or dorm, this cart is an excellent choice to streamline your laundry routine.\"\n" +
        "]";

    private static String data = "[\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f1.jpg\",\n" +
        "            \"fileSize\": \"1.61MB\",\n" +
        "            \"ruleName\": \"kw-f1\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f1.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-fb1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-fb1.jpg\",\n" +
        "            \"fileSize\": \"372.06KB\",\n" +
        "            \"ruleName\": \"kw-fb1\",\n" +
        "            \"fileParameter\": \"1080x1080\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-fb1.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-x1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-x1.jpg\",\n" +
        "            \"fileSize\": \"223.90KB\",\n" +
        "            \"ruleName\": \"kw-x1\",\n" +
        "            \"fileParameter\": \"750x1000\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-x1.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-12f1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f2.jpg\",\n" +
        "            \"fileSize\": \"1.18MB\",\n" +
        "            \"ruleName\": \"kw-f2\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f2.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f3.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f3.jpg\",\n" +
        "            \"fileSize\": \"896.78KB\",\n" +
        "            \"ruleName\": \"kw-f3\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f3.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f4.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f4.jpg\",\n" +
        "            \"fileSize\": \"1.14MB\",\n" +
        "            \"ruleName\": \"kw-f4\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f4.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f5.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f5.jpg\",\n" +
        "            \"fileSize\": \"1004.00KB\",\n" +
        "            \"ruleName\": \"kw-f5\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f5.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f6.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f6.jpg\",\n" +
        "            \"fileSize\": \"866.75KB\",\n" +
        "            \"ruleName\": \"kw-f6\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f6.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/Chair-Cover-a100-1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/Chair-Cover-a100-1.jpg\",\n" +
        "            \"fileSize\": \"126.27KB\",\n" +
        "            \"ruleName\": \"kw-a100-1\",\n" +
        "            \"fileParameter\": \"970x300\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/Chair-Cover-a100-1.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-2.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-2.jpg\",\n" +
        "            \"fileSize\": \"492.56KB\",\n" +
        "            \"ruleName\": \"kw-m100-2\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-2.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-2.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-2.jpg\",\n" +
        "            \"fileSize\": \"50.24KB\",\n" +
        "            \"ruleName\": \"kw-a100-2\",\n" +
        "            \"fileParameter\": \"300x400\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-2.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-3.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-3.jpg\",\n" +
        "            \"fileSize\": \"222.59KB\",\n" +
        "            \"ruleName\": \"kw-m100-3\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-3.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-3.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-3.jpg\",\n" +
        "            \"fileSize\": \"20.48KB\",\n" +
        "            \"ruleName\": \"kw-a100-3\",\n" +
        "            \"fileParameter\": \"350x175\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-3.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-4.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-4.jpg\",\n" +
        "            \"fileSize\": \"628.98KB\",\n" +
        "            \"ruleName\": \"kw-m100-4\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-4.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-5.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-5.jpg\",\n" +
        "            \"fileSize\": \"433.01KB\",\n" +
        "            \"ruleName\": \"kw-m100-5\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-5.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-6.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-6.jpg\",\n" +
        "            \"fileSize\": \"548.50KB\",\n" +
        "            \"ruleName\": \"kw-m100-6\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-6.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-7.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-7.jpg\",\n" +
        "            \"fileSize\": \"1.09MB\",\n" +
        "            \"ruleName\": \"kw-m100-7\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-7.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-8.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-8.jpg\",\n" +
        "            \"fileSize\": \"778.60KB\",\n" +
        "            \"ruleName\": \"kw-m100-8\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-8.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-9.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-9.jpg\",\n" +
        "            \"fileSize\": \"653.71KB\",\n" +
        "            \"ruleName\": \"kw-m100-9\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-9.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-10.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-10.jpg\",\n" +
        "            \"fileSize\": \"575.17KB\",\n" +
        "            \"ruleName\": \"kw-m100-10\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-10.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-1.1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-1.1.jpg\",\n" +
        "            \"fileSize\": \"1.48MB\",\n" +
        "            \"ruleName\": \"kw-m100-1.1\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-1.1.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-11.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-11.jpg\",\n" +
        "            \"fileSize\": \"587.81KB\",\n" +
        "            \"ruleName\": \"kw-m100-11\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-11.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-1.2.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-1.2.jpg\",\n" +
        "            \"fileSize\": \"89.29KB\",\n" +
        "            \"ruleName\": \"kw-m100-1.2\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-1.2.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-12.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-12.jpg\",\n" +
        "            \"fileSize\": \"623.69KB\",\n" +
        "            \"ruleName\": \"kw-m100-12\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-12.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/Chair-Cover-m100-1.3.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/Chair-Cover-m100-1.3.jpg\",\n" +
        "            \"fileSize\": \"238.14KB\",\n" +
        "            \"ruleName\": \"kw-m100-1.3\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/Chair-Cover-m100-1.3.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.4.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-1.4.jpg\",\n" +
        "            \"fileSize\": \"270.80KB\",\n" +
        "            \"ruleName\": \"kw-a100-1.4\",\n" +
        "            \"fileParameter\": \"970x600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.4.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.6.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-1.6.jpg\",\n" +
        "            \"fileSize\": \"302.29KB\",\n" +
        "            \"ruleName\": \"kw-a100-1.6\",\n" +
        "            \"fileParameter\": \"1200x628\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.6.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.9.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-1.9.jpg\",\n" +
        "            \"fileSize\": \"719.63KB\",\n" +
        "            \"ruleName\": \"kw-a100-1.9\",\n" +
        "            \"fileParameter\": \"1920x1080\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.9.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.92.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-1.92.jpg\",\n" +
        "            \"fileSize\": \"739.98KB\",\n" +
        "            \"ruleName\": \"kw-a100-1.92\",\n" +
        "            \"fileParameter\": \"1920x1080\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.92.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.93.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-1.93.jpg\",\n" +
        "            \"fileSize\": \"126.50KB\",\n" +
        "            \"ruleName\": \"kw-a100-1.93\",\n" +
        "            \"fileParameter\": \"640x360\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.93.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    }\n" +
        "]";
}
