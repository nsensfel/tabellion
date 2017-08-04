(tag_existing
   (
      (reg waveform STRUCT_SIMPLE_FLIP_FLOP_OUTPUT)
      (clk waveform STRUCT_SIMPLE_FLIP_FLOP_CLOCK)
      (ps process STRUCT_SIMPLE_FLIP_FLOP_PROCESS)
   )
   (and
      (is_explicit_process ps)
      (is_in_sensitivity_list clk ps)
      (CTL_verifies ps
         (AF
            (and
               (kind "if")
               (is_read_structure "(??)")
               (or
                  (is_read_element "0" "falling_edge")
                  (is_read_element "0" "rising_edge")
               )
               (is_read_element "1" clk)
               (EX
                  (and
                     (has_option "COND_WAS_TRUE")
                     (does_not_reach_parent_before
                        (and
                           (expr_writes reg)
                           (AX
                              (not
                                 (EF
                                    (expr_writes reg)
                                 )
                              )
                           )
                        )
                     )
                  )
               )
            )
         )
      )
   )
)
