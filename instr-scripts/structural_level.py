#!/usr/bin/env python3

import argparse
from lxml import etree

import id_manager
import waveform_manager
import process_internals

def is_function_or_literal (
    root,
    ref
):
    source = root.find(".//el[@id=\"" + ref + "\"]")

    if (source == None):
        return True
    elif (source.attrib.get("kind") == "function_declaration"):
        return True
    elif (source.attrib.get("kind") == "enumeration_literal"):
        return True

    return False

################################################################################
## Types #######################################################################
################################################################################
def new_element_from_xml (
    inst_list_output,
    id_manager,
    etype,
    xml_id
):
    result = id_manager.get_id_from_xml(xml_id)

    inst_list_output.write("(add_element " + etype + "  " + result + ")\n")

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

    inst_list_output.write("(add_element " + etype + "  " + result + ")\n")

    return result


################################################################################
## Components ##################################################################
################################################################################
def handle_port_map (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    design_file,
    architecture,
    component,
    linked_entity,
    port_map
):
    port_map_list = port_map.findall(
        "./el[@kind=\"association_element_by_expression\"]"
    )
    real_ports_list = linked_entity.findall(
        "./port_chain/el[@kind=\"interface_signal_declaration\"]"
    )

    for i in range(len(port_map_list)):
        actual_wfm = port_map_list[i].find("./actual/named_entity")
        true_port = ""

        formal = port_map_list[i].find("./formal")

        if (formal == None):
            ## The port is mapped using its position (i).
            inst_list_output.write(
                "(port_maps "
                + id_manager.get_id_from_xml(component.attrib.get("id"))
                + " "
                + wfm_manager.get_waveform_from_source(
                    id_manager.get_id_from_xml(actual_wfm.attrib.get("ref"))
                )
                + " "
                + id_manager.get_id_from_xml(
                    real_ports_list[i].attrib.get("id")
                )
                + ")\n"
            )
        else:
            ## The port is mapped "Destination => Origin" style
            #### Find the matching port
            true_port = linked_entity.find(
                "./port_chain/el[@identifier=\""
                + formal.attrib.get("identifier")
                + "\"]"
            )

            if (true_port == None):
                # TODO
                raise Exception("Could not find true port for ...")

            inst_list_output.write(
                "(port_maps "
                + id_manager.get_id_from_xml(component.attrib.get("id"))
                + " "
                + wfm_manager.get_waveform_from_source(
                    id_manager.get_id_from_xml(actual_wfm.attrib.get("ref"))
                )
                + " "
                + id_manager.get_id_from_xml(true_port.attrib.get("id"))
                + ")\n"
            )

## Component uses reference from the architecture declaration ################## 
def handle_component_internal_ref (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    design_file,
    architecture,
    component,
    source
):
    # We have to find the associated component declaration
    ref_id = source.find("./base_name").attrib.get("ref")

    component_declaration = architecture.find(
        "./declaration_chain/el[@kind=\"component_declaration\"][@id=\""
        + ref_id
        + "\"]"
    )

    # Then find the actual referenced entity
    linked_entity = root.find(
        ".//library_unit[@kind=\"entity_declaration\"][@identifier=\""
        + component_declaration.attrib.get("identifier")
        + "\"]"
    )

    if (linked_entity == None):
        print(
            "[W] Could not find entity linked to component "
            + id_manager.get_id_from_xml(component.attrib.get("id"))
            + " (XML id: \""
            + component.attrib.get("id")
            + "\", \""
            + component.attrib.get("label")
            + "\" from architecture \""
            + architecture.attrib.get("identifier")
            + "\")."
        )
        return None

    linked_entity_id = linked_entity.attrib.get("id")

    inst_list_output.write(
        "(is_component_of "
        + id_manager.get_id_from_xml(component.attrib.get("id"))
        + " "
        + id_manager.get_id_from_xml(linked_entity_id)
        + ")\n"
    )

    return linked_entity

