import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

void main() => runApp(LoginPage());

class LoginPage extends StatefulWidget {

  @override
  _LoginPageState createState() {
    return _LoginPageState();
  }
}

class _LoginPageState extends State<LoginPage> {

  //全局Key用来获取Form表单组件
  GlobalKey<FormState> loginKey = new GlobalKey();

  //用户名
  String userName;
  String password;

  void login() {
    //读取当前表单状态
    var loginForm = loginKey.currentState;

    //验证表单
    if (loginForm.validate()) {
      loginForm.save();
      print('userName：$userName,password：$password');
      Fluttertoast.showToast(msg: '登录成功');
    }
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Form表单案例',
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('Form表单案例'),
        ),
        body: new Column(
          children: <Widget>[
            new Container(
              padding: const EdgeInsets.all(16.0),
              child: new Form(key: loginKey,
                child: new Column(
                  children: <Widget>[
                    new TextFormField(
                      decoration: new InputDecoration(
                          labelText: '请输入用户名'
                      ),
                      keyboardType: TextInputType.text,
                      //当表单调用save方法时回调的函数
                      onSaved: (value) {
                        userName = value;
                      },
                      onFieldSubmitted: (value) {},
                    ),
                    new TextFormField(
                      decoration: new InputDecoration(
                          labelText: '请输入密码'
                      ),
                      obscureText: true,
                      //验证表单方法
                      validator: (value) {
                        return value.length < 6 ? '密码长度不能小于6位' : null;
                      },
                      onSaved: (value) {
                        password = value;
                      },
                    )
                  ],
                ),
              ),
            ),

            //按钮
            new SizedBox(
              width: 340.0,
              height: 50.0,
              child: new RaisedButton(
                onPressed: login,
                child: new Text('登录',
                  style: TextStyle(fontSize: 22.0, color: Colors.white
                  ),
                ),
                color: Colors.blueAccent,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
