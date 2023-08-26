# pipe-filter-parent
表达式引擎
## quick start
使用方式
```
        Map<String, Object> body = new HashMap<>();
        body.put("file-size", "20MB");
        body.put("image", "http://www.baidu.om/20");
        
        Map<String, Object> params = new HashMap<>();
        params.put("test", body);
        Object render = TemplateUtils.parse("{test | max-size-filter:file-size,20MB}").render(params);
```
使用介绍
TemplateUtils.parse(表达式).render(参数);
如上代码所示，表达式均是以花括号括起来的字符串，“{test | max-size-filter:file-size,20MB}”，其中test为输入变量的名称，为参数中map的key，传递参数需要包含test为key的值。
max-size-filter为内置的处理指令，引擎内部包括很多指令

## Maven依赖

```xml
        <dependency>
            <groupId>icu.develop</groupId>
            <artifactId>pipe-filter</artifactId>
            <version>1.0.1</version>
        </dependency>
```

## 使用示例

```
ProductCenter.imageList | ends-with:test1,test2,test3 | prior-ends-with:test3 | echo:wrap
```

从imageList中先按顺序获取test1，test2，test3后缀的图片，组成集合，在根据优先级获取test3的图片，若取出的图片为空，则使用回车换行拼接

> ProductCenter.imageList 变量
>
> ends-with 指令，根据后缀匹配指令
>
> prior-ends-with 指令，带优先级的后缀匹配指令
>
> echo 输出指令，若输入数据存在，则使用换行符拼接，若输入数据不存在，则直接拼接



## 变量写法

```
{inventory.stock}
```

变量使用{}括起来，**前面添加“.”号**，表示按照数据量依次填充excel，若前面不加“.”,则表示只填充一个数据



## 指令说明



### 指令：trim

```
ProductCenter.title | trim
```

#### 说明：

去掉字符串两边的空格

| 输入类型 | 输出类型 |
| -------- | -------- |
| 字符串   | 字符串   |



### 指令：equals

```
ProductCenter.titleMap | equals:test1,test2,test3
```

#### 说明：

ProductCenter.titleMap变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串相等匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行相等匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行相等匹配，若能匹配到，则返回对象key的值

从key value结构中，判断key是否和参数是否相等，若相等则输出key value中的value，若参数为多个，则输出集合

参数：test1，test2等可以多个，也可以使用一个

| 参数个数 | 输入数据类型    | 输出类型        |
| -------- | --------------- | --------------- |
| 一个     | Map{key：value} | value字符串     |
| 多个     | Map{key：value} | value字符串数组 |
| 一个     | 字符串数组      | 字符串          |
| 多个     | 字符串数组      | 字符串数组      |
| 一个     | 对象            | 对象属性值      |
| 多个     | 对象            | 对象属性值数组  |
| 一个     | Map数组         | Map             |
| 多个     | Map数组         | Map数组         |



### 指令：prior-equals

```
ProductCenter.titleMap | prior-equals:test1,test2,test3
```

#### 说明：

ProductCenter.titleMap变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串相等匹配，若能匹配到，则返回字符串

- Map，则根据map中的key来进行相等匹配，若能匹配到，则返回map中key对应的value值

- 对象，转换为Map，再根据key来进行相等匹配，若能匹配到，则返回对象key的值



从key value结构中，判断key是否和参数是否相等，若相等则输出key value中的value，若参数为多个，则按照优先级获取；

参数：test1，test2等可以多个，也可以使用一个；多个参数存在的情况下，若和test1的key不存在，则获取test2的key对应的value

| 参数个数 | 输入数据类型    | 输出类型    |
| -------- | --------------- | ----------- |
| 一个     | Map{key：value} | value字符串 |
| 多个     | Map{key：value} | value字符串 |
| 一个     | 字符串数组      | 字符串      |
| 多个     | 字符串数组      | 字符串      |
| 一个     | 对象            | 对象属性值  |
| 多个     | 对象            | 对象属性值  |
| 一个     | Map数组         | Map         |
| 多个     | Map数组         | Map         |






### 指令：ends-with

```
ProductCenter.imageList | ends-with:test1,test2,test3
```

#### 说明：

ProductCenter.imageList变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串后缀匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行后缀匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行后缀匹配，若能匹配到，则返回对象key的值

