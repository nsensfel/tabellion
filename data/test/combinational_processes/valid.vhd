library IEEE;

use IEEE.std_logic_1164.all;

entity valid is
   port
   (
      ip0, ip1, ip2, ip3:  in std_logic;
      op0, op1, op2, op3:  out std_logic
   );
end;

architecture RTL of valid is
   type enum_t is (V0, V1, V2, V3);
   signal s0, s1, s2, s3: std_logic;
   signal st0: enum_t;
   signal n0, n1, n2, n3: natural range 0 to 3;
begin
   s0 <= s1; -- $SOL:0:0$
   s0 <= (s1 and s2); -- $SOL:1:0$

   process (s0, s1) -- $SOL:2:0$
   begin
      case s1 is
         when '0' =>
            op0 <= s0;
         when others =>
            op0 <= s1;
      end case;
   end process;

   process (s0, s1) -- $SOL:3:0$
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

   process (s0, s1) -- $SOL:4:0$
   begin
      op2 <= '0';
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

   process (s0, s1, s2) -- $SOL:5:0$
   begin
      op2 <= '0';
      case s1 is
         when '0' =>
            if (s2 = '0')
            then
               op0 <= s0;
            else
               op0 <= s1;
            end if;
            op1 <= (s0 or s1);
         when others =>
            op1 <= (s1 or '0');
            op0 <= s1;
            op2 <= '1';
      end case;
   end process;

   with ip0 select -- $SOL:6:0$
      s1 <=
         ip1 when '0',
         ip2 when '1',
         ip3 when others;

   with st0 select -- $SOL:7:0$
      s2 <=
         ip1 when V0,
         ip2 when V1,
         ip3 when V2,
         s1 when V3;

   with st0 select -- $SOL:8:0$
      s2 <=
         ip1 when V0,
         ip2 when V1,
         ip3 when others;

   process (s0, s1, s2, s3) -- $SOL:9:0$
   begin
      case st0 is
         when V3 =>
            op0 <= s0;
         when V2 =>
            op0 <= s1;
         when V1 =>
            op0 <= s2;
         when V0 =>
            op0 <= s3;
      end case;
   end process;

   process (s0, s1, s2, s3) -- $SOL:10:0$
   begin
      case st0 is
         when V3 =>
            op0 <= s0;
         when V2 =>
            op0 <= s1;
         when others =>
            op0 <= s2;
      end case;
   end process;

   process (n0, n2) -- $SOL:11:0$
   begin
      case n0 is
         when 0 =>
            n1 <= 0;
         when 1 to 2 =>
            n1 <= n2;
         when 3 =>
            n1 <= 2;
      end case;
   end process;

   process (n0, n2) -- $SOL:12:0$
   begin
      case n0 is
         when 0 =>
            n1 <= 0;
         when 1 =>
            n1 <= n3;
         when 2 =>
            n1 <= n2;
         when 3 =>
            n1 <= 2;
      end case;
   end process;

   process (n0, n3) -- $SOL:13:0$
   begin
      case n0 is
         when 0 =>
            n1 <= 0;
         when 1 =>
            n1 <= n3;
         when others =>
            n1 <= n3;
      end case;
   end process;
end;
