(tag_existing
   (
      (wfm waveform STD_04800_DOUBLE_SENSITIVITY_EDGE_CLOCK)
   )
   (and
      (exists ps_re process
         (CTL_verifies ps_re
            (EF
               (or
                  (and
                     (is_read_structure "(??)")
                     (is_read_element "0" "rising_edge")
                     (is_read_element "1" wfm)
                  )
                  (and
                     (is_read_structure "(?(??)(???))")
                     (is_read_element "0" "and")
                     (is_read_element "1" "event")
                     (is_read_element "2" wfm)
                     (is_read_element "3" "=")
                     (or
                        (and
                           (is_read_element "4" wfm)
                           (is_read_element "5" "'1'")
                        )
                        (and
                           (is_read_element "4" "'1'")
                           (is_read_element "5" wfm)
                        )
                     )
                  )
                  (and
                     (is_read_structure "(?(???)(??))")
                     (is_read_element "0" "and")
                     (is_read_element "1" "=")
                     (or
                        (and
                           (is_read_element "2" wfm)
                           (is_read_element "3" "'1'")
                        )
                        (and
                           (is_read_element "2" "'1'")
                           (is_read_element "3" wfm)
                        )
                     )
                     (is_read_element "4" "event")
                     (is_read_element "5" wfm)
                  )
               )
            )
         )
      )
      (exists ps_fe process
         (CTL_verifies ps_fe
            (EF
               (or
                  (and
                     (is_read_structure "(??)")
                     (is_read_element "0" "falling_edge")
                     (is_read_element "1" wfm)
                  )
                  (and
                     (is_read_structure "(?(??)(???))")
                     (is_read_element "0" "and")
                     (is_read_element "1" "event")
                     (is_read_element "2" wfm)
                     (is_read_element "3" "=")
                     (or
                        (and
                           (is_read_element "4" wfm)
                           (is_read_element "5" "'0'")
                        )
                        (and
                           (is_read_element "4" "'0'")
                           (is_read_element "5" wfm)
                        )
                     )
                  )
                  (and
                     (is_read_structure "(?(???)(??))")
                     (is_read_element "0" "and")
                     (is_read_element "1" "=")
                     (or
                        (and
                           (is_read_element "2" wfm)
                           (is_read_element "3" "'0'")
                        )
                        (and
                           (is_read_element "2" "'0'")
                           (is_read_element "3" wfm)
                        )
                     )
                     (is_read_element "4" "event")
                     (is_read_element "5" wfm)
                  )
               )
            )
         )
      )
   )
)
