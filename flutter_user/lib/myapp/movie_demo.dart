import 'package:flutter/material.dart';

void main() => runApp(new MyApp());

///电影海报实例
class MyApp extends StatelessWidget {
  final double heightImg = 120.0;

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: '电影海报demo',
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('电影海报demo'),
        ),
        body:
//            GridView.count(
//              //列数
//              crossAxisCount: 3,
//              padding: EdgeInsets.all(10.0),
//              //每个item直接的间距
//              crossAxisSpacing: 10.0,
//              children: <Widget>[
//                const Text('电影海报demo'),
//                const Text('电影海报demo'),
//                const Text('电影海报demo'),
//                const Text('电影海报demo'),
//                const Text('电影海报demo'),
//                const Text('电影海报demo'),
//              ],
//            )
        GridView(
          gridDelegate:
          SliverGridDelegateWithFixedCrossAxisCount(
            //列数
            crossAxisCount: 3,
            //左右间距
            crossAxisSpacing: 3.0,
            //上下间距
            mainAxisSpacing: 3.0,
            //设置宽高比例
            childAspectRatio: 0.8,
          ),
          children: <Widget>[
            new Image.network(
              'https://ps.ssl.qhmsg.com/sdr/400__/t01ad947215fca99c97.jpg',
              fit: BoxFit.cover,),
            new Image.network(
              'https://ps.ssl.qhmsg.com/sdr/400__/t01ad947215fca99c97.jpg',
              fit: BoxFit.cover,),
            new Image.network(
              'https://ps.ssl.qhmsg.com/sdr/400__/t01ad947215fca99c97.jpg',
              fit: BoxFit.cover,),
            new Image.network(
              'https://ps.ssl.qhmsg.com/sdr/400__/t01ad947215fca99c97.jpg',
              fit: BoxFit.cover,),
            new Image.network(
              'https://ps.ssl.qhmsg.com/sdr/400__/t01ad947215fca99c97.jpg',
              fit: BoxFit.cover,),
          ],
//          children: getGridList(),
        )
        ,
      )
      ,
    );
  }

  List<Widget> getGridList() {
    List<Widget> list = new List();
    ColorSwatch col = Colors.green;
    for (int i = 0; i < 8; i++) {
      var container = new Container(
//        child: new Text('123', style: new TextStyle(
//            fontSize: 24,
//            fontStyle: FontStyle.italic
//        ),
        child: new Image.network(
          'https://ps.ssl.qhmsg.com/sdr/400__/t01ad947215fca99c97.jpg',
          fit: BoxFit.cover,
        ),
        color: col,
      );
      list.add(container);
    }
    return
      list;
  }

  ///造数据
  List<String> getDataList() {
    List<String> list = [];
    //创建list方式一
    for (var i = 0; i < 20; i++) {
      list.add(i.toString());
    }
    //创建list方式二
//    return new List<String>.generate(20, (i) {
//      return i.toString();
//    });
    //创建list方式三
//    return new List<String>.generate(20, (i)=>i.toString());
    return list;
  }

  List<Widget> getWidgetList() {
    return getDataList().map((item) => getItemContainer(item)).toList();
  }

  Widget getItemContainer(String item) {
    return Container(
      alignment: Alignment.center,
      child: Text(item, style: TextStyle(color: Colors.white, fontSize: 20.0),),
      color: Colors.green,
    );
  }
}