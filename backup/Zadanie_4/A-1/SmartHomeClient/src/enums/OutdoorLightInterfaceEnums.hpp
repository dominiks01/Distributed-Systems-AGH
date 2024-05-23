#ifndef OUTDOOR_INTERFACE_COMMANDS
#define OUTDOOR_INTERFACE_COMMANDS

#include <string>

enum OutdoorInterfaceCommands {
    ADJUST_BRIGHTNESS,
    GET_BRIGHTNESS_LEVEL,
    INVALID_COMMAND
};

OutdoorInterfaceCommands resolveOutdoorInterfaceCommands(const std::string& input) {
    if( input == "adjustBrightness" ) return ADJUST_BRIGHTNESS;
    if( input == "getBrightnessLevel" ) return GET_BRIGHTNESS_LEVEL;
    //...

    return INVALID_COMMAND;
}

#endif