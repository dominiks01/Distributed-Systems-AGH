#ifndef DETECTOR_COMMANDS
#define DETECTOR_COMMANDS

#include <string>

enum DetectorInterfaceCommands {
    SET_SENSITIVITY_LEVEL,
    GET_SENSITIVITY_LEVEL,
    GET_LOCATION,
    SET_LOCATION,
    INVALID_COMMAND
};

DetectorInterfaceCommands resolveDetectorCommand(const std::string& input) {
    if( input == "setSensitivityLevel" ) return SET_SENSITIVITY_LEVEL;
    if( input == "getSensitivityLevel" ) return GET_SENSITIVITY_LEVEL;
    if( input == "getLocation" ) return GET_LOCATION;
    if( input == "setLocation" ) return SET_LOCATION;
    //...

    return INVALID_COMMAND;
}

#endif