import 'package:flutter/material.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';

import 'app.dart';
import 'loading.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: '即时通讯',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      routes: <String, WidgetBuilder>{
        "app": (context) => App(),
        "/friends": (_) =>
            WebviewScaffold(
              url: "https://flutter.io/",
              appBar: AppBar(
                title: Text('Flutter官网'),
              ),
              withZoom: true,
              withLocalStorage: true,
            ),
//        'search': (context) => Search()
      },
      home: LoadingPage(),
    );
  }
}
