#ifndef LIGHT_CONTROL
#define LIGHT_CONTROL

#include <iostream>
#include "./../SmartHome/SmartHome.h"
#include <Ice/Ice.h>
#include <glog/logging.h>
#include "CommandHandler.hpp"
#include "./../enums/LightControlInterfaceEnums.hpp"

class LightControlHandler: public Interfaces::LightControl, CommandHandler {
private:
public:
    explicit LightControlHandler(const Ice::CommunicatorPtr& communicator) : CommandHandler::CommandHandler(communicator){};
    bool handleCommand(const std::string& command) override;
    Interfaces::LightControlPrxPtr proxy;
};

bool LightControlHandler::handleCommand(const std::string &command) {
    switch (resolveLightInterfaceCommands(command)) {
        case TURN_ON:
            proxy->turnOn();
            LOG(INFO) << "LightControl - turn on ";
            return true;
        case TURN_OFF:
            proxy->turnOff();
            LOG(INFO) << "LightControl - turn off ";
            return true;
        case IS_ON:
            LOG(INFO) << "LightControl - is on - [" << proxy->isOn() << "]";
            return true;
        default:
            break;
    }

    return CommandHandler::handleCommand(command);
}


#endif