library IEEE;

use IEEE.std_logic_1164.all;

entity invalid is
   generic
   (
      g_clock_speed0: std_logic;
      g_clock_speed1: std_logic := '0';
      g_clock_speed2: natural;
      g_clock_speed3: natural := 3;
      g_g_param : std_logic
   );
end;

architecture RTL of invalid is
begin
end architecture;
