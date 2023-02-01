package CPU

import chisel3.iotesters.Driver
import chisel3.{Hexadecimal, PrintableHelper}
import chisel3.iotesters.{Driver, PeekPokeTester}
import CPU._
import utils.CPUConfig

class VerilatorTests(c: Tile) extends PeekPokeTester(c) {


  println("hello")
}
object Hello2 {
  val conf = new CPUConfig()
  conf.cpuType     = "single-cycle"
  //conf.memFile     = hexName
  conf.memType     = "combinational"
  conf.memPortType = "combinational-port"
  conf.memLatency  = 0
  def main(args: Array[String]): Unit = {
    if (!Driver(() => new Tile(conf),"verilator")(c => new VerilatorTests(c))) System.exit(1)
  }
}
