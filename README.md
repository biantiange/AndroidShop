# AndroidShop
# 项目说明
![](https://img.shields.io/badge/Api-23--26-green.svg)
![](https://img.shields.io/badge/License-Apache%202.0-orange.svg)

> 基于 https://github.com/a1098832322/MarketAndroidApp 开发，添加后台管理系统，未完善。添加安卓端社区功能，添加安卓端商城所需的很多细节，项目将继续持续完善。
>
> 希望可以给大家完成自己的商城App提供一个思路！

## 开发环境
* Android Studio
* gradle
* nginx
* idea

## 项目结构说明
* nginx ：后台管理前端
* shopback ：后端（包含app和后台管理）
* shop ：安卓app

## 功能说明
> 实现功能：安卓端
> * 账号注册、登录
> * 商品查询（按商品名，按商品类别等）
> * 点击查看商品详情，查看商品缩略图详情等
> * 选择商品规格 
> * 添加商品进购物车、查询购物车
> * 模拟购买商品、查询购买历史
> * 发布帖子
> * 删除帖子
> * 评论帖子
> * 点赞帖子

> 实现功能：后台管理
> * 用户管理
> * 商品管理
> * 订单管理
> * 购物车管理
> * 可视化统计（基于e-charts https://echarts.apache.org/zh/index.html ）


## 开发/编译/调试
1、确保开发环境没有问题  jdk mysql Android sdk 后端基于springboot swagger mybatis
2、拉取仓库代码 
3、建数据库，执行sql 
4、使用maven编译shopback
5、使用Android studio导入shop项目
