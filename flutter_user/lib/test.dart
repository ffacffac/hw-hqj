import 'dart:io';

void main() {
  print("Please Enter your name");

  //同步操作
//  String name = stdin.readLineSync();
//  print("your name is $name");

  //异步读取文件数据，非阻塞
  File file = new File(Directory.current.path + "\\data\\contact.txt");
  Future<String>future = file.readAsString();
  future.then((data) => print(data));
  print("End of main");
}