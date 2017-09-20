library IEEE;

use IEEE.std_logic_1164.all;

entity valid is
   generic
   (
      clock_speed0: std_logic; -- $SOL:0:0$
      clock_speed1: std_logic := '0'; -- $SOL:1:0$
      clock_g_speed2: natural; -- $SOL:2:0$
      clock_g_speed3: natural := 3; -- $SOL:3:0$
      i_g_g_param : std_logic -- $SOL:4:0$
   );
end;

architecture RTL of valid is
begin
end architecture;
