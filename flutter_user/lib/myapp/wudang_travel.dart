import 'package:flutter/material.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:fluttertoast/fluttertoast.dart';


final bannerImages = [
  "wudang.jpg",
  "uploadImage84.jpg",
  "uploadImage85.jpg",
  "uploadImage86.jpg",
  "uploadImage91.jpg",
  "uploadImage93.jpg"
];

final wudangDesc = '''
      武当山，中国道教圣地，又名大和山、谢罗山、参上山、仙室山 ， 古有“大岳”“玄岳”“大岳”之称。位于湖北西北部十堪市丹江口市境内 。 东接闻名古城襄阳市，西靠车城十堪市，南望原始森林神
农架，北临高峡平湖丹江口水库 。明代，武当山被皇帝封为“大岳飞“治世玄岳”，被尊为“皇室家庙 ” 。 武当山以“四大名山皆拱棒，
五方仙岳共朝宗”的“五岳之冠 ” 地位问名于世。1994 年 12 月 ， 武当山古建筑群入选《世界遗产名录》， 2006 年被整体列为 “全国重点文物保
护单位” 。 2007 年 ， 武当山和长城、丽江、周庄等景区一起入选“欧洲人最喜爱的中国十大景区 ” 。
2010 至 2013 年，武当山分别被评为国家 SA 级旅游区、国家森林公园、中国十大避暑名山、海峡两
岸交流基地 ， 入选最美“国家地质公园 ” 。截至 2013 年 ， 武当山有古建筑 53 处，建筑面积 2.7 万平方米 ， 建筑遗址 9 处 ， 占地面积 20 多万
平方米，全山保存各类文物 5035 件。武当山是道教名山和武当武术的发源地 ， 被称为“亘古无双胜境 ， 天下第一仙山 ” 。 武当武术，是中
华武术的重要流派 。 元未明初，道士张三丰集其大成 ， 开创武当派。
    ''';

final wudangDesc1 = '''   武当山，中国道教圣地，又名大和山、谢罗山、参上山、仙室山 ， 古有“大岳”“玄岳”“大岳”之称。位于湖北西北部十堪市丹江口市境内 。 东接闻名古城襄阳市，西靠车城十堪市，南望原始森林神农架，北临高峡平湖丹江口水库 。明代，武当山被皇帝封为“大岳飞“治世玄岳”，被尊为“皇室家庙 ” 。 武当山以“四大名山皆拱棒，五方仙岳共朝宗”的“五岳之冠 ” 地位问名于世。1994 年 12 月 ， 武当山古建筑群入选《世界遗产名录》， 2006 年被整体列为 “全国重点文物保护单位” 。 2007 年 ， 武当山和长城、丽江、周庄等景区一起入选“欧洲人最喜爱的中国十大景区 ” 。2010 至 2013 年，武当山分别被评为国家 SA 级旅游区、国家森林公园、中国十大避暑名山、海峡两岸交流基地 ， 入选最美“国家地质公园 ” 。截至 2013 年 ， 武当山有古建筑 53 处，建筑面积 2.7 万平方米 ， 建筑遗址 9 处 ， 占地面积 20 多万平方米，全山保存各类文物 5035 件。武当山是道教名山和武当武术的发源地 ， 被称为“亘古无双胜境 ， 天下第一仙山 ” 。 武当武术，是中华武术的重要流派 。 元未明初，道士张三丰集其大成 ， 开创武当派。''';

class WuDangMain extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    //地址部分
    Widget addressContainer = Container(
      padding: EdgeInsets.only(top: 4.0, left: 4.0),
      child: Row(
        children: <Widget>[
          //剩下的空间会被Expanded组件占满
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Container(
                  padding: EdgeInsets.only(bottom: 8.0, left: 4.0),
                  child: Text('风景区地址', style: TextStyle(
                      fontSize: 18.0, fontWeight: FontWeight.bold)),
                ),
                Container(
                  padding: EdgeInsets.only(left: 4.0),
                  child: Text('湖北省十堰市丹江口市'),
                ),
              ],
            ),
          ),
          Container(
            margin: EdgeInsets.only(right: 2.0),
            child: Icon(Icons.star, color: Colors.redAccent),
          ),
          Container(
            margin: EdgeInsets.only(right: 4.0),
            child: Text('66'),
          ),
        ],
      ),
    );

    ///单个按钮组件
    Column buildButtonColumn(String title, IconData icon) {
      return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Icon(icon, color: Colors.lightGreen[600]),
          Container(
            margin: EdgeInsets.only(top: 2.0),
            child: Text(title),
          )
        ],
      );
    }

    //中间按钮部分
    Widget buttonsContainer = Container(
      padding: EdgeInsets.all(0.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly, //水平方向均匀排列每个元素
        children: <Widget>[
          buildButtonColumn('电话', Icons.phone),
          buildButtonColumn('导航', Icons.near_me),
          buildButtonColumn('分享', Icons.share),
        ],
      ),
    );

    //文本介绍部分
    Widget textContainer = Container(
      padding: const EdgeInsets.only(left: 4.0, right: 4.0),
      child: Text(wudangDesc),
    );

    return Scaffold(
      appBar: AppBar(title: Text('武当山风景区')),
      body: ListView(
        children: <Widget>[
//          Image.asset(
//              'assets/images/wudang.jpg', height: 180.0, fit: BoxFit.cover),
          //轮播banner
          getBannerImages(context),
          addressContainer,
          Divider(color: Colors.grey),
          buttonsContainer,
          Divider(color: Colors.grey),
          textContainer
        ],
      )
      ,
    );
  }
}

///轮播banner
Widget getBannerImages(BuildContext context) {
  return Column(
    children: <Widget>[
      Container(
//        width: MediaQuery
//            .of(context)
//            .size
//            .width,
        margin: const EdgeInsets.only(bottom: 4.0),
        height: 180.0,
        child: Swiper(
          itemCount: bannerImages.length,
          itemBuilder: (context, index) => _swiperBuilder(context, index),
          pagination: SwiperPagination(
              builder: DotSwiperPaginationBuilder(
                  color: Colors.black38,
                  activeColor: Colors.blueAccent
              )
          ),
          control: null,
          duration: 300,
          scrollDirection: Axis.horizontal,
          autoplay: true,
          onTap: (index) {
            Fluttertoast.showToast(msg: '点击了第${index + 1}');
          },
        ),
      )
    ],
  );
}

Widget _swiperBuilder(BuildContext context, int index) {
  return Image.asset(
    "assets/images/${bannerImages[index]}", fit: BoxFit.cover,);
}