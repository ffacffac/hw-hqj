///类和对象

void main() {
  var per = new Person();
  var pers = Person.two("", 165, 60);
  Person per3;
  per3?.eat(); //per3为null，所有调用eat方法会报错，加上?操作符后不执行eat方法，也不报错

  ///类型转换
  var per4;
  per4 = "";
  per4 = new Person();
  //per4不确定是否是Person对象转成Person对象才能调用eat方法
  (per4 as Person).eat();
  //或者
  if (per4 is Person) {
//  if (per4 is! Person) {//如果不是Person类型
    per4.eat();
  }

  print(per.name);
  print(per.address);

  print(per.hg); //计算属性
}

class Person {

  String name;
  int age;
  String address;

  int height;
  int powr;

  int _p; //变量是私有的，此文件外的都不能访问到

  //构造方法是不能重载的
  Person();

//构造方法一 在构造方法执行前赋值
  Person.one(this.name, this.age, this.address, this.height, this.powr);

//构造方法二
  Person.two(this.name, this.height, this.powr);

  ///初始化列表，在构造函数执行之前执行:height=map["height"]
  ///常用于给final属性赋值
  Person.three(Map<String, Object> map)
      :height=map["height"],
        powr=map["powr"]{
    this.name = map["name"];
    this.age = map["age"];
  }

  void eat() {
    print('is person eat---->');
  }

  ///计算属性，通过计算转换到其他实例变量
  int get hg {
    return height * powr;
  }

  ///计算属性二
  int get gh => height * powr;

  ///通过计算属性给 类属性赋值
  set gh(value) {
    height = value / 20;
  }
}

///重载操作符
class Vector {
  final int x;
  final int y;

  const Vector(this.x, this.y);

  //两个Vector实例对象相加
  Vector operator +(Vector v) {
    return new Vector(x + v.x, y + v.y);
  }

  Vector operator -(Vector v) {
    return new Vector(x - v.x, y - v.y);
  }
}

///调用操作符
operatorDemo() {
  final v1 = new Vector(2, 3);
  final v2 = new Vector(2, 2);

  final r1 = v1 + v2;
  print('r1.x==${r1.x},r1.y==${r1.y}'); //r1. x=4 r1.y=5

  final r2 = v1 - v2;
  print('r2.x==${r2.x},r2.y==${r2.y}'); //r2.x=O r2.y=l
}
