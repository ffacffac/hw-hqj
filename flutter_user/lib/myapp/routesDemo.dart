import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_user/myapp/secendpage.dart';
import 'package:fluttertoast/fluttertoast.dart';

///路由、跳转
//void main() => runApp(new RoutesDemo());

class RoutesDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: '路由跳转实例',
      //自定义主题，颜色
      theme: new ThemeData(primaryColor: Colors.green),
//      home: new MyHomePage(),
      home: new FirstPage(),
      //初始化路由跳转配置
      routes: {
        '/first': (context) => FirstPage(),
        '/second': (context) => SecondPage()
      },
//      initialRoute: '/home', //初始化第一个页面
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() {
    return new _MyHomePageState();
  }
}

class _MyHomePageState extends State<MyHomePage> {

  final List<String> tab = ['地图', '搜索', '新增', '语言'];
  final List<Tab> myTabs = <Tab>[
    Tab(text: '热点'), Tab(text: '科技'), Tab(text: '体育'), Tab(text: '娱乐')
  ];

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      backgroundColor: Colors.white,
      appBar: new AppBar(
        title: new Text('路由跳转示例'),
        //标题栏右边增加两个按钮
        actions: <Widget>[
          IconButton(icon: Icon(Icons.search), tooltip: '搜索', onPressed: () {
            Fluttertoast.showToast(msg: '搜索', gravity: ToastGravity.CENTER);
          }),
          IconButton(icon: Icon(Icons.map), tooltip: '地图', onPressed: () {
            Fluttertoast.showToast(msg: '地图');
          })
        ],
      ),
      body: Center(
          child: Column(
            children: <Widget>[
              TabBarView(
                  children: myTabs.map((Tab tab) {
                    return Center(
                        child: Container(
                            child: Text(tab.text,
                                style: TextStyle(color: Colors.blueAccent))
                        ));
                  }).toList()
              ),
            ],
          )
      ),
      //底部导航栏
      bottomNavigationBar: new BottomNavigationBar(
        fixedColor: Colors.yellow,
        iconSize: 30.0,
        items: [
          BottomNavigationBarItem(icon: Icon(Icons.map),
              title: Text(tab[0]), backgroundColor: Colors.green),
          BottomNavigationBarItem(icon: Icon(Icons.search),
              title: Text(tab[1]), backgroundColor: Colors.green),
          BottomNavigationBarItem(icon: Icon(Icons.add),
              title: Text(tab[2]), backgroundColor: Colors.green),
          BottomNavigationBarItem(icon: Icon(Icons.language),
              title: Text(tab[3]), backgroundColor: Colors.green)
        ],
        onTap: (position) {
          _onItemClick(position);
        },
      ),
//      floatingActionButton: FloatingActionButton(
//          onPressed: () {}, tooltip: '增加',
//          child: Icon(Icons.add)),
//      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
    );
  }

  int _selectedPosition = 0;

  void _onItemClick(int position) {
    setState(() {
      _selectedPosition = position;
      Fluttertoast.showToast(msg: '选中了：' + tab[_selectedPosition]);
    });
  }
}

class FirstPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('first页面'),
        centerTitle: true, //文字居中
        actions: <Widget>[
          Icon(Icons.search),
          Icon(Icons.add),
        ],
      ),
      body: Center(
        //onPressed 传参为匿名参数
        child: RaisedButton(
            color: Colors.green,
            highlightColor: Color.fromARGB(0, 7, 139, 17),
//            highlightColor: Colors.red,
            colorBrightness: Brightness.light,
            onPressed: () {
//          Navigator.pushNamed(context, '/second');
              //第二种方式
              _toSecondPage(context);
            },
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8.0)),
            child: Text('这是第一页（first）',
                style: TextStyle(color: Colors.white, fontSize: 28.0))),
      ),
    );
  }

  _toSecondPage(BuildContext context) async {
    final result = await Navigator.push(
        context, MaterialPageRoute(builder: (context) => SecondPage()));
    print("_toSecondPage--------$result");
    Fluttertoast.showToast(msg: '$result');
  }
}