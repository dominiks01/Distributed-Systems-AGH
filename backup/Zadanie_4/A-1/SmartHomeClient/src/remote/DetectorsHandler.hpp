#ifndef DETECTORS_HANDLER
#define DETECTORS_HANDLER

#include <iostream>
#include "SmartHome.h"
#include <Ice/Ice.h>
#include <glog/logging.h>
#include "CommandHandler.hpp"
#include "./../enums/DetectorInterfaceEnums.hpp"

class DetectorsHandler: public CommandHandler{
private:
    Interfaces::DetectorPrxPtr proxy;
public:
    explicit DetectorsHandler(const Ice::CommunicatorPtr& communicator) : CommandHandler::CommandHandler(communicator){};
    bool handleCommand(const std::string& command) override;
};

bool DetectorsHandler::handleCommand(const std::string &command) {
    switch (resolveDetectorCommand(command)) {
        case SET_SENSITIVITY_LEVEL:
            LOG(INFO) << "DetectorsHandle - SET_SENSITIVITY_LEVEL [ " << 1 << "]";
            proxy->setSensitivityLevel(1);
            return true;
        case GET_SENSITIVITY_LEVEL:
            LOG(INFO) << "DetectorsHandle - GET_SENSITIVITY_LEVEL [ " << proxy->getSensitivityLevel() << "]";
            return true;
        case GET_LOCATION:
            LOG(INFO) << "DetectorsHandle - GET_LOCATION [ " << proxy->getLocation() << "]";
            return true;
        case SET_LOCATION:
            proxy->setLocation("text");
            LOG(INFO) << "DetectorsHandle - SET_LOCATION [ " << "text" << "]";
            return true;
        default:
            break;
    }

    return CommandHandler::handleCommand(command);
}

#endif