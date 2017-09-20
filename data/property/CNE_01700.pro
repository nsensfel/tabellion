(tag_existing
   (
      (x_re waveform CNE_01700_HAS_BAD_NAME)
   )
   (and
      (not (string_matches [identifier [is_waveform_of x_re]] ".*_re"))
;; 'x' is the signal 'x_re' detects the rising edge of.
(exists x waveform
   (and
      (not (eq x x_re))
;; 'x_r1' is 'x' delayed by a flipflop
(exists x_r1 waveform
   (and
      (not (eq x_r1 x_re))
      (not (eq x_r1 x))
;; 'ps2' is the process describing 'x_re' as the rising edge
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
                  ;; x_re <= (not x_r1) and x
                  (and
                     (is_read_structure "(?(??)?)")
                     (is_read_element "1" "not")
                     (is_read_element "2" x_r1)
                     (is_read_element "3" x)
                  )
                  ;; x_re <= x and (not x_r1)
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
;; 'ps1' is the process describing 'x_r1' as a flipflop.
(exists ps1 process
   (and
      (is_accessed_by x_r1 ps1)
      (is_accessed_by x ps1)
      (is_explicit_process ps1)
;; 'clk1' is the clock controlling that flipflop
(exists clk1 waveform
   (and
      (is_in_sensitivity_list clk1 ps1)
      (not (eq clk1 x_re))
      (not (eq clk1 x))
      (not (eq clk1 x_r1))
      (or
;; 'ps1' could be a simple flipflop
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
;; 'ps1' could be a flipflop with reset
;; 'rst1' is that reset signal
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
