#require "flip_flop"

(tag_existing
   (
      (x_re waveform CNE_01700_HAS_BAD_NAME)
   )
   (and
      (not (string_matches [identifier [is_waveform_of x_re]] ".*_re$"))
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
      (_flip_flop x_r1 _ ps1)
      (CTL_verifies ps1
         (EF
            (and
               (expr_writes x_r1)
               (is_read_structure "?")
               (is_read_element "0" x)
            )
         )
      )
   )
)
))))))
