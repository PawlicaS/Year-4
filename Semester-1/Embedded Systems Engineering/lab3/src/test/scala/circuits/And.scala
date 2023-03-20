package circuits

import Chisel.fromtIntToLiteral
import chisel3.{Hexadecimal, PrintableHelper}
import chisel3.iotesters.{Driver, PeekPokeTester}


class AndTests(c: And) extends PeekPokeTester(c) {

  var i0 = 1
  var i1 = 1
  poke(c.io.in1, i0)
  poke(c.io.in0, i1)
  step(1)
  printf("i0: %d i1: %d out: %d\n", i0,i1, peek(c.io.out))
  i0=0
  i1=1
  poke(c.io.in1, i0)
  poke(c.io.in0, i1)
  step(1)
  printf("i0: %d i1: %d out: %d\n", i0,i1, peek(c.io.out))
  i0=0
  i1=0
  poke(c.io.in1, i0)
  poke(c.io.in0, i1)
  step(1)
  printf("i0: %d i1: %d out: %d\n", i0,i1, peek(c.io.out))
}

object Hello {
  def main(args: Array[String]): Unit = {
    if (!Driver(() => new And())(c => new AndTests(c))) System.exit(1)
  }
}
