import 'package:flutter/widgets.dart';
import 'package:flutter/material.dart';
import 'package:flutter_user/myapp/tab_Drawer_Demo.dart';
import 'package:fluttertoast/fluttertoast.dart';

import 'cupertinoActivitylndicator.dart';
import 'cupertinoAlertDialog.dart';
import 'cupertinoNavigationBar.dart';

void main() => runApp(AllDemo());

final List<String> list = [
  "CupertinoActivitylndicator",
  "CupertinoAlertDialog",
  "CupNavigationBar",
  "TabAndDrawerSample",
  "ListView",
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
    Fluttertoast.showToast(msg: list[index]);
    Navigator.pushNamed(context, list[index]);
  }
}