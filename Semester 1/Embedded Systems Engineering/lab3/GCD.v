module GCD(
  input         clock,
  input         reset,
  input  [31:0] io_a,
  input  [31:0] io_b,
  input         io_e,
  output [31:0] io_z,
  output        io_v
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
`endif // RANDOMIZE_REG_INIT
  reg [31:0] x; // @[GCD.scala 14:16]
  reg [31:0] y; // @[GCD.scala 15:16]
  wire [31:0] _x_T_1 = x - y; // @[GCD.scala 17:14]
  wire [31:0] _y_T_1 = y - x; // @[GCD.scala 19:14]
  assign io_z = x; // @[GCD.scala 25:10]
  assign io_v = y == 32'h0; // @[GCD.scala 26:15]
  always @(posedge clock) begin
    if (io_e) begin // @[GCD.scala 21:17]
      x <= io_a; // @[GCD.scala 22:9]
    end else if (x > y) begin // @[GCD.scala 16:18]
      x <= _x_T_1; // @[GCD.scala 17:9]
    end
    if (io_e) begin // @[GCD.scala 21:17]
      y <= io_b; // @[GCD.scala 23:9]
    end else if (!(x > y)) begin // @[GCD.scala 16:18]
      y <= _y_T_1; // @[GCD.scala 19:9]
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  x = _RAND_0[31:0];
  _RAND_1 = {1{`RANDOM}};
  y = _RAND_1[31:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
