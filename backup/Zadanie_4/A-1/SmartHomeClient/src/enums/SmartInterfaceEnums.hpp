#ifndef SMART_INTERFACE_COMMANDS
#define SMART_INTERFACE_COMMANDS

#include <string>

enum SmartInterfaceCommands {
    CHANGE_MODE,
    GET_MODE,
    NOTIFY,
    INVALID_COMMAND
};

SmartInterfaceCommands resolveSmartCommands(const std::string& input) {
    if( input == "changeMode" ) return CHANGE_MODE;
    if( input == "getMode" ) return GET_MODE;
    if( input == "notify") return NOTIFY;
    //...

    return INVALID_COMMAND;
}

#endif