后缀匹配指令，参数test可以多个，根据参数的顺序，依次和传入字符串集合匹配，若存在则记录，直到所有参数都匹配结束

| 参数个数 | 输入数据类型    | 输出类型        |
| -------- | --------------- | --------------- |
| 一个     | Map{key：value} | value字符串     |
| 多个     | Map{key：value} | value字符串数组 |
| 一个     | 字符串数组      | 字符串          |
| 多个     | 字符串数组      | 字符串数组      |
| 一个     | 对象            | 对象属性值      |
| 多个     | 对象            | 对象属性值数组  |
| 一个     | Map数组         | Map             |
| 多个     | Map数组         | Map数组         |



### 指令：ends-with-list

```
ProductCenter.imageList | ends-with-list:test1,test2,test3
```

#### 说明：

ProductCenter.imageList变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串后缀匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行后缀匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行后缀匹配，若能匹配到，则返回对象key的值

后缀匹配指令，参数test可以多个，根据参数的顺序，依次和传入字符串集合匹配，若存在则记录，直到所有参数都匹配结束

| 参数个数 | 输入数据类型    | 输出类型        |
| -------- | --------------- | --------------- |
| 一个     | Map{key：value} | value字符串     |
| 多个     | Map{key：value} | value字符串数组 |
| 一个     | 字符串数组      | 字符串数组      |
| 多个     | 字符串数组      | 字符串数组      |
| 一个     | 对象            | 对象属性值      |
| 多个     | 对象            | 对象属性值数组  |
| 一个     | Map数组         | Map数组         |
| 多个     | Map数组         | Map数组         |








### 指令：prior-ends-with

```
ProductCenter.imageList | prior-ends-with:test1,test2,test3
```

#### 说明：

ProductCenter.imageList变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串后缀匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行匹配，若能匹配到，则返回对象key的值

后缀优先匹配指令，参数test可以多个，根据参数的顺序，依次和传入字符串集合匹配，若存在则直接返回，不进行后续匹配

| 参数个数 | 输入数据类型    | 输出类型    |
| -------- | --------------- | ----------- |
| 一个     | Map{key：value} | value字符串 |
| 多个     | Map{key：value} | value字符串 |
| 一个     | 字符串数组      | 字符串      |
| 多个     | 字符串数组      | 字符串数组  |
| 一个     | 对象            | 对象属性值  |
| 多个     | 对象            | 对象属性值  |
| 一个     | Map数组         | Map         |
| 多个     | Map数组         | Map         |



### 指令：prior-ends-with-list

```
ProductCenter.imageList | prior-ends-with-list:test1,test2,test3
```

#### 说明：

ProductCenter.imageList变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串后缀匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行匹配，若能匹配到，则返回对象key的值

后缀优先匹配指令，参数test可以多个，根据参数的顺序，依次和传入字符串集合匹配，若存在则直接返回，不进行后续匹配

| 参数个数 | 输入数据类型    | 输出类型       | 备注                  |
| -------- | --------------- | -------------- | --------------------- |
| 一个     | Map{key：value} | value字符串    |                       |
| 多个     | Map{key：value} | value字符串    |                       |
| 一个     | 字符串数组      | `字符串数组`， | test1的值为多个的情况 |
| 多个     | 字符串数组      | 字符串数组     |                       |
| 一个     | 对象            | 对象属性值     |                       |
| 多个     | 对象            | 对象属性值     |                       |
| 一个     | Map数组         | Map数组        |                       |
| 多个     | Map数组         | Map数组        |                       |






### 指令：starts-with

```
ProductCenter.imageList | starts-with:test1,test2,test3
```

#### 说明：

ProductCenter.imageList变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串前缀匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行前缀匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行前缀匹配，若能匹配到，则返回对象key的值

前缀匹配指令，参数test可以多个，根据参数的顺序，依次和传入字符串集合匹配，若存在则记录，直到所有参数都匹配结束

| 参数个数 | 输入数据类型    | 输出类型        |
| -------- | --------------- | --------------- |
| 一个     | Map{key：value} | value字符串     |
| 多个     | Map{key：value} | value字符串数组 |
| 一个     | 字符串数组      | 字符串          |
| 多个     | 字符串数组      | 字符串数组      |
| 一个     | 对象            | 对象属性值      |
| 多个     | 对象            | 对象属性值数组  |
| 一个     | Map数组         | Map             |
| 多个     | Map数组         | Map数组         |




