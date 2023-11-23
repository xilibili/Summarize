## 规则引擎调研总结

### 一、什么是规则引擎以及为什么需要规则引擎？

规则引擎就是一段if-then结构，if里面是策略规则，then是满足规则所要执行的行为。这一点很像我们人工智能里面学的专家系统和产生式推理。

> 一个小例子：
>  假如我们有个业务场景，当客户的积分位于一个区间A时，我们给予他一个头衔a，当一个客户的积分位于区间B时，我们给予他一个头衔b，当客户的积分位于区间C时，我们给予他一个 头衔c。如果我们使用if-else-then来写，是可以实现的，但是这里存在一个问题：规则定义和代码耦合在一起了。如果我们改变规则，把区间A，B，C 改成D，E，F，又或是将规则增加，从3组变为100组，那么我们改代码实在是太麻烦了。这时候规则引擎就派上用场了，我们希望把规则和代码解耦，形成一个规则引擎，以适应复杂多变的业务场景，或者更加精细化的运营。

### 二、推理策略算法

- Rete algorithm
- Leaps algorithm
- Rete OO algorithm

### 三、Rete算法

相关知识地址：

- 包括Rete算法和开源规则引擎和代码实践 https://developer.ibm.com/zh/technologies/artificial-intelligence/articles/os-drools/（网址打不开了）
- Rete算法的例子搭配原理讲解 https://blog.csdn.net/houwenbin1986/article/details/93893684
- Drools文档中ReteOO的实现 https://docs.jboss.org/drools/release/6.0.0.Final/drools-docs/html/HybridReasoningChapter.html#ReteOO
- CMU博士论文 http://reports-archive.adm.cs.cmu.edu/anon/1995/CMU-CS-95-113.pdf
- Rete算法相关链接汇总 https://blog.csdn.net/lthirdonel/article/details/80948922

现在的大多数规则引擎都是基于Rete算法实现的。我们首先将规则分为左右两个部分，分别叫做LHS和RHS，直译为左手边和右手边，分别代表规则的条件和结论。Rete算法首先要根据输入事实建立规则网络，然后再在网络中进行匹配得到结论

> 下面是一个简单的利用Rete算法进行规则匹配的例子：
> 规则内容如下：
> IF：
> 年级是三年级以上，
> 性别是男的，
> 年龄小于10岁，
> 身体健壮，
> 身高170cm以上，
> THEN：
>  这个男孩是一个篮球苗子，需要培养

这里匹配过程中事实在网络图中的执行顺序是A->B->C->D->E->F->G->H->I->规则匹配通过

