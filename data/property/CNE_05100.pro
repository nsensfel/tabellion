(tag_existing
   (
      (wfm waveform STRUCT_SINGLE_PROCESS_MULTIPLEXOR)
   )
   (exists ps process
      (and
         (is_explicit_process ps)
         (forall sl1 waveform
            (and
               (CTL_verifies ps
                  (implies
                     (EF (expr_writes sl1))
                     (AF (expr_writes sl1))
                  )
               )
               (implies
                  (exists target waveform
                     (CTL_verifies ps
                        (EF
                           (and
                              (is_read_element _ sl1)
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
                  (is_in_sensitivity_list sl1 ps)
               )
            )
         )
         (CTL_verifies ps
            (AF
               (and
                  (or
                     (kind "case")
                     (kind "if")
                  )
                  (does_not_reach_parent_before
                     (and
                        (expr_writes wfm)
                        (AX
                           (not
                              (EF
                                 (expr_writes wfm)
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
