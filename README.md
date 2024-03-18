# 仿叮咚买菜首页
仿叮咚买菜首页，主要功能点有：<br>1.双层吸顶悬浮；<br>2.搜索框滑动效果；<br>3.banner与头图背景融合对齐；<br>4.分类列表的自定义滑动指示器；<br>5.实现了抽屉式滚动效果的TabLayout控件。

上次发布《仿京东首页双层吸顶效果demo》后，现在看到叮咚买菜安卓端滑首页动事件不穿透的问题他们自己也解决了，后来偶然发现叮咚买菜IOS版首页的分类tab有个滑动效果，个人感觉用户体验挺好的，所以就计划着空闲时候来实现一下。

一个比较自然、柔和的TabLayout控件，纵向滑动时实现了抽屉式的滚动效果。

下面是实现后的效果图：<br>
![image](https://github.com/weioule/ScrollTabDemo/blob/main/app/img/gif_img.gif)&nbsp;&nbsp;

这里的核心是ScrollTabView，它是一个自定义控件，它在实现了TabLayout功能基础上添加了滑动效果处理，在我的当前首页架构上，要结合外层的AppBarLayout和内层RecyclerView的滚动调用它的notifyDataSetChanged函数实现。

tab的上面部分我直接放了一张叮咚买菜首页的截图，只是头部做了一下吸顶处理，还有底部商品列表的点击事件也都没去实现，因为这些内容都是辅助的，所以就没详细写。

主要思路是，当内层RecyclerView在滑动时，记录距离顶部的距离，在临界点上下滑动相应的内范围（根据实际高度做调整）进行透明度与高度的改动，实现tab下方的副标题与横线的切换效果。当外层的AppBarLayout下拉展开时将TabLayout的副标题也恢复展开状态。

<br>
2023/07/16.<br>
1 新增搜索框滑动效果<br>
2 新增banner并与头图对齐<br>
3 新增分类列表与自定义滑动指示器<br><br>

叮咚买菜的首页头部搜索框的滚动效果挺不错的，还有轮播图与背景图片的融合效果，以及轮播图下方的分类列表与它的滚动指示器的交互效果，这些UI设计当前应该也算是比较流行的，本次新增实现如下图效果，项目代码里有注释。

下面是效果动图：
<br>
![image](https://github.com/weioule/ScrollTabDemo/blob/main/app/img/sample_img.gif)


