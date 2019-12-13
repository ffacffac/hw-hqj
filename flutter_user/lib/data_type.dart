void main() {

  ///字符串
  var str1 = "abc";
  var str2 = "def";
  var str3 = "hello world";

  print(str1 * 3); //==abcabcabc 赋值三次
  print(str1 == str2); //比较是否相等
  print(str1.isEmpty);
  print(str1.isNotEmpty);

  print(str1.indexOf("a"));

  List<String> list = str3.split(" ");
  print(list);

  ///Map
  var map = {'name': "zhangsan", 'age': 20, "stu": true};
  var map2 = <String, Object>{'name': "zhangsan", 'age': 20, "stu": true};
  var map1 = new Map();
  var map3 = new Map<String, Object>();

//  map.forEach(mapFun); //遍历

  yunsuanfu();

  siwtchDemo();

//  chooseFun(10, 11); //可选参数
//  chooseFun(10, 11, var2: 12); //可选参数
//  chooseFun(10, 11, var2: 12, var3: 13); //可选参数，基于命名方式，位置可变
//  chooseFun(10, 11, var3: 14, var2: 15); //可选参数，基于命名方式，位置可变

  ///匿名方法,赋给一个变量
  var nim = () {
    print("我是匿名方法----------------");
  };
  nim(); //执行匿名方法

  ///匿名方法第二种调用，但不推荐
  (() {
    print("匿名方法调用---------------");
  })();

  ///将匿名方法当做参数传递funsVar方法中的times参数
  var res = funsVar(10, (str, ilo) {
    return str * 3;
  });
  print(res);

  ///调用闭包
  var clo = colosa();
  clo(); //clo 会持有colosa方法里面局部变量的状态
  clo(); //输出 2
  clo(); //输出 3

}

String funsVar(int a, String times(String t, int iol)) {
  print('a==$a，-------funsVar===${times("abcdefggg-0", 55)}');
  return times("abcdefggg-1", 156);
}

mapFun(key, value) {
  print("key=$key，value=$value");
}

///运算符
yunsuanfu() {
  print("-----------------------运算符---------------------");
  int a = 10;
  int b = 5;

  var c = b ??= a; //b 是否相等于a 如果不相等，则取左边值
  print(c);

  String s = "sss";
  String s1;

  print(s1 ?? s); // ?? 如果左边的为null则取右边的值，否则去左边的值
}

siwtchDemo() {
  print("-----------------------运算符---------------------");
  var lan = "";
  switch (lan) {
    case "Dart":
      print("--------------Dart--");
      break;
    case "Java":
      print("--------------java--");
//      continue;
      break;
    default:
      print("--------------default--");
      break;
  }
}

///=>方法返回值
funs(n1, n2) => "ni+n2==${n1 + n2}";

//三目运算符return
funss(n1) => n1 >= 10 ? "n1 >10" : "n1<10";

///可选参数 https://www.yiibai.com/dart/dart_programming_functions.html

chooseFun(var1, var4, {var2, var3}) {
  print("-----------------------可选参数---------------------");
  print("var1=$var1,var2=$var2,var3=$var3,var4=$var4");
}

///闭包
colosa() {
  int count = 1; //外部调用闭包会持有colosa方法里面局部变量的状态

  //闭包声明方式
  closb() {
    print('-----------闭包--------${count++}');
  }
  return closb;
}


