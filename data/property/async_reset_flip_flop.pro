(tag_existing
   (
      (reg waveform STRUCT_ASYNC_RST_FLIP_FLOP_OUTPUT)
      (clk waveform STRUCT_ASYNC_RST_FLIP_FLOP_CLOCK)
      (rst waveform STRUCT_ASYNC_RST_FLIP_FLOP_CLOCK)
      (ps process STRUCT_ASYNC_RST_FLIP_FLOP_PROCESS)
   )
   (and
      (not (eq clk reg))
      (not (eq rst reg))
      (not (eq rst clk))
      (is_explicit_process ps)
      (is_in_sensitivity_list clk ps)
      (is_in_sensitivity_list rst ps)
      (CTL_verifies ps
         (AU
            (not (expr_writes reg))
            (and
               (kind "if")
               (is_read_structure "(???)")
               (is_read_element "0" "=")
               (is_read_element _ rst)
               (EX
                  (and
                     (not (has_option "cond_was_true"))
                     (or
                        (and
                           (is_read_structure "(??)")
                           (or
                              (is_read_element "0" "falling_edge")
                              (is_read_element "0" "rising_edge")
                           )
                           (is_read_element "1" clk)
                        )
                        (and
                           (is_read_structure "(?(??)(???))")
                           (is_read_element "0" "and")
                           (is_read_element "1" "event")
                           (is_read_element "2" clk)
                           (is_read_element "3" "=")
                           (or
                              (is_read_element "4" clk)
                              (is_read_element "5" clk)
                           )
                        )
                        (and
                           (is_read_structure "(?(???)(??))")
                           (is_read_element "0" "and")
                           (is_read_element "1" "=")
                           (or
                              (is_read_element "2" clk)
                              (is_read_element "3" clk)
                           )
                           (is_read_element "4" "event")
                           (is_read_element "5" clk)
                        )
                     )
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
   )
)
