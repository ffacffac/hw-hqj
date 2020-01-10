import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';

final String url = "http://www.baidu.com";

class HttpDemo extends StatelessWidget {

  String res = '';

  @override
  Widget build(BuildContext context) {
    var height = MediaQuery
        .of(context)
        .size
        .height;
    var width = MediaQuery
        .of(context)
        .size
        .width;
    print("height=$height，width=$width");
    return Scaffold(
      appBar: AppBar(
        title: Text('Http案例'),
      ),
      body: Container(
        //整体宽高设置成屏幕宽高（铺满）
        height: height,
        width: width,
        margin: EdgeInsets.all(5.0),
        decoration: BoxDecoration(
          //圆角
            borderRadius: BorderRadius.circular(8.0),
            //设置阴影
            boxShadow: [
            ],
            //背景图片
            image: DecorationImage(
              image: ExactAssetImage("assets/images/bg_home.png"),
              fit: BoxFit.cover,
            )
        ),
        child: Column(
          children: <Widget>[
            RaisedButton(onPressed: () {
              _getHttp();
            },
              child: Text('get请求'),
            ),
            Container(
              child: Text(res),
            )
          ],
        ),
      ),
    );
  }

  _getHttp() async {
    var httpClient = HttpClient();
    try {
      var request = await httpClient.getUrl(Uri.parse(url));
      var response = await request.close();
      if (response.statusCode == HttpStatus.ok) {
        var httpHeaders = response.headers;
        print("httpHeaders：$httpHeaders");
//        var json = await response.transform(utf8.decoder).join();
//        var data = jsonDecode(json);
//        print("json：$json");
//        print("data：$data");
      }
    } catch (e) {
      print("请求异常$e");
    }
  }
}