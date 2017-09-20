(tag_existing
   (
      (pt port CNE_01100_BAD_NAME)
   )
   (exists pt2 port
      (and
;; Goes into infinity:
;;       (has_mode pt2 _)
;;       (has_mode pt2 "in")
;; Fixes all:
         (eq pt2 pt)
         (not
            (or
               (and
                  (string_matches [identifier pt] "^i_.*")
                  (has_mode pt "in")
               )
               (and
                  (string_matches [identifier pt] "^o_.*")
                  (has_mode pt "out")
               )
               (and
                  (string_matches [identifier pt] "^b_.*")
                  (has_mode pt "inout")
               )
            )
         )
      )
   )
)
