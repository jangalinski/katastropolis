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


  data class PrimeBase(
    val map: Map<Int, Int>
  ) {
    companion object {

      operator fun invoke(): PrimeBase {
        return PrimeBase(
          mapOf(
            1 to 0,
            2 to 0,
            3 to 0,
            5 to 0,
            7 to 0,
            11 to 0,
            13 to 0,
            17 to 0,
            19 to 0,
            23 to 0,
            29 to 0,
            31 to 0,
            37 to 0,
            41 to 0,
            43 to 0,
            47 to 0,
            53 to 0,
            59 to 0,
            61 to 0,
            67 to 0,
            71 to 0,
            73 to 0,
            79 to 0,
            83 to 0,
            89 to 0,
            97 to 0,
          )
        )
      }

      operator fun invoke(num: Int): PrimeBase {
        return PrimeBase(mapOf(1 to 0))
      }
    }

    val longValue by lazy {
      map.entries.fold(0L) { s, (k, v) ->
        s + k * v
      }
    }
  }

  fun factors(n: Int) {
    if (n < 1) return
    (1..n / 2)
      .filter { n % it == 0 }
      .forEach { print("$it ") }
  }

  fun printFactors(n: Int) {
    if (n < 1) return
    print("$n => ")
    (1..n / 2)
      .filter { n % it == 0 }
      .forEach { print("$it ") }
    println(n)
  }

}
