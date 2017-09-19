library IEEE;

use IEEE.std_logic_1164.all;

entity valid is
   port
   (
      ip0: in std_logic;
      ip1: in std_logic;
      ip2: in std_logic;
      ip3: in std_logic;
      op0: out std_logic;
      op1: out std_logic;
      op2: out std_logic;
      op3: out std_logic
   );
end;

architecture RTL of valid is
   signal s0: std_logic;
   signal s1: std_logic;
   signal s2: std_logic;
   signal s3: std_logic;
begin
   op0 <= ip1 when (ip0 = '0') else ip2;
   op0 <= ip1 when (ip0 = '1') else ip2;

   process (s1, ip1, s2)
   begin
      if (ip1 = '0')
      then
         op0 <= '0';
      else
         op0 <= s2;
      end if;

      if (ip1 = '0')
      then
         op1 <= s1;
      else
         op1 <= '1';
      end if;
   end process;

   process (s1, ip1, s2)
   begin
      if (ip1 = '1')
      then
         op0 <= '0';
      else
         op0 <= s2;
      end if;

      if (ip1 = '0')
      then
         op1 <= s1;
      else
         op1 <= '1';
      end if;
   end process;

   s3 <= s1 when (s0 = '0') else s2;
   s3 <= s1 when (s0 = '1') else s2;

   process (s1, ip1, s2)
   begin
      if (s1 = '0')
      then
         op0 <= '0';
      else
         op0 <= s2;
      end if;

      if (s1 = '0')
      then
         op1 <= ip1;
      else
         op1 <= '1';
      end if;
   end process;

   process (s1, ip1, s2)
   begin
      if (s1 = '1')
      then
         op0 <= '0';
      else
         op0 <= s2;
      end if;

      if (s1 = '0')
      then
         op1 <= ip1;
      else
         op1 <= '1';
      end if;
   end process;
end architecture;
