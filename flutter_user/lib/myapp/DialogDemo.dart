import 'package:flutter/widgets.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'dart:async';


import 'DialogDemo1.dart';

//void main() => runApp(DialogDemo());
//void main() => runApp(AlertDialogDemo());

enum Action {
  Ok,
  Cancel
}

class DialogDemo extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SimpleDialog弹窗示例',
      home: _getMyScaffold(context),
//      home: AlertDialogDemo(),
    );
  }
}


Scaffold _getMyScaffold(BuildContext context) {
//    var sm = _showMyDialog(context);
  return Scaffold(
    appBar: AppBar(title: Text('dialog弹窗示例1')),
    body: Center(
//          child: getSimpleDialog(),
      child: getAlertDialog(),
//        child: RaisedButton(
//          child: Text('展示dialog'),
//          onPressed: () {
//            _showMyDialog(context);
//          },
//        )
    )
    ,
  );
}

_showMyDialog(BuildContext context) async {
  await showDialog(context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('alertDialog'),
          content: Text("确认取消支付？"),
          actions: <Widget>[
            FlatButton(onPressed: () {}, child: Text('确认')),
            FlatButton(onPressed: () {}, child: Text('再想想')),
          ],
        );
      });
}


SimpleDialog getSimpleDialog() =>
    new SimpleDialog(
      title: Text('SimpleDialog弹窗'),
      children: <Widget>[
        SimpleDialogOption(
          onPressed: () {
            Fluttertoast.showToast(msg: '点击了选项一');
          },
          child: const Text('选项一'),
        ),
        SimpleDialogOption(
          onPressed: () {
            Fluttertoast.showToast(msg: '点击了选项二');
          },
          child: Text('选项二'),
        )
      ],
    );

AlertDialog getAlertDialog() {
  return AlertDialog(
    title: Text('AlertDialog'),
    content: SingleChildScrollView(
      child: Center(
        child: Text('确认是否删除？\n一旦删除，数据不可恢复'),
      ),
    ),
    actions: <Widget>[
      FlatButton(
        child: Text('确定'),
        onPressed: () {
          Fluttertoast.showToast(msg: '删除成功');
        },
      ),
      FlatButton(onPressed: () {}, child: Text('取消'))
    ],
  );
}