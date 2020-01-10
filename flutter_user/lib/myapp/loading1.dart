import 'package:flutter/material.dart';

class LoadingDialog extends Dialog {

  final bool canceledOnTouchOutside = true;

  @override
  Widget build(BuildContext context) {
    var width = MediaQuery
        .of(context)
        .size
        .width;
    var height = MediaQuery
        .of(context)
        .size
        .height;
    return Center(
      child: Material(
          color: Colors.transparent,
          child: Container(
            child: GestureDetector(
              onTap: () {
                //点击外面退出
                if (canceledOnTouchOutside) {
                  print('点击退出');
                  Navigator.pop(context);
                }
              },
              child: Container(
                width: width,
                height: height,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    _loadingWidget1(),
                    Container(
                      margin: EdgeInsets.only(top: 20.0),
                    ),
                    _loadingWidget2(width / 5 * 3),
                  ],
                ),
              ),
            ),
          )
      ),
    );
  }
}

///加载框1，正方形
Widget _loadingWidget1() {
  return SizedBox(
      width: 120.0,
      height: 120.0,
      child: Container(
        decoration: ShapeDecoration(
            color: Colors.white,
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8.0)
            )
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            CircularProgressIndicator(),
            Padding(padding: const EdgeInsets.only(top: 15.0),
              child: Text('加载中...'),
            )
          ],
        ),
      )
  );
}

///加载框2，长方形
Widget _loadingWidget2(var w) {
  return SizedBox(
      width: w,
      height: 60.0,
      child: Container(
        decoration: ShapeDecoration(
            color: Colors.white,
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8.0)
            )
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Container(
              height: 28.0,
              width: 28.0,
              child: CircularProgressIndicator(
//                backgroundColor: Colors.red,
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 15.0),
              child: Text('正在加载数据，请稍后...'),
            )
          ],
        ),
      )
  );
}

///半透明路由，设置了背景才能显示为半透明状态
class LoadingDialogRouter extends PageRouteBuilder {

  final Widget page;

  LoadingDialogRouter(this.page) :super(
      opaque: false,
      barrierColor: Colors.black54,
      pageBuilder: (context, animation, secondaryAnimation) => page,
      transitionsBuilder: (context, animation, secondaryAnimation,
          child) => child
  );
}