(tag_existing
   (
      (x_r waveform CNE_01900_HAS_BAD_NAME)
   )
   (and
      (not (string_matches [identifier [is_waveform_of x_r]] ".*_r[0-9]*"))
      (exists x waveform
         (and
            (not (eq x x_r))
            (exists ps1 process
               (and
                  (is_accessed_by x_r ps1)
                  (is_accessed_by x ps1)
                  (is_explicit_process ps1)
                  (exists clk1 waveform
                     (and
                        (is_in_sensitivity_list clk1 ps1)
                        (not (eq clk1 x_r))
                        (not (eq clk1 x))
                        (or
                           (CTL_verifies ps1
                              (AF
                                 (and
                                    (kind "if")
                                    (is_read_structure "(??)")
                                    (or
                                       (is_read_element "0" "falling_edge")
                                       (is_read_element "0" "rising_edge")
                                    )
                                    (is_read_element "1" clk1)
                                    (EX
                                       (and
                                          (has_option "cond_was_true")
                                          (does_not_reach_parent_before
                                             (and
                                                (expr_writes x_r)
                                                (is_read_structure "?")
                                                (is_read_element "0" x)
                                                (AX
                                                   (not
                                                      (EF
                                                         (expr_writes x_r)
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
                           (exists rst1 waveform
                              (and
                                 (is_in_sensitivity_list rst1 ps1)
                                 (not (eq rst1 x_r))
                                 (not (eq rst1 x))
                                 (not (eq rst1 clk1))
                                 (CTL_verifies ps1
                                    (AF
                                       (and
                                          (kind "if")
                                          (is_read_structure "(???)")
                                          (is_read_element "0" "=")
                                          (is_read_element _ rst1)
                                          (EX
                                             (and
                                                (not (has_option "cond_was_true"))
                                                (kind "if")
                                                (is_read_structure "(??)")
                                                (or
                                                   (is_read_element "0" "falling_edge")
                                                   (is_read_element "0" "rising_edge")
                                                )
                                                (is_read_element "1" clk1)
                                                (EX
                                                   (and
                                                      (has_option "cond_was_true")
                                                      (does_not_reach_parent_before
                                                         (and
                                                            (expr_writes x_r)
                                                            (is_read_structure "?")
                                                            (is_read_element "0" x)
                                                            (AX
                                                               (not
                                                                  (EF
                                                                     (expr_writes x_r)
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
)
