// Base memory classes

package memory

import chisel3._
import chisel3.util.Valid
import chisel3.util.experimental.loadMemoryFromFile

/**
  * Base class for all modular backing memories. Simply declares the IO and the memory file.
  */
abstract class BaseDualPortedMemory(size: Int, memfile: String) extends Module {
  def wireMemory (imem: BaseIMemPort, dmem: BaseDMemPort): Unit = {
    // Connect memory imem IO to dmem accessor
    this.io.imem.request <> imem.io.bus.request
    imem.io.bus.response <> this.io.imem.response
    // Connect memory dmem IO to dmem accessor
    this.io.dmem.request <> dmem.io.bus.request
    dmem.io.bus.response <> this.io.dmem.response
  }

  val io = IO(new Bundle {
    val imem = new MemPortBusIO
    val dmem = new MemPortBusIO
    val rx_data = Input(UInt(32.W))
    val rx_rstrb = Input(UInt(1.W))
    val rx_data_done = Output(UInt(1.W))
    val tx_data = Output(UInt(32.W))
    val tx_wstrb = Output(UInt(1.W))
  //  val tx_data_done = Input(UInt(1.W))
    val test_passed =Output(UInt(1.W))
  })

  // Intentional DontCares:
  // The connections between the ports and the backing memory, along with the
  // ports internally assigning values to the, means that these DontCares
  // should be completely 'overwritten' when the CPU is elaborated
  io.imem.request <> DontCare
  io.dmem.request <> DontCare
  io.rx_data <> DontCare
  io.rx_rstrb <> DontCare
  io.rx_data_done <> DontCare
  io.tx_data <> DontCare
  io.tx_wstrb <> DontCare
  io.test_passed <>DontCare
  // Zero out response ports to 0, so that the pipeline does not receive any
  // 'DontCare' values from the memory ports
  io.imem.response <> 0.U.asTypeOf(Valid (new Response))
  io.dmem.response <> 0.U.asTypeOf(Valid (new Response))

  val memory = Mem(math.ceil(size.toDouble/4).toInt, UInt(32.W))
  loadMemoryFromFile(memory, memfile)
  println("load from memory "+memfile)
}

/**
  * Base class for all instruction ports. Simply declares the IO.
  */
abstract class BaseIMemPort extends Module {
  val io = IO (new Bundle {
    val pipeline = new IMemPortIO
    val bus  = Flipped (new MemPortBusIO)
  })

  io.pipeline <> 0.U.asTypeOf (new IMemPortIO)
  // Intentional DontCare:
  // The connections between the ports and the backing memory, along with the
  // ports internally assigning values to the, means that these DontCares
  // should be completely 'overwritten' when the CPU is elaborated
  io.bus      <> DontCare
}

/**
  * Base class for all data ports. Simply declares the IO.
  */
abstract class BaseDMemPort extends Module {
  val io = IO (new Bundle {
    val pipeline = new DMemPortIO
    val bus = Flipped (new MemPortBusIO)
  })

  io.pipeline <> 0.U.asTypeOf (new DMemPortIO)
  // Intentional DontCare:
  // The connections between the ports and the backing memory, along with the
  // ports internally assigning values to the, means that these DontCares
  // should be completely 'overwritten' when the CPU is elaborated
  io.bus      <> DontCare

  io.pipeline.good := io.bus.response.valid
}
