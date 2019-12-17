import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

///基础布局案例
///
///
///
///

/*
 *对齐布局案例
 */
class AlignLayout extends StatelessWidget {

  final imgW = 140.0;
  final imgH = 140.0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('基础布局demo'),
      ),
      body: Stack(
        children: <Widget>[
          //左上角
          Align(
            alignment: FractionalOffset(0.0, 0.0),
            child: Image.asset(
              'assets/images/uploadImage15.jpg', width: imgW, height: imgH,),
          ),
          //右上角
          Align(
            alignment: FractionalOffset(1.0, 0.0),
            child: Image.asset(
              'assets/images/uploadImage83.jpg', width: imgW, height: imgH,),
          ),
          //水平垂直方向居中
          Align(
            alignment: FractionalOffset.center,
            child: Image.asset(
              'assets/images/uploadImage84.jpg', width: imgW, height: imgH,),
          ),
          //左下角
          Align(
            alignment: FractionalOffset.bottomLeft,
            child: Image.asset(
              'assets/images/uploadImage85.jpg', width: imgW, height: imgH,),
          ),
          //右下角
          Align(
            alignment: FractionalOffset.bottomRight,
            child: Image.asset(
              'assets/images/uploadImage86.jpg', width: imgW, height: imgH,),
          )
        ],
      ),
    );
  }
}

///垂直布局
class ColumnDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('垂直布局demo'),
      ),
      body: Column(
        //水平方向靠左对齐
        crossAxisAlignment: CrossAxisAlignment.start,
        //主轴方向最小化处理
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          Text('Flutter'),
          Text('垂直布局案例'),
          Expanded(
              child: FittedBox(
                fit: BoxFit.contain,
                child: const FlutterLogo(),
              ))
        ],
      ),
    );
  }
}

class StackDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Stack布局案例'),),
      body: Center(
        child: Stack(
          children: <Widget>[
            Image.network(
                'http://pic8.nipic.com/20100713/1954049_091647155567_2.jpg'),
            Positioned(
              //定位
              bottom: 50.0, //相对于底部边界距离
              right: 50.0, //相对于右边界距离
              child: Text('hi flutter', style: TextStyle(
                  fontSize: 36.0,
                  fontWeight: FontWeight.bold,
                  fontFamily: 'serif',
                  color: Colors.redAccent
              ),),
            )
          ],
        ),
      ),
    );
  }
}

class TableData {
  final String title;
  final int cunt;

  TableData(this.title, this.cunt);
}


///表格布局
class TableDemo extends StatelessWidget {

  TableDemo({Key key, @required TableData tableData}) :super(key: key) {
    print("table data:${tableData.title}，${tableData.cunt}");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Table表格布局示例'),
      ),
      body: Container(
        padding: EdgeInsets.all(4.0),
        child: Table(
          //设置表格列数
          columnWidths: const<int, TableColumnWidth>{
            //FixedColumnWidth固定大小
            //FlexColumnWidth，相对于屏幕宽度：参数为占比，不传参数平均分宽度
            //FractionColumnWidth(0.1)，自适应：参数为最小宽度的倍数，比如某一列宽度为两个字符，则0.5为这个宽度的5倍
            0: FlexColumnWidth(1),
            1: FlexColumnWidth(1),
            2: FlexColumnWidth(1),
            3: FlexColumnWidth(1),
            4: FlexColumnWidth(3),
          },
          //设置表格边框样式
          border: TableBorder.all(
              color: Colors.black38, width: 2.0, style: BorderStyle.solid),
          children: const<TableRow>[
            //第一行数据
            TableRow(
                children: <Widget>[
                  Text('序号',),
                  Text('姓名'),
                  Text('年龄'),
                  Text('性别'),
                  Text('住址'),
                ]
            ),
            //第二行数据
            TableRow(
                children: <Widget>[
                  Center(
//                      alignment: Alignment.center,
//                      padding: EdgeInsets.all(4.0),
                      child: Text('1')
                  ),
                  Text('张三'),
                  Text('22'),
                  Text('男'),
                  Text('广州市天河区'),
                ]
            ),
          ],
        ),
      ),
    );
  }
}

///控制文本显示隐藏
class MyOffstage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MyHomeOffstage();
  }

}

class MyHomeOffstage extends StatefulWidget {
  @override
  _MyHomeOffstagePage createState() {
    return _MyHomeOffstagePage();
  }
}

class _MyHomeOffstagePage extends State<MyHomeOffstage> {

  bool myOffstage = true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Offstage控制视图隐藏显示'),
      ),
      body: Center(
        child: Offstage(
          //控制是否显示
          offstage: myOffstage,
          child: Text('隐藏显示案例', style: TextStyle(fontSize: 24.0),),
        ),
      ),
      floatingActionButton: FloatingActionButton(
          tooltip: '隐藏显示',
          child: Icon(Icons.flip),
          onPressed: () {
            setState(() {
              myOffstage = !myOffstage;
            });
          }),
    );
  }
}