## Component uses the name of the other entity directly ######################## 
def handle_component_external_ref (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    design_file,
    architecture,
    component,
    source
):
    # FIXME: there's a simpler way: source->./entity_name/named_entity@ref
    entity_ref = source.find(
        "./entity_name[@kind=\"selected_name\"]"
    )

    library_ref = entity_ref.find(
        "./prefix[@kind=\"simple_name\"]"
    )

    # Then find the actual referenced entity
    linked_entity = root.find(
        "./el[@kind=\"library_declaration\"][@identifier=\""
        + library_ref.attrib.get("identifier")
        + "\"]//library_unit[@kind=\"entity_declaration\"][@identifier=\""
        + entity_ref.attrib.get("identifier")
        + "\"]"
    )

    if (linked_entity == None):
        print(
            "[W] Could not find entity linked to component "
            + id_manager.get_id_from_xml(component.attrib.get("id"))
            + " (XML id: \""
            + component.attrib.get("id")
            + "\", \""
            + elem.attrib.get("label")
            + "\" from architecture \""
            + architecture.attrib.get("identifier")
            + "\"), looked for \""
            + library_ref.attrib.get("identifier")
            + "."
            + + entity_ref.attrib.get("identifier")
            +"\"."
        )
        return None

    linked_entity_id = linked_entity.attrib.get("id")

    inst_list_output.write(
        "(is_component_of "
        + component.attrib.get("id")
        + " "
        + linked_entity_id
        + ")\n"
    )

    return linked_entity

