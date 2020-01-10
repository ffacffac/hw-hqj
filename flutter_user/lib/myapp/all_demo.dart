import 'package:flutter/widgets.dart';
import 'package:flutter/material.dart';
import 'package:flutter_user/myapp/GestureDetectorDemo.dart';
import 'package:flutter_user/myapp/exlist.dart';
import 'package:flutter_user/myapp/http_demo.dart';
import 'package:flutter_user/myapp/loading1.dart';
import 'package:flutter_user/myapp/photo_demo.dart';
import 'package:flutter_user/myapp/routesDemo.dart';
import 'package:flutter_user/myapp/tab_Drawer_Demo.dart';
import 'package:flutter_user/myapp/view_demo.dart';
import 'package:flutter_user/myapp/wudang_travel.dart';
import 'package:fluttertoast/fluttertoast.dart';

import 'anim/AnimDemo.dart';
import 'charts_demo.dart';
import 'cupertinoActivitylndicator.dart';
import 'cupertinoAlertDialog.dart';
import 'cupertinoNavigationBar.dart';
import 'gridview_demo.dart';

void main() => runApp(AllDemo());

final List<String> list = [
  "CupertinoActivitylndicator",
  "CupertinoAlertDialog",
  "CupNavigationBar",
  "TabAndDrawerSample",
  "baseViewDemo",
  "GridViewDemo",
  "ListView",
  "TableDemo",
  "MyOffstage隐藏显示",
  "WuDangView整体案例",
  "GestureDetectorDemo",
  "RoutesTab",
  "HttpDemo",
  "LoadingDialog",
  "动画",
  "ChartsDemo图表",
  "ExpandableList",
  "PhotoDemo拍照",
  "Dialog",
  "Form"
];

final List<Widget> widgetList = [];

class AllDemo extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '案例展示',
      routes: {
        list[0]: (context) => CupertinoActivitylndicator(),
        list[1]: (context) => CupertinoAlertDialogDemo(),
        list[2]: (context) => CupNavigationBar(),
        list[3]: (context) => TabBarDrawerSample(),
        list[4]: (context) => StackDemo(),
        list[5]: (context) => GridViewDemo(),
//        list[6]: (context) => GridViewDemo(),
        //传递参数
        list[7]: (context) => TableDemo(tableData: TableData('表格案例', 10)),
        list[8]: (context) => MyOffstage(),
        list[9]: (context) => WuDangMain(),
        list[10]: (context) => GestureDetectorDemo(),
        list[11]: (context) => RoutesDemo(),
        list[12]: (context) => HttpDemo(),
//        list[13]: (context) => LoadingDialog(),
        list[14]: (context) => AnimDemo(),
        list[15]: (context) => ChartsDemo(),
        list[16]: (context) => ExpandableList(),
        list[17]: (context) => PhotoDemo(),
      },
      home: Scaffold(
        appBar: AppBar(title: Text('案例展示')),
        body: Center(
          child: ListView.builder(
              itemCount: list.length,
              itemBuilder: (context, index) =>
                  Column(
                    children: <Widget>[
                      ListTile(
                        title: Text(list[index]),
                        onTap: () {
                          startChildDemo(context, index);
                        },
                      ),
                      Divider(color: Colors.grey,)
                    ],
                  )
          ),
        )
        ,
      )
      ,
    );
  }

  void startChildDemo(BuildContext context, int index) {
    if (list[index] == "LoadingDialog") {
      Navigator.push(
          context, LoadingDialogRouter(LoadingDialog()));
    } else {
      Fluttertoast.showToast(msg: list[index]);
      Navigator.pushNamed(context, list[index]);
    }
  }
}