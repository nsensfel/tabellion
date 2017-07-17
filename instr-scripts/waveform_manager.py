class Waveform_Manager:
    def __init__ (self, output_file, id_manager):
        self.output = output_file
        self.from_source = dict()
        self.to_source = dict()
        self.id_manager = id_manager

    def generate_new_waveform (self, source_id):
        result = self.id_manager.generate_new_pure_id()
        self.from_source[source_id] = result
        self.to_source[result] = source_id

        self.output.write(
            "(map_waveform "
            + result
            + " "
            + source_id
            + ")\n"
        )

        return result

    def get_waveform_from_source (self, source_id):
        result = self.from_source.get(source_id)

        if (result == None):
            return self.generate_new_waveform(source_id)
        else:
            return result

    def get_source_of_waveform (self, wfm_id):
        result = self.to_source.get(wfm_id)

        return result
