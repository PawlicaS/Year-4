package circuits

import chisel3._

class TwoAnds extends Module {
  val io = IO(new Bundle {
    val in0 = Input(UInt(1.W))
    val in1 = Input(UInt(1.W))
    val in2 = Input(UInt(1.W))
    val in3 = Input(UInt(1.W))
    val out = Output(UInt(1.W))
  })
  val add1 = Module(new And())
  add1.io.in0 := io.in0
  add1.io.in1 := io.in1
  val add2 = Module(new And())
  add2.io.in0 := io.in2
  add2.io.in1 := io.in3
  io.out := (add1.io.out & add2.io.out)
}
