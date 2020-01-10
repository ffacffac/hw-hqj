import 'package:flutter/material.dart';
import 'dart:async';

class LoadingPage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return _LoadingPage();
  }
}

class _LoadingPage extends StatefulWidget {

  @override
  LoadingPageState createState() {
    return LoadingPageState();
  }
}

class LoadingPageState extends State<_LoadingPage> {

  @override
  void initState() {
    super.initState();
    //在加载界面停留*秒
    Future.delayed(Duration(seconds: 3), () {
      print('延迟跳转界面');
      Navigator.of(context).pushReplacementNamed("app");
    });
  }

  @override
  Widget build(BuildContext context) {
    var width = MediaQuery
        .of(context)
        .size
        .width;
    var height = MediaQuery
        .of(context)
        .size
        .height;
    return Scaffold(
      body: Center(
        child: //加载界面的背景图
        Image.asset("assets/images/bg_start.jpg", fit: BoxFit.cover,
          width: width,
          height: height,
        ),
//        child: Stack(
//          children: <Widget>[
//            //加载界面的背景图
//            Image.asset("assets/images/bg_start.jpg", fit: BoxFit.cover,
//              width: 1000.0,
//              height: 1000.0,
//            ),
//          ],
//        ),
      ),
    );
  }
}