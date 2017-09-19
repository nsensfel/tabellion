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
   type enum_t is (V0, V1, V2, V3);
   signal s0, s1, s2, s3: std_logic;
   signal st0: enum_t;
   signal n0, n1, n2, n3: natural range 0 to 3;
begin
   P_s: process (s0, s1)
   begin
      case s1 is
         when '0' =>
            op0 <= s0;
         when others =>
            op0 <= s1;
      end case;
   end process;

   P_P_P_E: process (s0, s1) -- $SOL:1:0$
   begin
      case s1 is
         when '0' =>
            op0 <= s0;
            op1 <= (s0 or s1);
         when others =>
            op1 <= (s1 or '0');
            Nope: op0 <= s1;
      end case;
   end process;

   P_P: process (s0, s1)
   begin
      op2 <= '0';
      P_PP:
      case s1 is
         when '0' =>
            op0 <= s0;
            op1 <= (s0 or s1);
         when others =>
            op1 <= (s1 or '0');
            op0 <= s1;
            op2 <= '1';
      end case;
   end process;

   What: with ip0 select
      s1 <=
         ip1 when '0',
         ip2 when '1',
         ip3 when others;

   with st0 select
      s2 <=
         ip1 when V0,
         ip2 when V1,
         ip3 when V2,
         s1 when V3;

   P_but_maybe_still: with st0 select
      s2 <=
         ip1 when V0,
         ip2 when V1,
         ip3 when others;

   P_oops: process (s0, s1, s2, s3)
   begin
      case st0 is
         when V3 =>
            Cheese: op0 <= s0;
         when V2 =>
            May: op0 <= s1;
         when V1 =>
            Be: op0 <= s2;
         when V0 =>
            Available: op0 <= s3;
      end case;
   end process;
end;
