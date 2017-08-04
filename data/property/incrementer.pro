(tag_existing
   (
      (wf waveform INCREMENTED_WAVEFORM)
      (ps process INCREMENTER_PROCESS)
   )
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