### 指令：prior-starts-with

```
ProductCenter.imageList | prior-starts-with:test1,test2,test3
```

#### 说明：

ProductCenter.imageList变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串前缀匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行前缀匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行前缀匹配，若能匹配到，则返回对象key的值

前缀优先匹配指令，参数test可以多个，根据参数的顺序，依次和传入字符串集合匹配，若存在则直接返回，不进行后续匹配

| 参数个数 | 输入数据类型    | 输出类型    |
| -------- | --------------- | ----------- |
| 一个     | Map{key：value} | value字符串 |
| 多个     | Map{key：value} | value字符串 |
| 一个     | 字符串数组      | 字符串      |
| 多个     | 字符串数组      | 字符串数组  |
| 一个     | 对象            | 对象属性值  |
| 多个     | 对象            | 对象属性值  |
| 一个     | Map数组         | Map         |
| 多个     | Map数组         | Map         |




### 指令：pattern

```
ProductCenter.imageList | pattern:test1,test2,test3
```

#### 说明：

ProductCenter.imageList变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串正则匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行匹配，若能匹配到，则返回对象key的值

正则匹配指令，参数test可以多个，根据参数的顺序，依次和传入字符串集合匹配，若存在则记录，直到所有参数都匹配结束

| 参数个数 | 输入数据类型    | 输出类型        |
| -------- | --------------- | --------------- |
| 一个     | Map{key：value} | value字符串     |
| 多个     | Map{key：value} | value字符串数组 |
| 一个     | 字符串数组      | 字符串          |
| 多个     | 字符串数组      | 字符串数组      |
| 一个     | 对象            | 对象属性值      |
| 多个     | 对象            | 对象属性值数组  |
| 一个     | Map数组         | Map             |
| 多个     | Map数组         | Map数组         |



### 指令：prior-pattern

```
ProductCenter.imageList | prior-pattern:test1,test2,test3
```

#### 说明：

ProductCenter.imageList变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串正则匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行正则匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行正则匹配，若能匹配到，则返回对象key的值

正则优先匹配指令，参数test可以多个，根据参数的顺序，依次和传入字符串集合匹配，若存在则直接返回，不进行后续匹配

| 参数个数 | 输入数据类型    | 输出类型    |
| -------- | --------------- | ----------- |
| 一个     | Map{key：value} | value字符串 |
| 多个     | Map{key：value} | value字符串 |
| 一个     | 字符串数组      | 字符串      |
| 多个     | 字符串数组      | 字符串      |
| 一个     | 对象            | 对象属性值  |
| 多个     | 对象            | 对象属性值  |
| 一个     | Map数组         | Map         |
| 多个     | Map数组         | Map         |



### 指令：contains

```
ProductCenter.imageList | contains:test1,test2,test3
```

#### 说明：

ProductCenter.imageList变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串包含匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行包含匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行包含匹配，若能匹配到，则返回对象key的值

包含匹配指令，参数test可以多个，根据参数的顺序，依次和传入字符串集合匹配，若存在则记录，直到所有参数都匹配结束

| 参数个数 | 输入数据类型    | 输出类型        |
| -------- | --------------- | --------------- |
| 一个     | Map{key：value} | value字符串     |
| 多个     | Map{key：value} | value字符串数组 |
| 一个     | 字符串数组      | 字符串          |
| 多个     | 字符串数组      | 字符串数组      |
| 一个     | 对象            | 对象属性值      |
| 多个     | 对象            | 对象属性值数组  |
| 一个     | Map数组         | Map             |
| 多个     | Map数组         | Map数组         |



### 指令：prior-contains

```
ProductCenter.imageList | prior-contains:test1,test2,test3
```

#### 说明：

ProductCenter.imageList变量值的类型：字符串数组、Map、对象、Map数组或对象数组

- 字符串数组，则根据字符串包含匹配，若能匹配到，则返回字符串
- Map，则根据map中的key来进行包含匹配，若能匹配到，则返回map中key对应的value值
- 对象，转换为Map，再根据key来进行包含匹配，若能匹配到，则返回对象key的值

