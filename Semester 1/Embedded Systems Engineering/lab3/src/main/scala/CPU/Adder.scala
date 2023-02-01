package CPU
import chisel3._
/**
 * A simple adder which takes two inputs and returns the sum
 *
 * Input:  inputx the first input operand
 * Input:  inputy the second input operand
 * Output: result first + second
 */
class Adder extends Module {
  val io = IO(new Bundle{
    val inputx    = Input(UInt(32.W))
    val inputy    = Input(UInt(32.W))

    val result    = Output(UInt(32.W))
  })

  io.result := io.inputx + io.inputy
}