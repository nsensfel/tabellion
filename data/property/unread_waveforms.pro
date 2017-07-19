(tag_existing
   (
      (wf waveform UNREAD_WAVEFORM)
   )
   (not
      (exists ps process
         (is_accessed_by wf ps)
      )
   )
)
