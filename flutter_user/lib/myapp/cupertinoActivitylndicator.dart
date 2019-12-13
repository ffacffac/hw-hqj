import 'package:flutter/widgets.dart';
import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

class CupertinoActivitylndicator extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('CupertinoActivitylndicator 加载圈案例'),
      ),
      body: Center(
        child: CupertinoActivityIndicator(
          radius: 20.0,
        ),
      ),
    );
  }
}