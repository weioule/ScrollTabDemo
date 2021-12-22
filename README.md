# ScrollTabDemo
一个比较自然、柔和的TabLayout控件，纵向滑动时实现了抽屉式的滚动效果。

上次发布《仿京东首页双层吸顶效果demo》后，现在看到叮咚买菜安卓端滑首页动事件不穿透的问题他们自己也解决了，后来偶然发现叮咚买菜IOS版首页的分类tab有个滑动效果，个人感觉用户体验挺好，所以就计划着空闲时候来实现一下。

下面是实现后的效果图：</br>
![image](https://github.com/weioule/ScrollTabDemo/blob/main/app/img/gif_img.gif)&nbsp;&nbsp;


这里的核心是ScrollTabView，它是一个自定义控件，它在实现了TabLayout功能基础上添加了滑动效果处理，在我的当前首页架构上，要结合外层的AppBarLayout和内层RecyclerView的滚动调用它的notifyDataSetChanged函数实现。

tab的上面部分我直接放了一张叮咚买菜首页的截图，只是头部做了一下吸顶处理，还有底部商品列表的点击事件也都没去实现，因为这些内容都是辅助的，所以就没详细写。
  
主要思路是，当内层RecyclerView在滑动时，记录距离顶部的距离，在临界点上下滑动相应的内范围（根据实际高度做调整）进行透明度与高度的改动，实现tab下方的副标题与横线的切换效果。当外层的AppBarLayout下拉展开时将TabLayout的副标题也恢复展开状态。
