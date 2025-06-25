package io.github.jangalinski.kata.onearmedbandit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) = runApplication<OneArmedBanditApplication>(*args).let {  }

@SpringBootApplication
class OneArmedBanditApplication {

}
