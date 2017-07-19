(tag_existing
   (
      (ps process STRUCT_COMBINATIONAL_PROCESS)
   )
   (forall sl1 waveform
      (and
         (implies
            (exists target waveform
               (CTL_verifies ps
                  (EF
                     (and
                        (expr_reads sl1)
                        (expr_writes target)
                        (not
                           (AX
                              (AF
                                 (expr_writes target)
                              )
                           )
                        )
                     )
                  )
               )
            )
            (is_in_sensitivity_list sl1 ps)
         )
         (CTL_verifies ps
            (implies
               (EF (expr_writes sl1))
               (AF (expr_writes sl1))
            )
         )
      )
   )
)
