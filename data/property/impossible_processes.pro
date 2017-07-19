(tag_existing
   (
      (ps process WHAT_ARE_YOU_DOING)
   )
   (forall sl1 waveform
      (CTL_verifies ps
         (not
            (EF (expr_writes sl1))
         )
      )
   )
)
