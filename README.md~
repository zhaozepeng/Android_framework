# android_framework #
<em>an android framework in order for rapid development</em>

##1.开发环境##
环境为*android studio* + *jdk1.7*<br/>
![framework](./markdown_image/framework.png "framework")<br/>

##2.框架结构##
该框架分为三个部分：<br/>
<strong> libcore </strong>层<br/>
<strong> libcore-ui </strong>层<br/>
<strong> testsample </strong>层<br/>
第三层为测试代码，重点在第一和第二层...<br/>

##3.框架详细概述##

###第一层libcore###
该层为基础核心代码层，该层的代码特点是封装了应用所应该使用的基础功能，好处是封装的功能仅仅提供简单的接口，这样就能够使得应用只需要更改libcore的封装实现，而不用修改底层的代码，轻松实现功能的变更，功能列表如下所示

<ol>
<li>Application优化，具体看代码</li>
<li>最基础的Activity和Fragment类</li>
<li>ActivityManager类，该类使用栈来管理所有的activity</li>
<li>log类，该类用来统一log的打印，请确保应用同一使用该类，可以修改该类代码来实现log打印</li>
<li>Toast类，该类用来弹出toast，请确保统一使用该类</li>
<li>Utils类，分为CommonUtils，BitmapUtils....</li>
<li>GuideManager类，用来显示指引蒙版</li>
<li>dialog类，定义了一个应用dialog所应该具备的基础行为</li>
<li>database类，将最基本的数据库类进行了非常便捷的封装，对表名和列名进行了枚举的封装，使用很方便</li>
</ol>

###第二层libcore-ui层###
该层为基础核心扩展层，扩展libcore的层的代码，并且定义应用的基本样式，这样一个公司的不同应用就能够统一样式，方便管理

<ol>
<li>扩展实现的Activity和Fragment类，Activity类中定义了整个应用的基本简单样式，如底部的弹出框，顶部bar的样式等</li>
<li>继承自libcore层的dialog类，定义一个应用的dialog所具备的样式，并且完善dialog的功能</li>
<li>PermanentCacheDB类，用来存储一些和应用生命周期相关的变量，永久保存，写入数据库</li>
<li>基本实用的控件：
<ul>
<li>AutomaticNewlineLinearLayout类是自动换行的linearLayout，自定义了attr</li>
<li>LoadingDialog类，用来定义一个应用最基本的加载dialog框，加载时有“...”跳跃动画</li>
</ul>
</li>
</ol>

###第三层sample层###
该层为模拟应用层，该层用来测试下层的代码
