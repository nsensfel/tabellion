(tag_existing
   (
      (x_re waveform CNE_01700_HAS_BAD_NAME)
   )
   (and
      (not (string_matches [identifier [is_waveform_of x_re]] ".*_re"))
      (exists x waveform
         (and
            (not (eq x x_re))
            (exists x_r1 waveform
               (and
                  (not (eq x_r1 x_re))
                  (not (eq x_r1 x))
                  (exists ps2 process
                     (and
                        (is_in_sensitivity_list x_r1 ps2)
                        (is_in_sensitivity_list x ps2)
                        (is_accessed_by x_re ps2)
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
                  (exists ps1 process
                     (and
                        (is_accessed_by x_r1 ps1)
                        (is_accessed_by x ps1)
                        (is_explicit_process ps1)
                        (exists clk1 waveform
                           (and
                              (is_in_sensitivity_list clk1 ps1)
                              (not (eq clk1 x_re))
                              (not (eq clk1 x))
                              (not (eq clk1 x_r1))
                              (or
                                 (CTL_verifies ps1
                                    (AF
                                       (and
                                          (kind "if")
                                          (or
                                             (and
                                                (is_read_structure "(??)")
                                                (or
                                                   (is_read_element "0" "falling_edge")
                                                   (is_read_element "0" "rising_edge")
                                                )
                                                (is_read_element "1" clk1)
                                             )
                                             (and
                                                (is_read_structure "(?(??)(???))")
                                                (is_read_element "0" "and")
                                                (is_read_element "1" "event")
                                                (is_read_element "2" clk1)
                                                (is_read_element "3" "=")
                                                (or
                                                   (is_read_element "4" clk1)
                                                   (is_read_element "5" clk1)
                                                )
                                             )
                                             (and
                                                (is_read_structure "(?(???)(??))")
                                                (is_read_element "0" "and")
                                                (is_read_element "1" "=")
                                                (or
                                                   (is_read_element "2" clk1)
                                                   (is_read_element "3" clk1)
                                                )
                                                (is_read_element "4" "event")
                                                (is_read_element "5" clk1)
                                             )
                                          )
                                          (EX
                                             (and
                                                (has_option "cond_was_true")
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
                                       (not (eq rst1 x_re))
                                       (not (eq rst1 x))
                                       (not (eq rst1 x_r1))
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
                                                      (or
                                                         (and
                                                            (is_read_structure "(??)")
                                                            (or
                                                               (is_read_element "0" "falling_edge")
                                                               (is_read_element "0" "rising_edge")
                                                            )
                                                            (is_read_element "1" clk1)
                                                         )
                                                         (and
                                                            (is_read_structure "(?(??)(???))")
                                                            (is_read_element "0" "and")
                                                            (is_read_element "1" "event")
                                                            (is_read_element "2" clk1)
                                                            (is_read_element "3" "=")
                                                            (or
                                                               (is_read_element "4" clk1)
                                                               (is_read_element "5" clk1)
                                                            )
                                                         )
                                                         (and
                                                            (is_read_structure "(?(???)(??))")
                                                            (is_read_element "0" "and")
                                                            (is_read_element "1" "=")
                                                            (or
                                                               (is_read_element "2" clk1)
                                                               (is_read_element "3" clk1)
                                                            )
                                                            (is_read_element "4" "event")
                                                            (is_read_element "5" clk1)
                                                         )
                                                      )
                                                      (EX
                                                         (and
                                                            (has_option "cond_was_true")
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
                  )
               )
            )
         )
      )
   )
)
