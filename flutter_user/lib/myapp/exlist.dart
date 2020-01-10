import 'package:flutter/material.dart';

class ExpandableList extends StatefulWidget {
  @override
  _ExpandableListState createState() => _ExpandableListState();
}

class _ExpandableListState extends State<ExpandableList> {
  //生成一组测试数据
  final list = List.generate(10, (i) => "这是第$i个item数据");

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ListView.builder( //build的item为header的内容
        itemBuilder: (context, index) {
          return ExpansionTile(
            title: Text("这是第$index个"),
            children: list.map((f) =>
                ListTile(title: Text(f), onTap: () {
                  print('点击了。。。。。');
                },)).toList(),
          );
        },
        itemCount: 5,
      ),
    );
  }
}