包含优先匹配指令，参数test可以多个，根据参数的顺序，依次和传入字符串集合匹配，若存在则直接返回，不进行后续匹配

| 参数个数 | 输入数据类型    | 输出类型    |
| -------- | --------------- | ----------- |
| 一个     | Map{key：value} | value字符串 |
| 多个     | Map{key：value} | value字符串 |
| 一个     | 字符串数组      | 字符串      |
| 多个     | 字符串数组      | 字符串      |
| 一个     | 对象            | 对象属性值  |
| 多个     | 对象            | 对象属性值  |
| 一个     | Map数组         | Map         |
| 多个     | Map数组         | Map         |



### 指令：list-index

```
ProductCenter.imageList | list-index:ind
```

#### 说明：

集合根据下标取字符串指令，参数idx必须是数字，idx下标输入0/1都表示第一个，最大不能超过输入集合的最大值

| 参数个数 | 输入类型   | 输出类型   |
| -------- | ---------- | ---------- |
| 一个     | 字符串集合 | 一个字符串 |



### 指令：list-echo

```
ProductCenter.imageList | list-echo:BLANK/WRAP/COMMA/xxx
```

#### 说明：

集合转换字符串指令，BLANK表示采用空格拼接，WRAP表示采用回车换行拼接，COMMA表示采用逗号拼接，xxx表示直接使用xxx字符串拼接

**xxx支持转义字符，请参考文档附录-转义字符表**

| 参数个数                  | 输入类型   | 输出类型   |
| ------------------------- | ---------- | ---------- |
| 一个:BLANK/WRAP/COMMA/xxx | 字符串集合 | 一个字符串 |



### 指令：list-echo-condition

```
ProductCenter.imageList | list-echo-condition:BLANK/WRAP/COMMA/xxx,(max-length:100)
```

#### 说明：

集合转换字符串指令，BLANK表示采用空格拼接，WRAP表示采用回车换行拼接，COMMA表示采用逗号拼接，xxx表示直接使用xxx字符串拼接

**xxx支持转义字符，请参考文档附录-转义字符表**

按照顺序拼接，根据子指令（max-length）判断，若长度超过100，则返回上一次拼接的字符串，若不超过100，则继续拼接，直到超过最大值或者集合拼接完毕

| 参数个数                         | 输入类型   | 输出类型   |
| -------------------------------- | ---------- | ---------- |
| 2个:BLANK/WRAP/COMMA/xxx和子指令 | 字符串集合 | 一个字符串 |





### 指令：list-range

```
ProductCenter.imageList | list-range:index,count
```

#### 说明：

集合范围获取指令，参数index表示集合下标，从0开始，参数count表示获取的数量，从index开始连续获取count数量；若返回中只有一个字符串，则输出类型扔为包含一个字符串的集合

| 参数个数 | 输入类型   | 输出类型   |
| -------- | ---------- | ---------- |
| 2个      | 字符串集合 | 字符串集合 |



### 指令：echo

```
ProductCenter.title | echo:BLANK/WRAP/COMMA/xxx
```

#### 说明：

字符串后追加特定字符指令，用于判断传入字符串是否存在，若不存在则不处理，存在则进行字符追加

BLANK表示采用空格拼接，WRAP表示采用回车换行拼接，COMMA表示采用逗号拼接，xxx表示直接使用xxx字符串拼接

**xxx支持转义字符，请参考文档附录-转义字符表**

| 参数个数                | 输入类型 | 输出类型  |
| ----------------------- | -------- | --------- |
| 1个BLANK/WRAP/COMMA/xxx | 字符串   | 空/字符串 |



### 指令：cal-add

```
ProductCenter.number | cal-add:num,int/number_n
```

#### 说明：

数值加法指令，第一个参数num表示需要进行加法运算的数值，第二个int/number_n参数，表示输出类型，int表示输出int类型，number表示输出带小数的数字，number_n中的n表示小数位数。

| 参数个数 | 输入类型 | 输出类型        |
| -------- | -------- | --------------- |
| 2个参数  | 数字     | 整形数字/浮点数 |



### 指令：cal-sub

