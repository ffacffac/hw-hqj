package com.hw.ourlife.activity


class KotlinDemo {

    fun myWhen(obj: Any): String {
        when (obj) {
            1 -> "str"
            "hello" -> "Greeting"
            is Long -> "Long"
            !is String -> "NOT A String"
            else -> "Unknown"
        }
        return ""
    }
}

interface MyInterface {
    fun my()
    fun me()
}

interface A {
    fun foo() {
        print("A")
    }

    fun bar()
}

interface B {
    fun foo() {
        print("B")
    }

    fun bar() {
        print("bar")
    }
}

class C : A {
    override fun bar() {
        print("bar")
    }
}

class D : A, B {
    override fun bar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun foo() {
        super<A>.foo()
    }
}

fun MutableList<Int>.swp(c: Int, i: Int) {
    val tmp = this[c]
    this[c] = this[i]
    this[i] = tmp

//    val l = mutableListOf(1, 2, 3)
//    l.swp(0, 2)
}