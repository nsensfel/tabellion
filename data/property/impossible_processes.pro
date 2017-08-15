(tag_existing
   (
      (ps process WHAT_ARE_YOU_DOING)
   )
   (CTL_verifies ps
      (not
         (EF (expr_writes _))
      )
   )
)
