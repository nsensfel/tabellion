(tag_existing
   (
      (clock port LIKELY_A_CLOCK)
   )
   (exists wf waveform
      (and
         (is_waveform_of wf clock)
         (exists ps process
            (CTL_verifies ps
               (EF
                  (and
                     (kind "if")
                     (is_read_structure "(??)")
                     (or
                        (is_read_element "0" "rising_edge")
                        (is_read_element "0" "falling_edge")
                     )
                     (is_read_element "1" wf)
                  )
               )
            )
         )
      )
   )
)
