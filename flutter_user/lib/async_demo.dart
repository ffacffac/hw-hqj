import 'dart:io';

void main() {
//  initState();
  _reqBaiDu();
}

Future<String> _getUserInfo() async {
  //延迟3秒在返回
  await new Future.delayed(new Duration(milliseconds: 3000));
  return "我是用户";
}


Future _loadUserInfo() async {
  print("_loadUserInfo:${new DateTime.now()}");
  //await 阻塞3秒才能执行后面的代码
  String userRe = await _getUserInfo();
  print(userRe);

  //then 是不等待的，会直接执行后面的代码
//  _getUserInfo().then((info) {
//    print(info);
//  });

  print("_loadUserInfo:${new DateTime.now()}");
}

void initState() {
  /// 初始化状态，加载用户信息
  print("initState:${new DateTime.now()}");
  _loadUserInfo();
  print("initState:${new DateTime.now()}");
}

_reqBaiDu() async {
  var url = "http://www.baidu.com";
  var httpClient = HttpClient();
  var request = await httpClient.getUrl(Uri.parse(url));
  var response = await request.close();
  if (response.statusCode == 200) {
    print("请求成功");
  }
}