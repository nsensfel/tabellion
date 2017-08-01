(tag_existing
   (
      (wfm waveform CNE_00100_HAS_BAD_NAME)
   )
   (and
      (not (string_matches [identifier [is_waveform_of wfm]] ".*_n"))
      (exists p1 process
         (CTL_verifies p1
            (EF
               (and
                  (kind "if")
                  (is_read_structure "(???)")
                  (is_read_element "0" "=")
                  (or
                     (and
                        (is_read_element "1" "'0'")
                        (IS_READ_Element "2" wfm)
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
      )
   )
)
