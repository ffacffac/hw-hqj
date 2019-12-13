import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';

void main() => runApp(PopupMenu());

enum ConferenceItem { AddMember, LockConference, ModifyLayout, TurnoffAll }

class PopupMenu extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'popupMenu示例',
      home: Scaffold(
        appBar: AppBar(title: Text('popupMenu示例'),
          actions: <Widget>[
            IconButton(icon: Icon(Icons.add), onPressed: () {
              print('点击了新增按钮');
              Scaffold.of(context).showSnackBar(
                  SnackBar(content: Text('SnackBar info 新增成功')));
            }),
            FlatButton(
              onPressed: () {},
              child: getPopupMenuButton(),
              textColor: Colors.white,
            )
          ],
        ),
        body: Center(
          child: FlatButton(
            onPressed: () {},
            child: getPopupMenuButton(),
          ),
        ),
      ),
    );
  }
}

PopupMenuButton getPopupMenuButton() {
  return PopupMenuButton<ConferenceItem>(
      onSelected: (ConferenceItem conferenceItem) {
        Fluttertoast.showToast(msg: '点击了-----');
      },
      //=>相当于(BuildContext context){
      // return <PopupMenuEntry<ConferenceItem>>[];
      // }
      itemBuilder: (BuildContext context) =>
      <PopupMenuEntry<ConferenceItem>>[
        const PopupMenuItem<ConferenceItem>(
          child: Text('添加成员'), value: ConferenceItem.AddMember,),
        const PopupMenuItem<ConferenceItem>(
          child: Text('锁定会议'),
          value: ConferenceItem.LockConference,),
        const PopupMenuItem<ConferenceItem>(
          child: Text('修改布局'), value: ConferenceItem.ModifyLayout,),
        const PopupMenuItem<ConferenceItem>(
          child: Text('挂断所有'), value: ConferenceItem.TurnoffAll,)
      ]
  );
}