```
ProductCenter.number | cal-sub:num,int/number_n
```

#### 说明：

数值减法指令，第一个参数num表示需要进行减法运算的数值，第二个int/number_n参数，表示输出类型，int表示输出int类型，number表示输出带小数的数字，number_n中的n表示小数位数。

| 参数个数 | 输入类型 | 输出类型        |
| -------- | -------- | --------------- |
| 2个参数  | 数字     | 整形数字/浮点数 |



### 指令：cal-nul

```
ProductCenter.number | cal-mul:num,[int/number_n]
```

#### 说明：

数值乘法指令，第一个参数num表示需要进行乘法运算的数值，第二个int/number_n参数，表示输出类型，int表示输出int类型，number表示输出带小数的数字，number_n中的n表示小数位数。

| 参数个数 | 输入类型 | 输出类型        |
| -------- | -------- | --------------- |
| 2个参数  | 数字     | 整形数字/浮点数 |



### 指令：cal-div

```
ProductCenter.number | cal-div:num,[int/number_n]
```

#### 说明：

数值除法指令，第一个参数num表示需要进行除法运算的数值，数值不能为0，第二个int/number_n参数，表示输出类型，int表示输出int类型，number表示输出带小数的数字，number_n中的n表示小数位数。

| 参数个数 | 输入类型 | 输出类型        |
| -------- | -------- | --------------- |
| 2个参数  | 数字     | 整形数字/浮点数 |



### 指令：substring

```
ProductCenter.string | substring:beginIndex,endIndex
```

#### 说明：

字符串截取指令，第一个参数beginIndex表示从哪个位置开始，第二个参数endIndex表示到哪个位置结束

beginIndex:从0开始

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 2个参数  | 字符串   | 字符串   |



### 指令：replace

```
ProductCenter.string | replace:oldChar,newChar,all
```

#### 说明：

字符串替换指令，第一个参数oldChar表示被替换的字符，第二个参数newChar表示替换的新字符，第三个参数all是数值类型，1/0 ,1表示字符串中查找到oldChar全部替换为newChar；0 表示 只在字符串中查找到第一个oldChar，后替换为newChar。

**支持转义字符，请参考文档附录-转义字符表**

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 3个参数  | 字符串   | 字符串   |



### 指令：replace-exclude-number

```
ProductCenter.string | replace-exclude-number:oldChar,newChar,all
```

#### 说明：

字符串替换指令，第一个参数oldChar表示被替换的字符，第二个参数newChar表示替换的新字符，第三个参数all是数值类型，1/0 ,1表示字符串中查找到oldChar全部替换为newChar；0 表示 只在字符串中查找到第一个oldChar，后替换为newChar。

**支持转义字符，请参考文档附录-转义字符表**

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 3个参数  | 字符串   | 字符串   |





### 指令：replace-regex

```
ProductCenter.string | replace-regex:oldChar,newChar,all
```

#### 说明：

使用正则表达式替换

字符串替换指令，第一个参数oldChar表示被替换的字符，第二个参数newChar表示替换的新字符，第三个参数all是数值类型，1/0 ,1表示字符串中查找到oldChar全部替换为newChar；0 表示 只在字符串中查找到第一个oldChar，后替换为newChar。

**支持转义字符，请参考文档附录-转义字符表**

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 3个参数  | 字符串   | 字符串   |





### 指令：date-format

```
ProductCenter.date | date-format:DATE_FORMAT
```

#### 说明：

日期格式化指令，参数DATE_FORMAT为日期格式化字符串，例如：yyyy-MM-dd HH:mm:ss

参数DATE_FORMAT可以没有，若没有则使用默认格式化，默认格式化为yyyy-MM-dd HH:mm:ss

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 0/1      | 日期类型 | 字符串   |



### 指令：condition-echo

```
ProductCenter.date | condition-echo:是,Yes,No
```

#### 说明：

判断date是否等于"是"，若相等，则输出"Yes"，不相等则输出"no"

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 3        | 字符串   | 字符串   |



### 指令：replace-special

```
ProductCenter.title | replace-special
```

#### 说明：

替换title中的音标字符，没有参数

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 0        | 字符串   | 字符串   |



### 指令：max-size

```
ProductCenter.number | max-size:max
```

#### 说明：

