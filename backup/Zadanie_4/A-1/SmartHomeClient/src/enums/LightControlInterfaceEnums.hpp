#ifndef LIGHT_CONTROL_COMMANDS
#define LIGHT_CONTROL_COMMANDS

#include <string>

enum LightInterfaceCommands {
    TURN_ON,
    TURN_OFF,
    IS_ON,
    INVALID_COMMAND
};

LightInterfaceCommands resolveLightInterfaceCommands(const std::string& input) {
    if( input == "turnOn" ) return TURN_ON;
    if( input == "turnOff" ) return TURN_OFF;
    if (input == "isOn" ) return IS_ON;
    //...

    return INVALID_COMMAND;
}

#endif