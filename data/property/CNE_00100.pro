(tag_existing
   (
      (wfm waveform CNE_00100_HAS_BAD_NAME)
   )
   (and
      ;; If the name of the waveform does not end in "_n",
      (not (string_matches [identifier [is_waveform_of wfm]] ".*_n"))
      ;; and there is a process
      (exists p1 process
         ;; that.
         (CTL_verifies p1
            ;; at some point,
            (EF
               (and
                  ;; has a condition which tests the the signal against '0'.
                  (kind "if")
                  (is_read_structure "(???)")
                  (is_read_element "0" "=")
                  (or
                     (and
                        (is_read_element "1" "'0'")
                        (is_read_element "2" wfm)
                     )
                     (and
                        (is_read_element "1" wfm)
                        (is_read_element "2" "'0'")
                     )
                  )
               )
            )
         )
      )
      ;; and that signal is never tested against '1'.
      (not
         (exists p2 process
            (CTL_verifies p2
               (EF
                  (and
                     (kind "if")
                     (is_read_structure "(???)")
                     (is_read_element "0" "=")
                     (or
                        (and
                           (is_read_element "1" "'1'")
                           (is_read_element "2" wfm)
                        )
                        (and
                           (is_read_element "1" wfm)
                           (is_read_element "2" "'1'")
                        )
                     )
                  )
               )
            )
         )
      )
   )
   ;; Then it is misnamed.
)
