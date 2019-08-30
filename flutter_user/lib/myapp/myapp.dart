import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

//void main() => runApp(MyApp1());
void main() => runApp(AppListView());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "myApp",
      home: Scaffold(
        appBar: AppBar(
          title: new Text('myAppHome'),
        ),
        body: Center(
          //Text
          child: Text(
            "Hello World",
            textAlign: TextAlign.center, //居中对齐
            maxLines: 3, //最多显示三行
            overflow: TextOverflow.ellipsis, //ellipsis：超出行数显示省略号
            style: TextStyle(
              fontSize: 25.0, //数字必须是浮点型
              color: Color.fromARGB(255, 255, 150, 150), //字体颜色
              decoration: TextDecoration.underline, //下划线
              decorationStyle: TextDecorationStyle.solid, //实线
            ),
          ),
        ),
      ),
    );
  }
}

///Container容器组件
class MyApp1 extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: "Container 组件",
      home: new Scaffold(
        //头部标题
        appBar: new AppBar(
          title: new Text('Container 组件'),
        ),
        //内容
        body: new Center(
          //Container容器，可设置对齐方式
          child: new Container(
            child: new Text(
              'Holle World',
              style: TextStyle(
                  fontSize: 40.0
              ),
            ),
            //底部居中对齐
            alignment: Alignment.center,
            width: 500,
            height: 400,
            //设置宽高
//            padding: const EdgeInsets.all(20.0),//所有内边距设置20.0
            //设置上下左右内边距
            padding: const EdgeInsets.fromLTRB(
                10.0, 10.0, 10.0, 10.0),
            //外边距
            margin: const EdgeInsets.fromLTRB(10.0, 10.0, 10.0, 10.0),
            //设置背景色
//            color: Colors.lightBlue,
            //实现渐变效果，则color属性要去掉
            decoration: new BoxDecoration(
              gradient: const LinearGradient(
                  colors: [Colors.lightBlue, Colors.greenAccent, Colors.purple
                  ]),
              //边框
              border: Border.all(
                  color: Colors.green,
                  width: 2.0),
              //边框的弧度
              borderRadius: BorderRadius.all(const Radius.circular(6.0)),
            ),
          ),
        ),
        floatingActionButton: new FloatingActionButton(
            onPressed: null, tooltip: 'Increment', child: Icon(Icons.add)),
      ),
    );
  }
}

class MyAppImage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: '图片显示',
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('图片显示'),
        ),
        body: new Center(
          child: new Container(
            child: new Image.network(
              'http://pic8.nipic.com/20100713/1954049_091647155567_2.jpg',
              //图片缩放
              scale: 3.0,
              //BoxFit.fill：充满整个容器，500*300
              //BoxFit.contain：以容器为基础，最大显示图片
              //BoxFit.cover：图片充满容器，但是不会被拉伸，可能会被裁剪
              //BoxFit.fitWidth：横向充满，高度可能会被裁剪掉
              // BoxFit.fitHeight：高度被充满，横向可能被裁剪掉
              fit: BoxFit.fitHeight,
//              //给图片设置颜色
//              color: Colors.greenAccent,
//              //设置模式
//              colorBlendMode: BlendMode.darken,
              //图片重复
//              repeat: ImageRepeat.repeat,
            ),
            width: 500,
            height: 300,
            color: Colors.greenAccent,
          ),
        ),
      ),
    );
  }
}

///ListView 组件
class AppListView extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'ListView 组件',
        home: new Scaffold(
            appBar: new AppBar(
              title: new Text('ListView 组件'),
            ),
            body: new Center(
              child: new Container(
//                  height: 200.0,
                  child: new MyListView()
              ),
            ))
    );
  }

  List<Widget> getList() {
    List<Widget> list = [
      new Image.network(
          "http://pic8.nipic.com/20100713/1954049_091647155567_2.jpg",
          fit: BoxFit.contain),
      new Image.network(
          "http://attach.bbs.miui.com/forum/201312/03/165620x7cknad7vruvec1z.jpg",
          fit: BoxFit.contain),
      new Image.network("http://p0.so.qhmsg.com/t0119f17b7e47c98a39.jpg",
          fit: BoxFit.contain),
      new Image.network(
          "https://ps.ssl.qhmsg.com/sdr/400__/t01ad947215fca99c97.jpg",
          fit: BoxFit.contain),
      new Image.asset('/images/house.png', fit: BoxFit.contain,),
    ];
    return list;
  }

  List<Widget> getListTile() {
    List<Widget> list = [
      new ListTile(
          leading: new Icon(Icons.android),
          title: new Text('Icons.android')
      ),
      new ListTile(
          leading: new Icon(Icons.email),
          title: new Text('Icons.email')
      ),
      new ListTile(
          leading: new Icon(Icons.library_music),
          title: new Text('Icons.library_music')
      ),
    ];
    return list;
  }
}

///抽取代码，自定义ListView
class MyListView extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return new ListView(
      //垂直/横向滑动
//      scrollDirection: Axis.horizontal,
      //                  children: getList(),
      //                  children: getListTile(),
      children: <Widget>[
        new Container(
          margin: EdgeInsets.only(top: 5.0),
          height: 120.0,
          color: Colors.greenAccent,
        ),
        new Container(
          height: 120.0,
          margin: EdgeInsets.only(top: 5.0),
          color: Colors.blue,
        ),
        new Container(
          height: 120.0,
          margin: EdgeInsets.only(top: 5.0),
          color: Colors.yellowAccent,
        ),
        new Container(
          height: 120.0,
//          width: 180.0,
          margin: EdgeInsets.only(top: 5.0),
          color: Colors.cyanAccent,
        ),
        new Container(
          height: 120.0,
          margin: EdgeInsets.only(top: 5.0),
//          width: 180.0,
          color: Colors.cyanAccent,
          child: new MyButton(),
          alignment: Alignment.centerLeft,
        ),
        new Container(
          height: 120.0,
          margin: EdgeInsets.only(top: 5.0),
//          width: 180.0,
          color: Colors.cyanAccent,
          child: new Container(
            child: IconButton(
                iconSize: 80.0,
                tooltip: '按下操作',
                icon: Image.asset(
                    'assets/images/food06.jpeg'),
                onPressed: () {
                  print('按下操作');
                  Fluttertoast.showToast(
                      msg: "点击了此按钮",
                      toastLength: Toast.LENGTH_SHORT,
                      gravity: ToastGravity.CENTER);
                }),
            alignment: Alignment.centerLeft,
          ),
        ),
        new Container(
          height: 120.0,
          color: Colors.green,
          margin: EdgeInsets.only(top: 5.0),
          child: new ListTile(
            leading: Image.asset(
              'assets/images/house.png',
              width: 80.0,
              fit: BoxFit.contain,),
            title: Text('aaaaaaa'),
          ),
          alignment: Alignment.centerLeft,
        ),
      ],
    );
  }
}

///手势
class MyButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTapDown: showToast('手势按下'),
      onTapUp: showToast('手势抬起'),
      onDoubleTap: showToast('双击'),
      child: Text('手势案例', textAlign: TextAlign.center, style: TextStyle(
          color: Colors.blueAccent, fontSize: 24.0
      ),),);
  }
}

showToast(String msg) {
  Fluttertoast.showToast(
      msg: msg, toastLength: Toast.LENGTH_SHORT, gravity: ToastGravity.CENTER);
}