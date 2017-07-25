(tag_existing
   (
      (wf waveform INCREMENTED_WAVEFORM)
   )
   (exists ps process
      (CTL_verifies ps
         (EF
            (and
               (is_read_structure "(??L)")
               (is_read_element "0" "+")
               (is_read_element "1" wf)
            )
         )
      )
   )
)
