library calculater_lib;

import 'dart:math';

int add(int firstNumber, int secondNumber) {
  print("excet--add");
  return firstNumber + secondNumber;
}

int modulus(int firstNumber, int secondNumber) {
  print("inside modulus method of Calculator Library ");
  return firstNumber % secondNumber;
}

int random(int no) {
  return new Random().nextInt(no);
}
