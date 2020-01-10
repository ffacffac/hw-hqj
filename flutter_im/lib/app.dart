import 'package:flutter/material.dart';

class App extends StatefulWidget {

  @override
  AppState createState() {
    return AppState();
  }
}

class AppState extends State<App> {

  //当前选中页面索引
  var _currentIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Text('主页'),
    );
  }
}