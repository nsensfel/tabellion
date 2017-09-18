library IEEE;

use IEEE.std_logic_1164.all;

entity invalid is
   port
   (
      ip0, ip1, ip2, ip3:  in std_logic;
      op0, op1, op2, op3:  out std_logic
   );
end;

architecture RTL of invalid is
   signal s0, s1, s2, s3 : std_logic;
begin
   -- Missing:
   -- - s1 in sensitivity list.
   process (s0, s3)
   begin
      case s1 is
         when '0' =>
            op0 <= s0;
         when others =>
            op0 <= s1;
      end case;
   end process;

   -- Missing:
   -- - s1 in sensitivity list.
   -- - s0 in sensitivity list.
   process (ip3)
   begin
      case s1 is
         when '0' =>
            op0 <= s0;
            op1 <= (s0 or s1);
         when others =>
            op1 <= (s1 or '0');
            op0 <= s1;
      end case;
   end process;

   -- Missing
   -- - op1 not defined when (s1 != '0').
   process (s0, s1)
   begin
      op2 <= '0';
      case s1 is
         when '0' =>
            op0 <= s0;
            op1 <= (s0 or s1);
         when others=>
            op0 <= s1;
            op2 <= '1';
      end case;
   end process;

   -- Missing
   -- - op0 not defined when ((s1 == '0') && (s2 = '0')).
   process (s0, s1, s2)
   begin
      op2 <= '0';
      case s1 is
         when '0' =>
            if (s2 = '0')
            then
               op0 <= s0;
            else
               op1 <= s1;
            end if;
            op1 <= (s0 or s1);
         when others =>
            op1 <= (s1 or '0');
            op0 <= s1;
            op2 <= '1';
      end case;
   end process;
end;
