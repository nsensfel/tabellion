(tag_existing
   (
      (ps process STRUCT_COMBINATIONAL_PROCESS)
   )
   (forall sl1 signal
      (and
         (CTL_verifies ps
            (implies
               (EF (expr_writes sl1))
               (AF (expr_writes sl1))
            )
         )
         (implies
            (exists target signal
               (CTL_verifies ps
                  (EF
                     (and
                        (expr_reads sl1)
                        (not
                           (and
                              (expr_writes target)
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
            )
            (is_in_sensitivity_list ps sl1)
         )
      )
   )
)
