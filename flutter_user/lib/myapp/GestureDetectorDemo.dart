import 'package:flutter/material.dart';

///手势检测
class GestureDetectorDemo extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('手势检测demo'),
      ),
      body: Center(
        child: MyButton(),
      ),
    );
  }
}

class MyButton extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    //手势检测一定要包含在GestureDetector里面
    return GestureDetector(
      onTap: () {
        //底部消息提示
        final snackBar = SnackBar(
          content: Text('手势按下'), duration: Duration(milliseconds: 1000),);
        Scaffold.of(context).showSnackBar(snackBar);
      },
      child: Container(
        //模拟圆角按钮
        padding: EdgeInsets.all(10.0),
        decoration: BoxDecoration(
            color: Theme
                .of(context)
                .buttonColor,
            borderRadius: BorderRadius.circular(6.0)
        ),
        child: Text('测试按钮'),
      ),
    );
  }
}