(tag_existing
   (
      (ent entity STRUCT_ENTITY)
      (reg waveform STRUCT_SIMPLE_FLIP_FLOP_OUTPUT)
      (clk waveform STRUCT_SIMPLE_FLIP_FLOP_CLOCK)
      (ps process STRUCT_SIMPLE_FLIP_FLOP_PROCESS)
   )
   (and
      (is_explicit_process ps)
;; debt: ))
;; Get ps instance
(exists i_ps ps_instance
   (and
      (is_visible_in i_ps ent)
      (is_ps_instance_of i_ps ps)
;; debt: )) ))
;; Get a local waveform matching a clk instance
(exists i_clk wfm_instance
   (and
      (process_instance_maps i_ps i_clk _)
;;      (is_visible_in i_clk ent)
      (is_wfm_instance_of i_clk clk)
      (exists local_clk waveform
         (and
            (process_instance_maps i_ps i_clk local_clk)
            (is_in_sensitivity_list local_clk ps)
;; debt: )))) ))))
;; Get a local waveform matching a reg instance
(exists i_reg wfm_instance
   (and
      (process_instance_maps i_ps i_reg _)
;;      (is_visible_in i_reg ent)
      (is_wfm_instance_of i_reg reg)
      (exists local_reg waveform
         (and
            (process_instance_maps i_ps i_reg local_reg)
;; debt: )))) ))))))))
;; Analyze ps using the local waveforms
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
               (is_read_element "1" local_clk)
            )
            (and
               (is_read_structure "(?(??)(???))")
               (is_read_element "0" "and")
               (is_read_element "1" "event")
               (is_read_element "2" local_clk)
               (is_read_element "3" "=")
               (or
                  (is_read_element "4" local_clk)
                  (is_read_element "5" local_clk)
               )
            )
            (and
               (is_read_structure "(?(???)(??))")
               (is_read_element "0" "and")
               (is_read_element "1" "=")
               (or
                  (is_read_element "2" local_clk)
                  (is_read_element "3" local_clk)
               )
               (is_read_element "4" "event")
               (is_read_element "5" local_clk)
            )
         )
         (EX
            (and
               (has_option "COND_WAS_TRUE")
               (does_not_reach_parent_before
                  (and
                     (expr_writes local_reg)
                     (AX
                        (not
                           (EF
                              (expr_writes local_reg)
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
))))))))))))
