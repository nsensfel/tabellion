(tag_existing
   (
      (x_re waveform CNE_01700_HAS_BAD_NAME)
   )
   (and
      (not (string_matches [identifier x_re] ".*_re"))
      (exists x waveform
         (exists x_r1 waveform
            (and
               (exists clk1 waveform
                  (exists ps1 process
                     (and
                        (is_explicit_process ps1)
                        (is_in_sensitivity_list clk1 ps1)
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
                                          (has_option "COND_WAS_TRUE")
                                          (does_not_reach_parent_before
                                             (and
                                                (expr_writes x_r1)
                                                (is_read_structure "?")
                                                (is_read_element "0" x)
                                                (AX
                                                   (not
                                                      (EF
                                                         (expr_writes x_r1)
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
                                 (CTL_verifies ps1
                                    (AF
                                       (and
                                          (kind "if")
                                          (is_read_structure "(???)")
                                          (is_read_element "0" "=")
                                          (is_read_element _ rst1)
                                          (EX
                                             (and
                                                (not (has_option "COND_WAS_TRUE"))
                                                (kind "if")
                                                (is_read_structure "(??)")
                                                (or
                                                   (is_read_element "0" "falling_edge")
                                                   (is_read_element "0" "rising_edge")
                                                )
                                                (is_read_element "1" clk1)
                                                (EX
                                                   (and
                                                      (has_option "COND_WAS_TRUE")
                                                      (does_not_reach_parent_before
                                                         (and
                                                            (expr_writes x_r1)
                                                            (is_read_structure "?")
                                                            (is_read_element "0" x)
                                                            (AX
                                                               (not
                                                                  (EF
                                                                     (expr_writes x_r1)
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
               (exists ps2 process
                  (and
                     (is_in_sensitivity_list x_r1 ps2)
                     (is_in_sensitivity_list x ps2)
                     (CTL_verifies ps2
                        (AF
                           (and
                              (expr_writes x_re)
                              (is_read_element "0" "and")
                              (or
                                 (and
                                    (is_read_structure "(?(??)?)")
                                    (is_read_element "1" "not")
                                    (is_read_element "2" x_r1)
                                    (is_read_element "3" x)
                                 )
                                 (and
                                    (is_read_structure "(??(??))")
                                    (is_read_element "1" x)
                                    (is_read_element "2" "not")
                                    (is_read_element "3" x_r1)
                                 )
                              )
                              (AX
                                 (not
                                    (EF
                                       (expr_writes x_r1)
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