################################################################################
def handle_component (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    design_file,
    architecture,
    elem
):
    xml_id = elem.attrib.get("id")

    local_id = new_element_from_xml(
        inst_list_output,
        id_manager,
        "component",
        xml_id
    )

    ## Set Functions ###########################################################
    #### line
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("line")
    )
    inst_list_output.write(
        "(set_function line "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### column
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("col")
    )
    inst_list_output.write(
        "(set_function column "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### label
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("label")
    )
    inst_list_output.write(
        "(set_function label "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    ## Set Unary Predicates ####################################################

    ## Find Source #############################################################
    source = elem.find("./instantiated_unit")

    linked_entity = None
    #### If it is from a "component" declaration ###############################
    if (source.attrib.get("kind") == "simple_name"):
        linked_entity = handle_component_internal_ref(
            inst_list_output,
            extra_output,
            id_manager,
            wfm_manager,
            root,
            design_file,
            architecture,
            elem,
            source
        )
    #### If it is a more direct instantiation ##################################
    elif (source.attrib.get("kind") == "entity_aspect_entity"):
        linked_entity = handle_component_external_ref(
            inst_list_output,
            extra_output,
            id_manager,
            wfm_manager,
            root,
            design_file,
            architecture,
            elem,
            source
        )
    else:
        raise Exception (
            "Unhandled component instantiation type for component "
            + local_id
            + " (XML id: \""
            + xml_id
            + "\", \""
            + elem.attrib.get("label")
            + "\" from architecture \""
            + architecture.attrib.get("identifier")
            + "\")."
        )

    if (linked_entity != None):
        ## Find Mapped Ports ###################################################
        port_map = elem.find("./port_map_aspect_chain")

        if (port_map == None):
            print(
                "[W] Component instantiation "
                + local_id
                + " (XML id: \""
                + xml_id
                + "\", \""
                + elem.attrib.get("label")
                + "\" from architecture \""
                + architecture.attrib.get("identifier")
                + "\") does not have any associated port map."
            )
        else:
            handle_port_map(
                inst_list_output,
                extra_output,
                id_manager,
                wfm_manager,
                root,
                design_file,
                architecture,
                elem,
                linked_entity,
                port_map
            )

    return xml_id

################################################################################
## PROCESSES ###################################################################
################################################################################
def handle_process (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    design_file,
    architecture,
    elem
):
    xml_id = elem.attrib.get("id")

    local_id = new_element_from_xml(
        inst_list_output,
        id_manager,
        "process",
        xml_id
    )

    ## Set Functions ###########################################################
    #### line
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("line")
    )
    inst_list_output.write(
        "(set_function line "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### column
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("col")
    )
    inst_list_output.write(
        "(set_function column "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### label
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("label")
    )
    inst_list_output.write(
        "(set_function label "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    ## Set Unary Predicates ####################################################
    if (elem.attrib.get("seen_flag") == "true"):
        inst_list_output.write("(has_seen_flag " + local_id + ")\n")

    if (elem.attrib.get("end_has_postponed") == "true"):
        inst_list_output.write("(end_has_postponed " + local_id + ")\n")

    if (elem.attrib.get("is_ref") == "true"):
        inst_list_output.write("(is_ref " + local_id + ")\n")
    else:
        inst_list_output.write("(is_explicit_process " + local_id + ")\n")

    if (elem.attrib.get("passive_flag") == "true"):
        inst_list_output.write("(has_passive_flag " + local_id + ")\n")

    if (elem.attrib.get("postponed_flag") == "true"):
        inst_list_output.write("(has_postponed_flag " + local_id + ")\n")

    if (elem.attrib.get("visible_flag") == "true"):
        inst_list_output.write("(has_visible_flag " + local_id + ")\n")

    if (elem.attrib.get("is_within_flag") == "true"):
        inst_list_output.write("(is_within_flag " + local_id + ")\n")

    if (elem.attrib.get("has_label") == "true"):
        inst_list_output.write("(has_label " + local_id + ")\n")

    if (elem.attrib.get("has_is") == "true"):
        inst_list_output.write("(has_is " + local_id + ")\n")

    if (elem.attrib.get("end_has_reserved_id") == "true"):
        inst_list_output.write("(end_has_reserved_id " + local_id + ")\n")

    if (elem.attrib.get("end_has_identifier") == "true"):
        inst_list_output.write("(end_has_identifier " + local_id + ")\n")

    ## Link with Waveforms #####################################################
    children = elem.findall(
        "./sensitivity_list/el/named_entity"
    )
    for c in children:
        inst_list_output.write(
            "(is_in_sensitivity_list "
            + wfm_manager.get_waveform_from_source(
                id_manager.get_id_from_xml(c.attrib.get("ref"))
            )
            + " "
            + local_id
            + ")\n"
        )

    children = elem.findall(
        "./sensitivity_list/el[@ref]"
    )
    for c in children:
        inst_list_output.write(
            "(is_in_sensitivity_list "
            + wfm_manager.get_waveform_from_source(
                id_manager.get_id_from_xml(c.attrib.get("ref"))
            )
            + " "
            + local_id
            + ")\n"
        )

    children = elem.findall(
        ".//*[@kind=\"simple_name\"]/named_entity"
    )
    for c in children:
        ref = c.attrib.get("ref")

        if (is_function_or_literal(root, ref)):
            print(
                "Assumed that \""
                + c.find("./..").attrib.get("identifier")
                + "\" is a function or literal (XML id: \""
                + ref
                + "\")."
            )

            continue

        ref = wfm_manager.get_waveform_from_source(
                id_manager.get_id_from_xml(c.attrib.get("ref"))
            )

        inst_list_output.write(
            "(is_accessed_by "
            + ref
            + " "
            + local_id
            + ")\n"
        )

    ############################################################################
    inst_list_output.write("\n")

    # TODO: use 'pfp' parameter.
    pf = open(('./pfp_id_' + local_id + '.mod'), 'w')

    internals = process_internals.Process_Internals(
        root,
        elem,
        id_manager,
        wfm_manager,
        local_id,
        pf
    )
    internals.parse()
    pf.write("\n")
    pf.close()

    return xml_id

################################################################################
## Signals #####################################################################
################################################################################
def handle_signal (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    design_file,
    architecture,
    elem
):
    xml_id = elem.attrib.get("id")

    local_id = new_element_from_xml(
        inst_list_output,
        id_manager,
        "signal",
        xml_id
    )

    ## Set Functions ###########################################################
    #### line
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("line")
    )
    inst_list_output.write(
        "(set_function line "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### column
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("col")
    )
    inst_list_output.write(
        "(set_function column "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### column
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("identifier")
    )
    inst_list_output.write(
        "(set_function identifier "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    ## Set Unary Predicates #################################################### 
    if (elem.attrib.get("has_disconnect_flag") == "true"):
        inst_llocalist_output.write("(has_disconnect_flag " + local_id + ")\n")

    if (elem.attrib.get("is_ref") == "true"):
        inst_list_output.write("(is_ref " + local_id + ")\n")

    if (elem.attrib.get("has_active_flag") == "true"):
        inst_list_output.write("(has_active_flag " + local_id + ")\n")

    if (elem.attrib.get("has_identifier_list") == "true"):
        inst_list_output.write("(has_identifier_list " + local_id + ")\n")

    if (elem.attrib.get("visible_flag") == "true"):
        inst_list_output.write("(has_visible_flag " + local_id + ")\n")

    if (elem.attrib.get("after_drivers_flag") == "true"):
        inst_list_output.write("(has_after_drivers_flag " + local_id + ")\n")

    if (elem.attrib.get("use_flag") == "true"):
        inst_list_output.write("(has_use_flag " + local_id + ")\n")

    if (elem.attrib.get("guarded_signal_flag") == "true"):
        inst_list_output.write("(has_guarded_signal_flag " + local_id + ")\n")

    ## Set Other Predicates #################################################### 
    #### Link with Signal Kinds ################################################
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("signal_kind").upper()
    )
    inst_list_output.write(
        "(is_of_kind "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    inst_list_output.write("\n")

    ## Matching Waveform #######################################################
    #### Add Element ###########################################################
    waveform_id = wfm_manager.get_waveform_from_source(local_id)
    inst_list_output.write("(add_element waveform " + waveform_id + ")\n")

    #### Link With Signal ######################################################
    inst_list_output.write(
        "(is_waveform_of "
        + waveform_id
        + " "
        + local_id
        + ")\n"
    )

    ############################################################################
    inst_list_output.write("\n")

    return xml_id

################################################################################
## Architectures ###############################################################
################################################################################
def handle_architecture (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    design_file,
    elem
):
    xml_id = elem.attrib.get("id")

    local_id = new_element_from_xml(
        inst_list_output,
        id_manager,
        "architecture",
        xml_id
    )

    ## Set Functions ###########################################################
    #### line
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("line")
    )
    inst_list_output.write(
        "(set_function line "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### column
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("col")
    )
    inst_list_output.write(
        "(set_function column "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### identifier
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("identifier")
    )
    inst_list_output.write(
        "(set_function identifier "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    ## Set Unary Predicates ####################################################
    if (elem.attrib.get("foreign_flag") == "true"):
        inst_list_output.write("(has_foreign_flag " + local_id + ")\n")

    if (elem.attrib.get("visible_flag") == "true"):
        inst_list_output.write("(has_visible_flag " + local_id + ")\n")

    if (elem.attrib.get("is_within_flag") == "true"):
        inst_list_output.write("(is_within_flag " + local_id + ")\n")

    if (elem.attrib.get("end_has_reserved_id") == "true"):
        inst_list_output.write("(end_has_reserved_id " + local_id + ")\n")

    if (elem.attrib.get("end_has_identifier") == "true"):
        inst_list_output.write("(end_has_identifier " + local_id + ")\n")

    ## Link to Parent ##########################################################
    inst_list_output.write(
        "(is_in_file "
        + local_id
        + " "
        + id_manager.get_id_from_xml(design_file.attrib.get("id"))
        + ")\n"
    )

    inst_list_output.write("\n")

    ## Handle Children #########################################################
    #### Internal Signals ######################################################
    children = elem.findall(
        "./*/el[@kind=\"signal_declaration\"]"
    )
    for c in children:
        c_id = handle_signal(
            inst_list_output,
            extra_output,
            id_manager,
            wfm_manager,
            root,
            design_file,
            elem,
            c
        )
        inst_list_output.write(
            "(belongs_to_architecture "
            + id_manager.get_id_from_xml(c_id)
            + " "
            + local_id
            + ")\n"
        )

    #### Internal Processes ####################################################
    children = elem.findall(
        "./*/el[@kind=\"sensitized_process_statement\"]"
    )
    for c in children:
        c_id = handle_process(
            inst_list_output,
            extra_output,
            id_manager,
            wfm_manager,
            root,
            design_file,
            elem,
            c
        )
        inst_list_output.write(
            "(belongs_to_architecture "
            + id_manager.get_id_from_xml(c_id)
            + " "
            + local_id
            + ")\n"
        )

    #### Internal Components ###################################################
    children = elem.findall(
        "./*/el[@kind=\"component_instantiation_statement\"]"
    )
    for c in children:
        c_id = handle_component(
            inst_list_output,
            extra_output,
            id_manager,
            wfm_manager,
            root,
            design_file,
            elem,
            c
        )
        inst_list_output.write(
            "(belongs_to_architecture "
            + id_manager.get_id_from_xml(c_id)
            + " "
            + local_id
            + ")\n"
        )

    ############################################################################
    entity = elem.find("./entity_name/named_entity")
    inst_list_output.write(
        "(is_architecture_of "
        + local_id
        + " "
        + id_manager.get_id_from_xml(entity.attrib.get("ref"))
        + ")\n"
    )
    return xml_id

################################################################################
## Generic Constants ###########################################################
################################################################################
def handle_generic (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    design_file,
    entity,
    elem
):
    xml_id = elem.attrib.get("id")

    local_id = new_element_from_xml(
        inst_list_output,
        id_manager,
        "generic",
        xml_id
    )

    ## Matching Waveform #######################################################
    #### Add Element ###########################################################
    waveform_id = wfm_manager.get_waveform_from_source(local_id)
    inst_list_output.write("(add_element waveform " + waveform_id + ")\n")

    #### Link With Signal ######################################################
    inst_list_output.write(
        "(is_waveform_of "
        + waveform_id
        + " "
        + local_id
        + ")\n"
    )

    ## Set Functions ###########################################################
    #### line
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("line")
    )
    inst_list_output.write(
        "(set_function line "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### column
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("col")
    )
    inst_list_output.write(
        "(set_function column "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### identifier
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("identifier")
    )
    inst_list_output.write(
        "(set_function identifier "
        + local_id
        + " "
        + string_id
        + ")\n"
    )


    ## Set Unary Predicates ####################################################
    if (elem.attrib.get("has_class") == "true"):
        inst_list_output.write("(has_class " + local_id + ")\n")

    if (elem.attrib.get("is_ref") == "true"):
        inst_list_output.write("(is_ref " + local_id + ")\n")

    if (elem.attrib.get("has_identifier_list") == "true"):
        inst_list_output.write("(has_identifier_list " + local_id + ")\n")

    if (elem.attrib.get("visible_flag") == "true"):
        inst_list_output.write("(has_visible_flag " + local_id + ")\n")

    if (elem.attrib.get("after_drivers_flag") == "true"):
        inst_list_output.write("(has_after_drivers_flag " + local_id + ")\n")

    if (elem.attrib.get("use_flag") == "true"):
        inst_list_output.write("(has_use_flag " + local_id + ")\n")

    if (elem.attrib.get("guarded_signal_flag") == "true"):
        inst_list_output.write("(has_guarded_signal_flag " + local_id + ")\n")

    ############################################################################
    inst_list_output.write("\n")

    return xml_id

