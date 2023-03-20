package circuits
import chisel3._

class Counter(size: Int) extends Module {
  val io = IO(new Bundle {
    val inc = Input(Bool())
    val amt = Input(UInt(4.W))
    val tot = Output(UInt(8.W))
  })
  val x = RegInit(0.U(size.W))
  def wrapAround(n: UInt) =
    Mux(n > 100.U, 1.U, n)

  val r1 = RegInit(0.U(size.W))
  r1 := r1 + 1.U

  io.tot := r1
  when (io.inc) { x := wrapAround(x + io.amt) }




}