![](https://gitee.com/YY-oops/blogimage/raw/master/img/Rete%E7%AE%97%E6%B3%95%E4%BE%8B%E5%AD%90.png)

这里涉及到一些结点，具体的结点的意思在下图讲解，结点基本上是一一对应的，需要说明的是上图的红色结点对应于下图的绿色结点，都是Join Nodes，对匹配的规则链进行合并。上图的绿色结点对应于下图的紫色结点，是Beta Mermory，用于将匹配的模式链存在内存中进行后续的合并操作。

![image-20210316150306439](https://gitee.com/YY-oops/blogimage/raw/master/img/image-20210316150306439.png)

下面这个图来源于MCU博士论文，能够很好的融合展示上述两个图的内容

![image-20210316150810505](https://gitee.com/YY-oops/blogimage/raw/master/img/image-20210316150810505.png)

### 四、目前已有的规则引擎

- #### **Drools**
  
  **描述：**目前最为广泛并且开源的规则引擎
  **特点：**

- 具有非常完善的社区支持

- 适用于Java项目

- 非常的笨重，复杂性提高

- 引入了新的规则定义语言
  **Download地址：**https://www.drools.org/

- #### **Easy Rules**
  
  **描述：**是一个比较简单的开源的规则引擎，使用简单的Java注解方式或者Java代码编程方式或者使用表达式语言或者用规则描述算子定义规则，然后使用非常简单的Java代码加载事实，规则，然后就可以在已知的事实上实现具体的行为了。
  **特点：**

- 简单易用

- 一个非常轻量级的框架

- 定义规则的方式丰富多样
  **Download地址：**https://github.com/j-easy/easy-rules/releases/tag/easy-rules-4.1.0

- #### RuleEngine
  
  **描述：**一个可以使用SQL脚本来定义规则的中间件，如下的地址是github上基于RuleEngine的一个web可视化配置项目。
  **Download地址：**https://github.com/rule-engine/rule-engine/releases/tag/v1.0-beta.1

- #### **Aviator**
  
  **描述：**是一个高性能、轻量级的基于java实现的表达式引擎，它动态地将String类型的表达式编译成Java ByteCode并交给JVM执行。
  **Download地址：**https://code.google.com/archive/p/aviator/downloads

- #### **Aswan**
  
  **描述：**陌陌开发的静态规则引擎
  **Download地址：**https://github.com/momosecurity/aswan

- #### **URULE**
  
  **描述：**URULE是一款基于Rete算法的纯Java规则引擎，提供规则集、决策表、决策树、评分卡，规则流等各种规则表现工具及基于网页的可视化设计器，可快速开发出各种复杂业务规则。目前开源版本无人维护。
  **Download地址：**https://github.com/youseries/urule/

- #### Esper
  
  **描述：**Esper设计目标为CEP的轻量级解决方案，可以方便的嵌入服务中，提供CEP功能。
  **Download地址：**https://www.espertech.com/esper/esper-downloads/

- #### **JLisa**
  
  **描述**：JLisa是一个利用java构建商业规则的强大框架。它实现了JSR94 Rule Engine API。
  **Download地址：**http://jlisa.sourceforge.net/

- #### CLIPS
  
  **描述：**是采用C语言实现的规则引擎，是一个构建专家系统的工具，具有高移植性、高扩展性、强大的知识表示能力和编程方式以及低成本的特点
  **Download地址：**http://www.clipsrules.net/

- #### CKrule
  
  **描述：**非商用则可免费，使用C#编写，商用需要购买
  **Download地址：**http://ckrule.cn/cn/product/buy.html

- #### Jess
  
  **描述：**是CLIPS的Java实现，未开源

- #### IBM ILOG
  
  **描述：** 商业产品，未开源

- #### **Visual Rules**
  
  **描述：**商业产品，未开源

### 五、其他公司的选择

1. **美团点评(自研Maze框架)**
   在门店信息校验使用硬编码，门店审核流程使用Drools，绩效指标计算业务中使用绩效定制引擎。最后三个方案均放弃了，原因如下：
   
   > - 硬编码迭代成本高。
   > - Drools维护门槛高。视图对非技术人员不友好，即使对于技术人员来说维护成本也不比硬编码低。
   > - 绩效定制引擎表达能力有限且扩展性差，无法推广到别的业务。

最后美团根据需求和业务特点，开发了Maze框架，其中包含两个主要的引擎分别是MazeGO（策略引擎）和MazeQL（结构化数据处理引擎）。
文章地址：https://tech.meituan.com/2017/06/09/maze-framework.html

1. **美团酒旅(使用开源Aviator)**
   综合考虑了Esper，Drools，Aviator后选择了Aviator。
   文章地址：https://tech.meituan.com/2018/04/19/hb-rt-operation.html

2. **美团信息安全（自研框架Zeus）**
   文章地址：https://tech.meituan.com/2020/05/14/meituan-security-zeus.html

3. **网易考拉（自研，名字未知）**
   一开始就放弃了Drools使用groovy脚本实现
   文章地址：https://sq.163yun.com/blog/article/213006222321659904?spm=a2c4e.10696291.0.0.74c319a4n3RKZk

4. **有赞（使用开源Drools）**
   文章地址：https://tech.youzan.com/rules-engine/?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io

5. **携程（使用开源Drools）**
   文章地址：https://tech.youzan.com/rules-engine/?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io

6. **Apache Nifi，Open Remote，Open Smart Register Platform （使用Easy Rules规则引擎）**
   文章地址：https://github.com/j-easy/easy-rules