################################################################################
## Ports #######################################################################
################################################################################
def handle_port (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    design_file,
    entity,
    elem
):
    xml_id = elem.attrib.get("id")

    local_id = new_element_from_xml(
        inst_list_output,
        id_manager,
        "port",
        xml_id
    )

    ## Set Functions ###########################################################
    #### line
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("line")
    )
    inst_list_output.write(
        "(set_function line "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### column
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("col")
    )
    inst_list_output.write(
        "(set_function column "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### identifier
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("identifier")
    )
    inst_list_output.write(
        "(set_function identifier "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    ## Set Unary Predicates ####################################################
    if (elem.attrib.get("has_disconnect_flag") == "true"):
        inst_list_output.write("(has_disconnect_flag " + local_id + ")\n")

    if (elem.attrib.get("has_class") == "true"):
        inst_list_output.write("(has_class " + local_id + ")\n")

    if (elem.attrib.get("is_ref") == "true"):
        inst_list_output.write("(is_ref " + local_id + ")\n")

    if (elem.attrib.get("has_active_flag") == "true"):
        inst_list_output.write("(has_active_flag " + local_id + ")\n")

    if (elem.attrib.get("has_identifier_list") == "true"):
        inst_list_output.write("(has_identifier_list " + local_id + ")\n")

    if (elem.attrib.get("visible_flag") == "true"):
        inst_list_output.write("(has_visible_flag " + local_id + ")\n")

    if (elem.attrib.get("after_drivers_flag") == "true"):
        inst_list_output.write("(has_after_drivers_flag " + local_id + ")\n")

    if (elem.attrib.get("use_flag") == "true"):
        inst_list_output.write("(has_use_flag " + local_id + ")\n")

    if (elem.attrib.get("open_flag") == "true"):
        inst_list_output.write("(has_open_flag " + local_id + ")\n")

    if (elem.attrib.get("guarded_signal_flag") == "true"):
        inst_list_output.write("(has_guarded_signal_flag " + local_id + ")\n")

    ## Link With Signal Modes ##################################################
    if (elem.attrib.get("has_mode") == "true"):
        string_id = new_element_from_string(
            inst_list_output,
            id_manager,
            elem.attrib.get("mode").upper()
        )
        inst_list_output.write(
            "(is_of_mode "
            + local_id
            + " "
            + string_id
            + ")\n"
        )
    else:
        string_id = new_element_from_string(
            inst_list_output,
            id_manager,
            elem.attrib.get("NONE").upper()
        )
        inst_list_output.write("(is_of_mode " + local_id + " " + string_id + ")\n")

    inst_list_output.write("\n")

    ## Matching Waveform #######################################################
    #### Add Element ###########################################################
    wfm_id = wfm_manager.get_waveform_from_source(local_id)
    inst_list_output.write(
        "(add_element waveform "
        + wfm_id
        + ")\n"
    )

    #### Link With Port ########################################################
    inst_list_output.write("(is_waveform_of " + wfm_id + " " + local_id + ")\n")

    ############################################################################
    inst_list_output.write("\n")

    return xml_id

################################################################################
## Entities ####################################################################
################################################################################
def handle_entity (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    design_file,
    elem
):
    xml_id = elem.attrib.get("id")

    local_id = new_element_from_xml(
        inst_list_output,
        id_manager,
        "entity",
        xml_id
    )

    ## Set Functions ###########################################################
    #### line
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("line")
    )
    inst_list_output.write(
        "(set_function line "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### column
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("col")
    )
    inst_list_output.write(
        "(set_function column "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    #### identifier
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("identifier")
    )
    inst_list_output.write(
        "(set_function identifier "
        + local_id
        + " "
        + string_id
        + ")\n"
    )

    ## Set Unary Predicates ####################################################
    if (elem.attrib.get("has_begin") == "true"):
        inst_list_output.write("(has_begin " + local_id + ")\n")

    if (elem.attrib.get("visible_flag") == "true"):
        inst_list_output.write("(has_visible_flag " + local_id + ")\n")

    if (elem.attrib.get("is_within_flag") == "true"):
        inst_list_output.write("(is_within_flag " + local_id + ")\n")

    if (elem.attrib.get("end_has_reserved_id") == "true"):
        inst_list_output.write("(end_has_reserved_id " + local_id + ")\n")

    if (elem.attrib.get("end_has_identifier") == "true"):
        inst_list_output.write("(end_has_identifier " + local_id + ")\n")

    ## Link to Parent ##########################################################
    inst_list_output.write(
        "(is_in_file "
        + local_id
        + " "
        + id_manager.get_id_from_xml(design_file.attrib.get("id"))
        + ")\n"
    )

    inst_list_output.write("\n")

    ## Handle Children #########################################################
    #### Ports #################################################################
    interfaces = elem.findall(
        "./port_chain/el[@kind=\"interface_signal_declaration\"]"
    )
    for p in interfaces:
        p_id = handle_port(
            inst_list_output,
            extra_output,
            id_manager,
            wfm_manager,
            root,
            design_file,
            elem,
            p
        )
        inst_list_output.write(
            "(is_port_of "
            + id_manager.get_id_from_xml(p_id)
            + " "
            + local_id
            + ")\n"
        )

    #### Generic Constants #####################################################
    interfaces = elem.findall(
        "./generic_chain/el[@kind=\"interface_constant_declaration\"]"
    )
    for g in interfaces:
        g_id = handle_generic(
            inst_list_output,
            extra_output,
            id_manager,
            wfm_manager,
            root,
            design_file,
            elem,
            g
        )
        inst_list_output.write(
            "(is_generic_of "
            + id_manager.get_id_from_xml(g_id)
            + " "
            + local_id
            + ")\n"
        )

    return xml_id

