import 'mylib/calculater.dart' as calcu; //定义库前缀（别名）
import 'dart:io';
import 'dart:isolate';

//出现：Setting VM flags failed: Unrecognized flags: checked
/*错误时 参考：https://blog.csdn.net/oZhuiMeng123/article/details/90056326*/
void main() {
  print(printNum(50));

  print(calcu.add(8, 5)); //引用calculater.dart方法
  print(calcu.modulus(8, 5)); //引用calculater.dart方法

  //集合
  var list = new List();
  list.add("555");
  list[0] = "111";
  list.add("222");
  var listPosition = list[1];
  print(listPosition);
  var len = list.length; //长度
  //list的常用属性
  print(list.first); //返回第一个元素
  print(list.last); //返回最后一个元素
//  print(list.single); //检查列表是否只有一个元素并返回它

  var danList = new List();
  List<int> ls = [1, 2, 3, 3, 5];
//  var danList = new List<dynamic>();//或者表示可以装载任何类型
  danList.add("zs");
  danList.add(123);
  danList.add(true);
  danList.add(12.03);
  print(danList);

  //遍历list
//  list.forEach((v) => print('value is $v'));
//  for (int i = 0; i < list.length; i++) {
//    print(list[i]);
//  }
//  for (String value in list) {
//    print("for in value==" + value);
//  }

  //-----------------映射 Map----------------
  var map = new Map();
  map["name"] = "张三";
  map["address"] = "广州天河区"; // 映射值可以是包括NULL的任何对象。
  print(map);
  var map1 = {'name': '李四', 'address': '广州越秀区'};
  map1['age'] = '20'; //动态添加
  print(map1);

  //遍历Map
  map.forEach((k, v) => print('$k:$v'));

  demoEnum(); //枚举

  demoInterface(); //接口

  demoExtends(); //继承

  demoTryOnCatch(15, 0); //try on catch

  ///同步、异步执行
//  demoSync();

  demoIsolates(); //并发
}

///
printNum(num an) {
  print('an is $an');
  return "zhangsan";
}

///枚举
enum Status {
  none,
  running,
  stopped,
  paused
}

demoEnum() {
  print(Status.values);
  Status.values.forEach((v) => print('value: $v, index: ${v.index}'));
}

///接口，类定义、构造函数生成
///
demoInterface() {
  var con = ConsolePrinter("李四", 20, "广州");
  var two = ConsolePrinter.two("张三", "上海");
  con.printData();

  print(con.name + '--two：' + two.name);
  print(con.address + '--two：' + two.address);
  print(con.age);

  two._nameD(1, 2); //添加下划线，表示方法私有，只能在本类使用
}

class Printer {
  void printData() {
    print("-------------Printer----print_data-------------");
  }
}

class ConsolePrinter implements Printer {

  ///属性默认生成getter、setter方法
  String name;
  int age;
  String address;

  ///方式一：构造函数，默认是无参的构造函数
  ConsolePrinter(this.name, this.age, this.address);

  ///方式二：
//  ConsolePrinter(name, age, address) {
//    this.name = name;
//    this.age = age;
//    this.address = address;
//  }

  ///命名构造函数，以使类定义多个构造函数
  ConsolePrinter.two(this.name, this.address);

  @override
  void printData() {
    print("-------------ConsolePrinter----print_data-------------");
  }

  @override
  String toString() {
    return 'ConsolePrinter{name: $name, age: $age, address: $address}';
  }

  ///添加下划线，表示方法私有，只能在本类使用，不对外暴露
  _nameD(aa, bb) {
    print("私有方法");
  }
}


///继承
demoExtends() {
  //级联运算符: .. 存在一系列调用的情况下，可用作速记
  new ClsB()
    ..ab = 10
    ..ac = "张三"
    ..clssb()
    ..clsaa()
    ..toString();
}

class ClsA {
  void clsaa() {
    print("---------------clsaa-----");
  }
}

class ClsAB extends ClsA {

  static void clsABMedeth() {
    print("static modeth----->");
  }
}

///多重继承
class ClsB extends ClsA {
  int ab;
  String ac;

  void clssb() {
    super.clsaa(); //可以忽略super关键字
  }
}

///try  on  catch
demoTryOnCatch(int x, int y) {
  /// const和 final声明的都是常量，后续不能修改其值
  const va = 20;
  final vb = 30;

  //使用try on 指定具体的Exception
  try {
    var res = x ~/ y; // [ ~/相除取整 ，  / 相除带有小数点]
  } on IntegerDivisionByZeroException {
    print('Cannot divide by zero');
  }
  try {
    var d = x ~/ y;
  } catch (e) {
    print(e); //输出-IntegerDivisionByZeroException
  }
}

///同步、异步执行
demoSync() {
  print("----------------------同步、异步执行-------------------");

  ///同步
//  print("Enter your name :");
//  //是一种同步方法。它将阻止执行readLineSync()函数调用之后的所有指令，直到执行完成
//  //stdin.readLineSync()：等待输入。它停止执行并且在收到用户输入之前不再执行任何操作
//  String name = stdin.readLineSync();
//  print("Hello Mr. $name");
//  print("end of main");

  ///异步读取文件
  File file = new File(Directory.current.path + "/data/contact.txt");
  Future<String> future = file.readAsString(); //异步读取
//  file.writeAsString("9966");//写入，覆盖
  future.then((data) => print('$data'));
  print("end of read file"); //异步执行不会阻塞主线程，这行代码也会继续执行
}

///并发

foo(var message) {
  print('execution from foo ... the message is :$message');
}

demoIsolates() {
  print("--------------------并发--------------------");

  Isolate.spawn(foo, "Hello--->"); //参数1：方法名，参数2：传递方法参数
  Isolate.spawn(foo, "Word----->");

  print('execution from main1');
  print('execution from main2');
  print('execution from main3');
}

///异步运行方法，耗时操作
demoAsync() async {

}