///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS com.github.ajalt.clikt:clikt-jvm:5.1.0


import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main

class Foo : CliktCommand() {
  override fun run() {
    println("Hello World")
  }
}

fun main(vararg args: String) {

  Foo().main(args)
}
