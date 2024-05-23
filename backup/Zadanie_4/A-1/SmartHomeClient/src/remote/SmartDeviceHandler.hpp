#ifndef SMART_DEVICE_HANDLER
#define SMART_DEVICE_HANDLER

#include <iostream>
#include <Ice/Ice.h>
#include "CommandHandler.hpp"
#include "./../SmartHome/SmartHome.h"
#include "./../utils/utils.hpp"
#include "./../enums/SmartInterfaceEnums.hpp"
#include <glog/logging.h>

class SmartDeviceHandler: CommandHandler::CommandHandler{
private:
public:
    explicit SmartDeviceHandler(const Ice::CommunicatorPtr& communicator) : CommandHandler::CommandHandler(communicator){};
    bool handleCommand(const std::string& command) override;
};

bool SmartDeviceHandler::handleCommand(const std::string &command) {
    Interfaces::SmartDevicePrx proxy = Interfaces::SmartDevicePrx::checkedCast(getProxy(command));
    Enums::Mode mode;

    if(proxy){
        switch (resolveSmartCommands(command)) {
            case GET_MODE:
                mode = proxy->getMode();
                LOG(INFO) << "SmartDeviceHandler - GetMode - current [ " <<  mode << "]";
                return true;
            case CHANGE_MODE:
                proxy->changeMode();
                LOG(INFO) << "SmartDeviceHandler - ChangeMode - current [ " <<  mode << "]";
                return true;
            case NOTIFY:
                LOG(INFO) << "SmartDeviceHandler - notify";
                return true;
            case INVALID_COMMAND:
                return false;
        }
    }
}


#endif