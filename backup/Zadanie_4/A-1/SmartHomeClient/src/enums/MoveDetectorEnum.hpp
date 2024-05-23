#ifndef MOVE_DETECTOR_COMMANDS
#define MOVE_DETECTOR_COMMANDS

#include <string>

enum MoveDetectorInterfaceCommands {
    MOTION_DETECTED,
    INVALID_COMMAND
};

MoveDetectorInterfaceCommands resolveMoveDetectorCommand(const std::string& input) {
    if( input == "motionDetected" ) return MOTION_DETECTED;
    //...

    return INVALID_COMMAND;
}

#endif