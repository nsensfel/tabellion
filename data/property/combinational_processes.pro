(tag_existing
   (
      (ps process STRUCT_COMBINATIONAL_PROCESS)
   )
   (forall sl1 waveform
      (and
         (implies
            ;; If a signal is read,
            (exists target waveform
               (CTL_verifies ps
                  (EF
                     (is_read_element _ sl1)
                  )
               )
            )
            ;; Then it must be in the sensitivity list.
            (is_in_sensitivity_list sl1 ps)
         )
         (CTL_verifies ps
            (implies
               ;; If a signal is somehow assigned by this process,
               (EF (expr_writes sl1))
               ;; Then it is assigned at every execution of the process.
               (AF (expr_writes sl1))
            )
         )
      )
   )
)
