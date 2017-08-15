(tag_existing
   (
      (wf waveform INCREMENTED_WAVEFORM)
      (ps process INCREMENTER_PROCESS)
   )
   (CTL_verifies ps
      (EF
         (and
            (is_read_structure "(???)")
            (is_read_element "0" "+")
            (is_read_element _ wf)
            (is_read_element _ "L")
         )
      )
   )
)
