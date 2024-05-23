#ifndef OUTDOOR_LIGHTS_HANDLER
#define OUTDOOR_LIGHTS_HANDLER

#include <iostream>
#include "./../SmartHome/SmartHome.h"
#include "SmartDeviceHandler.hpp"
#include "./../enums/OutdoorLightInterfaceEnums.hpp"
#include <Ice/Ice.h>
#include <glog/logging.h>

class OutdoorLightHandler : public SmartDeviceHandler{
public:
    explicit OutdoorLightHandler(const Ice::CommunicatorPtr& communicator) : SmartDeviceHandler(communicator){};
    bool handleCommand(const std::string& command) override;
private:
    Lights::OutdoorLightsPrxPtr proxy;
};

bool OutdoorLightHandler::handleCommand(const std::string &command) {
    switch (resolveOutdoorInterfaceCommands(command)) {
        case ADJUST_BRIGHTNESS:
            LOG(INFO) << "OutdoorLightHandler - Adjust Brightness - new value [ " <<  1 << "]";
            proxy->adjustBrightness(1);
            return true;
        case GET_BRIGHTNESS_LEVEL:
            LOG(INFO) << "OutdoorLightHandler - Actual Brightness - [ " <<  proxy->getBrightnessLevel() << "]";
            return true;
        default:
            break;
    }

    return SmartDeviceHandler::handleCommand(command);
}

#endif