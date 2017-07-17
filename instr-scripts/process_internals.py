def new_element_from_xml (
    inst_list_output,
    id_manager,
    etype,
    xml_id
):
    result = id_manager.get_id_from_xml(xml_id)

    inst_list_output.write("(add_element " + etype + " " + result + ")\n")

    return result

def new_element_from_string (
    inst_list_output,
    id_manager,
    string
):
    result = id_manager.get_id_from_string(string)

    inst_list_output.write("(add_element string " + result + ")\n")

    return result

def new_element (
    inst_list_output,
    id_manager,
    etype
):
    result = id_manager.generate_new_pure_id()

    inst_list_output.write("(add_element " + etype + " " + result + ")\n")

    return result

class Process_Internals:
    def __init__ (self, xml, id_manager, wfm_manager, output):
        self.xml = xml
        self.id_manager = id_manager
        self.wfm_manager = wfm_manager
        self.output = output

    def parse (self):
        start = self.xml.find("./sequential_statement_chain")

        self.handle_sequential_statement_chain(
            start,
            [],
            0,
            []
        )

    def handle_sequential_statement_chain (
        self,
        xml,
        prev_nodes,
        node_depth,
        extra_attributes
    ):
        instructions = xml.findall("./el")

        for el in instructions:
            el_kind = el.attrib.get("kind")

            if (el_kind == "if_statement"):
                prev_nodes = self.handle_if_node(
                    el,
                    prev_nodes,
                    node_depth,
                    extra_attributes
                )
            elif (el_kind == "simple_signal_assignment_statement"):
                prev_nodes = self.handle_signal_assignment_node(
                    el,
                    prev_nodes,
                    node_depth,
                    extra_attributes
                )
            elif (el_kind == "case_statement"):
                prev_nodes = self.handle_case_node(
                    el,
                    prev_nodes,
                    node_depth,
                    extra_attributes
                )
            else:
                raise Exception (
                    "Unimplemented instruction kind \""
                    + el_kind
                    + "\""
                )

            extra_attributes = []

        return prev_nodes


    def handle_if_node (self, xml, prev_nodes, node_depth, extra_attributes):
        cond_node_id = new_element(self.output, self.id_manager, "node")

        for pn in prev_nodes:
            self.output.write(
                "(node_connect "
                + pn
                + " "
                + cond_node_id
                + ")\n"
            )

        cond_node_xml = xml.find("./condition")

        sources_xml = cond_node_xml.findall(
            ".//named_entity"
        )

        for src_xml in sources_xml:
            self.output.write(
                "(expr_reads "
                + cond_node_id
                + " "
                + self.id_manager.get_id_from_xml(src_xml.attrib.get("ref"))
                + ")\n"
            )

        #### label
        string_id = new_element_from_string(
            self.output,
            self.id_manager,
            xml.attrib.get("label")
        )
        self.output.write(
            "(set_attribute label "
            + cond_node_id
            + " "
            + string_id
            + ")\n"
        )

        #### Expression
        # TODO

        #### Kind
        string_id = new_element_from_string(
            self.output,
            self.id_manager,
            "IF"
        )
        self.output.write(
            "(set_attribute kind "
            + cond_node_id
            + " "
            + string_id
            + ")\n"
        )

        #### Options
        for attr in extra_attributes:
            string_id = new_element_from_string(
                self.output,
                self.id_manager,
                attr
            )
            self.output.write(
                "(add_option " + cond_node_id + " " + string_id  + ")\n"
            )

        #### Depth
        self.output.write(
            "(set_attribute depth "
            + cond_node_id
            + " "
            + str(node_depth + 1)
            + ")\n"
        )

        #### File / Line / Column
        # TODO

        true_branch_xml = xml.find("./sequential_statement_chain")

        exit_points = self.handle_sequential_statement_chain (
            true_branch_xml,
            [cond_node_id],
            (node_depth + 2),
            ["COND_WAS_TRUE"]
        )

        false_branch_xml = xml.find("./else_clause/sequential_statement_chain")

        if (false_branch_xml == None):
            exit_points += prev_nodes
        else:
            exit_points += self.handle_sequential_statement_chain (
                false_branch_xml,
                [cond_node_id],
                (node_depth + 2),
                ["COND_WAS_FALSE"]
            )

        return exit_points

    def handle_signal_assignment_node (
        self,
        xml,
        prev_nodes,
        node_depth,
        extra_attributes
    ):
        node_id = new_element(self.output, self.id_manager, "node")

        for pn in prev_nodes:
            self.output.write(
                "(node_connect "
                + pn
                + " "
                + node_id
                + ")\n"
            )

        #### label
        string_id = new_element_from_string(
            self.output,
            self.id_manager,
            xml.attrib.get("label")
        )
        self.output.write(
            "(set_attribute label "
            + node_id
            + " "
            + string_id
            + ")\n"
        )

        #### Expression
        # TODO

        #### Kind
        string_id = new_element_from_string(
            self.output,
            self.id_manager,
            "INSTRUCTION"
        )
        self.output.write(
            "(set_attribute kind "
            + node_id
            + " "
            + string_id
            + ")\n"
        )

        #### Options
        for attr in extra_attributes:
            string_id = new_element_from_string(
                self.output,
                self.id_manager,
                attr
            )
            self.output.write(
                "(add_option " + node_id + " " + string_id  + ")\n"
            )

        #### Depth
        self.output.write(
            "(set_attribute depth "
            + node_id
            + " "
            + str(node_depth)
            + ")\n"
        )

        #### File / Line / Column
        # TODO

        # <target kind="indexed_name" if vector
        target_xml = xml.find(
            "./target"
        )

        if (target_xml.attrib.get("kind") == "indexed_name"):
            target_xml = target_xml.find("./prefix/named_entity")
        else:
            target_xml = target_xml.find("./named_entity")

        self.output.write(
            "(expr_writes "
            + node_id
            + " "
            + self.id_manager.get_id_from_xml(target_xml.attrib.get("ref"))
            + ")\n"
        )

        sources_xml = xml.findall(
            "./waveform_chain//named_entity"
        )

        for src_xml in sources_xml:
            self.output.write(
                "(expr_reads "
                + node_id
                + " "
                + self.id_manager.get_id_from_xml(src_xml.attrib.get("ref"))
                + ")\n"
            )

        ## Predicates ##########################################################

        return [node_id]

    def handle_case_node (self, xml, prev_nodes, node_depth, extra_attributes):
        cond_node_id = new_element(self.output, self.id_manager, "node")

        for pn in prev_nodes:
            self.output.write(
                "(node_connect "
                + pn
                + " "
                + cond_node_id
                + ")\n"
            )

        cond_node_xml = xml.find("./expression")

        sources_xml = cond_node_xml.findall(
            ".//named_entity"
        )

        for src_xml in sources_xml:
            self.output.write(
                "(expr_reads "
                + cond_node_id
                + " "
                + self.id_manager.get_id_from_xml(src_xml.attrib.get("ref"))
                + ")\n"
            )

        #### label
        string_id = new_element_from_string(
            self.output,
            self.id_manager,
            xml.attrib.get("label")
        )
        self.output.write(
            "(set_attribute label "
            + cond_node_id
            + " "
            + string_id
            + ")\n"
        )

        #### Expression
        # TODO

        #### Kind
        string_id = new_element_from_string(
            self.output,
            self.id_manager,
            "CASE"
        )
        self.output.write(
            "(set_attribute kind "
            + cond_node_id
            + " "
            + string_id
            + ")\n"
        )

        #### Options
        for attr in extra_attributes:
            string_id = new_element_from_string(
                self.output,
                self.id_manager,
                attr
            )
            self.output.write(
                "(add_option " + cond_node_id + " " + string_id  + ")\n"
            )

        #### Depth
        self.output.write(
            "(set_attribute depth "
            + cond_node_id
            + " "
            + str(node_depth + 1)
            + ")\n"
        )

        #### File / Line / Column
        # TODO

        others_branch_xml = xml.find(
            "./case_statement_alternative_chain/el[@kind=\"choice_by_others\"]"
        )

        if (others_branch_xml == None):
            exit_points = []
        else:
            exit_points = self.handle_when_node(
                others_branch_xml,
                [cond_node_id],
                (node_depth + 2),
                ["WHEN_OTHERS"]
            )

        when_branches_xml = xml.findall(
            "./case_statement_alternative_chain/el[@kind=\"choice_by_expression\"]"
        )

        for when_branch in when_branches_xml:
            exit_points += self.handle_when_node(
                when_branch,
                [cond_node_id],
                (node_depth + 2),
                []
            )

        false_branch_xml = xml.find("./else_cause/sequential_statement_chain")

        if (false_branch_xml == None):
            exit_points += prev_nodes
        else:
            exit_points += self.handle_sequential_statement_chain (
                false_branch_xml,
                [cond_node_id],
                (node_depth + 2),
                ["COND_WAS_FALSE"]
            )

        return exit_points

    def handle_when_node (self, xml, prev_nodes, node_depth, extra_attributes):
        node_id = new_element(self.output, self.id_manager, "node")

        sources_xml = xml.findall(
            "./choice_expression//named_entity"
        )

        for src_xml in sources_xml:
            self.output.write(
                "(expr_reads "
                + node_id
                + " "
                + self.id_manager.get_id_from_xml(src_xml.attrib.get("ref"))
                + ")\n"
            )

        for pn in prev_nodes:
            self.output.write(
                "(node_connect "
                + pn
                + " "
                + node_id
                + ")\n"
            )

        #### label
        string_id = new_element_from_string(
            self.output,
            self.id_manager,
            ""
        )
        self.output.write(
            "(set_attribute label "
            + node_id
            + " "
            + string_id
            + ")\n"
        )

        #### Expression
        # TODO

        #### Kind
        string_id = new_element_from_string(
            self.output,
            self.id_manager,
            "WHEN"
        )
        self.output.write(
            "(set_attribute kind "
            + node_id
            + " "
            + string_id
            + ")\n"
        )

        #### Options
        for attr in extra_attributes:
            string_id = new_element_from_string(
                self.output,
                self.id_manager,
                attr
            )
            self.output.write(
                "(add_option " + node_id + " " + string_id  + ")\n"
            )

        #### Depth
        self.output.write(
            "(set_attribute depth "
            + node_id
            + " "
            + str(node_depth)
            + ")\n"
        )

        #### File / Line / Column
        # TODO

        chain_xml = xml.find("./associated_chain")

        exit_points = self.handle_sequential_statement_chain(
            chain_xml,
            [node_id],
            (node_depth + 1),
            ["COND_WAS_TRUE"]
        )

        return exit_points
