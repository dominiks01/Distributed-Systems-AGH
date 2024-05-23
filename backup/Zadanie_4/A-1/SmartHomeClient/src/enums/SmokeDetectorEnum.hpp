#ifndef SMOKE_DETECTOR_COMMANDS
#define SMOKE_DETECTOR_COMMANDS

#include <string>

enum SmokeDetectorInterfaceCommands {
    GET_SMOKE_DENSITY,
    IS_SAFE,
    INVALID_COMMAND
};

SmokeDetectorInterfaceCommands resolveSmokeDetectorCommand(const std::string& input) {
    if( input == "getSmokeDensity" ) return GET_SMOKE_DENSITY;
    if( input == "isSafe" ) return IS_SAFE;
    //...

    return INVALID_COMMAND;
}

#endif