ProductCenter.number 变量值为数字或可转换为数字的字符串，max参数为数字；max-size指令逻辑：若ProductCenter.number变量值比max小，则正常输出，若比max大，则提示错误信息

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 1        | 数字     | 数字     |



### 指令：min-size

```
ProductCenter.number | min-size:min
```

#### 说明：

ProductCenter.number 变量值为数字或可转换为数字的字符串，min参数为数字；min-size指令逻辑：若ProductCenter.number变量值比min大，则正常输出，若比min小，则提示错误信息

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 1        | 数字     | 数字     |





### 指令：max-size-filter

```
ProductCenter.number | max-size-filter:key,max
```

#### 说明：

ProductCenter.number 变量值为map或对象，或是对象或map的list, key参数是map的Key或是对象的属性，max参数为数字；max-size-filter指令逻辑：若ProductCenter.number变量值比max小，则正常输出，若比max大，则提示错误信息

| 参数个数 | 输入类型        | 输出类型        |
| -------- | --------------- | --------------- |
| 2        | 对象或Map       | 对象或Map       |
| 2        | 对象或Map的List | 对象或Map的List |



### 指令：min-size-filter

```
ProductCenter.number | min-size-filter:key,xxx
```

#### 说明：

ProductCenter.number 变量值为map或对象，或是对象或map的list, key参数是map的Key或是对象的属性，xxx参数为数字；min-size-filter指令逻辑：若ProductCenter.number变量值比xxx大，则正常输出，若比xxx小，则提示错误信息

| 参数个数 | 输入类型        | 输出类型        |
| -------- | --------------- | --------------- |
| 2        | 对象或Map       | 对象或Map       |
| 2        | 对象或Map的List | 对象或Map的List |





### 指令：max-length

```
ProductCenter.string | max-length:max
```

#### 说明：

ProductCenter.string变量值为字符串，max参数为数字；max-length指令逻辑：若ProductCenter.string变量长度比max小，则正常输出，若比max大，则提示错误信息

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 1        | 字符串   | 字符串   |



### 指令：formula

```
.formula | formula:formulaValue
```

#### 说明：

formula变量值为空或者是公式字符串，formulaValue参数为公式字符串。公式中若涉及到“,”号，则替换为“#”。formula指令逻辑：若formula变量值存在，则使用变量值作为公式字符串输出，若formula变量值不存在，则采用formulaValue参数值作为公式输出

| 参数个数 | 输入类型   | 输出类型 |
| -------- | ---------- | -------- |
| 1        | 公式字符串 | 公式     |



### 指令：must

```
ProductCenter.number | must
```

#### 说明：

must指令，判断变量ProductCenter.number是否必填，若ProductCenter.number变量值没有，则提示错误，若值存在，则进行下一步操作

| 参数个数 | 输入类型 | 输出类型 |
| -------- | -------- | -------- |
| 0        | 任意类型 | 任意类型 |



### 指令：wrapper

```
ProductCenter.title | wrapper:BLANK/WRAP/COMMA/xxx
```

#### 说明：

在字符串左右追加特定字符指令，若传入字符串是存在，则进行字符前后追加，若不存在则不处理

BLANK表示采用空格拼接，WRAP表示采用回车换行拼接，COMMA表示采用逗号拼接，xxx表示直接使用xxx字符串拼接



**支持转义字符，请参考文档附录-转义字符表**

```
ProductCenter.title | wrapper:BLANK
ProductCenter.title | wrapper:BLANK,BLANK
# 使用空格包裹字符串，例如 输出“ title ”

ProductCenter.title | wrapper:WRAP
ProductCenter.title | wrapper:WRAP,WRAP
# 使用空格包裹字符串，例如 输出“\ntitle\n”

ProductCenter.title | wrapper:COMMA
ProductCenter.title | wrapper:COMMA,COMMA
# 使用空格包裹字符串，例如 输出“,title,”

ProductCenter.title | wrapper:<b>,</b> 
# 使用空格包裹字符串，例如 输出“<b>title</b>”
```



| 参数个数                         | 输入类型 | 输出类型  |
| -------------------------------- | -------- | --------- |
| 1个BLANK/WRAP/COMMA/xxx或2个参数 | 字符串   | 空/字符串 |



