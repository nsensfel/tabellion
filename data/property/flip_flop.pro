#require "simple_flip_flop"
#require "async_reset_flip_flop"

(tag_existing
   (
      (reg waveform STRUCT_FLIP_FLOP_OUTPUT)
      (clk waveform STRUCT_FLIP_FLOP_CLOCK)
      (ps process STRUCT_FLIP_FLOP_PROCESS)
   )
   (and
      (not (eq reg clk))
;;      (is_accessed_by reg ps)
;;      (is_accessed_by clk ps)
      (or
         (_simple_flip_flop reg clk ps)
         (_async_reset_flip_flop reg clk _ ps)
      )
   )
)
