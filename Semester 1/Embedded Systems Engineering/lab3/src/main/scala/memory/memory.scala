// The instruction and data memory modules

package memory

import chisel3._
import memory.MemoryOperation._

/**
  * This is the actual memory. You should never directly use this in the CPU.
  * This module should only be instantiated in the Top file.
  *
  * The I/O for this module is defined in [[MemPortBusIO]].
  */

class DualPortedCombinMemory(size: Int, memfile: String) extends BaseDualPortedMemory (size, memfile) {
  def wireMemPipe(portio: MemPortBusIO): Unit = {
    portio.response.valid := false.B
    // Combinational memory is inherently always ready for port requests
    portio.request.ready := true.B
  }
  // Instruction port
  val r1 = RegInit(0.U)
  val passed_reg = RegInit(0.U)
  val r2 = RegInit(0.U(32.W))
  val r3 = RegInit(0.U(1.W))
  val r4 = RegInit(0.U(1.W))
 // val r2  = RegNext(io.rx_data)

  wireMemPipe(io.imem)

  when (io.imem.request.valid) {
    // Put the Request into the instruction pipe and signal that instruction memory is busy
    val request = io.imem.request.bits

    // We should only be expecting a read from instruction memory
    assert(request.operation === Read)
    // Check that address is pointing to a valid location in memory

    // TODO: Revert this back to the assert form "assert (request.address < size.U)"
    // TODO: once CSR is integrated into CPU
   //PAUL
    // when (request.address < size.U) {
      io.imem.response.valid := true.B
      io.imem.response.bits.data := memory(request.address >> 2)
    //} .otherwise {
     // io.imem.response.valid := false.B
   // }
  } .otherwise {
    io.imem.response.valid := false.B
  }

  // Data port

  wireMemPipe(io.dmem)

  val memAddress = io.dmem.request.bits.address
  val memWriteData = io.dmem.request.bits.writedata

  io.rx_data_done := r1
  r1 := 0.U
  io.tx_data := r2
  io.tx_wstrb := r3
  io.test_passed := passed_reg
  when (memAddress =/= 0x10000000.U) {
    r3 := 0.U // tx_wstrb
  }
  when (io.dmem.request.valid) {
    //printf("memaddress %x\n",memAddress)
    val request = io.dmem.request.bits

    // Check that non-combin write isn't being used
    assert (request.operation =/= Write)
    // Check that address is pointing to a valid location in memory
    //assert (request.address < size.U)

    // Read path
    when ( request.operation === Read && memAddress === 0x20000008.U) {
      printf("read path as well operation \n")
    }

   //printf("memory %x operation %d write val%d\n",memAddress, request.operation.asUInt(),io.dmem.request.bits.writedata)
    when ( memAddress === 0x20000000.U) {
      io.dmem.response.bits.data := io.rx_data

    }.elsewhen ( memAddress === 0x20000004.U) {
      io.dmem.response.bits.data := io.rx_rstrb
    }.otherwise {
      io.dmem.response.bits.data := memory.read(memAddress >> 2)
    }
    io.dmem.response.valid := true.B

    // Write path
    when (request.operation === ReadWrite) {
      when (memAddress === 0x20000008.U) {
        printf("write to address 200008 %d\n", memWriteData)
      }.elsewhen(memAddress === 0x20000010.U) {
        r1 := memWriteData
      }.elsewhen ( memAddress === 0x10000000.U) { // tx_data
        printf("write to address tx_data 10000 %d\n", io.dmem.request.bits.writedata)
        r2 :=  io.dmem.request.bits.writedata
        r3:= 1.U
      }.elsewhen ( memAddress === 0x30000000.U && memWriteData === 56789.U) {
        passed_reg := 1.U
        printf("test passed*************** %d\n",io.test_passed)
      }.otherwise {
        memory(memAddress >> 2) := memWriteData
      }
    }




  } .otherwise {
    io.dmem.response.valid := false.B
  }
}