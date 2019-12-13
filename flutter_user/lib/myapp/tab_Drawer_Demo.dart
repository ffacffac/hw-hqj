import 'package:flutter/widgets.dart';
import 'package:flutter/material.dart';

//void main() => runApp(TabBarSample());

class ItemView {

  final String title; //标题
  final IconData icon; //图标

  const ItemView({this.title, this.icon});
}

final List<ItemView> items = <ItemView>[
  const ItemView(title: '地图', icon: Icons.map),
  const ItemView(title: '搜索', icon: Icons.search),
  const ItemView(title: '新增', icon: Icons.add),
  const ItemView(title: '短信', icon: Icons.mail),
  const ItemView(title: '菜单', icon: Icons.widgets),
];

class TabBarDrawerSample extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      child: getDefaultTabController(),
    );

//    return Column(
//        children: [
////      new MaterialApp(
////      title: 'tab实例',
////      theme: ThemeData(primaryColor: Colors.green),
////      home: DefaultTabController(
//          Divider(),
//          getDefaultTabController()
//        ]);
  }
}

DefaultTabController getDefaultTabController() {
  return DefaultTabController(
      length: items.length,
      child: Scaffold(
        appBar: AppBar(title: Text('TabBar选项卡示例'),
          bottom: TabBar(
              indicatorColor: Colors.yellow, //item tab 下划线颜色
              isScrollable: true,
              tabs: items.map((ItemView item) {
                return Tab(text: item.title, icon: Icon(item.icon));
              }).toList()
          ),
        ),
        drawer: Drawer(
          child: getMyDrawer(),
        ),
        body: TabBarView(children: items.map((ItemView item) {
          return Padding(padding: const EdgeInsets.all(1.0),
            child: SelectedView(itemView: item,),);
        }).toList()),
      )
  );
}

class SelectedView extends StatelessWidget {

  final ItemView itemView;

  const SelectedView({Key key, this.itemView}) :super(key: key);

  @override
  Widget build(BuildContext context) {
    final TextStyle textStyle = Theme
        .of(context)
        .textTheme
        .display1;
    return Card(
      color: Colors.white,
      child: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min, //垂直方向最小化处理
          crossAxisAlignment: CrossAxisAlignment.center, //水平方向居中对齐
          children: <Widget>[
            Icon(itemView.icon, size: 128.0, color: textStyle.color),
            Text(itemView.title, style: textStyle)
          ],
        ),
      ),
    );
  }
}

///侧滑菜单
Drawer getMyDrawer() {
  return Drawer(
    child: ListView(
      children: <Widget>[
        //设置用户头像
        UserAccountsDrawerHeader(
          accountName: Text('HQJ'),
          accountEmail: Text('123456789@qq.com'),
          currentAccountPicture: CircleAvatar(
              backgroundImage: AssetImage('assets/images/uploadImage15.jpg')
          ),
          onDetailsPressed: () {},
          otherAccountsPictures: <Widget>[
            Container(
              child: Image.asset('assets/images/food06.jpeg'),
            )
          ],
        ),
        ListTile(
          leading: CircleAvatar(child: Icon(Icons.color_lens),), //导航栏菜单
          title: Text('个性装扮', style: TextStyle(fontSize: 22.0)),
        ),
        ListTile(
          leading: CircleAvatar(child: Icon(Icons.photo),),
          title: Text('我的相册', style: TextStyle(fontSize: 22.0)),
        ),
        ListTile(
          leading: CircleAvatar(child: Icon(Icons.wifi),),
          title: Text('免流量特权', style: TextStyle(fontSize: 22.0)),
        ),
        ListTile(
          leading: CircleAvatar(child: Icon(Icons.search),),
          title: Text('搜索', style: TextStyle(fontSize: 22.0)),
        ),
//        FlatButton(onPressed: () {}, child: Text('设置'))
      ],
    ),
  );
}
