package circuits

import Chisel.fromtIntToLiteral
import chisel3.{Hexadecimal, PrintableHelper}
import chisel3.iotesters.{Driver, PeekPokeTester}


class PeakTests(c: Peak) extends PeekPokeTester(c) {
var a4: Array[Int]= Array(1,1,4,8,6,2,7,1)
  for (i1 <- 0 until a4.length) {
    poke(c.io.channel.bits, a4(i1))
    poke(c.io.channel.valid, 1)
    step(1)
    printf("i0: %d count: %d st: %d prev: %d\n", a4(i1), peek(c.io.cnt),peek(c.io.st),peek(c.io.pr))
  }
 /* var i0 = 1
  poke(c.io.channel.bits, i0)
  poke(c.io.channel.valid, 1)
  step(1)
  printf("i0: %d count: %d st: %d prev: %d\n", i0, peek(c.io.cnt),peek(c.io.st),peek(c.io.pr))
  i0=2
  poke(c.io.channel.valid, 1)
  poke(c.io.channel.bits, i0)
  step(1)
  printf("i0: %d count: %d st: %d prev: %d\n", i0, peek(c.io.cnt),peek(c.io.st),peek(c.io.pr))
  i0=1
  poke(c.io.channel.valid, 1)
  poke(c.io.channel.bits, i0)
  step(1)
  printf("i0: %d count: %d st: %d prev: %d\n", i0, peek(c.io.cnt),peek(c.io.st),peek(c.io.pr))
*/
}

object Hello4 {
  def main(args: Array[String]): Unit = {
    if (!Driver(() => new Peak())(c => new PeakTests(c))) System.exit(1)
  }
}
