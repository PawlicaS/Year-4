package circuits

import Chisel.fromtIntToLiteral
import chisel3.{Hexadecimal, PrintableHelper}
import chisel3.iotesters.{Driver, PeekPokeTester}


class TwoAndTests(c: TwoAnds) extends PeekPokeTester(c) {

  var i0 = 1
  var i1 = 1
  var i2 = 1
  var i3 = 1
  for (i0 <- 0 until 2) {
    for (i1 <- 0 until 2) {
      for (i2 <- 0 until 2) {
        for (i3 <- 0 until 2) {
          poke(c.io.in1, i0)
          poke(c.io.in0, i1)
          poke(c.io.in2, i2)
          poke(c.io.in3, i3)
          step(1)
          printf("i0: %d i1: %d i2: %d i3: %d out: %d\n", i0, i1,i2,i3, peek(c.io.out))
        }
      }
    }
  }
}

object Hello2 {
  def main(args: Array[String]): Unit = {
    if (!Driver(() => new TwoAnds(),"verilator")(c => new TwoAndTests(c))) System.exit(1)
  }
}
