import 'package:flutter/material.dart';

void main() =>
    runApp(new MyApp(
      //出入数据
        items: new List<String>.generate(1000, (index) => "item--$index")
    ));


///实现动态列表
class MyApp extends StatelessWidget {

  final List<String> items;

  /// @required items 参数必须传值
  /// 记得调用super方法
  MyApp({Key key, @required this.items}) :super(key: key);

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: '动态列表ListView',
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('动态列表ListView'),
        ),
        body: new Center(
          child: new ListView.builder(
              itemCount: items.length,
              //item显示的样子
              itemBuilder: (context, index) {
                return new ListTile(
                  title: new Text(items[index]),
                  leading: new Icon(Icons.android),
                );
              }
          ),
        ),
      ),
    );
  }
}