import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';

void main() => runApp(CardDemo());

class CardDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'cardDemo示例',
      home: Scaffold(
        appBar: AppBar(
          title: Text('cardDemo示例'),
        ),
        body: Center(
          child: getSizedBox(),
        ),
      ),
    );
  }

  SizedBox getSizedBox() {
    return new SizedBox(
//      height: 200.0,
      child: Card(
        child: Column(
          children: <Widget>[
            ListTile(
              title: Text('卡片式布局demo1'),
              subtitle: Text('hqj'),
              leading: Icon(Icons.home, color: Colors.lightBlue),
              onLongPress: () {
                Fluttertoast.showToast(msg: 'select home long');
              },
            ),
            Divider(),
            ListTile(
              title: Text('卡片式布局demo1'),
              subtitle: Text('hqj'),
              leading: Icon(Icons.school, color: Colors.lightBlue,),
              onTap: () {
                Fluttertoast.showToast(msg: 'select school');
              },
            ),
            Divider(),
          ],
        ),
      ),
    );
  }
}