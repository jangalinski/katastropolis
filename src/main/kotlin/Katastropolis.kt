package io.github.jangalinski.kata

object Katastropolis {


  inline fun <T> Iterable<T>.peek(crossinline action: (T) -> Unit): List<T> {
    return map {
      action(it)
      it
    }
  }

  fun <T> Iterable<T>.println(): List<T> = peek(::println)


  fun Int.even() = this % 2 == 0
  fun Int.odd() = !even()

}
