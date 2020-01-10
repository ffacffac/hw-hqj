import 'package:flutter/material.dart';

class HeroDemo extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Hero页面切换过渡动画'),
      ),
      body: GestureDetector(
        child: Hero(
            tag: '第一张图片',
            child: Image.network(
                "http://pic8.nipic.com/20100713/1954049_091647155567_2.jpg"
            )
        ),
        onTap: () {
          Navigator.push(context, MaterialPageRoute(builder: (context) {
            return SecondPage();
          }));
        },
      ),
    );
  }
}


class SecondPage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Hero页面二'),
      ),
      body: GestureDetector(
        child: Hero(
            tag: '第一张图片',
            child: Image.network(
                "https://ps.ssl.qhmsg.com/sdr/400__/t01ad947215fca99c97.jpg"
            )
        ),
        onTap: () {
          Navigator.pop(context);
        },
      ),
    );
  }
}

