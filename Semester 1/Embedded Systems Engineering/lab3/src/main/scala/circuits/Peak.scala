package circuits
import Chisel.RegNext
import chisel3._
import chisel3.util._



  class PeakIO extends DecoupledIO(UInt(8.W)) {
    override def cloneType: this.type = new PeakIO().asInstanceOf[this.type]
  }

  class Peak extends Module {
    val io = IO(new Bundle {
      val channel = Flipped(new PeakIO())
      val cnt = Output(UInt(8.W))
      val pr = Output(UInt(8.W))
      val st = Output(UInt(8.W))
    })
    val data = RegInit(0.U(8.W))
    val prev = RegInit(0.U(8.W))
    var count = RegInit(0.U(8.W))
    val zero :: one :: two:: Nil= Enum(3)
    val state = RegInit(zero)


    io.pr := prev
    io.st :=state
    io.channel.ready := 1.U
    when (io.channel.valid) {
      data:= io.channel.bits
     // prev := RegNext(data)
      prev := RegNext(io.channel.bits)
      printf("bits: %d data %d prev: %d count: %d state: %d \n", io.channel.bits, data, prev, count,state)
      switch(state) {
        is (zero){
          printf("zero\n")
          state := one
        }
        is(one) {
          printf("one\n")
          when(prev < io.channel.bits) {
            printf("prev<bits\n")
            state := two
           // prev := io.channel.bits
          }/*.otherwise {
            prev := io.channel.bits
          }*/
        }
        is(two) {
          printf("two\n")
          when(prev > io.channel.bits) {
            printf("prev>bits\n")
            state := one
            count := count + 1.U
            //prev := io.channel.bits
          }/*.otherwise {
            prev := io.channel.bits
          }*/

        }

      }
    }
    io.cnt:=count
  }

