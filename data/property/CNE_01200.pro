(tag_existing
   (
      (ps process CNE_01200_BAD_NAME)
   )
   (and
      (is_explicit_process ps)
      (not
         (and
            (has_label ps)
            (string_matches [label ps] "^P_.*")
         )
      )
   )
)
