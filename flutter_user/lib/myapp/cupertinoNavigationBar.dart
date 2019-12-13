import 'package:flutter/widgets.dart';
import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

class CupNavigationBar extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return MyPage();
  }
}

class MyPage extends StatefulWidget {

  @override
  _MyPageState createState() {
    return _MyPageState();
  }
}

class _MyPageState extends State<MyPage> {

  @override
  Widget build(BuildContext context) {
    return CupertinoTabScaffold(
        tabBar: CupertinoTabBar(
          //选项卡背景色
            backgroundColor: CupertinoColors.lightBackgroundGray,
            items: <BottomNavigationBarItem>[
              BottomNavigationBarItem(
                  icon: Icon(CupertinoIcons.home), title: Text('主页')),
              BottomNavigationBarItem(
                  icon: Icon(CupertinoIcons.conversation_bubble),
                  title: Text('聊天'))
            ]),
        tabBuilder: (context, index) {
          //选项卡绑定的视图
          return CupertinoTabView(
            builder: (context) {
              switch (index) {
                case 0:
                  return HomePage();
                case 1:
                  return ChatPage();
                default:
                  return Container();
              }
            },
          );
        }
    );
  }
}

class HomePage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      //基本布局结构，包含内容和导航栏
        navigationBar: CupertinoNavigationBar(
          //导航栏，只包含中部标题部分
          middle: Text('主页'),
        ),
        child: Center(
          child: Text('主页', style: TextStyle(fontSize: 30.0),),
        )
    );
  }
}

class ChatPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
        navigationBar: CupertinoNavigationBar(
          backgroundColor: CupertinoColors.activeOrange,
          middle: Text('聊天'),
          //右边图标
          trailing: Icon(CupertinoIcons.add),
          //左边图标
          leading: Icon(CupertinoIcons.back),
        ),
        child: Center(
          child: Text('聊天', style: Theme
              .of(context)
              .textTheme
              .button,),
        ));
  }

}