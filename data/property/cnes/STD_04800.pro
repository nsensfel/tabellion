(tag_existing
   (
      (wfm waveform STD_04800_DOUBLE_SENSITIVITY_EDGE_CLOCK)
   )
   (and
      (exists ps_re process
         (CTL_verifies ps_re
            (EF
               (and
                  (is_read_structure "(??)")
                  (is_read_element "0" "rising_edge")
                  (is_read_element "1" wfm)
               )
            )
         )
      )
      (exists ps_fe process
         (CTL_verifies ps_fe
            (EF
               (and
                  (is_read_structure "(??)")
                  (is_read_element "0" "falling_edge")
                  (is_read_element "1" wfm)
               )
            )
         )
      )
   )
)
