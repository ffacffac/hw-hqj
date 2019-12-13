import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

///仿iOS弹窗
class CupertinoAlertDialogDemo extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('CupertinoAlertDialog弹窗'),
      ),
      body: Center(
        child: CupertinoAlertDialog(
          title: Text('提示'),
          content: Text('是否要取消订单？'),
          actions: <Widget>[
            CupertinoButton(
                child: Text('是'),
                color: Colors.yellow,
                padding: EdgeInsets.all(5.0),
                onPressed: null),
            CupertinoButton(
                child: Text('否'),
                color: Colors.yellow,
                padding: EdgeInsets.all(5.0),
                onPressed: null)
          ],
        ),
      ),
    );
  }

}