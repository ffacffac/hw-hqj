import 'package:flutter/material.dart';

class AnimatedOpacityDemo extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return AnimatedOpacityPage();
  }
}

class AnimatedOpacityPage extends StatefulWidget {
  @override
  AnimatedOpacityState createState() {
    return AnimatedOpacityState();
  }
}

class AnimatedOpacityState extends State<AnimatedOpacityPage> {

  //控制显示隐藏
  bool _visible = true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('AnimatedOpacity渐变'),
      ),
      body: Center(
        child: AnimatedOpacity(
          //控制opacity值，范围从0.0到1.0
          //0.0~1.0淡入，1.0~0.0淡出
          opacity: _visible ? 1.0 : 0.0,
          //设置动画时长
          duration: Duration(
              milliseconds: 1000
          ),
          child: Container(
            width: 300.0,
            height: 300.0,
            color: Colors.green,
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          setState(() {
            _visible = !_visible;
          });
        },
        tooltip: '隐藏显示',
        child: Icon(Icons.flip),
      ),
    );
  }
}