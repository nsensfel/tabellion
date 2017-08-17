(tag_existing
   (
      ;; waveform initialized using a reset.
      (i_wfm waveform CNE_04500_INITIALIZED_SIGNAL)
      ;; waveform not initialized using a reset.
      (ni_wfm waveform CNE_04500_NON_INITIALIZED_SIGNAL)
      (ps process CNE_04500_BAD_PROCESS)
   )
   (and
      (is_accessed_by i_wfm ps)
      (is_accessed_by ni_wfm ps)
      (not (eq ni_wfm i_wfm))
      (exists clk1 waveform
         (and
            (is_accessed_by clk1 ps)
            (not (eq clk1 i_wfm))
            (not (eq clk1 ni_wfm))
            (exists rst1 waveform
               (and
                  (is_accessed_by rst1 ps)
                  (not (eq rst1 i_wfm))
                  (not (eq rst1 ni_wfm))
                  (not (eq rst1 clk1))
                  (or
                     ;; With a synchronous reset.
                     (and
                        (not (is_in_sensitivity_list rst1 ps))
                        (CTL_verifies ps
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
                                             (kind "if")
                                             (is_read_structure "(???)")
                                             (is_read_element "0" "=")
                                             (is_read_element _ rst1)
                                             (EX
                                                (and
                                                   (or
                                                      (has_option "cond_was_true")
                                                      (has_option "cond_was_false")
                                                   )
                                                   (does_not_reach_parent_before
                                                      (and
                                                         (expr_writes i_wfm)
                                                         (not
                                                            (AX
                                                               (AF
                                                                  (expr_writes i_wfm)
                                                               )
                                                            )
                                                         )
                                                      )
                                                   )
                                                   (not
                                                      (does_not_reach_parent_before
                                                         (and
                                                            (expr_writes ni_wfm)
                                                            (not
                                                               (AX
                                                                  (AF
                                                                     (expr_writes ni_wfm)
                                                                  )
                                                               )
                                                            )
                                                         )
                                                      )
                                                   )
                                                )
                                             )
                                             (EX
                                                (and
                                                   (or
                                                      (has_option "cond_was_true")
                                                      (has_option "cond_was_false")
                                                   )
                                                   (does_not_reach_parent_before
                                                      (and
                                                         (expr_writes i_wfm)
                                                         (not
                                                            (AX
                                                               (AF
                                                                  (expr_writes i_wfm)
                                                               )
                                                            )
                                                         )
                                                      )
                                                   )
                                                   (does_not_reach_parent_before
                                                      (and
                                                         (expr_writes ni_wfm)
                                                         (not
                                                            (AX
                                                               (AF
                                                                  (expr_writes ni_wfm)
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
                     ;; With an asynchronous reset.
                     (and
                        (is_in_sensitivity_list rst1 ps)
                        (CTL_verifies ps
                           (AF
                              (and
                                 ;; if (rst = _)
                                 (kind "if")
                                 (is_read_structure "(???)")
                                 (is_read_element "0" "=")
                                 (is_read_element _ rst1)
                                 (EX
                                    (and
                                       ;; then
                                       (has_option "cond_was_true")
                                       (does_not_reach_parent_before
                                          (and
                                             (expr_writes i_wfm)
                                             (not
                                                (AX
                                                   (AF
                                                      (expr_writes i_wfm)
                                                   )
                                                )
                                             )
                                          )
                                       )
                                       (not
                                          (does_not_reach_parent_before
                                             (and
                                                (expr_writes ni_wfm)
                                                (not
                                                   (AX
                                                      (AF
                                                         (expr_writes ni_wfm)
                                                      )
                                                   )
                                                )
                                             )
                                          )
                                       )
                                    )
                                 )
                                 (EX
                                    (and
                                       ;; else
                                       (not (has_option "cond_was_true"))
                                       (does_not_reach_parent_before
                                          (and
                                             ;; if (edge(clk))
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
                                                         (expr_writes i_wfm)
                                                         (not
                                                            (AX
                                                               (AF
                                                                  (expr_writes i_wfm)
                                                               )
                                                            )
                                                         )
                                                      )
                                                   )
                                                   (does_not_reach_parent_before
                                                      (and
                                                         (expr_writes ni_wfm)
                                                         (not
                                                            (AX
                                                               (AF
                                                                  (expr_writes ni_wfm)
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
