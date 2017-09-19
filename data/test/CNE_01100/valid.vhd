library IEEE;

use IEEE.std_logic_1164.all;

entity valid is
   port
   (
      ip0: in std_logic; -- $SOL:0:0$
      b_i_ip1: in std_logic; -- $SOL:1:0$
      not_i_ip2: in std_logic; -- $SOL:2:0$
      o_ip3: in std_logic; -- $SOL:3:0$
      b_ip4: in std_logic; -- $SOL:4:0$
      op0: out std_logic; -- $SOL:5:0$
      b_o_op1: out std_logic; -- $SOL:6:0$
      not_o_op2: out std_logic; -- $SOL:7:0$
      i_op3: out std_logic; -- $SOL:8:0$
      b_op4: out std_logic; -- $SOL:9:0$
      bp0: inout std_logic; -- $SOL:10:0$
      o_b_bp1: inout std_logic; -- $SOL:11:0$
      not_b_bp2: inout std_logic; -- $SOL:12:0$
      i_bp3: inout std_logic; -- $SOL:13:0$
      o_bp4: inout std_logic -- $SOL:14:0$
   );
end;

architecture RTL of valid is
begin
end architecture;
