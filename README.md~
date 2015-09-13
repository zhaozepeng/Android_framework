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
<li>Application封装，使用了weakRefrence指向当前Activity的context，方便使用，还有增加了应用crash处理，应用关闭等处理</li>
<li>最基础的Activity和Fragment类，配合Application类和ActivityManager类进行Activity的集中管理</li>
<li>ActivityManager类，使用栈来管理所有的activity</li>
<li>log类，用来打印log</li>
<li>Toast类，该类用来弹出toast，支持弹出toast的位置</li>
<li>GuideManager类，用来显示指引蒙版，支持全屏展示和只在内容区域展示</li>
<li>dialog类，定义了一个应用dialog所应该具备的基础行为</li>
<li>database类，将数据库类进行了非常便捷的封装，创建数据库应该继承自BaseDB类，为了该数据库的访问应该再创建一个helper类继承自BaseDBHelper，封装该数据库的所有操作，另外还需要对表名和列名进行了枚举的封装，这样使用该数据库直接使用该枚举类获取表名和表的相关列名。对数据库的版本升级也做了相应快捷的处理</li>
<li>CacheManager类，这个类使用SharedPreference来存储基本对象，有临时和永久两种，临时存储将会在每次应用退出之后自动清空，永久存储则永久存储</li>
<li>FileDownloadManager类，用来下载相关文件，为多线程断点续传式下载，支持开始，停止和删除操作</li>
<li>Utils类：
<ul>
<li>CommonUtils，用来集中管理一些杂项函数，比如dp2px等</li>
<li>FileUtils，用来管理文件的相关操作</li>
<li>ImageUtils，用来处理图片的相关操作</li>
</ul>
</li>
</ol>

###第二层libcore-ui层###
该层为基础核心扩展层，扩展libcore的层的代码，并且定义应用的基本样式，这样一个公司的不同应用就能够统一样式，方便管理

<ol>
<li>扩展实现的Activity和Fragment类，Activity类中定义了整个应用的基本简单样式（现在提供两种样式，顶部透明样式和底部透明样式），底部的弹出框，顶部bar的样式(顶部bar有自定义bar和系统控件toolbar)等；Fragment类定义了fragment和activity之间的通信方式和topbar的交互，</li>
<li>继承自libcore层的dialog类，完善dialog的功能，并且定义一个工厂类用来后去需要显示的dialog样式</li>
<li>PermanentCacheDB类，用来存储一些和应用生命周期相关的变量，写入数据库，永久保存</li>
<li>WebFragment类，该fragment用来显示网页，可以单独作为一个fragment嵌入一个页面的任何地方</li>
<li>WebActivity类，该activity用来展示网页，传入url即可显示网页，有进度条和刷新操作</li>
<li>基本实用的控件：
<ul>
<li>AutomaticNewlineLinearLayout类是自动换行的linearLayout，自定义了attr</li>
<li>LoadingDialog类，用来定义一个应用最基本的加载dialog框，加载时有“...”跳跃动画</li>
</ul>
</li>
</ol>

###第三层sample层###
该层为模拟应用层，用来测试下层的代码。一个应用在使用了libcore层和libcore-ui层之后，可能还需要在封装一层或几层module，但是保证最基础的两层是应用所通用的module即可
