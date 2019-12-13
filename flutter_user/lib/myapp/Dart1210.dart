import 'package:flutter/material.dart';

//void main() => runApp(MyApp());

class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: "容器组件示例",
      home: Scaffold(
        appBar: AppBar(
          title: Text("容器组件示例"),
        ),
        body: Center(
          child: Container(
            width: 200.0,
            height: 200.0,
            alignment: Alignment.center,
            decoration: BoxDecoration(
                color: Colors.white,
                border: new Border.all(
                    color: Colors.grey, width: 8.0
                ),
                borderRadius: const BorderRadius.all(
                    const Radius.circular(8.0))),
            //边框的弧度
            child: Text(
              'Flutter',
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 28.0),
            ),
          ),
        ),
      ),
    );
  }
}