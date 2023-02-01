package CPU

import chisel3._
import memory._

class CoreIO extends Bundle {
  val imem = Flipped(new IMemPortIO)
  val dmem = Flipped(new DMemPortIO)
}