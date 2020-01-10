import 'package:flutter/material.dart';

import 'animated_opacity_demo.dart';
import 'hero_demo.dart';


class AnimDemo extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('动画案例'),
      ),
      body: Center(
        child: Column(
          //主轴居中显示
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            RaisedButton(
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) {
                  return AnimatedOpacityDemo();
                }));
              },
              child: Text('AnimatedOpacity渐变'),
            ),
            RaisedButton(
              onPressed: () {
                Navigator.push(context,
                    MaterialPageRoute(builder: (context) => HeroDemo()));
              },
              child: Text('Hero页面切换效果'),
            )
          ],
        ),
      ),
    );
  }

}