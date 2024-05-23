#ifndef MOVE_DETECTORS_HANDLER_H
#define MOVE_DETECTORS_HANDLER_H

#include <iostream>
#include "./../SmartHome/SmartHome.h"
#include <Ice/Ice.h>
#include <glog/logging.h>
#include "DetectorsHandler.hpp"
#include "./../enums/MoveDetectorEnum.hpp"

class MoveDetectorsHandler: public DetectorsHandler{
private:
    Detectors::MoveDetectorPrxPtr proxy;
public:
    explicit MoveDetectorsHandler(const Ice::CommunicatorPtr& communicator) : DetectorsHandler::DetectorsHandler(communicator){};
    bool handleCommand(const std::string& command) override;
};

bool MoveDetectorsHandler::handleCommand(const std::string &command) {
    switch (resolveMoveDetectorCommand(command)) {
        case MOTION_DETECTED:
            LOG(INFO) << "MOVE DETECTOR - MOTION_DETECTED - [ " << proxy->motionDetected() << "]";
            return true;
        default:
            break;
    }

    return DetectorsHandler::handleCommand(command);
}

#endif