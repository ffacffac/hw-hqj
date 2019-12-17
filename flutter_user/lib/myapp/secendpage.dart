import 'package:flutter/material.dart';

class SecondPage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(title: Text('Second Page')),
      body: new Center(
        child: RaisedButton(onPressed: () {
          //跳转到第一个节目
//          Navigator.pushNamed(context, '/first');
          //界面返回
          Navigator.pop(context, 'SecondPage返回数据');
        },
            child: Text('这是第二个界面', style: TextStyle(fontSize: 28.0))),
      ),
    );
  }
}