################################################################################
## Files #######################################################################
################################################################################
def handle_design_file (
    inst_list_output,
    extra_output,
    id_manager,
    wfm_manager,
    root,
    elem
):
    xml_id = elem.attrib.get("id")
    local_id = new_element_from_xml(
        inst_list_output,
        id_manager,
        "file",
        xml_id
    )

    ## Set Functions ###########################################################
    #### filename
    string_id = new_element_from_string(
        inst_list_output,
        id_manager,
        elem.attrib.get("file")
    )
    inst_list_output.write(
        "(set_function filename "
        + local_id
        + " "
        + string_id
        + ")\n"
    )
    local_id = id_manager.get_id_from_xml(xml_id)

    ## Handle Children #########################################################
    #### Entities ##############################################################
    children = elem.findall(
        "./*/*/library_unit[@kind=\"entity_declaration\"]"
    )
    for c in children:
        c_id = handle_entity(
            inst_list_output,
            extra_output,
            id_manager,
            wfm_manager,
            root,
            elem,
            c
        )

    #### Architectures #########################################################
    children = elem.findall(
        "./*/*/library_unit[@kind=\"architecture_body\"]"
    )
    for c in children:
        c_id = handle_architecture(
            inst_list_output,
            extra_output,
            id_manager,
            wfm_manager,
            root,
            elem,
            c
        )

    ############################################################################
    return xml_id

