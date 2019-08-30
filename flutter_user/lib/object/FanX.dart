void main() {
  var per = new Person<String>();
  per.pRead("张三"); //必须指定为String
  per.pWrit(111); //任意类型
}

//类的泛型
class Person<T> {
  T t;

  void pRead(T empl) {
    this.t = empl;
    print("t is ==$t");
  }

  void pWrit<T>(T empl) {
    print(empl);
  }
}

//方法指定泛型
moe<T>(T t) {

}