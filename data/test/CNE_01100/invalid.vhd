library IEEE;

use IEEE.std_logic_1164.all;

entity invalid is
   port
   (
      i_ip0: in std_logic;
      i_clock: in std_logic;
      i_o: in std_logic;
      i_o_reset: in std_logic;
      i_o_b_reset: in std_logic;
      i_i_reset: in std_logic;
      o_ip0: out std_logic;
      o_clock: out std_logic;
      o_o: out std_logic;
      o_i_reset: out std_logic;
      o_o_reset: out std_logic;
      o_o_b_reset: out std_logic;
      b_ip0: inout std_logic;
      b_clock: inout std_logic;
      b_o: inout std_logic;
      b_i_reset: inout std_logic;
      b_o_reset: inout std_logic;
      b_b_o_reset: inout std_logic
   );
end;

architecture RTL of invalid is
begin
end architecture;
