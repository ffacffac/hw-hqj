void main() {
  var stuimpl = Stuimpl();
  stuimpl.read("my read--");
}

class Person {
  String name;

  final int height;

  Person(this.name, this.height);

  Person.withPar(this.name, this.height);


}

class Student extends Person {

  final String lov;

  //继承Person要调用父类的构造方法
  // [ : ]表示在构造函数执行之前进行执行，并且先调用父类的构造函数
  Student(String name, int height, String lovv)
      : lov=lovv,
//        super(name, height);
        super.withPar(name, height);
}

///抽象类
abstract class Per {
  run();
}

///接口
class Stu {
  void read(String msg) {
    print("student read ---");
  }
}

class Stuimpl implements Stu {
  @override
  read(String msg) {
    print('Stuimpl read------$msg');
  }
}

///多继承
class A {
  int a;
}

class B {
  int b;
}

class C {
  int c;
}

///Mxins 多继承 with
///条件：
///1.继承的类不能有显式的构造函数
///2.A、B、C类只能继承于Object，不能继承其他类
///3.
class D extends A with B, C {
  int d;
}

///或者这样写
//class D = A with B,C;


///操作符覆写


///枚举
enum SS {
  S,
  A,
  T,
  H
}

enumDemo() {
  print(SS.values[0]); //枚举可通过下标来取值
}