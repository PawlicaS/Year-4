package circuits

import Chisel.fromtIntToLiteral
import chisel3.{Hexadecimal, PrintableHelper}
import chisel3.iotesters.{Driver, PeekPokeTester}


class Mux2Tests(c: Mux2) extends PeekPokeTester(c) {
  for (s <- 0 until 2) {
    for (i0 <- 0 until 2) {
      for (i1 <- 0 until 2) {
        poke(c.io.sel, s)
        poke(c.io.in1, i1)
        poke(c.io.in0, i0)
        step(1)
        printf("next s: %d i0: %d i1: %d out: %d\n",s,i0,i1,peek(c.io.out))
        expect(c.io.out, if (s == 1) i1 else i0)
      }
    }
  }

}

object Hello1 {
  def main(args: Array[String]): Unit = {
    if (!Driver(() => new Mux2(),"verilator")(c => new Mux2Tests(c))) System.exit(1)
  }
}