### 指令：map-get

```
ProductCenter.imageInfo | map-get:url
```

#### 说明：

ProductCenter.imageInfo 参数为对象或map，map-get获取url，输出url对象，若url是字符串，则是字符串

| 参数个数 | 输入类型      | 输出类型         | 举例              |
| -------- | ------------- | ---------------- | ----------------- |
| 1个      | 对象或map     | 一个任意类型对象 | map-get:url       |
| 多个     | 对象或map     | 任意类型数组     | map-get:url1,url2 |
| 1个      | 对象或map数组 | 数组             | map-get:url       |
| 多个     | 对象或map数组 | 数组             | map-get:url1,url2 |



### 指令：watch

```
ProductCenter.imageInfo | watch
```

#### 说明：

watch表示需要监听字段，只是标识指令，不做其他操作



### 指令：error-continue

```
ProductCenter.imageInfo | list-range:index,count | error-continue | map-get:url
```

#### 说明：

error-continue指令，若前面指令错误，是否继续执行，添加指令则表明继续执行，不添加则停止后面的指令执行

如上面所示，list-range指令错误后，map-get指令会继续执行



### 指令：default

```
ProductCenter.imageInfo | default:xxxx
```

#### 说明：

default指令，若前面值为空或者null，则返回xxxx默认值

| 参数个数 | 输入类型           | 输出类型         | 举例        |
| -------- | ------------------ | ---------------- | ----------- |
| 1个      | 对象、集合、字符串 | 一个任意类型对象 | defalut:xxx |



### 指令：extract

```
ProductCenter.imageInfo | extract:begin,end
```

#### 说明：

extract指令，begin,end参数，可以是数字或字符，若输入数字，则是和substring指令一样；如是字符，则查找字符的位置，再截取。支持转义字符及优先级括号

| 参数个数 | 输入类型           | 输出类型   | 举例          |
| -------- | ------------------ | ---------- | ------------- |
| 2个      | 字符串集合、字符串 | 字符串数组 | extract:(:),2 |



### 指令：prior-extract

```
ProductCenter.imageInfo | prior-extract:(begin,end),(begin,end)
```

#### 说明：

优先级prior-extract指令，begin,end参数，可以是数字或字符，若输入数字，则是和substring指令一样；如是字符，则查找字符的位置，再截取。支持转义字符及优先级括号

| 参数个数 | 输入类型           | 输出类型   | 举例                            |
| -------- | ------------------ | ---------- | ------------------------------- |
| 2个      | 字符串集合、字符串 | 字符串数组 | extract:(begin,end),(begin,end) |



### 指令：random-string

```
.empty | random-string:mode,digit
```

#### 说明：

random-string指令，mode参数可选范围1,2,3；1表示数字，2表示大小写字幕，3表示数字和字母混合；digit表示随机位数，最大256位



### 指令：echo-cell-row

```
.empty | echo-cell-row
```

#### 说明：

echo-cell-row指令，输出excel单元格的行号，从1开始



## 附录

### 转义字符表

| 字符                           | 转义字符 | 描述   |
| ------------------------------ | -------- | ------ |
| "                              | &quot    | 双引号 |
| &                              | &amp     |        |
| <                              | &lt      |        |
| >                              | &gt      |        |
| 不断开空格(non-breaking space) | &nbsp    |        |
| ¥                              | &yen     |        |
| \|                             | &brvbar  |        |
| §                              | &sect    |        |
| «                              | &laquo   |        |
| #                              | &#35     |        |
| $                              | &#36     |        |
| %                              | &#37     |        |
| '                              | &#39     | 单引号 |
| (                              | &#40     |        |
| )                              | &#41     |        |
| *                              | &#42     |        |
| +                              | &#43     |        |
| ,                              | &#44     | 逗号   |
| -                              | &#45     | 连字号 |
| .                              | &#46     | 句号   |
| /                              | &#47     | 斜杠   |
| :                              | &#58     | 冒号   |
| ;                              | &#59     | 分号   |
| =                              | &#61     |        |
| [                              | &#91     |        |
| \                              | &#92     | 反斜杠 |
| ]                              | &#93     |        |
| _                              | &#95     | 下划线 |
| 空                             | &empty   | 空     |

