import 'package:flutter/widgets.dart';
import 'package:flutter/material.dart';

class GridViewDemo extends StatelessWidget {

  final List<String> imageList = [
    "uploadImage15.jpg",
    "uploadImage83.jpg",
    "uploadImage84.jpg",
    "uploadImage85.jpg",
    "uploadImage86.jpg",
    "uploadImage91.jpg",
    "uploadImage93.jpg",
    "uploadImage85.jpg",
    "food06.jpeg"
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('GridView示例'),
      ),
      body: Center(
//        child:buildGrid(),
        child:buildGrid1(),
      ),
    );
  }

  List<Container> _buildGridTitleList(int count) {
    return List<Container>.generate(count,(int index){
    return Container(
    padding: EdgeInsets.all(0.0),
    //用FittedBox控制图片视图的缩放
    child:FittedBox(
    fit: BoxFit.fitHeight,
    child: Image.asset("assets/images/${imageList[index]}"),
    ),
    );
    });
  }

  Widget buildGrid() {
    return GridView.extent(
      //次轴的宽度(y轴方向，也就是item的宽度)
      maxCrossAxisExtent: 120.0,
      semanticChildCount: 2,
      //上下左右内边距
      padding: const EdgeInsets.all(3.0),
      //主轴间距（横向（x轴）间距）
      mainAxisSpacing: 4.0,
      //次轴间距(垂直方向（y轴）间距)
      crossAxisSpacing: 5.0,
      children: _buildGridTitleList(9),
    );
  }

  Widget buildGrid1() {
    return GridView.builder(
        itemCount: imageList.length,
        padding: EdgeInsets.all(4.0),
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 3,
            mainAxisSpacing: 4.0,
            crossAxisSpacing: 5.0,
        ),
        itemBuilder: (BuildContext context, int index) =>
            FittedBox(
              fit: BoxFit.cover,
              child: Image.asset("assets/images/${imageList[index]}"),
            )
    );
  }
}
