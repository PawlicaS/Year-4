package CPU

import chisel3._
import utils.CPUConfig
import memory._
class Tile(val conf: CPUConfig) extends Module
{
  val io = IO(new Bundle{
    val success = Output(Bool())
    val addr = Output(UInt(32.W))
    val rx_data = Input(UInt(32.W))
    val rx_rstrb = Input(UInt(1.W))
    val rx_data_done = Output(UInt(1.W))
    val tx_data = Output(UInt(32.W))
    val tx_wstrb = Output(UInt(1.W))
    val test_passed =Output(UInt(1.W))
  })

  io.success := DontCare


  val cpu  = Module(conf.getCPU())
  val mem  = Module(conf.getNewMem())

  val imem = Module(conf.getIMemPort())
  val dmem = Module(conf.getDMemPort())

  io.addr := dmem.io.pipeline.address
  io.test_passed := mem.io.test_passed
  //printf("top1 cpu.io.test_passed %d\n",cpu.io.test_passed)
  conf.printConfig()
  mem.io.rx_data := io.rx_data
  mem.io.rx_rstrb := io.rx_rstrb
  io.rx_data_done := mem.io.rx_data_done

  io.tx_data := mem.io.tx_data
  io.tx_wstrb:= mem.io.tx_wstrb
  //printf(" data %d rstrb %d done %d\n",io.rx_data,io.rx_rstrb,mem.io.rx_data_done)
  mem.wireMemory (imem, dmem)
  cpu.io.imem <> imem.io.pipeline
  cpu.io.dmem <> dmem.io.pipeline
}
