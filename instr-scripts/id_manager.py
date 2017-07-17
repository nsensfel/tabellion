class Id_Manager:
    def __init__ (self, output_file, starting_index):
        self.output = output_file
        self.next_id = starting_index
        self.xml_to_id = dict()
        self.id_to_xml = dict()
        self.string_to_id = dict()
        self.id_to_string = dict()

    def generate_new_pure_id (self):
        result = str(self.next_id)
        self.next_id += 1

        return result

    def generate_new_id (self, xml_id):
        result = str(self.next_id)
        self.next_id += 1
        self.xml_to_id[xml_id] = result
        self.id_to_xml[result] = xml_id

        self.output.write(
            "(map_xml_id "
            + xml_id
            + " "
            + result
            + ")\n"
        )

        return result

    def generate_new_id_for_string (self, string):
        result = str(self.next_id)
        self.next_id += 1
        self.string_to_id[string] = result
        self.id_to_string[result] = string

        self.output.write(
            "(map_string \""
            + string
            + "\" "
            + result
            + ")\n"
        )

        return result

    def get_id_from_string (self, string):
        result = self.string_to_id.get(string)

        if (result == None):
            return self.generate_new_id_for_string(string)
        else:
            return result

    def get_string_from_id (self, tid):
        return self.string_from_id[tid]

    def get_id_from_xml (self, xml_id):
        result = self.xml_to_id.get(xml_id)

        if (result == None):
            return self.generate_new_id(xml_id)
        else:
            return result

    def get_xml_from_id (self, tid):
        return self.id_to_xml.get(tid)

    def force_id_association (self, xml_id, tid):
        self.xml_to_id[xml_id] = tid
        self.id_to_xml[tid] = xml_id

    def set_next_id (self, next_id):
        self.next_id = next_id

    def finalize (self):
        self.output.write(
            "(is_next_id "
            + str(self.next_id)
            + ")\n"
        )