################################################################################
### MAIN #######################################################################
################################################################################
parser = argparse.ArgumentParser(
    description = (
        "Generates a list of instructions to construct the Structural Level."
    )
)

parser.add_argument(
    '-i',
    '--input',
    type = str,
    required = True,
    help = 'AST of the system produced by GHDL (XML).'
)

parser.add_argument(
    '-io', '--instructions-output',
    type = argparse.FileType(mode = 'w', encoding = 'UTF-8'),
    default = 'structural.mod',
    help = 'Instruction List output file (default: system.mod)',
)

parser.add_argument(
    '-eo', '--extra-output',
    type = argparse.FileType(mode = 'w', encoding = 'UTF-8'),
    default = 'system_extra.txt',
    help = 'Extra Information output file (default: system_extra.txt)',
)

parser.add_argument(
    '-pfp',
    '--process-files-prefix',
    type = str,
    default = "./process_instr_",
    help = 'Resulting process description files: this + their ID + ".mod".'
)

args = parser.parse_args()

xmltree = etree.parse(args.input)

xmlroot = xmltree.getroot()

id_manager = id_manager.Id_Manager(args.extra_output, 0)
wfm_manager = waveform_manager.Waveform_Manager(args.extra_output, id_manager)
## Handle Libraries ############################################################
#elements = xmlroot.findall("./*/el[@kind=\"library_declaration\"]")
#[handle_library(e, root) for e in elements]

## Handle Files ################################################################
elements = xmlroot.findall("./*/*/el[@kind=\"design_file\"][@file]")
[
    handle_design_file(
        args.instructions_output,
        args.extra_output,
        id_manager,
        wfm_manager,
        xmlroot,
        e
    ) for e in elements
]

id_manager.finalize()
