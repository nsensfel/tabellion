(tag_existing
   (
      (clock port LIKELY_A_CLOCK)
   )
   (exists wf waveform
      (and
         (is_waveform_of wf clock)
         (exists ps process
            (and
               (is_accessed_by wf ps)
               (is_in_sensitivity_list wf ps)
               (CTL_verifies ps
                     (EF
                        (and
                           (kind "if")
                           (or
                              (and
                                 (is_read_structure "(??)")
                                 (or
                                    (is_read_element "0" "falling_edge")
                                    (is_read_element "0" "rising_edge")
                                 )
                                 (is_read_element "1" wf)
                              )
                              (and
                                 (is_read_structure "(?(??)(???))")
                                 (is_read_element "0" "and")
                                 (is_read_element "1" "event")
                                 (is_read_element "2" wf)
                                 (is_read_element "3" "=")
                                 (or
                                    (is_read_element "4" wf)
                                    (is_read_element "5" wf)
                                 )
                              )
                              (and
                                 (is_read_structure "(?(???)(??))")
                                 (is_read_element "0" "and")
                                 (is_read_element "1" "=")
                                 (or
                                    (is_read_element "2" wf)
                                    (is_read_element "3" wf)
                                 )
                                 (is_read_element "4" "event")
                                 (is_read_element "5" wf)
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
